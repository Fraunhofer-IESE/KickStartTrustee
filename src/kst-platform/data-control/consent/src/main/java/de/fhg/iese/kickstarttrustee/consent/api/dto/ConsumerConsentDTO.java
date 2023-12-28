package de.fhg.iese.kickstarttrustee.consent.api.dto;

import de.fhg.iese.kickstarttrustee.consent.business.model.Consent;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentPurpose;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentStatus;

import java.time.Instant;
import java.util.Set;

public record ConsumerConsentDTO(String id, String consentRequestId, String ownerId, Instant createdAt,
								 Set<String> dataItemTypes, String dataUsageStatement, ConsentPurpose purpose,
								 ConsentStatus status) {

	public ConsumerConsentDTO {
		dataItemTypes = Set.copyOf(dataItemTypes);
	}

	public ConsumerConsentDTO(Consent consent) {
		this(consent.getId(), consent.getConsentRequestId(), consent.getOwnerId(), consent.getCreatedAt(),
				consent.getConsentPermissions().getConsumedDataItemTypes(),
				consent.getDataUsageStatement().orElse(null), consent.getPurpose().orElse(null),
				consent.getStatus());
	}
}
