package de.fhg.iese.kickstarttrustee.consent.api.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.fhg.iese.kickstarttrustee.consent.api.dto.ProviderConsentDTO;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentStatus;
import de.fhg.iese.kickstarttrustee.consent.business.service.ProviderConsentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/provider/consent")
@Tag(name = "provider.consent")
public class ProviderConsentController {
    private final ProviderConsentService consentService;

    public ProviderConsentController(ProviderConsentService consentService) {
        this.consentService = consentService;
    }

    @GetMapping
    public Flux<ProviderConsentDTO> getAllConsents(@RequestParam Optional<ConsentStatus> status) {
        return consentService.getAllConsents(status).map(ProviderConsentDTO::new);
    }

    @GetMapping("/{id}")
    public Mono<ProviderConsentDTO> getConsentById(@PathVariable String id) {
        return consentService.getConsentById(id).map(ProviderConsentDTO::new);
    }
}
