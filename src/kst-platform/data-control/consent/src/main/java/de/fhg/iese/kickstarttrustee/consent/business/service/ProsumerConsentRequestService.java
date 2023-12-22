package de.fhg.iese.kickstarttrustee.consent.business.service;

import de.fhg.iese.kickstarttrustee.consent.business.exception.InvalidConsentRequestException;
import de.fhg.iese.kickstarttrustee.consent.business.exception.WrongConsentRequestStatusException;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentOperationType;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentPermissions;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentPurpose;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentRequest;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentRequestStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.Set;

@Service
public class ProsumerConsentRequestService {
	private final ConsentRequestService consentRequestService;

	public ProsumerConsentRequestService(ConsentRequestService consentRequestService) {
		this.consentRequestService = consentRequestService;
	}

	public Mono<ConsentRequest> getRequestedConsentRequestById(String id) {
		return consentRequestService.getConsentRequestForRoleById(ConsentUserService.PROSUMER_ROLE, id);
	}

	public Flux<ConsentRequest> getAllRequestedConsentRequests(Optional<ConsentRequestStatus> status) {
		return consentRequestService.getAllConsentRequestsForRole(ConsentUserService.PROSUMER_ROLE, status);
	}

	public Mono<ConsentRequest> requestConsent(String ownerId, Set<String> consumedDataItemTypes, Set<String> providedDataItemTypes, String dataUsageStatement, ConsentPurpose purpose) {
		if (!consumedDataItemTypes.isEmpty() && (!StringUtils.hasText(dataUsageStatement) || purpose == null)) {
			throw new InvalidConsentRequestException("DataUsageStatement and/or purpose is missing!");
		}
		final ConsentPermissions consentPermissions = new ConsentPermissions();
		consumedDataItemTypes.forEach(dataItemType -> {
			consentPermissions.addOperationType(dataItemType, ConsentOperationType.DATA_CONSUMPTION);
		});
		providedDataItemTypes.forEach(dataItemType -> {
			consentPermissions.addOperationType(dataItemType,ConsentOperationType.DATA_PROVISION);
		});
		return consentRequestService.requestConsentForRole(ConsentUserService.PROSUMER_ROLE, ownerId, consentPermissions, dataUsageStatement, purpose);
	}

	public Mono<Void> retractConsentRequest(String id) {
		final Mono<ConsentRequest> consentRequest = this.getRequestedConsentRequestById(id);
		return consentRequestService.retractConsentRequest(consentRequest);
	}

	public Mono<Void> updateRequestedConsentRequestStatus(String id, ConsentRequestStatus status) {
		if (status != ConsentRequestStatus.RETRACTED) {
			return Mono.error(new WrongConsentRequestStatusException());
		}
		return this.retractConsentRequest(id);
	}
}
