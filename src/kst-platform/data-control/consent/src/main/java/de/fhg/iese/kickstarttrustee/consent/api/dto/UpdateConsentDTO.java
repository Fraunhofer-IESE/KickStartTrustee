package de.fhg.iese.kickstarttrustee.consent.api.dto;

import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record UpdateConsentDTO(@NotBlank String id, @NotNull ConsentStatus status) {
}
