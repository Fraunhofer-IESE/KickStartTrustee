package de.fhg.iese.kickstarttrustee.diseasewarning.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import de.fhg.iese.kickstarttrustee.common.api.dto.ExceptionResponse;
import de.fhg.iese.kickstarttrustee.diseasewarning.business.exception.DiseaseWarningSettingsAlreadyExists;
import de.fhg.iese.kickstarttrustee.diseasewarning.business.exception.DiseaseWarningSettingsNotFound;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class DiseaseWarningExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(DiseaseWarningExceptionHandler.class);

    @ExceptionHandler(DiseaseWarningSettingsNotFound.class)
    Mono<ResponseEntity<ExceptionResponse>> diseaseWarningSettingsNotFound(DiseaseWarningSettingsNotFound ex) {
        log.debug("Exception occurred", ex);
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(ex)));
    }

    @ExceptionHandler(DiseaseWarningSettingsAlreadyExists.class)
    Mono<ResponseEntity<ExceptionResponse>> diseaseWarningSettingsAlreadyExists(DiseaseWarningSettingsAlreadyExists ex) {
        log.debug("Exception occurred", ex);
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(ex)));
    }
}
