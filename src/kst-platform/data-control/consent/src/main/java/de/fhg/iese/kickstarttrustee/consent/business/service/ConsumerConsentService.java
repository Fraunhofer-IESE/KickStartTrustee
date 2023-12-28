package de.fhg.iese.kickstarttrustee.consent.business.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import de.fhg.iese.kickstarttrustee.consent.business.model.Consent;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ConsumerConsentService {
    private final ConsentService consentService;

    public ConsumerConsentService(ConsentService consentService) {
        this.consentService = consentService;
    }

    public Flux<Consent> getAllConsents(Optional<ConsentStatus> status) {
        return consentService.getAllConsentsForRole(ConsentUserService.CONSUMER_ROLE, status);
    }

    public Mono<Consent> getConsentById(String id) {
        return consentService.getConsentForRoleById(ConsentUserService.CONSUMER_ROLE, id);
    }
}
