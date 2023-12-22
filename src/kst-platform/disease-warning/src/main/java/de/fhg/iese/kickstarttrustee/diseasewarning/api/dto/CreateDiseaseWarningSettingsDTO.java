package de.fhg.iese.kickstarttrustee.diseasewarning.api.dto;

public record CreateDiseaseWarningSettingsDTO(boolean isDiseaseReportProcessingEnabled,
        boolean isDiseaseWarningCreationEnabled, boolean isEmailNotificationEnabled) {
}
