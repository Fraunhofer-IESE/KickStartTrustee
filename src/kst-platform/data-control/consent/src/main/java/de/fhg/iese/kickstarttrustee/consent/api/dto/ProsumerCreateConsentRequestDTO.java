package de.fhg.iese.kickstarttrustee.consent.api.dto;

import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentPurpose;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

public record ProsumerCreateConsentRequestDTO(@NotBlank String ownerId,
											  @NotNull Set<String> consumedDataItemTypes,
											  @NotNull Set<String> providedDataItemTypes,
											  String dataUsageStatement,
											  ConsentPurpose purpose) {
}
