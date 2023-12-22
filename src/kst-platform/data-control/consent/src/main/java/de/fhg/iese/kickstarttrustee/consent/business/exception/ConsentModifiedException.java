package de.fhg.iese.kickstarttrustee.consent.business.exception;

public class ConsentModifiedException extends RuntimeException {
    public ConsentModifiedException() {
        super("This consent has been modified");
    }
}
