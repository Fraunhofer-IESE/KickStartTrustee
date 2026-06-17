
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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.service;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.config.BackendProperties;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper.StaticText;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.sys.JenaSystem;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Component
@RequiredArgsConstructor
public class ServiceRunStartup {

  private final BackendProperties backendProperties;

  private final ServiceRunDatabase serviceRunDatabase;

  @PostConstruct
  public void onStartup() {

    this.validateConfiguredPaths();
    log.info("Initializing Apache Jena System...");
    JenaSystem.init();
    log.info("Apache Jena System initialized.");
    this.setUpDatabasePaths();
  }

  private void validateConfiguredPaths() {
    log.info("Validating configured backend paths...");
    this.validateDirectory("ontology base", this.backendProperties.getOntology().getBasePath());
    if (this.backendProperties.getMetadata() == null) {
      throw new IllegalStateException("Metadata base path not configured");
    }
    this.validateDirectory("metadata base", this.backendProperties.getMetadata().getBasePath());
  }

  private void validateDirectory(String label, String pathValue) {
    if (pathValue == null || pathValue.isBlank()) {
      throw new IllegalStateException(label + " path not configured");
    }
    final Path path = Path.of(pathValue.trim());
    if (!Files.exists(path)) {
      throw new IllegalStateException(label + " path not found: " + path);
    }
    if (!Files.isDirectory(path)) {
      throw new IllegalStateException(label + " path is not a directory: " + path);
    }
    log.debug("{} path validated: {}", label, path.toAbsolutePath());
  }

  private void setUpDatabasePaths() {
    this.serviceRunDatabase.setUriOfBase(StaticText.KICKSTARTTRUSTEE_ONTOLOGY_NAMESPACE);
    final String basePath = this.backendProperties.getOntology().getBasePath();
    this.serviceRunDatabase
        .put(StaticText.KICKSTARTTRUSTEE_ONTOLOGY_NAMESPACE, basePath + "/currentBase.ttl");
    this.serviceRunDatabase.put(StaticText.FAKE_CARUSO_IRI, basePath + "/fake-caruso.ttl");
  }
}
