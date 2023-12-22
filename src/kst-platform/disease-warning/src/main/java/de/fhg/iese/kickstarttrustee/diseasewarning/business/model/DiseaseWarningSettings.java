package de.fhg.iese.kickstarttrustee.diseasewarning.business.model;

import de.fhg.iese.kickstarttrustee.diseasewarning.persistence.entity.DiseaseWarningSettingsEntity;

import java.util.Objects;

public class DiseaseWarningSettings {
    private final String id;
    private final String ownerId;
    private boolean isDiseaseReportProcessingEnabled;
    private boolean isDiseaseWarningCreationEnabled;
    private boolean isEmailNotificationEnabled;

    public DiseaseWarningSettings(String ownerId, boolean isDiseaseReportProcessingEnabled, boolean isDiseaseWarningCreationEnabled, boolean isEmailNotificationEnabled) {
        this.id = null;
        this.ownerId = ownerId;
        this.isDiseaseReportProcessingEnabled = isDiseaseReportProcessingEnabled;
        this.isDiseaseWarningCreationEnabled = isDiseaseWarningCreationEnabled;
        this.isEmailNotificationEnabled = isEmailNotificationEnabled;
    }

    public DiseaseWarningSettings(DiseaseWarningSettingsEntity entity) {
        this.id = entity.id();
        this.ownerId = entity.ownerId();
        this.isDiseaseReportProcessingEnabled = entity.isDiseaseReportProcessingEnabled();
        this.isDiseaseWarningCreationEnabled = entity.isDiseaseWarningCreationEnabled();
        this.isEmailNotificationEnabled = entity.isEmailNotificationEnabled();
    }

    public String getId() {
        return id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public boolean isDiseaseReportProcessingEnabled() {
        return isDiseaseReportProcessingEnabled;
    }

    public void setDiseaseReportProcessingEnabled(boolean diseaseReportProcessingEnabled) {
        isDiseaseReportProcessingEnabled = diseaseReportProcessingEnabled;
    }

    public boolean isDiseaseWarningCreationEnabled() {
        return isDiseaseWarningCreationEnabled;
    }

    public void setDiseaseWarningCreationEnabled(boolean diseaseWarningCreationEnabled) {
        isDiseaseWarningCreationEnabled = diseaseWarningCreationEnabled;
    }

    public boolean isEmailNotificationEnabled() {
        return isEmailNotificationEnabled;
    }

    public void setEmailNotificationEnabled(boolean emailNotificationEnabled) {
        isEmailNotificationEnabled = emailNotificationEnabled;
    }

    public DiseaseWarningSettingsEntity toEntity() {
        return new DiseaseWarningSettingsEntity(id, ownerId, isDiseaseReportProcessingEnabled, isDiseaseWarningCreationEnabled, isEmailNotificationEnabled);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiseaseWarningSettings that = (DiseaseWarningSettings) o;
        return isDiseaseReportProcessingEnabled == that.isDiseaseReportProcessingEnabled && isDiseaseWarningCreationEnabled == that.isDiseaseWarningCreationEnabled && isEmailNotificationEnabled == that.isEmailNotificationEnabled && Objects.equals(id, that.id) && Objects.equals(ownerId, that.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerId, isDiseaseReportProcessingEnabled, isDiseaseWarningCreationEnabled, isEmailNotificationEnabled);
    }
}
