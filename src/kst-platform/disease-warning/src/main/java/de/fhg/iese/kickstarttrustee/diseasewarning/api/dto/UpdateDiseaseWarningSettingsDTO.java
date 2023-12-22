package de.fhg.iese.kickstarttrustee.diseasewarning.api.dto;

public record UpdateDiseaseWarningSettingsDTO(boolean isDiseaseReportProcessingEnabled,
        boolean isDiseaseWarningCreationEnabled, boolean isEmailNotificationEnabled) {
}
