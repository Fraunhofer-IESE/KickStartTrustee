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

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.DataTrusteeModelCompareResponseDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.DataTrusteeModelResponseDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.DataTrusteeModelSummaryDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.upload.DataTrusteeModelUploadRequestDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.upload.DataTrusteeModelUploadResponseDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.wizard.request.DataTrusteeWizardRequestDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.wizard.response.DataTrusteeWizardBuildResponseDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.validation.ShaclReportDTO;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.datatrusteemodel.DataTrusteeModelService;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.datatrusteemodel.DataTrusteeModelUploadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/datatrusteemodels")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Data Trustee Model Controller", description = "Provides endpoints to retrieve, compare, and upload data trustee models.")
public class DataTrusteeModelController {

  private final DataTrusteeModelService dataTrusteeModelService;

  private final DataTrusteeModelUploadService dataTrusteeModelUploadService;

  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Upload a data trustee model", description = "Stores wizard data and the uploaded image in a temporary directory and returns the storage details.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Data trustee model stored successfully", content = @Content(schema = @Schema(implementation = DataTrusteeModelUploadResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "Invalid upload payload"),
      @ApiResponse(responseCode = "500", description = "Unexpected server error")
  })
  @RequestBody(content = @Content(
      mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
      encoding = {
          @Encoding(name = "request", contentType = MediaType.APPLICATION_JSON_VALUE),
          @Encoding(name = "image", contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
      }))
  @WithRateLimitProtection
  public ResponseEntity<DataTrusteeModelUploadResponseDto> uploadDataTrusteeModel(
      @Valid @RequestPart("request") DataTrusteeModelUploadRequestDto uploadRequest,
      @RequestPart("image") MultipartFile image) {
    log.info("Uploading data trustee model with wizard data and image");
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(
            this.dataTrusteeModelUploadService.storeInTemporaryDirectory(uploadRequest, image));
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get all data trustee models",
      description = "Retrieves a summary list of all available data trustee models including name, image, and description.")
  public List<DataTrusteeModelSummaryDto> getAllDataTrusteeModels() {
    log.info("Retrieving all data trustee model summaries");
    return this.dataTrusteeModelService.getAllDataTrusteeModelSummaries()
        .stream()
        .map(this::withAbsoluteImageUrl)
        .toList();
  }

  @GetMapping(value = "/{modelId}/avatar")
  @Operation(summary = "Get the model image by ID",
      description = "Retrieves the stored image for a specific data trustee model.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Model image retrieved successfully",
          content = @Content(mediaType = "image/*",
              schema = @Schema(type = "string", format = "binary"))),
      @ApiResponse(responseCode = "404", description = "Model image not found")
  })
  public ResponseEntity<Resource> getDataTrusteeModelAvatar(@PathVariable String modelId) {
    log.info("Retrieving model image for data trustee model with id {}", modelId);
    this.requireModelExists(modelId);

    final Path imageFile = this.dataTrusteeModelService.findDataTrusteeModelAvatarFile(modelId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Data trustee model image not found for id '" + modelId + "'"));
    return this.dataTrusteeModelService.readDataTrusteeModelAvatar(modelId, imageFile);
  }

  @GetMapping(value = "/{modelId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get data trustee model package by ID",
      description = "Retrieves the full generated package for a specific data trustee model, including request, model, TTL, SHACL validation, and executive summary.")
  public DataTrusteeWizardBuildResponseDto getDataTrusteeModelPackageById(
      @PathVariable String modelId,
      @RequestParam(name = "lang", defaultValue = "de") String lang) {
    log.info("Retrieving data trustee model package with id {}", modelId);
    this.requireModelExists(modelId);
    return this.dataTrusteeModelService.getDataTrusteeModelPackage(modelId, lang);
  }

  @GetMapping(value = "/{modelId}/request", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get the generated request by ID",
      description = "Retrieves the generated wizard request part for a specific data trustee model.")
  public DataTrusteeWizardRequestDto getDataTrusteeModelRequestById(
      @PathVariable String modelId) {
    log.info("Retrieving generated request for data trustee model with id {}", modelId);
    this.requireModelExists(modelId);
    return this.dataTrusteeModelService.getDataTrusteeModelRequest(modelId);
  }

  @GetMapping(value = "/{modelId}/model", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get the generated model by ID",
      description = "Retrieves the generated model response part for a specific data trustee model.")
  public DataTrusteeModelResponseDto getDataTrusteeModelById(@PathVariable String modelId,
      @RequestParam(name = "lang", defaultValue = "de") String lang) {
    log.info("Retrieving generated model for data trustee model with id {}", modelId);
    this.requireModelExists(modelId);
    return this.dataTrusteeModelService.getDataTrusteeModelResponse(modelId, lang);
  }

  @GetMapping(value = "/{modelId}/ttl", produces = "text/turtle")
  @Operation(summary = "Get the generated TTL by ID",
      description = "Retrieves the generated Turtle representation for a specific data trustee model.")
  public String getDataTrusteeModelTtl(@PathVariable String modelId) {
    log.info("Retrieving generated TTL for data trustee model with id {}", modelId);
    this.requireModelExists(modelId);
    return this.dataTrusteeModelService.getDataTrusteeModelTtl(modelId);
  }

  @GetMapping(value = "/{modelId}/validation", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get the generated validation report by ID",
      description = "Retrieves the generated SHACL validation report for a specific data trustee model.")
  public ShaclReportDTO getDataTrusteeModelValidation(@PathVariable String modelId) {
    log.info("Retrieving generated validation report for data trustee model with id {}",
        modelId);
    this.requireModelExists(modelId);
    return this.dataTrusteeModelService.getDataTrusteeModelValidation(modelId);
  }

  @GetMapping(value = "/{modelId}/executive-summary", produces = MediaType.TEXT_HTML_VALUE)
  @Operation(summary = "Get the generated executive summary by ID",
      description = "Retrieves the generated executive summary HTML for a specific data trustee model.")
  public String getDataTrusteeModelExecutiveSummary(@PathVariable String modelId) {
    log.info("Retrieving generated executive summary for data trustee model with id {}",
        modelId);
    this.requireModelExists(modelId);
    return this.dataTrusteeModelService.getDataTrusteeModelExecutiveSummary(modelId);
  }

  @GetMapping(value = "/compare", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Compare two data trustee models", description = "Retrieves two specific data trustee models in a single response for direct comparison.")
  public DataTrusteeModelCompareResponseDto compareDataTrusteeModels(
      @RequestParam String leftModelId, @RequestParam String rightModelId,
      @RequestParam(name = "lang", defaultValue = "de") String lang) {
    log.info("Comparing data trustee models left={} right={}", leftModelId, rightModelId);

    return DataTrusteeModelCompareResponseDto
        .builder()
        .leftModelId(leftModelId)
        .rightModelId(rightModelId)
        .left(this.toResponseDtoOrNotFound(leftModelId, lang))
        .right(this.toResponseDtoOrNotFound(rightModelId, lang))
        .build();
  }

  private DataTrusteeModelResponseDto toResponseDtoOrNotFound(String modelId, String lang) {
    return this.dataTrusteeModelService
        .findDataTrusteeModelResponse(modelId, lang)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Data trustee model not found for id '" + modelId + "'"));
  }

  private void requireModelExists(String modelId) {
    this.dataTrusteeModelService
        .findDataTrusteeModel(modelId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Data trustee model not found for id '" + modelId + "'"));
  }

  private DataTrusteeModelSummaryDto withAbsoluteImageUrl(DataTrusteeModelSummaryDto summary) {
    if (summary == null) {
      return null;
    }

    return DataTrusteeModelSummaryDto.builder()
        .modelId(summary.getModelId())
        .name(summary.getName())
        .description(summary.getDescription())
        .imageUrl(this.toAbsoluteUrl(summary.getImageUrl()))
        .build();
  }

  private String toAbsoluteUrl(String maybeRelativeUrl) {
    if (maybeRelativeUrl == null || maybeRelativeUrl.isBlank()
        || maybeRelativeUrl.startsWith("http://")
        || maybeRelativeUrl.startsWith("https://")) {
      return maybeRelativeUrl;
    }

    final String normalizedPath = maybeRelativeUrl.startsWith("/")
        ? maybeRelativeUrl
        : "/" + maybeRelativeUrl;
    return ServletUriComponentsBuilder.fromCurrentContextPath()
        .path(normalizedPath)
        .toUriString();
  }
}
