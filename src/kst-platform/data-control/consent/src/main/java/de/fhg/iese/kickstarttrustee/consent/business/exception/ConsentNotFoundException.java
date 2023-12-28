package de.fhg.iese.kickstarttrustee.consent.business.exception;

public class ConsentNotFoundException extends RuntimeException {
    public ConsentNotFoundException(){
        super("This consent does not exist!");
    }
}
