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

import de.fhg.iese.kickstarttrustee.consent.api.dto.ConsumerConsentRequestDTO;
import de.fhg.iese.kickstarttrustee.consent.api.dto.ConsumerCreateConsentRequestDTO;
import de.fhg.iese.kickstarttrustee.consent.api.dto.UpdateConsentRequestDTO;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentRequestStatus;
import de.fhg.iese.kickstarttrustee.consent.business.service.ConsumerConsentRequestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/consumer/consent-request")
@Tag(name = "consumer.consent")
public class ConsumerConsentRequestController {
    private final ConsumerConsentRequestService consentRequestService;

    public ConsumerConsentRequestController(ConsumerConsentRequestService consentRequestService) {
        this.consentRequestService = consentRequestService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ConsumerConsentRequestDTO> createConsentRequest(
            @RequestBody @Valid ConsumerCreateConsentRequestDTO requestDTO) {
        return consentRequestService.requestConsent(requestDTO.ownerId(), requestDTO.dataItemTypes(),
                requestDTO.dataUsageStatement(), requestDTO.purpose()).map(ConsumerConsentRequestDTO::new);
    }

    @GetMapping
    public Flux<ConsumerConsentRequestDTO> getAllRequestedConsentRequests(@RequestParam Optional<ConsentRequestStatus> status) {
        return consentRequestService.getAllRequestedConsentRequests(status).map(ConsumerConsentRequestDTO::new);
    }

    @GetMapping("/{id}")
    public Mono<ConsumerConsentRequestDTO> getRequestedConsentRequestById(@PathVariable String id) {
        return consentRequestService.getRequestedConsentRequestById(id).map(ConsumerConsentRequestDTO::new);
    }

    @PatchMapping("/{id}")
    public Mono<Void> updateRequestedConsentRequest(@PathVariable String id,
            @RequestBody @Valid UpdateConsentRequestDTO requestDTO) {
        return consentRequestService.updateRequestedConsentRequestStatus(id, requestDTO.status());
    }
}
