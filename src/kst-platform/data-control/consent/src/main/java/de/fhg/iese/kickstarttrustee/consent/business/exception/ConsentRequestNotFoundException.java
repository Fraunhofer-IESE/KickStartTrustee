package de.fhg.iese.kickstarttrustee.consent.business.exception;

public class ConsentRequestNotFoundException extends RuntimeException {
    public ConsentRequestNotFoundException() {
        super("This consent request does not exist!");
    }
}
