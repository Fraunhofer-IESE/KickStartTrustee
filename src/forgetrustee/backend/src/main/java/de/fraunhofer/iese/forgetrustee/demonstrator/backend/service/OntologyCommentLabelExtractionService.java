
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

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper.commentExtractor.OntologyCommentLabelExtractor;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper.commentExtractor.OntologyCommentLabelValidationResult;

import lombok.extern.slf4j.Slf4j;
import org.apache.jena.rdf.model.Model;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Generates the ontology comment/label export JSON used by the demonstrator.
 */
@Service
@Slf4j
public class OntologyCommentLabelExtractionService {

  private static final Path INPUT_ONTOLOGY_FILE = Path.of("data/currentBase.ttl");

  private static final Path OUTPUT_DIRECTORY = Path.of("data");

  private static final Set<String> SELECTED_PROPERTIES = Set
      .of("hasDomain", "hasFunder", "hasRetentionPeriod", "hasDataAdministratedBy");

  public Path generateOntologyCommentLabelExport() throws IOException {

    Files.createDirectories(OUTPUT_DIRECTORY);

    final Path outputJsonFile = OUTPUT_DIRECTORY.resolve("ontology-comments-labels.json");

    final Model model = OntologyCommentLabelExtractor.loadTurtleModel(INPUT_ONTOLOGY_FILE);

    final OntologyCommentLabelExtractor extractor = new OntologyCommentLabelExtractor(model);

    final Map<String, Object> result = new LinkedHashMap<>();

    result.putAll(extractor.extractClassEntries());

    result.putAll(extractor.extractSelectedObjectPropertyEntries(SELECTED_PROPERTIES));

    extractor.writeAsJson(result, outputJsonFile);

    this.printValidationWarnings(extractor.getValidationResult());

    return outputJsonFile;
  }

  private void printValidationWarnings(OntologyCommentLabelValidationResult validation) {

    if (!validation.hasWarnings()) {
      return;
    }

    log.warn("Warnungen gefunden:");

    if (!validation.getMissingLabels().isEmpty()) {

      log.warn("Ohne rdfs:label:");

      validation.getMissingLabels().forEach(name -> log.warn("- {}", name));
    }

    if (!validation.getMissingDocumentation().isEmpty()) {

      log.warn("Ohne rdfs:comment oder skos:definition:");

      validation.getMissingDocumentation().forEach(name -> log.warn("- {}", name));
    }

    if (!validation.getDuplicateLanguageEntries().isEmpty()) {

      log.warn("Mehrere Einträge pro Sprache:");

      validation.getDuplicateLanguageEntries().forEach(name -> log.warn("- {}", name));
    }
  }
}
