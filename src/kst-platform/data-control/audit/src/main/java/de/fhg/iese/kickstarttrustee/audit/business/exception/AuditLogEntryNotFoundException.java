package de.fhg.iese.kickstarttrustee.audit.business.exception;

public class AuditLogEntryNotFoundException extends RuntimeException {
    public AuditLogEntryNotFoundException() {
        super("Audit log entry not found!");
    }
}
