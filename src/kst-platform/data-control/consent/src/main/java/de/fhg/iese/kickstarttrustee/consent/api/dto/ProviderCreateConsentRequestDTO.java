package de.fhg.iese.kickstarttrustee.consent.api.dto;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public record ProviderCreateConsentRequestDTO(@NotBlank String ownerId, @NotEmpty Set<String> dataItemTypes) {
    public ProviderCreateConsentRequestDTO {
        dataItemTypes = Set.copyOf(dataItemTypes);
    }
}
