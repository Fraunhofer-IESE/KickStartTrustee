package de.fhg.iese.kickstarttrustee.owner.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import de.fhg.iese.kickstarttrustee.common.api.dto.ExceptionResponse;
import de.fhg.iese.kickstarttrustee.owner.business.exception.NotSupportedLanguageException;
import de.fhg.iese.kickstarttrustee.owner.business.exception.OwnerProfileAlreadyExistsException;
import de.fhg.iese.kickstarttrustee.owner.business.exception.OwnerProfileNotFoundException;
import reactor.core.publisher.Mono;

@RestControllerAdvice
class OwnerRestExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(OwnerRestExceptionHandler.class);

    @ExceptionHandler(NotSupportedLanguageException.class)
    Mono<ResponseEntity<ExceptionResponse>> notSupportedLanguage(NotSupportedLanguageException ex) {
        log.debug("Exception occured", ex);
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(ex)));
    }

    @ExceptionHandler(OwnerProfileNotFoundException.class)
    Mono<ResponseEntity<ExceptionResponse>> ownerProfileNotFound(OwnerProfileNotFoundException ex) {
        log.debug("Exception occured", ex);
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(ex)));
    }

    @ExceptionHandler(OwnerProfileAlreadyExistsException.class)
    Mono<ResponseEntity<ExceptionResponse>> ownerProfileAlreadyExists(OwnerProfileAlreadyExistsException ex) {
        log.debug("Exception occured", ex);
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(ex)));
    }
}
