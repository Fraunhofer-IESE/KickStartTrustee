package de.fhg.iese.kickstarttrustee.consent.business.service;

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import de.fhg.iese.kickstarttrustee.consent.business.exception.ConsentRequestModifiedException;
import de.fhg.iese.kickstarttrustee.consent.business.exception.ConsentRequestNotFoundException;
import de.fhg.iese.kickstarttrustee.consent.business.exception.WrongConsentRequestStatusException;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentRequest;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentRequestStatus;
import de.fhg.iese.kickstarttrustee.consent.persistence.entity.ConsentRequestEntity;
import de.fhg.iese.kickstarttrustee.consent.persistence.repository.ConsentRequestRepository;
import de.fhg.iese.kickstarttrustee.event.IEventbus;
import de.fhg.iese.kickstarttrustee.event.account.OwnerAccountDeletedEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OwnerConsentRequestService implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(OwnerConsentRequestService.class);
    private static final Sort DEFAULT_SORT = Sort.by(Direction.DESC, "createdAt");

    private final IEventbus eventbus;
    private final ConsentService consentService;
    private final ConsentUserService consentUserService;
    private final ConsentRequestRepository consentRequestRepository;

    public OwnerConsentRequestService(IEventbus eventbus, ConsentService consentService,
            ConsentUserService consentUserService, ConsentRequestRepository consentRequestRepository) {
        this.eventbus = eventbus;
        this.consentService = consentService;
        this.consentUserService = consentUserService;
        this.consentRequestRepository = consentRequestRepository;
    }

    @Override
    public void afterPropertiesSet() {
        eventbus.registerConsumer(OwnerAccountDeletedEvent.class, e -> this.deleteConsentRequestsByOwnerId(e.accountId()));
    }

    public Mono<ConsentRequest> getOwnedConsentRequestById(String id) {
        final Mono<String> currentUserId = consentUserService.getCurrentUserId();
        final Mono<ConsentRequestEntity> entity = currentUserId.flatMap(userId -> consentRequestRepository.findById(id)
                .filter(c -> Objects.equals(c.ownerId(), userId)));
        return entity.switchIfEmpty(Mono.error(new ConsentRequestNotFoundException()))
                .map(ConsentRequest::new);
    }

    public Flux<ConsentRequest> getAllOwnedConsentRequests(Optional<String> requesterIdParam, Optional<ConsentRequestStatus> statusParam) {
        final Mono<String> currentUserId = consentUserService.getCurrentUserId();
        Flux<ConsentRequestEntity> entityFlux = currentUserId.flatMapMany(ownerId -> consentRequestRepository.findAllByOwnerId(ownerId, DEFAULT_SORT));
        if (requesterIdParam.isPresent()) {
            final String requesterId = requesterIdParam.get();
            entityFlux = entityFlux.filter(c -> requesterId.equals(c.requesterId()));
        }
        if (statusParam.isPresent()) {
            final String consentRequestStatus = statusParam.get().name();
            entityFlux = entityFlux.filter(c -> consentRequestStatus.equals(c.status()));
        }
        return entityFlux.map(ConsentRequest::new);
    }

    public Mono<Void> acceptConsentRequest(String id) {
        final Mono<ConsentRequest> consentRequest = this.getOwnedConsentRequestById(id);
        return consentRequest.flatMap(c -> {
                    c.accept();
                    return consentRequestRepository.save(c.toEntity());
                })
                .onErrorMap(OptimisticLockingFailureException.class, ignored -> new ConsentRequestModifiedException())
                .doOnNext(c -> log.info("Consent request accepted: id={}", c.id()))
                .map(ConsentRequest::new)
                .flatMap(consentService::createConsentFromConsentRequest)
                .then();
    }

    public Mono<Void> rejectConsentRequest(String id) {
        final Mono<ConsentRequest> consentRequest = this.getOwnedConsentRequestById(id);
        return consentRequest.flatMap(c -> {
                    c.reject();
                    return consentRequestRepository.save(c.toEntity());
                })
                .onErrorMap(OptimisticLockingFailureException.class, ignored -> new ConsentRequestModifiedException())
                .doOnNext(c -> log.info("Consent request rejected: id={}", c.id()))
                .then();
    }

    public Mono<Void> updateOwnedConsentRequestStatus(String id, ConsentRequestStatus status) {
        switch (status) {
            case ACCEPTED -> {
                return this.acceptConsentRequest(id);
            }
            case REJECTED -> {
                return this.rejectConsentRequest(id);
            }
            default -> {
            }
        }
        return Mono.error(new WrongConsentRequestStatusException());
    }

    private void deleteConsentRequestsByOwnerId(String ownerId) {
        consentRequestRepository.deleteByOwnerId(ownerId)
                .doOnSuccess(ignored -> log.info("Consent request deleted for owner:  ownerId={}", ownerId))
                .doOnError(e -> log.warn("Consent request deletion for owner failed: ownerId={} error={}", ownerId,
                        e.getMessage()))
                .subscribe();
    }
}
