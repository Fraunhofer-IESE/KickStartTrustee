package de.fhg.iese.kickstarttrustee.consent.business.service;

import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentOwner;
import de.fhg.iese.kickstarttrustee.consent.persistence.repository.ConsentOwnerRepository;
import de.fhg.iese.kickstarttrustee.event.IEventbus;
import de.fhg.iese.kickstarttrustee.event.account.OwnerAccountDeletedEvent;
import de.fhg.iese.kickstarttrustee.event.profile.OwnerProfileCreated;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class ConsentOwnerService implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(ConsentOwnerService.class);

    private final IEventbus eventbus;
    private final ConsentOwnerRepository repository;

    public ConsentOwnerService(IEventbus eventbus, ConsentOwnerRepository repository) {
        this.eventbus = eventbus;
        this.repository = repository;
    }

    @Override
    public void afterPropertiesSet() {
        eventbus.registerConsumer(OwnerProfileCreated.class, this::onOwnerProfileCreated);
        eventbus.registerConsumer(OwnerAccountDeletedEvent.class, this::onOwnerAccountDeleted);
    }

    public Mono<Boolean> existsByUserId(String userId) {
        return repository.existsByUserId(userId);
    }

    private void onOwnerProfileCreated(OwnerProfileCreated event) {
        Mono.just(event)
                .map(e -> new ConsentOwner(e.id(), e.userId()))
                .map(ConsentOwner::toEntity)
                .flatMap(repository::save)
                .subscribe();
    }

    private void onOwnerAccountDeleted(OwnerAccountDeletedEvent event) {
        final String userId = event.accountId();
        repository.deleteByUserId(userId)
                .doOnSuccess(ignored -> log.info("Consent owner deleted for user:  userId={}", userId))
                .doOnError(e -> log.warn("Consent owner deletion for user failed: userId={} error={}", userId,
                        e.getMessage()))
                .subscribe();
    }
}
