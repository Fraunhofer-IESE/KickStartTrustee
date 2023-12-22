package de.fhg.iese.kickstarttrustee.owner.api.dto;

import javax.validation.constraints.NotBlank;

public record UpdateOwnerProfileDTO(@NotBlank String preferredLanguage) {
    
}
