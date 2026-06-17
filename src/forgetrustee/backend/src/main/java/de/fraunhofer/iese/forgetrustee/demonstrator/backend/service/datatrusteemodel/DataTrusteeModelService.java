
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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.datatrusteemodel;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.config.BackendProperties;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModel;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardModel;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.DataTrusteeModelMapper;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.DataTrusteeModelResponseDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.DataTrusteeModelResponseMapper;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.read.DataTrusteeModelSummaryDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.wizard.request.DataTrusteeWizardRequestDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.wizard.request.DataTrusteeWizardRequestMapper;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.wizard.response.DataTrusteeWizardBuildResponseDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.validation.ShaclReportDTO;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper.commentMapper.DataTrusteeModelUiDocumentationMapper;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper.commentMapper.OntologyDocumentationDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper.commentMapper.OntologyDocumentationLoader;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.repository.datatrusteemodel.DataTrusteeModelRepository;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.MetadataService;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.OntologyWizardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataTrusteeModelService {

  private static final String GENERATED_DIRECTORY = "generated";

  private static final String CONTROLLER_RESPONSE_FILE = "controller-response.json";

  private static final String TTL_FILE = "ontology.ttl";

  private static final String EXECUTIVE_SUMMARY_FILE = "executive-summary.html";

  private static final String SHACL_REPORT_FILE = "shacl-report.json";

  private static final Set<String> IMAGE_EXTENSIONS = Set.of("png", "jpg", "jpeg", "gif",
      "webp", "bmp", "tif", "tiff", "svg");

  private final DataTrusteeModelRepository dataTrusteeModelRepository;

  private final OntologyWizardService ontologyWizardService;

  private final DataTrusteeModelResponseMapper dataTrusteeModelResponseMapper;

  private final DataTrusteeModelMapper dataTrusteeModelMapper;

  private final DataTrusteeWizardRequestMapper dataTrusteeWizardRequestMapper;

  private final ObjectMapper objectMapper;

  private final BackendProperties backendProperties;

  private final MetadataService metadataService;

  public List<DataTrusteeModel> getAllDataTrusteeModels() {
    log.info("Retrieving all data trustee models");
    return this.dataTrusteeModelRepository.findAll();
  }

  public List<DataTrusteeModelSummaryDto> getAllDataTrusteeModelSummaries() {
    log.info("Retrieving all data trustee model summaries");
    final List<DataTrusteeModelSummaryDto> summaries = new ArrayList<>();
    for (final Map.Entry<String, DataTrusteeModel> entry : this.getAllDataTrusteeModelsById()
        .entrySet()) {
      final String modelId = entry.getKey();
      final DataTrusteeModel model = entry.getValue();
      if (model == null) {
        continue;
      }
      summaries.add(this.toSummaryDto(modelId, model));
    }
    return List.copyOf(summaries);
  }

  public Map<String, DataTrusteeModel> getAllDataTrusteeModelsById() {
    log.info("Retrieving all data trustee models by id");
    return this.dataTrusteeModelRepository.findAllById();
  }

  public Optional<DataTrusteeModel> findDataTrusteeModel(String modelId) {
    log.info("Retrieving data trustee model with id {}", modelId);
    return this.dataTrusteeModelRepository.findById(modelId);
  }

  public DataTrusteeModel getDataTrusteeModel(String modelId) {
    return this.findDataTrusteeModel(modelId).orElse(null);
  }

  public void refreshModels() {
    log.info("Refreshing data trustee model cache");
    this.dataTrusteeModelRepository.refresh();
  }

  public Map<String, Path> importAllDataTrusteeModels() {
    log.info("Importing all data trustee models and generating derived artifacts");
    this.refreshModels();

    final Map<String, Path> exportedModelDirectories = new LinkedHashMap<>();
    for (final Map.Entry<String, DataTrusteeModel> entry : this
        .getAllDataTrusteeModelsById()
        .entrySet()) {
      final String modelId = entry.getKey();
      final DataTrusteeModel model = entry.getValue();
      if (model == null) {
        log.warn("Skipping null data trustee model entry for id {}", modelId);
        continue;
      }

      final Path exportDirectory = this.exportModelArtifacts(modelId, model);
      exportedModelDirectories.put(modelId, exportDirectory);
    }

    log.info("Imported and exported {} data trustee model(s).", exportedModelDirectories.size());
    return Map.copyOf(exportedModelDirectories);
  }

  public DataTrusteeWizardBuildResponseDto getDataTrusteeModelPackage(String modelId) {
    return this.getDataTrusteeModelPackage(modelId, "de");
  }

  public DataTrusteeWizardBuildResponseDto getDataTrusteeModelPackage(String modelId, String lang) {
    final DataTrusteeWizardBuildResponseDto buildResponse = this.readGeneratedJsonArtifact(modelId,
        CONTROLLER_RESPONSE_FILE, DataTrusteeWizardBuildResponseDto.class);
    buildResponse.setDataTrusteeModel(
        this.enrichWithPresentation(buildResponse.getDataTrusteeModel(), lang));
    return buildResponse;
  }

  public DataTrusteeWizardRequestDto getDataTrusteeModelRequest(String modelId) {
    return this.getDataTrusteeModelPackage(modelId).getRequest();
  }

  public DataTrusteeModelResponseDto getDataTrusteeModelResponse(String modelId) {
    return this.getDataTrusteeModelResponse(modelId, "de");
  }

  public DataTrusteeModelResponseDto getDataTrusteeModelResponse(String modelId, String lang) {
    return this.getDataTrusteeModelPackage(modelId, lang).getDataTrusteeModel();
  }

  public Optional<DataTrusteeModelResponseDto> findDataTrusteeModelResponse(String modelId,
      String lang) {
    return this.findDataTrusteeModel(modelId)
        .map(this.dataTrusteeModelResponseMapper::toDto)
        .map(dto -> this.enrichWithPresentation(dto, lang));
  }

  public String getDataTrusteeModelTtl(String modelId) {
    return this.readGeneratedTextArtifact(modelId, TTL_FILE);
  }

  public String getDataTrusteeModelExecutiveSummary(String modelId) {
    return this.readGeneratedTextArtifact(modelId, EXECUTIVE_SUMMARY_FILE);
  }

  public ShaclReportDTO getDataTrusteeModelValidation(String modelId) {
    return this.readGeneratedJsonArtifact(modelId, SHACL_REPORT_FILE, ShaclReportDTO.class);
  }

  public Optional<Path> findDataTrusteeModelAvatarFile(String modelId) {
    final Path modelDirectory = this.getModelBasePath().resolve(modelId);
    return this.findImageFile(modelDirectory);
  }

  public ResponseEntity<Resource> getDataTrusteeModelAvatar(String modelId) {
    final Path imageFile = this.findDataTrusteeModelAvatarFile(modelId)
        .orElseThrow(() -> new IllegalStateException(
            "Image not found for data trustee model '" + modelId + "'"));

    return this.readDataTrusteeModelAvatar(modelId, imageFile);
  }

  public ResponseEntity<Resource> readDataTrusteeModelAvatar(String modelId, Path imageFile) {
    if (imageFile == null) {
      throw new IllegalStateException("Image file must not be null");
    }

    try {
      final FileSystemResource imageResource = new FileSystemResource(imageFile);
      final HttpHeaders headers = new HttpHeaders();
      headers.setContentType(this.getImageContentType(imageResource));
      headers.setContentLength(imageResource.contentLength());
      return ResponseEntity.ok().headers(headers).body(imageResource);
    } catch (final IOException ex) {
      throw new IllegalStateException(
          "Failed to read image for data trustee model '" + modelId + "'", ex);
    }
  }

  private Path exportModelArtifacts(String modelId, DataTrusteeModel model) {
    try {
      final Path exportDirectory = this.getModelBasePath().resolve(modelId).resolve(DataTrusteeModelService.GENERATED_DIRECTORY);
      Files.createDirectories(exportDirectory);

      final DataTrusteeWizardModel wizardModel = this.ontologyWizardService.toWizardModel(model);
      if (wizardModel == null) {
        throw new IllegalStateException(
            "Unable to convert data trustee model to wizard model for id '" + modelId + "'");
      }

      final DataTrusteeWizardRequestDto requestDto = this.dataTrusteeWizardRequestMapper
          .toDto(wizardModel);
      final DataTrusteeModelResponseDto responseDto = this.dataTrusteeModelResponseMapper
          .toDto(this.dataTrusteeModelMapper.toModel(wizardModel));

      final var ontModel = this.ontologyWizardService.buildFromDataTrusteeModel(wizardModel);
      final String ttl = this.ontologyWizardService.toTTL(ontModel);
      final ShaclReportDTO shaclReport = this.ontologyWizardService.validate(ontModel, wizardModel, "default");
      final String executiveSummary = this.ontologyWizardService
          .generateExecutiveSummary(wizardModel);

      final OntologyDocumentationDto docs = OntologyDocumentationLoader
          .load(Path.of("data", "ontology-comments-labels.json"));
      DataTrusteeModelUiDocumentationMapper.enrich(responseDto, docs, "de");

      final DataTrusteeWizardBuildResponseDto buildResponse = DataTrusteeWizardBuildResponseDto
          .builder()
          .request(requestDto)
          .dataTrusteeModel(this.enrichWithPresentation(responseDto, "de"))
          .ttl(ttl)
          .shaclReport(shaclReport)
          .executiveSummary(executiveSummary)
          .build();

      this.writeJson(exportDirectory.resolve("controller-response.json"), buildResponse);
      Files.writeString(exportDirectory.resolve("ontology.ttl"), ttl, StandardCharsets.UTF_8);
      Files
          .writeString(exportDirectory.resolve("executive-summary.html"), executiveSummary,
              StandardCharsets.UTF_8);
      this.writeJson(exportDirectory.resolve("shacl-report.json"), shaclReport);

      log.info("Exported generated artifacts for model '{}' to {}", modelId, exportDirectory);
      return exportDirectory;
    } catch (final IOException ex) {
      throw new IllegalStateException(
          "Failed to export generated artifacts for model '" + modelId + "'", ex);
    }
  }

  private void writeJson(Path targetFile, Object value) throws IOException {
    this.objectMapper.writerWithDefaultPrettyPrinter().writeValue(targetFile.toFile(), value);
  }

  private Path getModelBasePath() {
    if (this.backendProperties != null && this.backendProperties.getDataTrusteeModel() != null
        && this.backendProperties.getDataTrusteeModel().getBasePath() != null
        && !this.backendProperties.getDataTrusteeModel().getBasePath().isBlank()) {
      return Path.of(this.backendProperties.getDataTrusteeModel().getBasePath().trim());
    }
    return Path.of("data", "datatrusteemodels");
  }

  private DataTrusteeModelSummaryDto toSummaryDto(String modelId, DataTrusteeModel model) {
    final Path modelDirectory = this.getModelBasePath().resolve(modelId);
    final Optional<Path> imageFile = this.findImageFile(modelDirectory);
    return DataTrusteeModelSummaryDto
        .builder()
        .modelId(modelId)
        .name(model.getCore() != null ? model.getCore().getDataTrusteeName() : null)
        .description(model.getCore() != null ? model.getCore().getDataTrusteeDescription() : null)
        .imageUrl(imageFile.isPresent() ? "/datatrusteemodels/" + modelId + "/avatar" : null)
        .build();
  }

  private Optional<Path> findImageFile(Path modelDirectory) {
    if (modelDirectory == null || !Files.isDirectory(modelDirectory)) {
      return Optional.empty();
    }

    try (Stream<Path> files = Files.list(modelDirectory)) {
      return files
          .filter(Files::isRegularFile)
          .filter(path -> !path.getFileName().toString().endsWith(".json"))
          .filter(this::isImageFile)
          .sorted()
          .findFirst();
    } catch (final IOException ex) {
      throw new IllegalStateException("Failed to inspect data trustee model directory: "
          + modelDirectory, ex);
    }
  }

  private boolean isImageFile(Path path) {
    final String fileName = path.getFileName().toString();
    final int dotIndex = fileName.lastIndexOf('.');
    if (dotIndex < 0 || dotIndex == fileName.length() - 1) {
      return false;
    }

    final String extension = fileName.substring(dotIndex + 1).toLowerCase();
    return IMAGE_EXTENSIONS.contains(extension);
  }

  private MediaType getImageContentType(final Resource imageResource) {
    Optional<MediaType> mimeTypeOptional = MediaTypeFactory.getMediaType(imageResource);
    return mimeTypeOptional.orElse(MediaType.APPLICATION_OCTET_STREAM);
  }

  private <T> T readGeneratedJsonArtifact(String modelId, String fileName, Class<T> type) {
    final Path generatedFile = this.getGeneratedDirectory(modelId).resolve(fileName);
    if (!Files.exists(generatedFile)) {
      throw new IllegalStateException(
          "Generated artifact not found for model '" + modelId + "': " + generatedFile);
    }

    try {
      return this.objectMapper.readValue(generatedFile.toFile(), type);
    } catch (final RuntimeException ex) {
      throw new IllegalStateException(
          "Failed to read generated artifact for model '" + modelId + "': " + generatedFile,
          ex);
    }
  }

  private String readGeneratedTextArtifact(String modelId, String fileName) {
    final Path generatedFile = this.getGeneratedDirectory(modelId).resolve(fileName);
    if (!Files.exists(generatedFile)) {
      throw new IllegalStateException(
          "Generated artifact not found for model '" + modelId + "': " + generatedFile);
    }

    try {
      return Files.readString(generatedFile, StandardCharsets.UTF_8);
    } catch (final IOException ex) {
      throw new IllegalStateException(
          "Failed to read generated artifact for model '" + modelId + "': " + generatedFile,
          ex);
    }
  }

  private Path getGeneratedDirectory(String modelId) {
    return this.getModelBasePath().resolve(modelId).resolve(GENERATED_DIRECTORY);
  }

  public DataTrusteeModelResponseDto enrichWithPresentation(DataTrusteeModelResponseDto response,
      String lang) {
    if (response == null) {
      return null;
    }

    response.setPresentation(this.metadataService.getResultPresentation("compare", lang));
    return response;
  }
}
