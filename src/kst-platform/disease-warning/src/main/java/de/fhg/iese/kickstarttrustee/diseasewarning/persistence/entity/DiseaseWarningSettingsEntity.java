package de.fhg.iese.kickstarttrustee.diseasewarning.persistence.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("disease_warning_settings")
public record DiseaseWarningSettingsEntity(String id, @Indexed String ownerId, boolean isDiseaseReportProcessingEnabled,
                                           boolean isDiseaseWarningCreationEnabled,
                                           boolean isEmailNotificationEnabled) {
}
