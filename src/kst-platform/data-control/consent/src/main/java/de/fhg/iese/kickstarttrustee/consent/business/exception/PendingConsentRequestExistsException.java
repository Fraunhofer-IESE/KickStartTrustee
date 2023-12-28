package de.fhg.iese.kickstarttrustee.consent.business.exception;

public class PendingConsentRequestExistsException extends RuntimeException {
	public PendingConsentRequestExistsException() {
		super("Pending consent request for this user already exists!");
	}
}
