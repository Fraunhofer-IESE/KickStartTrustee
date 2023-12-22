package de.fhg.iese.kickstarttrustee.diseasewarning.api.dto;

import de.fhg.iese.kickstarttrustee.diseasewarning.business.model.DiseaseWarningSettings;

public record DiseaseWarningSettingsDTO(String id, String ownerId, boolean isDiseaseReportProcessingEnabled,
        boolean isDiseaseWarningCreationEnabled, boolean isEmailNotificationEnabled) {

    public DiseaseWarningSettingsDTO(DiseaseWarningSettings settings) {
        this(settings.getId(), settings.getOwnerId(), settings.isDiseaseReportProcessingEnabled(),
                settings.isDiseaseWarningCreationEnabled(), settings.isEmailNotificationEnabled());
    }
}
