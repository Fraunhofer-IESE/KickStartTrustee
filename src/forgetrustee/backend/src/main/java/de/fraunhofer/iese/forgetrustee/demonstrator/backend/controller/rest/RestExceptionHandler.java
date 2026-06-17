/*
 * Copyright (C) 2026 Fraunhofer IESE
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.controller.rest;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.ApiErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

  @ExceptionHandler(RateLimitException.class)
  public ResponseEntity<ApiErrorMessage> handleInvalidFieldsInValidJson(
      final RateLimitException rateLimitException, final HttpServletRequest request) {
    final ApiErrorMessage apiErrorMessage = rateLimitException
        .toApiErrorMessage(request.getRequestURI());
    logIncomingCallException(rateLimitException, apiErrorMessage);
    return new ResponseEntity<>(apiErrorMessage, HttpStatus.TOO_MANY_REQUESTS);
  }

  private static void logIncomingCallException(final RateLimitException rateLimitException,
      final ApiErrorMessage apiErrorMessage) {
    log.warn("{}: {}", apiErrorMessage.getId(), rateLimitException.getMessage(), rateLimitException);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
      HttpServletRequest request) {
    final Throwable root = ex.getMostSpecificCause();
    final String rootMessage = root.getMessage();

    log
        .warn("Bad JSON request: method={}, path={}, error={}", request.getMethod(),
            request.getRequestURI(), rootMessage, ex);

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("Invalid JSON payload: " + rootMessage);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiErrorDTO> handleIllegalArgumentException(IllegalArgumentException ex,
      HttpServletRequest request) {
    log.warn("Validation error: method={}, path={}, message={}", request.getMethod(),
        request.getRequestURI(), ex.getMessage(), ex);
    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ApiErrorDTO("Validation error", ex.getMessage()));
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ApiErrorDTO> handleIllegalStateException(IllegalStateException ex,
      HttpServletRequest request) {
    log.error("Processing error: method={}, path={}, message={}", request.getMethod(),
        request.getRequestURI(), ex.getMessage(), ex);
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ApiErrorDTO("Processing error", ex.getMessage()));
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<ApiErrorDTO> handleMaxUploadSizeExceededException(
      MaxUploadSizeExceededException ex, HttpServletRequest request) {
    log.warn("Upload too large: method={}, path={}, message={}", request.getMethod(),
        request.getRequestURI(), ex.getMessage(), ex);
    return ResponseEntity
        .status(HttpStatus.PAYLOAD_TOO_LARGE)
        .body(new ApiErrorDTO("Upload too large",
            "The uploaded multipart request exceeds the configured size limit"));
  }

  @ExceptionHandler(ErrorResponseException.class)
  public ResponseEntity<ApiErrorDTO> handleErrorResponseException(ErrorResponseException ex) {
    HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
    if (status == null) {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    String message = ex.getMessage();
    if (ex instanceof ResponseStatusException responseStatusException
        && responseStatusException.getReason() != null
        && !responseStatusException.getReason().isBlank()) {
      message = responseStatusException.getReason();
    }

    return ResponseEntity
        .status(status)
        .body(new ApiErrorDTO(status.getReasonPhrase(), message));
  }
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorDTO> handleGenericException(Exception ex, HttpServletRequest request) {
    log.error("Unhandled REST exception: method={}, path={}, message={}", request.getMethod(),
        request.getRequestURI(), ex.getMessage(), ex);
    return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiErrorDTO("Internal error", "Internal error while processing request"));
  }

}
