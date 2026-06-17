package de.fraunhofer.iese.forgetrustee.demonstrator.backend.controller.rest;

import static org.assertj.core.api.Assertions.assertThat;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.ApiErrorDTO;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;

class RestExceptionHandlerTest {

  @Test
  void handleResponseStatusExceptionPreservesNotFoundStatus() {
    final RestExceptionHandler handler = new RestExceptionHandler();

    final ResponseEntity<ApiErrorDTO> response = handler
        .handleErrorResponseException(new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Module not found"));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getError()).isEqualTo("Not Found");
    assertThat(response.getBody().getMessage()).isEqualTo("Module not found");
  }

  @Test
  void handleMaxUploadSizeExceededReturnsPayloadTooLarge() {
    final RestExceptionHandler handler = new RestExceptionHandler();
    final MockHttpServletRequest request = new MockHttpServletRequest("POST", "/upload");

    final ResponseEntity<ApiErrorDTO> response = handler
        .handleMaxUploadSizeExceededException(new MaxUploadSizeExceededException(350L * 1024L),
            request);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.PAYLOAD_TOO_LARGE);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getError()).isEqualTo("Upload too large");
  }
}
