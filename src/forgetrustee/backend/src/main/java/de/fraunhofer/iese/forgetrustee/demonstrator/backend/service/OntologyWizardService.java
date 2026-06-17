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
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModel;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.wizard.DataTrusteeWizardModel;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.validation.ShaclReportDTO;
import org.apache.jena.ontapi.model.OntModel;
import org.apache.jena.rdf.model.Model;

public interface OntologyWizardService {

    OntModel buildFromDataTrusteeModel(DataTrusteeWizardModel model);

    DataTrusteeWizardModel loadFromTtl(String ttlPath, String ontologyURI);

    String generateExecutiveSummary(DataTrusteeWizardModel model);

    DataTrusteeWizardModel toWizardModel(DataTrusteeModel model);

    Model createNewInstanceOfOntology(String newOntologyURI);

    ForgeIndividual createDataTrustee(String label, String ontologyInstance_URI, String lang);

    ForgeIndividual createActorInOntology(String label, String ontologyInstance_URI, String role, String lang);

    ForgeIndividual createDataTrusteeModel(String dataTrusteeModelName, String ontologyURI, String langSetting);

    ForgeIndividual createDataInOntology(String dataName, String ontologyURI, String lang);

    ForgeIndividual createIndividual(String individualName, String ontologyURI, String classURI, String langSetting);

    void addProperty(String subject_URI, String property_URI, String object_URI, String ontologyInstance_URI, boolean propertyBetweenIndividuals);

    void removeProperty(String subject_URI, String property_URI, String object_URI, String ontologyInstance_URI, boolean propertyBetweenIndividuals);

    ForgeIndividual addLiteral(String subjectURI, String propertyURI, String literal, String lang, String ontologyURI);

    void addNameToActor(String actor_URI, String name, String modelIndividualsAreFrom_URI);

    void addAffiliationToActor(String actor_URI, String affiliation_URI, String modelIndividualsAreFrom_URI);

    void addDataTrusteeGoal(String goal_URI, String modelIndividualsAreFrom_URI);

    void addPropertyToIndividual(String subject_URI, String property_URI, String object_URI, String uriOfModelIndividualsAreFrom);

    void addData(String categoryName, String description);

    void addDataSubclass(String data_URI, String subclass_URI);

    ForgeIndividual addNewSubtype(String ontologyURI, String subjectURI, String subtypeURI);

    ForgeIndividual removeNewSubtype(String ontologyURI, String subjectURI, String subtypeURI);

    DataTrusteeWizardModel retrieveDataModelFromTTL(String ontologyURI);

    String toTTL(OntModel ontModel);

    ShaclReportDTO validate(OntModel ontModel, DataTrusteeWizardModel wizardModel,  String aDefault);
}
