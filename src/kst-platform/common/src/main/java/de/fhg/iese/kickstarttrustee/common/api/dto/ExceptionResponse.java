package de.fhg.iese.kickstarttrustee.common.api.dto;

import java.time.Instant;

public record ExceptionResponse(String timestamp, String reason, String message) {
    public ExceptionResponse(Exception exception) {
        this(Instant.now().toString(), exception.getClass().getSimpleName(), exception.getMessage());
    }
}
