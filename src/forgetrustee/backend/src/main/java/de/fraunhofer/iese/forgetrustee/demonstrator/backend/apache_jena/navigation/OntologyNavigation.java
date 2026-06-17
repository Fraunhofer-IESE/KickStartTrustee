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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.apache_jena.navigation;


//import org.apache.jena.ontology.Individual;
import org.apache.jena.ontapi.model.OntIndividual;
//import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontapi.model.OntClass;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

import java.util.List;

public interface OntologyNavigation {

    List<OntClass> getAllOntologyClasses();
    List<OntClass> getAllOntologyClasses(String... extensionPrefix);
    OntClass getOntologyClass(String uri);

    List<OntIndividual> getAllIndividuals(String ontologyUri);
    List<OntIndividual.Named> getAllIndividuals(String ontologyUri, String... extensionPrefix);
    OntIndividual getIndividual(String individualURI, String ontologyURI);

    Property getPropertyByURI(String propertyURI, String modelURI);

    String getOntologyIRI(Model model);
    String getOntologyIRI(boolean fromBase, Model model);

    boolean isSkosConcept(String uri, String modelURI);
    Resource getSkosConcept(String uri, String modelURI);

    boolean isSelectionStatus(String uri, String modelURI);
    Resource getSelectionStatus(String uri, String modelURI);

    boolean isOntologyClass(String uri, String modelURI);

}
