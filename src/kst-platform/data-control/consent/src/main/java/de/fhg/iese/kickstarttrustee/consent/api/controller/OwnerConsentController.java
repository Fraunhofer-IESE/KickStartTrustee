package de.fhg.iese.kickstarttrustee.consent.api.controller;

import java.util.Optional;

import de.fhg.iese.kickstarttrustee.consent.business.service.OwnerConsentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.fhg.iese.kickstarttrustee.consent.api.dto.OwnerConsentDTO;
import de.fhg.iese.kickstarttrustee.consent.api.dto.UpdateConsentDTO;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/owner/consent")
@Tag(name = "owner.consent")
public class OwnerConsentController {
    private final OwnerConsentService consentService;

    public OwnerConsentController(OwnerConsentService consentService) {
        this.consentService = consentService;
    }

    @GetMapping
    public Flux<OwnerConsentDTO> getOwnedConsents(@RequestParam Optional<ConsentStatus> status) {
        return consentService.getAllConsents(status).map(OwnerConsentDTO::new);
    }

    @GetMapping("/{id}")
    public Mono<OwnerConsentDTO> getOwnedConsentById(@PathVariable String id) {
        return consentService.getConsentById(id).map(OwnerConsentDTO::new);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> updateOwnedConsent(@PathVariable String id, @RequestBody @Valid UpdateConsentDTO requestDTO) {
        return consentService.updateConsent(id, requestDTO.status());
    }
}
