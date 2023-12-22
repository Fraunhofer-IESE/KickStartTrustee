package de.fhg.iese.kickstarttrustee.consent.api.dto;

import java.util.Set;

import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentRequest;
import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentRequestStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record ProviderConsentRequestDTO(@NotBlank String id, @NotBlank String ownerId,
										@NotNull Set<String> dataItemTypes, @NotNull ConsentRequestStatus status) {
    public ProviderConsentRequestDTO {
        dataItemTypes = Set.copyOf(dataItemTypes);
    }

    public ProviderConsentRequestDTO(ConsentRequest consentRequest) {
        this(consentRequest.getId(), consentRequest.getOwnerId(),
				consentRequest.getConsentPermissions().getProvidedDataItemTypes(),
				consentRequest.getStatus());
    }
}
