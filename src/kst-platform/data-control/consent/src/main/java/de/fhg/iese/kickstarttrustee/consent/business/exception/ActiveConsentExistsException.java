package de.fhg.iese.kickstarttrustee.consent.business.exception;

public class ActiveConsentExistsException extends RuntimeException {
    public ActiveConsentExistsException() {
        super("Active consent already exists!");
    }
}
