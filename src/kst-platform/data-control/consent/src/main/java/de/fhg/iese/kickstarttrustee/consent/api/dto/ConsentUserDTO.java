package de.fhg.iese.kickstarttrustee.consent.api.dto;

import de.fhg.iese.kickstarttrustee.consent.business.model.ConsentUser;

public record ConsentUserDTO(String id, String name) {
    public ConsentUserDTO(ConsentUser consentUser) {
        this(consentUser.getId(), consentUser.getName());
    }
}
