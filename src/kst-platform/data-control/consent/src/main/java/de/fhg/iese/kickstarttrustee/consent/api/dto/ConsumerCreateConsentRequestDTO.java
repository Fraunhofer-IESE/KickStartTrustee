package de.fhg.iese.kickstarttrustee.consent.api.dto;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentPurpose;

public record ConsumerCreateConsentRequestDTO(@NotBlank String ownerId, @NotEmpty Set<String> dataItemTypes,
        @NotBlank String dataUsageStatement, @NotNull ConsentPurpose purpose) {
    public ConsumerCreateConsentRequestDTO {
        dataItemTypes = Set.copyOf(dataItemTypes);
    }
}
