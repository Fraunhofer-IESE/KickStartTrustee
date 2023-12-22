package de.fhg.iese.kickstarttrustee.audit.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import de.fhg.iese.kickstarttrustee.audit.business.exception.AuditLogEntryNotFoundException;
import de.fhg.iese.kickstarttrustee.common.business.exception.InvalidTimeRangeException;
import de.fhg.iese.kickstarttrustee.common.api.dto.ExceptionResponse;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class AuditRestExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(AuditRestExceptionHandler.class);

    @ExceptionHandler(InvalidTimeRangeException.class)
    Mono<ResponseEntity<ExceptionResponse>> invalidTimeRange(InvalidTimeRangeException ex) {
        log.debug("Exception occurred", ex);
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(ex)));
    }

    @ExceptionHandler(AuditLogEntryNotFoundException.class)
    Mono<ResponseEntity<ExceptionResponse>> auditLogEntryNotFound(AuditLogEntryNotFoundException ex) {
        log.debug("Exception occurred", ex);
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(ex)));
    }
}
