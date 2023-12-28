package de.fhg.iese.kickstarttrustee.consent.business.exception;

public class InvalidConsentRequestException extends RuntimeException {
	public InvalidConsentRequestException(String reason) {
		super(reason);
	}
}
