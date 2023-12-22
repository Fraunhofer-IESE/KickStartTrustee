package de.fhg.iese.kickstarttrustee.diseasewarning.business.exception;

public class DiseaseWarningSettingsNotFound extends RuntimeException {
    public DiseaseWarningSettingsNotFound() {
        super("No disease warning settings found!");
    }
}
