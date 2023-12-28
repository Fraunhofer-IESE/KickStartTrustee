package de.fhg.iese.kickstarttrustee.consent.api.dto;

import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentPurpose;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentRequest;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentRequestStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.Set;

public record ProsumerConsentRequestDTO(@NotBlank String id, @NotBlank String ownerId,
										@NotNull Set<String> consumedDataItemTypes,
										@NotNull Set<String> providedDataItemTypes,
										Optional<String> dataUsageStatement, ConsentPurpose purpose,
										@NotNull ConsentRequestStatus status) {

	public ProsumerConsentRequestDTO {
		consumedDataItemTypes = Set.copyOf(consumedDataItemTypes);
		providedDataItemTypes = Set.copyOf(providedDataItemTypes);
	}

	public ProsumerConsentRequestDTO(ConsentRequest consentRequest) {
		this(consentRequest.getId(), consentRequest.getOwnerId(),
				consentRequest.getConsentPermissions().getConsumedDataItemTypes(),
				consentRequest.getConsentPermissions().getProvidedDataItemTypes(),
				consentRequest.getDataUsageStatement(), consentRequest.getPurpose().orElse(ConsentPurpose.OTHER),
				consentRequest.getStatus());
	}
}
