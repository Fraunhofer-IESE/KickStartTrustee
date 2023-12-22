package de.fhg.iese.kickstarttrustee.consent.api.controller;

import de.fhg.iese.kickstarttrustee.consent.api.dto.OwnerConsentRequestDTO;
import de.fhg.iese.kickstarttrustee.consent.api.dto.UpdateConsentRequestDTO;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentRequestStatus;
import de.fhg.iese.kickstarttrustee.consent.business.service.OwnerConsentRequestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/owner/consent-request")
@Tag(name = "owner.consent")
public class OwnerConsentRequestController {
    private final OwnerConsentRequestService consentRequestService;

    public OwnerConsentRequestController(OwnerConsentRequestService consentRequestService) {
        this.consentRequestService = consentRequestService;
    }

    @GetMapping
    public Flux<OwnerConsentRequestDTO> getOwnedConsentRequests(@RequestParam Optional<String> requesterId, @RequestParam Optional<ConsentRequestStatus> status) {
        return consentRequestService.getAllOwnedConsentRequests(requesterId, status).map(OwnerConsentRequestDTO::new);
    }

    @GetMapping("/{id}")
    public Mono<OwnerConsentRequestDTO> getOwnedConsentRequestById(@PathVariable String id) {
        return consentRequestService.getOwnedConsentRequestById(id).map(OwnerConsentRequestDTO::new);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> updateOwnedConsentRequest(@PathVariable String id,
                                                @RequestBody @Valid UpdateConsentRequestDTO requestDTO) {
        return consentRequestService.updateOwnedConsentRequestStatus(id, requestDTO.status());
    }
}
