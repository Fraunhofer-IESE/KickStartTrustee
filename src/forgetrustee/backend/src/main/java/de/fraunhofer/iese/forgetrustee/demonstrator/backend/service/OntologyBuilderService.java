
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

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.datamodel.ForgeIndividual;

import org.apache.jena.rdf.model.Model;

/**
 *
 */
public interface OntologyBuilderService {

  Model createNewOntologyInstance(String iri, String baseOntologyUri,
      String[] baseOntologyExtensionPrefixes);

  Model createNewOntologyInstanceOfMergedBase(String newOntologyURI);

  ForgeIndividual createIndividual(String uriOfNewInstance, String uriOfClassToInstanceFrom,
      String uriOfModel, String uriOfModelToAddInstanceTo);

  ForgeIndividual addLabel(String uriOfOIndividual, String label, String language,
      String uriOfModel);

  ForgeIndividual addProperty(String subject_URI, String property_URI, String object_URI,
                              String uriOfModelPropertyIsFrom, String uriOfModelIndividualsAreFrom, boolean propertyBetweenIndividuals);


  void removeProperty(String subjectUri, String propertyUri, String objectUri, String uriOfBase, String ontologyInstanceUri);

  ForgeIndividual addAdditionalType(String subject_URI, String parent_URI,
                                    String uriOfModelIndividualsAreFrom);

  ForgeIndividual addLiteral(String subject_URI, String property_URI, String annotation,
      String language, String uriOfModelPropertyIsFrom, String uriOfModel);
}
