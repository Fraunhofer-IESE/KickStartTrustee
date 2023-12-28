package de.fhg.iese.kickstarttrustee.consent.api.dto;

import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentPurpose;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentRequest;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentRequestStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Set;

public record OwnerConsentRequestDTO(@NotBlank String id, ConsentUserDTO requester, @NotNull Instant createdAt,
									 @NotNull Set<String> consumedDataItemTypes,
									 @NotNull Set<String> providedDataItemTypes,
									 String dataUsageStatement, ConsentPurpose purpose,
									 @NotNull ConsentRequestStatus status) {

	public OwnerConsentRequestDTO {
		consumedDataItemTypes = Set.copyOf(consumedDataItemTypes);
		providedDataItemTypes = Set.copyOf(providedDataItemTypes);
	}

	public OwnerConsentRequestDTO(ConsentRequest consentRequest) {
		this(consentRequest.getId(), new ConsentUserDTO(consentRequest.getRequester()), consentRequest.getCreatedAt(),
				consentRequest.getConsentPermissions().getConsumedDataItemTypes(),
				consentRequest.getConsentPermissions().getProvidedDataItemTypes(),
				consentRequest.getDataUsageStatement().orElse(null),
				consentRequest.getPurpose().orElse(null), consentRequest.getStatus());
	}
}
