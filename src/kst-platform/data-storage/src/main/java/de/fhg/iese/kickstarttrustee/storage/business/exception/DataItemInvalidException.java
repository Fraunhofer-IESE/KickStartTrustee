package de.fhg.iese.kickstarttrustee.storage.business.exception;

public class DataItemInvalidException extends RuntimeException {
    public DataItemInvalidException() {
        super("This dataItem is invalid");
    }
}
