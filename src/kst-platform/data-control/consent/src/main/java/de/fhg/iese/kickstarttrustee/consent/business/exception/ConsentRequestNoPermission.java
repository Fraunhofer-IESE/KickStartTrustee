package de.fhg.iese.kickstarttrustee.consent.business.exception;

public class ConsentRequestNoPermission extends RuntimeException {
    public ConsentRequestNoPermission() {
        super("No permission to request consent!");
    }
}
