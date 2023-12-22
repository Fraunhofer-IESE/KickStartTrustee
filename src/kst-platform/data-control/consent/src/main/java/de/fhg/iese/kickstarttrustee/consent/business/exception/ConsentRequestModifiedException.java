package de.fhg.iese.kickstarttrustee.consent.business.exception;

public class ConsentRequestModifiedException extends RuntimeException {
    public ConsentRequestModifiedException() {
        super("This consent request has been modified");
    }
}
