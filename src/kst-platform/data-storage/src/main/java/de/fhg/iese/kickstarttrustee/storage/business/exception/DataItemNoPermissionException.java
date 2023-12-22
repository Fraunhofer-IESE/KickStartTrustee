package de.fhg.iese.kickstarttrustee.storage.business.exception;

public class DataItemNoPermissionException extends RuntimeException {
    public DataItemNoPermissionException(String msg) {
        super(msg);
    }

    public static DataItemNoPermissionException forAction(String action) {
        return new DataItemNoPermissionException("No permision to " + action + " dataItem");
    }
}
