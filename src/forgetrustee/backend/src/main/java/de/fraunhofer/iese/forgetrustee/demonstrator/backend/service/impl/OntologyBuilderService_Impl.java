
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
/* Created by chwalek on 11.08.2025 */

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.apache_jena.factory.OntologyFactory;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.apache_jena.navigation.OntologyNavigation;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.apache_jena.translater.OntologyTranslater;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.datamodel.ForgeIndividual;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.OntologyBuilderService;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.ServiceRunDatabase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.apache.jena.ontology.Individual;
import org.apache.jena.ontapi.model.OntIndividual;
//import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontapi.model.OntClass;
import org.apache.jena.ontapi.model.OntObjectProperty;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OntologyBuilderService_Impl implements OntologyBuilderService {

  private final OntologyFactory factory;

  private final OntologyTranslater translater;

  private final OntologyNavigation navigation;

  private final ServiceRunDatabase runDatabase;

  @Override
  public Model createNewOntologyInstance(String iri, String baseOntologyUri,
      String[] baseOntologyExtensionPrefixes) {
    return this.factory.createOntModel(iri, baseOntologyUri, baseOntologyExtensionPrefixes);
  }

  @Override
  public Model createNewOntologyInstanceOfMergedBase(String newOntologyURI) {
    return this.factory.createOntModel(newOntologyURI, runDatabase.getBaseUri(), null);
  }

  @Override
  public ForgeIndividual createIndividual(String uriOfNewInstance, String uriOfClassToInstanceFrom,
      String uriOfModel, String uriOfModelToAddInstanceTo) {
    final OntClass clsToInstanceFrom = this.navigation.getOntologyClass(uriOfClassToInstanceFrom);
    if (clsToInstanceFrom == null) {
      log.warn("Für {} wird mit einem leeren Knoten verbunden", uriOfNewInstance);
    }
    String cleanedUri = toLocalName(uriOfNewInstance);
    log.info("Vor Individual {} Erstellung", uriOfNewInstance);
    final OntIndividual.Named individual = (OntIndividual.Named) this.factory
        .createIndividualNew(uriOfModelToAddInstanceTo + cleanedUri, clsToInstanceFrom,
            uriOfModel);
    this.factory.addInstanceToModel(individual, uriOfModel, uriOfModelToAddInstanceTo);
    this.factory.removeIndividualFromOntology(individual, uriOfModel);
    final ForgeIndividual forgeIndividual = this.translater.translateToForgeIndividual(individual);
    return forgeIndividual;
  }

  private static String toLocalName(String s) {
    String out =  s.trim()
            .replaceAll("ä", "ae")
            .replaceAll("ö", "oe")
            .replaceAll("ü", "ue")
            .replaceAll("Ä", "Ae")
            .replaceAll("Ö", "Oe")
            .replaceAll("Ü", "Ue")
            .replaceAll("ß", "ss")
            .replaceAll("\\s+", "")          // Spaces ->
            .replaceAll("[^A-Za-z0-9_\\-\\.]", ""); // Rest raus

    // falls leer -> fallback
    if (out.isEmpty()) {
      out = "_";
    }

    // verhindern, dass es mit Zahl anfängt
    if (Character.isDigit(out.charAt(0))) {
      out = "_" + out;
    }

    return out;
  }

  @Override
  public ForgeIndividual addLabel(String uriOfOIndividual, String label, String language,
      String uriOfModel) {
    final OntIndividual.Named individual = (OntIndividual.Named) this.navigation.getIndividual(uriOfOIndividual, uriOfModel);
    this.factory.addLiteral(individual, RDFS.label, label, language, uriOfModel);
    final ForgeIndividual forgeIndividual = this.translater.translateToForgeIndividual(individual);
    return forgeIndividual;
  }

  @Override
  public ForgeIndividual addProperty(String subject_URI, String property_URI, String object_URI,
                                     String uriOfModelPropertyIsFrom, String uriOfModelIndividualsAreFrom, boolean propertyBetweenIndividuals) {
    final OntIndividual.Named subject = (OntIndividual.Named) this.navigation
        .getIndividual(subject_URI, uriOfModelIndividualsAreFrom);
    final Property property = this.navigation
            .getPropertyByURI(property_URI, uriOfModelPropertyIsFrom);

    // Ziel kann Individual ODER Klasse sein -> Resource ist gemeinsamer Nenner
    final org.apache.jena.rdf.model.RDFNode objectNode;

    final OntIndividual object;
    if(propertyBetweenIndividuals){
      objectNode = this.navigation
              .getIndividual(object_URI, uriOfModelIndividualsAreFrom);
      this.factory.addProperty(subject, property, (OntIndividual) objectNode, uriOfModelIndividualsAreFrom);
    }
    else{

      Resource objectResource;

      //if (this.navigation.isOntologyClass(object_URI, uriOfModelIndividualsAreFrom)) {
      if(this.navigation.getOntologyClass(object_URI) != null){
        objectResource = this.navigation.getOntologyClass(object_URI);
      }
      else if (this.navigation.isSkosConcept(object_URI, uriOfModelPropertyIsFrom)) {
        objectResource = this.navigation.getSkosConcept(object_URI, uriOfModelPropertyIsFrom);
      }
      else if(this.navigation.isSelectionStatus(object_URI, uriOfModelPropertyIsFrom)){
        objectResource = this.navigation.getSelectionStatus(object_URI, uriOfModelPropertyIsFrom);
      }
      else {
        objectResource = this.navigation.getIndividual(object_URI, uriOfModelIndividualsAreFrom);
      }

      this.factory.addPropertyConcept(
              subject,
              property,
              objectResource,
              uriOfModelIndividualsAreFrom
      );

      /**objectNode = this.navigation.getOntologyClass(object_URI);
      this.factory.addPropertyConcept(subject, property, (Resource) objectNode, uriOfModelIndividualsAreFrom);**/

      // Sonderfall: object_URI ist eigentlich ein SKOS Concept (Individual), nicht owl:Class
      //if (this.navigation.isIndividual(object_URI, /* z.B. Base-Ontology-Model */ null)
      //        || this.navigation.isSkosConceptIndividual(object_URI)) {
      //  objectNode = this.navigation.getIndividualFromBaseOrAnyModel(object_URI); // falls vorhanden
      //} else {
      //  objectNode = this.navigation.getOntologyClass(object_URI); // OntClass
      //}
    }

    final ForgeIndividual forgeIndividual = this.translater.translateToForgeIndividual(subject);
    return forgeIndividual;
  }


  @Override
  public ForgeIndividual addAdditionalType(String subject_URI, String type_URI, String uriOfModelIndividualsAreFrom) {
    final OntIndividual.Named subject = (OntIndividual.Named) this.navigation
            .getIndividual(subject_URI, uriOfModelIndividualsAreFrom);
    final OntClass object = this.navigation
            .getOntologyClass(type_URI);

    this.factory.addType(subject, RDF.type, object, uriOfModelIndividualsAreFrom);

    final ForgeIndividual forgeIndividual = this.translater.translateToForgeIndividual(subject);
    return forgeIndividual;
  }

  @Override
  public ForgeIndividual addLiteral(String subject_URI, String property_URI, String annotation,
      String language, String uriOfModelPropertyIsFrom, String uriOfModel) {
    final OntIndividual.Named individual = (OntIndividual.Named) this.navigation.getIndividual(subject_URI, uriOfModel);
    //final Property property = this.navigation.getPropertyByURI(property_URI, uriOfModelPropertyIsFrom);
    final Property property = resolveProperty(property_URI, uriOfModelPropertyIsFrom);

    this.factory.addLiteral(individual, property, annotation, language, uriOfModel);
    final ForgeIndividual forgeIndividual = this.translater.translateToForgeIndividual(individual);
    return forgeIndividual;
  }

  @Override
  public void removeProperty(String subjectUri, String propertyUri, String objectUri, String uriOfBase, String ontologyInstanceUri) {

  }

  /**
   * Nutzt bei bekannten Namespaces (rdf, rdfs, owl, xsd, …) eine generische Property
   * und greift sonst auf navigation.getPropertyByURI(...) zurück.
   */
  private Property resolveProperty(String property_URI, String uriOfModelPropertyIsFrom) {
    // Standard-Namespaces zuerst
    if (property_URI.startsWith(RDFS.getURI())
            || property_URI.startsWith(RDF.getURI())
            || property_URI.startsWith(OWL.NS)
            || property_URI.startsWith(XSD.NS)) {
      // generische Property, nicht modellgebunden
      return ResourceFactory.createProperty(property_URI);
      // oder wenn du magst: direkt Konstanten wie RDFS.label, RDF.type usw.
    }

    // sonst: wie bisher im Quellmodell nachschlagen
    Property property =
            navigation.getPropertyByURI(property_URI, uriOfModelPropertyIsFrom);

    // Fallback: nie null zurückgeben
    return property != null
            ? property
            : ResourceFactory.createProperty(property_URI);
  }

}
