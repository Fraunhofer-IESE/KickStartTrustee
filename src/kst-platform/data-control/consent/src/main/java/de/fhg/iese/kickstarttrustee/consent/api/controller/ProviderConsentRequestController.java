package de.fhg.iese.kickstarttrustee.consent.api.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.fhg.iese.kickstarttrustee.consent.api.dto.ProviderCreateConsentRequestDTO;
import de.fhg.iese.kickstarttrustee.consent.api.dto.ProviderConsentRequestDTO;
import de.fhg.iese.kickstarttrustee.consent.api.dto.UpdateConsentRequestDTO;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentRequestStatus;
import de.fhg.iese.kickstarttrustee.consent.business.service.ProviderConsentRequestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/provider/consent-request")
@Tag(name = "provider.consent")
public class ProviderConsentRequestController {
    private final ProviderConsentRequestService consentRequestService;

    public ProviderConsentRequestController(ProviderConsentRequestService consentRequestService) {
        this.consentRequestService = consentRequestService;
    }

    @GetMapping
    public Flux<ProviderConsentRequestDTO> getAllRequestedConsentRequests(@RequestParam Optional<ConsentRequestStatus> status) {
        return consentRequestService.getAllRequestedConsentRequests(status).map(ProviderConsentRequestDTO::new);
    }

    @GetMapping("/{id}")
    public Mono<ProviderConsentRequestDTO> getRequestedConsentRequestById(@PathVariable String id) {
        return consentRequestService.getRequestedConsentRequestById(id).map(ProviderConsentRequestDTO::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProviderConsentRequestDTO> createConsentRequest(@RequestBody @Valid ProviderCreateConsentRequestDTO requestDTO) {
        return consentRequestService.requestConsent(requestDTO.ownerId(), requestDTO.dataItemTypes())
                .map(ProviderConsentRequestDTO::new);
    }

    @PatchMapping("/{id}")
    public Mono<Void> updateRequestedConsentRequest(@PathVariable String id, @RequestBody @Valid UpdateConsentRequestDTO requestDTO) {
        return consentRequestService.updateRequestedConsentRequestStatus(id, requestDTO.status());
    }
}
