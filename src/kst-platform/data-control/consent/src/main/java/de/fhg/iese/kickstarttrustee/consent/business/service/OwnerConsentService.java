package de.fhg.iese.kickstarttrustee.consent.business.service;

import de.fhg.iese.kickstarttrustee.consent.business.exception.ConsentModifiedException;
import de.fhg.iese.kickstarttrustee.consent.business.exception.ConsentNotFoundException;
import de.fhg.iese.kickstarttrustee.consent.business.exception.WrongConsentStatusException;
import de.fhg.iese.kickstarttrustee.consent.business.model.Consent;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentStatus;
import de.fhg.iese.kickstarttrustee.consent.persistence.entity.ConsentEntity;
import de.fhg.iese.kickstarttrustee.consent.persistence.repository.ConsentRepository;
import de.fhg.iese.kickstarttrustee.event.IEventbus;
import de.fhg.iese.kickstarttrustee.event.consent.ConsentRevoked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static de.fhg.iese.kickstarttrustee.consent.business.service.ConsentService.DEFAULT_SORT;

@Service
public class OwnerConsentService {
	private static final Logger log = LoggerFactory.getLogger(OwnerConsentService.class);

	private final IEventbus eventbus;
	private final ConsentUserService consentUserService;
	private final ConsentRepository consentRepository;

	public OwnerConsentService(IEventbus eventbus, ConsentUserService consentUserService,
							   ConsentRepository consentRepository) {
		this.eventbus = eventbus;
		this.consentUserService = consentUserService;
		this.consentRepository = consentRepository;
	}

	private static ConsentRevoked createConsentRevokedEvent(Consent consent, String actorId) {
		final String id = consent.getId();
		final String consentActorId = consent.getActor().getId();
		final String ownerId = consent.getOwnerId();
		final Map<String, Set<String>> consentPermissions = consent.getConsentPermissions().toSerializedMap();
		return new ConsentRevoked(id, actorId, consentActorId, ownerId, consentPermissions);
	}

	public Flux<Consent> getAllConsents(Optional<ConsentStatus> status) {
		final Mono<String> currentUserId = consentUserService.getCurrentUserId();
		return currentUserId.flatMapMany(ownerId -> {
			if (status.isEmpty()) {
				return consentRepository.findAllByOwnerId(ownerId, DEFAULT_SORT);
			}
			final String consentStatus = status.get().name();
			return consentRepository.findAllByOwnerIdAndStatus(ownerId, consentStatus, DEFAULT_SORT);
		}).map(Consent::new);
	}

	public Mono<Consent> getConsentById(String id) {
		final Mono<String> currentUserId = consentUserService.getCurrentUserId();
		final Mono<ConsentEntity> entity = currentUserId.flatMap(userId -> consentRepository.findFirstByIdAndOwnerId(id, userId));
		return entity.switchIfEmpty(Mono.error(ConsentNotFoundException::new)).map(Consent::new);
	}

	public Mono<Consent> revokeConsent(String id) {
		return this.getConsentById(id)
				.flatMap(c -> {
					c.revoke();
					return consentRepository.save(c.toEntity());
				})
				.map(Consent::new)
				.onErrorMap(OptimisticLockingFailureException.class, ignored -> new ConsentModifiedException())
				.doOnNext(c -> log.info("Consent revoked: id={}", c.getId()))
				.zipWith(consentUserService.getCurrentClientId())
				.publishOn(Schedulers.boundedElastic())
				.doOnNext(t -> {
					final Consent consent = t.getT1();
					final String actorId = t.getT2();
					final ConsentRevoked consentRevoked = createConsentRevokedEvent(consent, actorId);
					eventbus.publishEventAsync(consentRevoked);
				}).map(Tuple2::getT1);
	}

	public Mono<Void> updateConsent(String id, ConsentStatus status) {
		if (status != ConsentStatus.REVOKED) {
			return Mono.error(new WrongConsentStatusException());
		}
		return this.revokeConsent(id).then();
	}
}
