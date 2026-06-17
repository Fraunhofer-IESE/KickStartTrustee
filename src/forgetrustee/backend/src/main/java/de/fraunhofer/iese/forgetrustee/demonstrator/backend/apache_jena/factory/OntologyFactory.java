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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.apache_jena.factory;

//import org.apache.jena.ontology.Individual;
import org.apache.jena.ontapi.model.OntIndividual;
//import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontapi.model.OntClass;
//import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontapi.model.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public interface OntologyFactory {

    OntModel createOntModel(String URI, String baseOntologyURI, String[] extensionPrefixes);

    OntIndividual createIndividualNew(String uriOfNewInstance, OntClass clsToInstanceFrom, String uriOfModel);

    OntIndividual createIndividual(String uriOfNewInstance, String uriOfClassToInstanceFrom, String uriOfModel);

    String getModelTTL(String uri);

    void saveModel(String modelToSaveUri, Model modelToSave, String format, boolean onlyInstances);

    String toTTL(OntModel model, boolean onlyInstances);

    void saveNewModel(String modelToSaveUri, Model modelToSave, String format);

    void addInstanceToModel(OntIndividual instance, String modelURIIndividualIsFrom, String modelURIToAddIndividualTo);

    void removeIndividualFromOntology(OntIndividual individual, String uriOfModel);

    void addLiteral(OntIndividual subject, Property property, String literal, String language, String uriOfModel);

    void addProperty(OntIndividual subject, Property property, OntIndividual object, String uriOfModelOfIndividuals);

    void addPropertyConcept(OntIndividual subject, Property property, Resource concept, String uriOfModelOfIndividuals);

    void addType(OntIndividual subject, Property property, OntClass object, String uriOfModelOfIndividuals);
}
