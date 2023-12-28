package de.fhg.iese.kickstarttrustee.owner.business.service;

import de.fhg.iese.kickstarttrustee.event.IEventbus;
import de.fhg.iese.kickstarttrustee.event.account.OwnerAccountDeletedEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class OwnerAccountService {
    private static final Logger log = LoggerFactory.getLogger(OwnerAccountService.class);

    private final IEventbus eventbus;
    private final OwnerIdpService ownerIdpService;
    private final OwnerProfileService ownerProfileService;

    public OwnerAccountService(IEventbus eventbus, OwnerIdpService ownerIdpService, OwnerProfileService ownerProfileService) {
        this.eventbus = eventbus;
        this.ownerIdpService = ownerIdpService;
        this.ownerProfileService = ownerProfileService;
    }

    public Mono<Void> deleteAccountById(String id) {
        return ownerProfileService.deleteOwnProfile(id)
                .then(ownerIdpService.deleteUserById(id))
                .doOnSuccess(ignored -> log.info("Account deleted: id={}", id))
                .publishOn(Schedulers.boundedElastic())
                .doOnSuccess(ignored -> eventbus.publishEventAsync(new OwnerAccountDeletedEvent(id)));
    }
}
