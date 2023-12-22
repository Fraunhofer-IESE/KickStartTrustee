package de.fhg.iese.kickstarttrustee.consent.business.service;

import de.fhg.iese.kickstarttrustee.consent.business.model.Consent;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class ProsumerConsentService {
	private final ConsentService consentService;

	public ProsumerConsentService(ConsentService consentService) {
		this.consentService = consentService;
	}

	public Flux<Consent> getAllConsents(Optional<ConsentStatus> status) {
		return consentService.getAllConsentsForRole(ConsentUserService.PROSUMER_ROLE, status);
	}

	public Mono<Consent> getConsentById(String id) {
		return consentService.getConsentForRoleById(ConsentUserService.PROSUMER_ROLE, id);
	}
}
