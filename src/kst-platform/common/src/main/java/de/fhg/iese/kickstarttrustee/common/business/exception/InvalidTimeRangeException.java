package de.fhg.iese.kickstarttrustee.common.business.exception;

public class InvalidTimeRangeException extends RuntimeException {
    public InvalidTimeRangeException() {
        super("Time range is invalid");
    }
}
