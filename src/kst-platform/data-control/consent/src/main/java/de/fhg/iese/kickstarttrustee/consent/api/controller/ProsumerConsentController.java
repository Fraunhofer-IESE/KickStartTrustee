package de.fhg.iese.kickstarttrustee.consent.api.controller;

import de.fhg.iese.kickstarttrustee.consent.api.dto.ProsumerConsentDTO;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentStatus;
import de.fhg.iese.kickstarttrustee.consent.business.service.ProsumerConsentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/prosumer/consent")
@Tag(name = "prosumer.consent")
public class ProsumerConsentController {
	private final ProsumerConsentService consentService;

	public ProsumerConsentController(ProsumerConsentService consentService) {
		this.consentService = consentService;
	}

	@GetMapping
	public Flux<ProsumerConsentDTO> getAllConsents(@RequestParam Optional<ConsentStatus> status) {
		return consentService.getAllConsents(status).map(ProsumerConsentDTO::new);
	}

	@GetMapping("/{id}")
	public Mono<ProsumerConsentDTO> getConsentById(@PathVariable String id) {
		return consentService.getConsentById(id).map(ProsumerConsentDTO::new);
	}
}
