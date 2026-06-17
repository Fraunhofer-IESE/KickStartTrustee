
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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.impl;
/* Created by chwalek on 12.08.2025 */

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.apache_jena.navigation.OntologyNavigation;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.apache_jena.translater.OntologyTranslater;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.datamodel.ForgeClass;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.datamodel.ForgeIndividual;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.OntologyNavigationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.apache.jena.ontology.Individual;
import org.apache.jena.ontapi.model.OntIndividual;
//import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontapi.model.OntClass;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OntologyNavigationService_Impl implements OntologyNavigationService {

  private final OntologyNavigation navigation;

  private final OntologyTranslater translater;

  @Override
  public List<ForgeClass> getAllForgeClassesOfBaseOntology() {
    log.debug("Retrieving all ForgeClasses of the base ontology.");
    return this.getAllForgeClassesOfBaseOntology(new String[] {});
  }

  @Override
  public List<ForgeClass> getAllForgeClassesOfBaseOntology(String... extensionPrefix) {
    log
        .debug("Retrieving all ForgeClasses of the base ontology with extensions: {}",
            (Object[]) extensionPrefix);
    final List<OntClass> allOntClasses = this.navigation.getAllOntologyClasses(extensionPrefix);

    final List<ForgeClass> allForgeClasses = new ArrayList<>();
    for (final OntClass cls : allOntClasses) {
      log.debug("Translating OntClass {} to ForgeClass.", cls.getURI());
      allForgeClasses.add(this.translater.translateToForgeClass(cls));
    }

    log.debug("Total ForgeClasses retrieved: {}", allForgeClasses.size());
    return allForgeClasses;
  }

  @Override
  public ForgeClass getForgeClassOfBaseOntology(String classURI) {
    final OntClass retrievedOntClass = this.navigation.getOntologyClass(classURI);
    return this.translater.translateToForgeClass(retrievedOntClass);
  }

  @Override
  public List<ForgeIndividual> getAllForgeIndividualsOfOntology(String ontologyURI) {
    return this.getAllForgeIndividualsOfOntology(ontologyURI, new String[] {});
  }

  @Override
  public List<ForgeIndividual> getAllForgeIndividualsOfOntology(String ontologyURI,
      String... extensionPrefix) {
    final List<OntIndividual.Named> allIndividuals = this.navigation
        .getAllIndividuals(ontologyURI, extensionPrefix);
    final List<ForgeIndividual> allForgeInstances = new ArrayList<>();
    for (final OntIndividual.Named cls : allIndividuals) {
      allForgeInstances.add(this.translater.translateToForgeIndividual(cls));
    }

    return allForgeInstances;
  }

  @Override
  public ForgeIndividual getForgeIndividualOfInstanceOntology(String individualURI,
      String ontologyURI) {
    final OntIndividual.Named individual = (OntIndividual.Named) this.navigation.getIndividual(individualURI, ontologyURI);
    return this.translater.translateToForgeIndividual(individual);
  }
}
