package de.fhg.iese.kickstarttrustee.consent.business.service;

import de.fhg.iese.kickstarttrustee.consent.business.exception.ConsentNotFoundException;
import de.fhg.iese.kickstarttrustee.consent.business.model.Consent;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentRequest;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentStatus;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentUser;
import de.fhg.iese.kickstarttrustee.consent.persistence.repository.ConsentRepository;
import de.fhg.iese.kickstarttrustee.event.IEventbus;
import de.fhg.iese.kickstarttrustee.event.account.OwnerAccountDeletedEvent;
import de.fhg.iese.kickstarttrustee.event.consent.ConsentCreated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class ConsentService implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(ConsentService.class);
    static final Sort DEFAULT_SORT = Sort.by(Direction.DESC, "createdAt");

    private final IEventbus eventbus;
    private final ConsentUserService consentUserService;
    private final ConsentRepository consentRepository;

    public ConsentService(IEventbus eventbus, ConsentUserService consentUserService,
            ConsentRepository consentRepository) {
        this.eventbus = eventbus;
        this.consentUserService = consentUserService;
        this.consentRepository = consentRepository;
    }

    @Override
    public void afterPropertiesSet() {
        eventbus.registerConsumer(OwnerAccountDeletedEvent.class, e -> {
            this.deleteConsentsByOwnerId(e.accountId());
        });
    }

    private static ConsentCreated createConsentCreatedEvent(Consent consent, String actorId) {
        final String id = consent.getId();
        final String consentActorId = consent.getActor().getId();
        final String ownerId = consent.getOwnerId();
		final Map<String, Set<String>> consentPermissions = consent.getConsentPermissions().toSerializedMap();
        return new ConsentCreated(id, actorId, consentActorId, ownerId, consentPermissions);
    }

    private Mono<Boolean> isOwnerUser(final Consent consent) {
        final Mono<String> userIdMono = consentUserService.getCurrentUserId();
        return userIdMono.map(userId -> Objects.equals(userId, consent.getOwnerId()));
    }

    private Flux<Consent> getConsentsByStatus(Optional<ConsentStatus> status) {
        final Mono<String> currentActorId = consentUserService.getCurrentClientId();
        return currentActorId.flatMapMany(actorId -> {
            if (status.isEmpty()) {
                return consentRepository.findAllByActor_Id(actorId, DEFAULT_SORT);
            }
            final String consentStatus = status.get().name();
            return consentRepository.findAllByActor_IdAndStatus(actorId, consentStatus, DEFAULT_SORT);
        }).map(Consent::new);
    }

    Flux<Consent> getAllConsentsForRole(String role, Optional<ConsentStatus> status) {
        Objects.requireNonNull(role, "Role must not be null!");

        final Mono<Boolean> hasUserRoleMono = consentUserService.hasUserRole(role);
        return hasUserRoleMono.flatMapMany(hasUserRole -> {
            if (hasUserRole) {
                return getConsentsByStatus(status);
            }
            final Mono<Boolean> hasOwnerRole = consentUserService.hasUserRole(ConsentUserService.OWNER_ROLE);
            return hasOwnerRole.flatMapMany(isOwner -> {
                if (!isOwner) {
                    return Flux.empty();
                }
                return getConsentsByStatus(status).filterWhen(this::isOwnerUser);
            });
        });
    }

    private Mono<Consent> getConsentById(String id) {
        final Mono<String> currentActorId = consentUserService.getCurrentClientId();
        return currentActorId.flatMap(actorId -> consentRepository.findFirstByIdAndActor_Id(id, actorId))
				.switchIfEmpty(Mono.error(new ConsentNotFoundException())).map(Consent::new);
    }

    Mono<Consent> getConsentForRoleById(String role, String id) {
        Objects.requireNonNull(role, "Role must not be null!");
        Objects.requireNonNull(id, "Id must not be null!");

        final Mono<Boolean> hasUserRoleMono = consentUserService.hasUserRole(role);
        return hasUserRoleMono.flatMap(hasUserRole -> {
            if (hasUserRole) {
                return getConsentById(id);
            }
            final Mono<Boolean> hasOwnerRole = consentUserService.hasUserRole(ConsentUserService.OWNER_ROLE);
            return hasOwnerRole.flatMap(isOwner -> {
                if (!isOwner) {
                    return Mono.error(new ConsentNotFoundException());
                }
                return getConsentById(id).filterWhen(this::isOwnerUser)
						.switchIfEmpty(Mono.error(new ConsentNotFoundException()));
            });
        });
    }

    public Mono<Boolean> noActiveConsentExists(ConsentUser actor, String ownerId) {
        final String actorId = actor.getId();
        final String status = ConsentStatus.ACTIVE.name();
        return consentRepository
                .existsByActor_IdAndOwnerIdAndStatus(actorId, ownerId, status)
                .map(b -> Boolean.TRUE.equals(b) ? Boolean.FALSE : Boolean.TRUE);
    }

    public Mono<Consent> createConsentFromConsentRequest(ConsentRequest consentRequest) {
        return Mono.just(new Consent(consentRequest))
                .map(Consent::toEntity)
                .flatMap(consentRepository::save)
                .map(Consent::new)
                .doOnNext(consent -> log.info("Consent created: id={}", consent.getId()))
                .zipWith(consentUserService.getCurrentClientId())
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(t -> {
                    final Consent consent = t.getT1();
                    final String actorId = t.getT2();
                    final ConsentCreated event = ConsentService.createConsentCreatedEvent(consent, actorId);
                    eventbus.publishEventAsync(event);
                }).map(Tuple2::getT1);
    }


    private void deleteConsentsByOwnerId(String ownerId) {
        consentRepository.deleteByOwnerId(ownerId)
                .doOnSuccess(ignored -> log.info("Consents deleted for owner:  ownerId={}", ownerId))
                .doOnError(e -> log.warn("Consents deletion for owner failed: ownerId={} error={}", ownerId,
                        e.getMessage()))
                .subscribe();
    }
}
