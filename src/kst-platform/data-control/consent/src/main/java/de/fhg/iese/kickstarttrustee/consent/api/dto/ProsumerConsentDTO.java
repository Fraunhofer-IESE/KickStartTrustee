package de.fhg.iese.kickstarttrustee.consent.api.dto;

import java.time.Instant;
import java.util.Set;

import de.fhg.iese.kickstarttrustee.consent.business.model.Consent;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentPurpose;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record ProsumerConsentDTO(@NotBlank String id, @NotBlank String consentRequestId, @NotNull Instant createdAt,
								 @NotNull Set<String> consumedDataItemTypes,
								 @NotNull Set<String> providedDataItemTypes,
								 String dataUsageStatement, ConsentPurpose purpose,
								 @NotNull ConsentStatus status) {

	public ProsumerConsentDTO {
		consumedDataItemTypes = Set.copyOf(consumedDataItemTypes);
		providedDataItemTypes = Set.copyOf(providedDataItemTypes);
	}

	public ProsumerConsentDTO(Consent consent) {
		this(consent.getId(), consent.getConsentRequestId(), consent.getCreatedAt(),
				consent.getConsentPermissions().getConsumedDataItemTypes(),
				consent.getConsentPermissions().getProvidedDataItemTypes(),
				consent.getDataUsageStatement().orElse(null), consent.getPurpose().orElse(null),
				consent.getStatus());
	}
}
