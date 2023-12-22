package de.fhg.iese.kickstarttrustee.consent.business.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import de.fhg.iese.kickstarttrustee.consent.business.exception.WrongConsentRequestStatusException;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentRequest;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentPurpose;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentRequestStatus;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentOperationType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ConsumerConsentRequestService {
    private final ConsentRequestService consentRequestService;

    public ConsumerConsentRequestService(ConsentRequestService consentRequestService) {
        this.consentRequestService = consentRequestService;
    }

    public Mono<ConsentRequest> getRequestedConsentRequestById(String id) {
        return consentRequestService.getConsentRequestForRoleById(ConsentUserService.CONSUMER_ROLE, id);
    }

    public Flux<ConsentRequest> getAllRequestedConsentRequests(Optional<ConsentRequestStatus> status) {
        return consentRequestService.getAllConsentRequestsForRole(ConsentUserService.CONSUMER_ROLE, status);
    }

    public Mono<ConsentRequest> requestConsent(String ownerId, Set<String> dataItemTypes, String dataUsageStatement, ConsentPurpose purpose) {
        return consentRequestService.requestConsentForRole(ConsentUserService.CONSUMER_ROLE, ownerId, dataItemTypes, ConsentOperationType.DATA_CONSUMPTION, dataUsageStatement, purpose);
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
