package de.fhg.iese.kickstarttrustee.consent.api.dto;

import de.fhg.iese.kickstarttrustee.consent.business.model.Consent;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentPurpose;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Set;

public record OwnerConsentDTO(@NotBlank String id, @NotNull String consentRequestId, ConsentUserDTO actor,
							  @NotNull Instant createdAt, @NotNull Set<String> consumedDataItemTypes,
							  @NotNull Set<String> providedDataItemTypes,
							  String dataUsageStatement, ConsentPurpose purpose, @NotNull ConsentStatus status) {

    public OwnerConsentDTO {
		consumedDataItemTypes = Set.copyOf(consumedDataItemTypes);
		providedDataItemTypes = Set.copyOf(providedDataItemTypes);
    }

    public OwnerConsentDTO(Consent consent) {
        this(consent.getId(), consent.getConsentRequestId(), new ConsentUserDTO(consent.getActor()),
                consent.getCreatedAt(), consent.getConsentPermissions().getConsumedDataItemTypes(),
				consent.getConsentPermissions().getProvidedDataItemTypes(),
				consent.getDataUsageStatement().orElse(null), consent.getPurpose().orElse(null),
				consent.getStatus());
    }
}
