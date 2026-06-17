
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
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.datatrusteemodel.DataTrusteeModelService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

/**
 * Executes startup initialization tasks.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StartupService {

  private final MetadataService metadataService;

  private final DataTrusteeModelService dataTrusteeModelService;

  private final OntologyCommentLabelExtractionService ontologyCommentLabelExtractionService;

  private final BackendProperties backendProperties;

  @PostConstruct
  void initializeOnStartup() {
    log.info("Starting application initialization tasks...");
    this.preloadMetadataIfConfigured();
    this.generateOntologyCommentLabels();
    this.preloadDataTrusteeModels();
  }

  private void preloadMetadataIfConfigured() {
    if (!Boolean.TRUE.equals(this.backendProperties.getMetadata().getPreloadOnStartup())) {
      log.info("Metadata preloading at startup is disabled by configuration.");
      return;
    }

    try {
      log.info("Triggering metadata preloading at application startup...");
      // Trigger initial load, merge, enrichment, and consistency checks at startup.
      this.metadataService.listModules(null);
      log.info("Metadata preloading completed successfully.");
    } catch (final Exception ex) {
      log.error("Metadata preloading failed during startup.", ex);
      throw new IllegalStateException("Metadata preloading failed during startup.", ex);
    }
  }

  private void preloadDataTrusteeModels() {
    try {
      log.info("Importing data trustee models and generating derived artifacts at startup...");
      final int exportedCount = this.dataTrusteeModelService.importAllDataTrusteeModels().size();
      log
          .info("Data trustee model import completed successfully ({} model(s) exported).",
              exportedCount);
    } catch (final Exception ex) {
      log.error("Data trustee model preloading failed during startup.", ex);
      throw new IllegalStateException("Data trustee model preloading failed during startup.", ex);
    }
  }

  private void generateOntologyCommentLabels() {
    try {
      log.info("Generating ontology comment/label export at application startup...");
      final Path outputJsonFile = this.ontologyCommentLabelExtractionService
          .generateOntologyCommentLabelExport();
      log.info("Ontology comment/label export completed successfully: {}", outputJsonFile);
    } catch (final Exception ex) {
      log.error("Ontology comment/label export failed during startup.", ex);
      throw new IllegalStateException("Ontology comment/label export failed during startup.", ex);
    }
  }

}
