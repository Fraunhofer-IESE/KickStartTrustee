package de.fhg.iese.kickstarttrustee.owner.api.dto;

import de.fhg.iese.kickstarttrustee.owner.business.model.IdpProfile;

public record IdpProfileDTO(String userId, String email, String firstname, String lastname) {
    public IdpProfileDTO(IdpProfile idpProfile) {
        this(idpProfile.getId(), idpProfile.getEmail(), idpProfile.getFirstName(), idpProfile.getLastName());
    }
}
