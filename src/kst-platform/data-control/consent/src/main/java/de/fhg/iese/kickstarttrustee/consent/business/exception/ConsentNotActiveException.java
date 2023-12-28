package de.fhg.iese.kickstarttrustee.consent.business.exception;

public class ConsentNotActiveException extends RuntimeException {
    public ConsentNotActiveException() {
        super("This consent is not active anymore!");
    }
}
