
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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ConfigurationProperties(prefix = "backend")
@Validated
@Data
public class BackendProperties {
  private String exampleProperty;

  private OntologyProperties ontology = new OntologyProperties();

  private CorsProperties cors;

  @NotNull
  private MetadataProperties metadata;

  @NotNull
  private DataTrusteeModelProperties dataTrusteeModel = new DataTrusteeModelProperties();

  @NotNull
  private MailProperties mail = new MailProperties();

  private int rateLimit = 200;

  private long rateDuration = 60000;

  @Data
  @Validated
  public static class OntologyProperties {
    @NotBlank
    private String basePath;

    /**
     * Base URI used to derive ontology instance URIs from DTO names.
     */
    @NotBlank
    private String baseUri = "https://www.example.de/ontology/";
  }

  @Data
  @Validated
  public static class CorsProperties {
    /**
     * List of allowed origins for CORS configuration. If empty or null, defaults to "*".
     */
    private List<String> allowedOrigins;

    /**
     * List of allowed HTTP methods for CORS configuration. If empty or null, defaults to
     * "GET,POST,PUT,DELETE,OPTIONS".
     */
    private List<String> allowedMethods = Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS");

    /**
     * List of allowed headers for CORS configuration. If empty or null, defaults to "*".
     */
    private List<String> allowedHeaders = List.of("*");

    public List<String> getAllowedOrigins() {
      return this.allowedOrigins;
    }

    public void setAllowedOrigins(List<String> allowedOrigins) {
      this.allowedOrigins = (allowedOrigins == null || allowedOrigins.isEmpty())
          ? List.of("*")
          : allowedOrigins;
    }

    public List<String> getAllowedHeaders() {
      return this.allowedHeaders;
    }

    public void setAllowedHeaders(List<String> allowedHeaders) {
      this.allowedHeaders = (allowedHeaders == null || allowedHeaders.isEmpty())
          ? List.of("*")
          : allowedHeaders;
    }

    public List<String> getAllowedMethods() {
      return this.allowedMethods;
    }

    public void setAllowedMethods(List<String> allowedMethods) {
      this.allowedMethods = (allowedMethods == null || allowedMethods.isEmpty())
          ? List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
          : allowedMethods;
    }
  }

  @Data
  @Validated
  public static class MetadataProperties {
    /**
     * Base path for metadata files.
     */
    @NotEmpty
    private String basePath;

    /**
     * Base package for domain modules.
     */
    private String domainBasePackage;

    /**
     * Enable schema vs i18n consistency check.
     */
    private Boolean consistencyCheckEnabled = Boolean.TRUE;

    /**
     * Preload metadata at application startup.
     */
    private Boolean preloadOnStartup = Boolean.TRUE;
  }

  @Data
  @Validated
  public static class DataTrusteeModelProperties {
    /**
     * Base path for model folders containing the stored data trustee model JSON file.
     */
    @NotEmpty
    private String basePath = "data/datatrusteemodels";

    /**
     * Base path where upload directories for stored data trustee models are created.
     */
    @NotEmpty
    private String uploadBasePath = "data/datatrusteemodels/uploads";

    /**
     * Maximum allowed size in bytes for a data trustee model upload (multipart request).
     */
    @Positive
    private long maxUploadSizeBytes = 350L * 1024L;
  }

  @Data
  @Validated
  public static class MailProperties {
    /**
     * Global switch to enable or disable upload notification mails.
     */
    private boolean enabled = false;

    /**
     * Recipients for upload notification mails. If empty, no mail is sent.
     */
    private List<String> recipients = Collections.emptyList();

    /**
     * Optional sender address for upload notification mails.
     */
    private String from;

    /**
     * If true, include both stored upload files as mail attachments.
     */
    private boolean includeUploadAttachments = false;
  }
}
