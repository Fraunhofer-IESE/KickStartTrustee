package de.fhg.iese.kickstarttrustee.catalog.api.controller;

import de.fhg.iese.kickstarttrustee.catalog.business.exception.JsonSchemaNotFoundException;
import de.fhg.iese.kickstarttrustee.common.api.dto.ExceptionResponse;
import de.fhg.iese.kickstarttrustee.common.business.exception.DataItemTypeNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class DataCatalogRestExceptionHandler {
	private static final Logger log = LoggerFactory.getLogger(DataCatalogRestExceptionHandler.class);

	@ExceptionHandler(DataItemTypeNotFoundException.class)
	Mono<ResponseEntity<ExceptionResponse>> dataItemNotFound(DataItemTypeNotFoundException ex) {
		log.debug("Exception occurred", ex);
		return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(ex)));
	}

	@ExceptionHandler(JsonSchemaNotFoundException.class)
	Mono<ResponseEntity<ExceptionResponse>> jsonSchemaNotFound(JsonSchemaNotFoundException ex) {
		log.debug("Exception occurred", ex);
		return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(ex)));
	}
}
