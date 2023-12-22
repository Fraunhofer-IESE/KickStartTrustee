package de.fhg.iese.kickstarttrustee.owner.api.dto;

import java.time.Instant;

import de.fhg.iese.kickstarttrustee.owner.business.model.OwnerProfile;

public record OwnerProfileDTO(String id, IdpProfileDTO idpProfile, String preferredLanguage, Instant createdAt) {
    public OwnerProfileDTO(OwnerProfile ownerProfile) {
        this(ownerProfile.getId().orElseThrow(), new IdpProfileDTO(ownerProfile.getIdpProfile()), ownerProfile.getPreferredLanguage().orElse(null), ownerProfile.getCreatedAt());
    }
}
