package de.fhg.iese.kickstarttrustee.consent.api.dto;

import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentPurpose;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentRequest;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentRequestStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.Set;

public record ConsumerConsentRequestDTO(@NotBlank String id, @NotBlank String ownerId,
										@NotNull Set<String> dataItemTypes, Optional<String> dataUsageStatement,
										ConsentPurpose purpose, @NotNull ConsentRequestStatus status) {
	public ConsumerConsentRequestDTO {
		dataItemTypes = Set.copyOf(dataItemTypes);
	}

	public ConsumerConsentRequestDTO(ConsentRequest consentRequest) {
		this(consentRequest.getId(), consentRequest.getOwnerId(),
				consentRequest.getConsentPermissions().getConsumedDataItemTypes(),
				consentRequest.getDataUsageStatement(), consentRequest.getPurpose().orElse(null),
				consentRequest.getStatus());
	}
}
