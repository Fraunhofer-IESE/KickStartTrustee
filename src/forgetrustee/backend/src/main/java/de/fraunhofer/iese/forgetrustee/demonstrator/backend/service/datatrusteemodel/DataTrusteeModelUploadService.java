
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
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.upload.DataTrusteeModelUploadRequestDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.upload.DataTrusteeModelUploadResponseDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.wizard.request.DataTrusteeWizardRequestDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.ZoneOffset;
import java.util.Locale;

import javax.imageio.ImageIO;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataTrusteeModelUploadService {

  private static final String TEMP_DIRECTORY_PREFIX = "forgetrustee-datatrusteemodel-";

  private static final String DEFAULT_FILE_BASE_NAME = "datatrustee-model";

  private static final DateTimeFormatter UPLOAD_DIRECTORY_DATE_FORMAT =
      DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").withZone(ZoneOffset.UTC);

  private static final String JSON_SUFFIX = ".json";

  private final ObjectMapper objectMapper;

  private final BackendProperties backendProperties;

  private final DataTrusteeModelUploadNotificationService dataTrusteeModelUploadNotificationService;

  public DataTrusteeModelUploadResponseDto storeInTemporaryDirectory(
      DataTrusteeModelUploadRequestDto uploadRequest, MultipartFile image) {
    if (uploadRequest == null) {
      throw new IllegalArgumentException("Upload request must not be null");
    }
    final DataTrusteeWizardRequestDto wizardData = uploadRequest.getWizardData();
    if (wizardData == null) {
      throw new IllegalArgumentException("Wizard data must not be null");
    }
    if (image == null) {
      throw new IllegalArgumentException("Image must not be null");
    }
    if (uploadRequest.getHoneypot() != null && !uploadRequest.getHoneypot().isBlank()) {
      throw new IllegalArgumentException("Invalid upload payload");
    }

    try {
      final Instant createdAt = Instant.now();
      final String fileBaseName = this.resolveFileBaseName(wizardData);
      final ValidatedImage validatedImage = this.validateImage(image);
      final Path uploadBaseDirectory = this.resolveUploadBaseDirectory();
      final Path uploadDirectory = Files.createTempDirectory(uploadBaseDirectory,
          this.resolveUploadDirectoryPrefix(uploadRequest));
      final Path wizardDataFile = uploadDirectory.resolve(fileBaseName + JSON_SUFFIX);
      final byte[] wizardJsonBytes = this.objectMapper
          .writerWithDefaultPrettyPrinter()
          .writeValueAsBytes(wizardData);

      Files.write(wizardDataFile, wizardJsonBytes, StandardOpenOption.CREATE_NEW);

      this.writeInfoFile(uploadDirectory, uploadRequest, createdAt);

      final String imageExtension = this
          .resolveImageExtension(validatedImage.contentType(), image.getOriginalFilename());
      final Path imageFile = uploadDirectory.resolve(fileBaseName + "-image." + imageExtension);

      Files.write(imageFile, validatedImage.bytes(), StandardOpenOption.CREATE_NEW);

      log.info("Stored DataTrusteeModel in temporary directory {}", uploadDirectory);
      if (uploadRequest.getName() != null || uploadRequest.getEmail() != null) {
        log
            .debug("Upload metadata name='{}', email='{}'", uploadRequest.getName(),
                uploadRequest.getEmail());
      }
      final DataTrusteeModelUploadResponseDto response = DataTrusteeModelUploadResponseDto
          .builder()
          .dataTrusteeName(this.extractDataTrusteeName(wizardData))
          .uploadDirectory(uploadDirectory.toString())
          .wizardDataFileName(wizardDataFile.getFileName().toString())
          .wizardDataFilePath(wizardDataFile.toString())
          .imageFileName(imageFile.getFileName().toString())
          .imageFilePath(imageFile.toString())
          .imageContentType(validatedImage.contentType())
          .wizardDataBytesWritten((long) wizardJsonBytes.length)
          .imageBytesWritten((long) validatedImage.bytes().length)
          .createdAt(createdAt.toString())
          .build();

      this.sendUploadNotification(uploadRequest, response);
      return response;
    } catch (final IOException ex) {
      throw new IllegalStateException("Failed to store DataTrusteeModel in temporary directory",
          ex);
    }
  }

  private ValidatedImage validateImage(MultipartFile image) throws IOException {
    final long maxUploadSizeBytes = this.resolveMaxUploadSizeBytes();
    if (image.isEmpty()) {
      throw new IllegalArgumentException("Image must not be empty");
    }
    if (image.getSize() > maxUploadSizeBytes) {
      throw new IllegalArgumentException(
          "Image exceeds maximum size of " + maxUploadSizeBytes + " bytes");
    }

    final String contentType = image.getContentType();
    if (contentType == null || !contentType.toLowerCase(Locale.ROOT).startsWith("image/")) {
      throw new IllegalArgumentException("Uploaded file must be an image");
    }

    final byte[] bytes = image.getBytes();
    final BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
    if (bufferedImage == null) {
      throw new IllegalArgumentException("Uploaded file could not be decoded as an image");
    }

    return new ValidatedImage(bytes, contentType);
  }

  private long resolveMaxUploadSizeBytes() {
    final BackendProperties.DataTrusteeModelProperties dataTrusteeModelProperties = this.backendProperties
        .getDataTrusteeModel();
    if (dataTrusteeModelProperties == null || dataTrusteeModelProperties.getMaxUploadSizeBytes() <= 0L) {
      throw new IllegalStateException(
          "Backend property 'backend.dataTrusteeModel.maxUploadSizeBytes' must be greater than zero");
    }
    return dataTrusteeModelProperties.getMaxUploadSizeBytes();
  }

  private Path resolveUploadBaseDirectory() throws IOException {
    final BackendProperties.DataTrusteeModelProperties dataTrusteeModelProperties = this.backendProperties
        .getDataTrusteeModel();
    if (dataTrusteeModelProperties == null || dataTrusteeModelProperties.getUploadBasePath() == null || dataTrusteeModelProperties.getUploadBasePath().isBlank()) {
      throw new IllegalStateException("Backend property 'backend.dataTrusteeModel.uploadBasePath' must not be blank");
    }

    final Path uploadBaseDirectory = Path.of(dataTrusteeModelProperties.getUploadBasePath());
    Files.createDirectories(uploadBaseDirectory);
    return uploadBaseDirectory;
  }

  private String resolveUploadDirectoryPrefix(DataTrusteeModelUploadRequestDto uploadRequest) {
    final StringBuilder prefix = new StringBuilder(TEMP_DIRECTORY_PREFIX)
        .append(Instant.now().atZone(ZoneOffset.UTC).format(UPLOAD_DIRECTORY_DATE_FORMAT))
        .append('-');

    final String modelName = this.sanitizePathComponent(this.extractDataTrusteeName(
        uploadRequest.getWizardData()));
    prefix.append(modelName.isBlank() ? DEFAULT_FILE_BASE_NAME : modelName).append('-');

    return prefix.toString();
  }

  private String resolveFileBaseName(DataTrusteeWizardRequestDto wizardData) {
    final String modelName = this.extractDataTrusteeName(wizardData);
    if (modelName == null || modelName.isBlank()) {
      return DEFAULT_FILE_BASE_NAME;
    }

    final String sanitized = modelName
        .trim()
        .replaceAll("[\\\\/:*?\"<>|]+", "-")
        .replaceAll("\\s+", "-")
        .replaceAll("-+", "-")
        .replaceAll("^[.-]+", "")
        .replaceAll("[.-]+$", "");

    if (sanitized.isBlank()) {
      return DEFAULT_FILE_BASE_NAME;
    }
    return sanitized.length() > 80 ? sanitized.substring(0, 80) : sanitized;
  }

  private String extractDataTrusteeName(DataTrusteeWizardRequestDto wizardData) {
    if (wizardData == null || wizardData.getCore() == null) {
      return null;
    }
    return wizardData.getCore().getDataTrusteeName();
  }

  private String resolveImageExtension(String contentType, String originalFilename) {
    if (originalFilename != null && originalFilename.contains(".")) {
      final String candidate = originalFilename
          .substring(originalFilename.lastIndexOf('.') + 1)
          .toLowerCase(Locale.ROOT)
          .replaceAll("[^a-z0-9]+", "");
      if (!candidate.isBlank() && candidate.length() <= 10) {
        return candidate;
      }
    }

    if (contentType == null) {
      return "img";
    }

    return switch (contentType.toLowerCase(Locale.ROOT)) {
      case "image/png" -> "png";
      case "image/jpeg" -> "jpg";
      case "image/jpg" -> "jpg";
      case "image/gif" -> "gif";
      case "image/webp" -> "webp";
      case "image/bmp" -> "bmp";
      case "image/tiff" -> "tiff";
      case "image/svg+xml" -> "svg";
      default -> "img";
    };
  }

  private String sanitizePathComponent(String value) {
    if (value == null || value.isBlank()) {
      return "";
    }

    final String sanitized = value
        .trim()
        .replaceAll("[\\\\/:*?\"<>|]+", "-")
        .replaceAll("[@.]+", "-")
        .replaceAll("\\s+", "-")
        .replaceAll("-+", "-")
        .replaceAll("^[.-]+", "")
        .replaceAll("[.-]+$", "");

    if (sanitized.isBlank()) {
      return "";
    }
    return sanitized.length() > 80 ? sanitized.substring(0, 80) : sanitized;
  }

  private void writeInfoFile(Path uploadDirectory, DataTrusteeModelUploadRequestDto uploadRequest,
      Instant createdAt) throws IOException {
    if ((uploadRequest.getName() == null || uploadRequest.getName().isBlank())
        && (uploadRequest.getEmail() == null || uploadRequest.getEmail().isBlank())) {
      return;
    }

    final StringBuilder info = new StringBuilder();
    info.append("uploadDate=").append(createdAt).append(System.lineSeparator());
    if (uploadRequest.getName() != null && !uploadRequest.getName().isBlank()) {
      info.append("name=").append(uploadRequest.getName().trim()).append(System.lineSeparator());
    }
    if (uploadRequest.getEmail() != null && !uploadRequest.getEmail().isBlank()) {
      info.append("email=").append(uploadRequest.getEmail().trim()).append(System.lineSeparator());
    }

    Files.writeString(uploadDirectory.resolve("info.txt"), info.toString(),
        StandardOpenOption.CREATE_NEW);
  }

  private void sendUploadNotification(DataTrusteeModelUploadRequestDto uploadRequest,
      DataTrusteeModelUploadResponseDto uploadResponse) {
    try {
      this.dataTrusteeModelUploadNotificationService.notifyAboutUpload(uploadRequest,
          uploadResponse);
    } catch (RuntimeException ex) {
      // Upload persistence must remain successful even if notification delivery fails.
      log.warn("Failed to send upload notification mail", ex);
    }
  }

  private record ValidatedImage(byte[] bytes, String contentType) {
  }
}
