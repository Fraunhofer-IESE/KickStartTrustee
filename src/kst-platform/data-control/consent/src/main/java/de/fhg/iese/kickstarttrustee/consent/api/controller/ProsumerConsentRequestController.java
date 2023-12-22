package de.fhg.iese.kickstarttrustee.consent.api.controller;

import de.fhg.iese.kickstarttrustee.consent.api.dto.ProsumerConsentRequestDTO;
import de.fhg.iese.kickstarttrustee.consent.api.dto.ProsumerCreateConsentRequestDTO;
import de.fhg.iese.kickstarttrustee.consent.api.dto.UpdateConsentRequestDTO;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentRequestStatus;
import de.fhg.iese.kickstarttrustee.consent.business.service.ProsumerConsentRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/prosumer/consent-request")
@Tag(name = "prosumer.consent")
public class ProsumerConsentRequestController {
	private final ProsumerConsentRequestService consentRequestService;

	public ProsumerConsentRequestController(ProsumerConsentRequestService consentRequestService) {
		this.consentRequestService = consentRequestService;
	}


	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(description = """
		Additional rules for the consent request creation:\n
		- If you create a consent request with a not empty consumedDataItemTypes field, then you must provide a dataUsageStatement and a purpose.\n
		- If you create a consent request and an empty consumedDataItemTypes field (this means you requested for data provision only), then you can omit the dataUsageStatement and the purpose.
		"""
    )
	public Mono<ProsumerConsentRequestDTO> createConsentRequest(@RequestBody @Valid ProsumerCreateConsentRequestDTO requestDTO) {
		return consentRequestService.requestConsent(requestDTO.ownerId(), requestDTO.consumedDataItemTypes(),
				requestDTO.providedDataItemTypes(), requestDTO.dataUsageStatement(), requestDTO.purpose())
				.map(ProsumerConsentRequestDTO::new);
	}

	@GetMapping
	public Flux<ProsumerConsentRequestDTO> getAllRequestedConsentRequests(@RequestParam Optional<ConsentRequestStatus> status) {
		return consentRequestService.getAllRequestedConsentRequests(status).map(ProsumerConsentRequestDTO::new);
	}

	@GetMapping("/{id}")
	public Mono<ProsumerConsentRequestDTO> getRequestedConsentRequestById(@PathVariable String id) {
		return consentRequestService.getRequestedConsentRequestById(id).map(ProsumerConsentRequestDTO::new);
	}

	@PatchMapping("/{id}")
	public Mono<Void> updateRequestedConsentRequest(@PathVariable String id,
													@RequestBody @Valid UpdateConsentRequestDTO requestDTO) {
		return consentRequestService.updateRequestedConsentRequestStatus(id, requestDTO.status());
	}
}
