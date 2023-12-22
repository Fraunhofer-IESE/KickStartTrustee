package de.fhg.iese.kickstarttrustee.consent.api.controller;

import de.fhg.iese.kickstarttrustee.consent.business.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import de.fhg.iese.kickstarttrustee.common.api.dto.ExceptionResponse;
import de.fhg.iese.kickstarttrustee.common.business.exception.DataOwnerNotFoundException;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class ConsentRestExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ConsentRestExceptionHandler.class);

    @ExceptionHandler(ActiveConsentExistsException.class)
    Mono<ResponseEntity<ExceptionResponse>> activeConsentAlreadyExists(ActiveConsentExistsException ex) {
        log.debug("Exception occurred", ex);
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(ex)));
    }

	@ExceptionHandler(PendingConsentRequestExistsException.class)
	Mono<ResponseEntity<ExceptionResponse>> pendingConsentRequestAlreadyExists(PendingConsentRequestExistsException ex) {
		log.debug("Exception occurred", ex);
		return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(ex)));
	}

    @ExceptionHandler(ConsentRequestNotFoundException.class)
    Mono<ResponseEntity<ExceptionResponse>> consentRequestNotFound(ConsentRequestNotFoundException ex) {
        log.debug("Exception occurred", ex);
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(ex)));
    }

    @ExceptionHandler(ConsentNotFoundException.class)
    Mono<ResponseEntity<ExceptionResponse>> consentNotFound(ConsentNotFoundException ex) {
        log.debug("Exception occurred", ex);
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(ex)));
    }

    @ExceptionHandler(ConsentRequestAlreadyProcessedException.class)
    Mono<ResponseEntity<ExceptionResponse>> consentRequestAlreadyProcessed(ConsentRequestAlreadyProcessedException ex) {
        log.debug("Exception occurred", ex);
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(ex)));
    }

    @ExceptionHandler(ConsentNotActiveException.class)
    Mono<ResponseEntity<ExceptionResponse>> consentNotActive(ConsentNotActiveException ex) {
        log.debug("Exception occurred", ex);
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(ex)));
    }

    @ExceptionHandler(ConsentModifiedException.class)
    Mono<ResponseEntity<ExceptionResponse>> consentModified(ConsentModifiedException ex) {
        log.debug("Exception occurred", ex);
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(ex)));
    }

    @ExceptionHandler(ConsentRequestModifiedException.class)
    Mono<ResponseEntity<ExceptionResponse>> consentRequestModified(ConsentRequestModifiedException ex) {
        log.debug("Exception occurred", ex);
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(ex)));
    }

    @ExceptionHandler(ConsentRequestNoPermission.class)
    Mono<ResponseEntity<ExceptionResponse>> consentRequestNoPermission(ConsentRequestNoPermission ex) {
        log.debug("Exception occurred", ex);
        return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionResponse(ex)));
    }

    @ExceptionHandler(DataOwnerNotFoundException.class)
    Mono<ResponseEntity<ExceptionResponse>> dataOwnerNotFound(DataOwnerNotFoundException ex) {
        log.debug("Exception occurred", ex);
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(ex)));
    }

    @ExceptionHandler(WrongConsentRequestStatusException.class)
    Mono<ResponseEntity<ExceptionResponse>> wrongConsentRequestStatus(WrongConsentRequestStatusException ex) {
        log.debug("Exception occurred", ex);
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(ex)));
    }

    @ExceptionHandler(WrongConsentStatusException.class)
    Mono<ResponseEntity<ExceptionResponse>> wrongConsentStatus(WrongConsentStatusException ex) {
        log.debug("Exception occurred", ex);
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(ex)));
    }

	@ExceptionHandler(InvalidConsentRequestException.class)
	Mono<ResponseEntity<ExceptionResponse>> invalidConsentRequest(InvalidConsentRequestException ex) {
        log.debug("Exception occurred", ex);
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(ex)));
	}
}
