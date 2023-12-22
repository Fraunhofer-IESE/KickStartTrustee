package de.fhg.iese.kickstarttrustee.consent.api.dto;

import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentRequestStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record UpdateConsentRequestDTO(@NotBlank String id, @NotNull ConsentRequestStatus status) {
}
