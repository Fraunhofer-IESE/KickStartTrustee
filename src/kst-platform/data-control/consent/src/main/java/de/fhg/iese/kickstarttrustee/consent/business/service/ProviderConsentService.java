package de.fhg.iese.kickstarttrustee.consent.business.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import de.fhg.iese.kickstarttrustee.consent.business.model.Consent;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProviderConsentService {
    private final ConsentService consentService;

    public ProviderConsentService(ConsentService consentService) {
        this.consentService = consentService;
    }

    public Flux<Consent> getAllConsents(Optional<ConsentStatus> status) {
        return consentService.getAllConsentsForRole(ConsentUserService.PROVIDER_ROLE, status);
    }

    public Mono<Consent> getConsentById(String id) {
        return consentService.getConsentForRoleById(ConsentUserService.PROVIDER_ROLE, id);
    }
}
