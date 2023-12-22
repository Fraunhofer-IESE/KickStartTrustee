package de.fhg.iese.kickstarttrustee.storage.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import de.fhg.iese.kickstarttrustee.common.api.dto.ExceptionResponse;
import de.fhg.iese.kickstarttrustee.storage.business.exception.DataItemInvalidException;
import de.fhg.iese.kickstarttrustee.storage.business.exception.DataItemNoPermissionException;
import de.fhg.iese.kickstarttrustee.storage.business.exception.DataItemNotFoundException;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class DataStorageRestExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(DataStorageRestExceptionHandler.class);

    @ExceptionHandler(DataItemInvalidException.class)
    Mono<ResponseEntity<ExceptionResponse>> dataItemInvalid(DataItemInvalidException ex) {
        log.debug("Exception occurred", ex);
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(ex)));
    }

    @ExceptionHandler(DataItemNoPermissionException.class)
    Mono<ResponseEntity<ExceptionResponse>> dataItemNoPermission(DataItemNoPermissionException ex) {
        log.debug("Exception occurred", ex);
        return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionResponse(ex)));
    }

    @ExceptionHandler(DataItemNotFoundException.class)
    Mono<ResponseEntity<ExceptionResponse>> dataItemNotFound(DataItemNotFoundException ex) {
        log.debug("Exception occurred", ex);
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(ex)));
    }
}
