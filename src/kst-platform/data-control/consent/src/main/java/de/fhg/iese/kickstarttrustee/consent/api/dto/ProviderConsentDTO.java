package de.fhg.iese.kickstarttrustee.consent.api.dto;

import java.time.Instant;
import java.util.Set;

import de.fhg.iese.kickstarttrustee.consent.business.model.Consent;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentStatus;

public record ProviderConsentDTO(String id, String consentRequestId, String ownerId, Instant createdAt,
								 Set<String> dataItemTypes, ConsentStatus status) {

    public ProviderConsentDTO {
        dataItemTypes = Set.copyOf(dataItemTypes);
    }

    public ProviderConsentDTO(Consent consent) {
        this(consent.getId(), consent.getConsentRequestId(), consent.getOwnerId(), consent.getCreatedAt(),
                consent.getConsentPermissions().getProvidedDataItemTypes(), consent.getStatus());
    }
}
