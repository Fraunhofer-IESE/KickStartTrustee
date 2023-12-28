package de.fhg.iese.kickstarttrustee.common.business.exception;

public class DataOwnerNotFoundException extends RuntimeException {
    public DataOwnerNotFoundException() {
        super("This user does not exist!");
    }
}
