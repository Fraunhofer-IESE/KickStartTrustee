
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

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.datamodel.ForgeClass;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.datamodel.ForgeIndividual;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.OntologyNavigationService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/ontology/navigation")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Ontology Navigation Controller", description = "Provides endpoints to navigate through the ontology, retrieve classes and individuals.")
public class OntologyNavigationController {
  private final OntologyNavigationService ontologyNavigationService;

  @GetMapping("/classes")
  public ResponseEntity<?> getForgeClasses(@RequestParam(required = false) String classURI) {
    if (classURI == null) {
      log.info("Called getForgeClasses() without classURI");
      final List<ForgeClass> result = this.ontologyNavigationService
          .getAllForgeClassesOfBaseOntology();
      return ResponseEntity.ok(result);
    } else {
      log.info("Called getForgeClasses() with classURI={}", classURI);
      final String decodedClassURI = URLDecoder.decode(classURI, StandardCharsets.UTF_8);
      log.debug("Decoded classURI: {}", decodedClassURI);
      final ForgeClass result = this.ontologyNavigationService
          .getForgeClassOfBaseOntology(decodedClassURI);
      return ResponseEntity.ok(result);
    }
  }

  @GetMapping("/classes/filter")
  public List<ForgeClass> getAllForgeClassesOfBaseOntologyFiltered(
      @RequestParam String[] extensionPrefix) {
    log
        .info("Called getAllForgeClassesOfBaseOntologyFiltered() with extensionPrefix={}",
            (Object) extensionPrefix);
      return this.ontologyNavigationService
          .getAllForgeClassesOfBaseOntology(extensionPrefix);
  }

  @GetMapping("/individuals")
  public ResponseEntity<?> getIndividuals(@RequestParam(required = true) String ontologyURI,
      @RequestParam(required = false) String individualURI,
      @RequestParam(required = false) String[] extensionPrefix) {
    log
        .info("Called getIndividuals() with ontologyURI={}, individualURI={}, extensionPrefix={}",
            ontologyURI, individualURI, extensionPrefix);

    // check if ontology uri is present
    if (ontologyURI == null || ontologyURI.isEmpty()) {
      log.warn("ontologyURI is required for getIndividuals endpoint");
      return ResponseEntity.badRequest().body("Parameter 'ontologyURI' is required.");
    }

    // ontology uri and individual uri are present
    if (individualURI != null) {
      final String decodedIndividualURI = URLDecoder.decode(individualURI, StandardCharsets.UTF_8);
      final String decodedOntologyURI = URLDecoder.decode(ontologyURI, StandardCharsets.UTF_8);
      final ForgeIndividual result = this.ontologyNavigationService
          .getForgeIndividualOfInstanceOntology(decodedIndividualURI, decodedOntologyURI);
      log.debug("Result: {}", result);
      return ResponseEntity.ok(result);
    }

    // ontology uri and extension prefix are present
    if (extensionPrefix != null) {
      final String decodedOntologyURI = URLDecoder.decode(ontologyURI, StandardCharsets.UTF_8);
      final List<ForgeIndividual> result = this.ontologyNavigationService
          .getAllForgeIndividualsOfOntology(decodedOntologyURI, extensionPrefix);
      log.debug("Result: {}", result);
      return ResponseEntity.ok(result);
    }

    // only ontology uri is present
    final String decodedOntologyURI = URLDecoder.decode(ontologyURI, StandardCharsets.UTF_8);
    final List<ForgeIndividual> result = this.ontologyNavigationService
        .getAllForgeIndividualsOfOntology(decodedOntologyURI);
    log.debug("Result: {}", result);
    return ResponseEntity.ok(result);
  }

}
