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
/* Created by chwalek on 11.08.2025 */

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.apache_jena.navigation.OntologySHACLConverter_Impl;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper.PrefixUtil;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper.StaticText;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.impl.FileReader_ServiceRun;
import lombok.RequiredArgsConstructor;
import org.apache.jena.ontapi.OntModelFactory;
import org.apache.jena.ontapi.OntSpecification;
import org.apache.jena.ontapi.model.OntIndividual;
//import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
//import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontapi.model.OntClass;
//import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontapi.model.OntModel;
//import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OntologyFactory_Impl implements OntologyFactory {

  //private final DatabaseConnection db;
  private final FileReader_ServiceRun db;

  @Override
  public OntModel createOntModel(String URI, String baseOntologyURI, String[] extensionPrefixes) {
    //final OntModelSpec spec = OntModelSpec.OWL_MEM_MICRO_RULE_INF;
    final OntModel model =
            OntModelFactory.createModel(OntSpecification.OWL2_FULL_MEM);

    // Ontology-Resource erzeugen (ähnlich wie model.createOntology(URI))
    Resource ontology = model.createResource(URI);
    model.add(ontology, org.apache.jena.vocabulary.RDF.type,
            org.apache.jena.vocabulary.OWL2.Ontology);
    model.setNsPrefix("", URI);

    if (extensionPrefixes == null || extensionPrefixes.length == 0) {
      return model;
    }

    for (final String extensionPrefix : extensionPrefixes) {
      model.setNsPrefix(extensionPrefix, baseOntologyURI + extensionPrefix + "#");
    }
    return model;
  }

  @Override
  public OntIndividual createIndividualNew(String uriOfNewInstance, OntClass clsToInstanceFrom,
      String uriOfModel) {
    final String stringModelToInstanceFrom = this.db.getModelTTL(uriOfModel);
    final OntModel modelToInstanceFrom = OntologySHACLConverter_Impl
        .retrieveOntModelFromText(stringModelToInstanceFrom, StaticText.TURTLE);
    final OntIndividual newInstance = modelToInstanceFrom
        .createIndividual(uriOfNewInstance, clsToInstanceFrom);
    this.saveModel(uriOfModel, modelToInstanceFrom, StaticText.TURTLE, false);

    return newInstance;
  }

  @Override
  public OntIndividual createIndividual(String uriOfNewInstance, String uriOfClassToInstanceFrom,
      String uriOfModel) {
    final String stringModelToInstanceFrom = this.db.getModelTTL(uriOfModel);
    final OntModel modelToInstanceFrom = OntologySHACLConverter_Impl
        .retrieveOntModelFromText(stringModelToInstanceFrom, StaticText.TURTLE);
    final OntClass classToInstanceFrom = modelToInstanceFrom.getOntClass(uriOfClassToInstanceFrom);
    final OntIndividual newInstance = modelToInstanceFrom
        .createIndividual(uriOfNewInstance, classToInstanceFrom);
    this.saveModel(uriOfModel, modelToInstanceFrom, StaticText.TURTLE, true);

    return newInstance;
  }

  @Override
  public void addInstanceToModel(OntIndividual individual, String modelURIIndividualIsFrom,
      String modelURIToAddIndividualTo) {
    final String stringModelToInstanceFrom = this.db.getModelTTL(modelURIIndividualIsFrom);
    final OntModel modelToInstanceFrom = OntologySHACLConverter_Impl
        .retrieveOntModelFromText(stringModelToInstanceFrom, StaticText.TURTLE);
    final String stringModelToAddTo = this.db.getModelTTL(modelURIToAddIndividualTo);
    final OntModel modelToAddTo = OntologySHACLConverter_Impl
        .retrieveOntModelFromText(stringModelToAddTo, StaticText.TURTLE);

    String uri = individual.getURI();
    Resource subj = modelToInstanceFrom.getResource(uri);

    //modelToAddTo.add(modelToInstanceFrom.listStatements(individual, null, (RDFNode) null));
    modelToAddTo.add(modelToInstanceFrom.listStatements(subj, null, (RDFNode) null));

    PrefixUtil.derivePrefixesFromUsedNamespaces(modelToAddTo);
    this.saveModel(modelURIToAddIndividualTo, modelToAddTo, StaticText.TURTLE, true);

  }

  @Override
  public void removeIndividualFromOntology(OntIndividual individual, String uriOfModel) {
    final String stringModelToInstanceFrom = this.db.getModelTTL(uriOfModel);
    final OntModel modelToDeleteFrom = OntologySHACLConverter_Impl
        .retrieveOntModelFromText(stringModelToInstanceFrom, StaticText.TURTLE);
    modelToDeleteFrom.removeAll(individual, null, (RDFNode) null);
    modelToDeleteFrom.removeAll(null, null, individual);
    this.saveModel(uriOfModel, modelToDeleteFrom, StaticText.TURTLE, false);
  }

  @Override
  public void addLiteral(OntIndividual subject, Property property, String literal, String language,
      String uriOfModel) {
    final String stringModelToInstanceFrom = this.db.getModelTTL(uriOfModel);
    final OntModel model = OntologySHACLConverter_Impl
        .retrieveOntModelFromText(stringModelToInstanceFrom, StaticText.TURTLE);
    if (language != null && !language.isBlank()) {
      model.add(subject, property, literal, language);
    } else {
      model.add(subject, property, literal);
    }
    this.saveModel(uriOfModel, model, StaticText.TURTLE, true);
  }

  @Override
  public void addProperty(OntIndividual subject, Property property, OntIndividual object,
      String uriOfModelOfIndividuals) {
    final String stringModelToInstanceFrom = this.db.getModelTTL(uriOfModelOfIndividuals);
    final OntModel model = OntologySHACLConverter_Impl
        .retrieveOntModelFromText(stringModelToInstanceFrom, StaticText.TURTLE);

    /**final OntProperty inverseProperty = ((ObjectProperty) property).getInverse();
    model.add(subject, property, object);
    model.add(object, inverseProperty, subject);
    this.saveModel(uriOfModelOfIndividuals, model, StaticText.TURTLE, true);**/

    // Re-bind in dieses Model (nur über URI!)
    Resource s = model.createResource(subject.getURI());
    Property p = model.createProperty(property.getURI());
    Resource o = model.createResource(object.getURI());

    // Forward triple
    model.add(s, p, o);

    // Inverse nur wenn vorhanden
    if (p.canAs(ObjectProperty.class)) {
      ObjectProperty op = p.as(ObjectProperty.class);
      OntProperty inv = op.getInverse();
      if (inv != null) {
        model.add(o, inv, s);
      }
    }

    this.saveModel(uriOfModelOfIndividuals, model, StaticText.TURTLE, true);

  }

  @Override
  public void addPropertyConcept(OntIndividual subject,
                                 Property property,
                                 Resource conceptOrClassIri,
                                 String uriOfModelOfIndividuals) {

    if (subject == null) {
      throw new IllegalArgumentException("Cannot add concept property: subject is null");
    }
    if (property == null) {
      throw new IllegalArgumentException("Cannot add concept property: property is null");
    }
    if (conceptOrClassIri == null || conceptOrClassIri.getURI() == null
        || conceptOrClassIri.getURI().isBlank()) {
      throw new IllegalArgumentException(String.format(
          "Cannot add concept property '%s' for subject '%s': object concept/class URI is missing",
          property.getURI(), subject.getURI()));
    }

    final String ttl = this.db.getModelTTL(uriOfModelOfIndividuals);
    final OntModel model = OntologySHACLConverter_Impl
            .retrieveOntModelFromText(ttl, StaticText.TURTLE);

    // Nur über URI referenzieren (kein getIndividual(), kein rdf:type setzen)
    Resource s = model.createResource(subject.getURI());
    Resource o = model.createResource(conceptOrClassIri.getURI());

    // Forward triple: Individual -> (Concept/Class IRI)
    model.add(s, property, o);

    // Inverse nur, wenn vorhanden und sinnvoll
    if (property.canAs(ObjectProperty.class)) {
      //ObjectProperty op = property.as(ObjectProperty.class);
      final OntProperty inverseProperty = ((ObjectProperty) property).getInverse();
      //OntProperty inv = op.getInverse();
      if (inverseProperty != null) {
        model.add(o, inverseProperty, s);
      }
    }

    this.saveModel(uriOfModelOfIndividuals, model, StaticText.TURTLE, true);
  }

  @Override
  public void addType(OntIndividual subject, Property property, OntClass object, String uriOfModelOfIndividuals) {
    final String stringModelToInstanceFrom = this.db.getModelTTL(uriOfModelOfIndividuals);
    final OntModel model = OntologySHACLConverter_Impl
            .retrieveOntModelFromText(stringModelToInstanceFrom, StaticText.TURTLE);
    model.add(subject, property, object);
    this.saveModel(uriOfModelOfIndividuals, model, StaticText.TURTLE, true);
  }

  @Override
  public String getModelTTL(String uri){
    return this.db.getModelTTL(uri);
  }

  @Override
  public void saveModel(String modelToSaveUri, Model modelToSave, String format, boolean onlyInstances) {
    if (onlyInstances) {
      Model out = ModelFactory.createDefaultModel();
      out.setNsPrefixes(modelToSave.getNsPrefixMap());

      // 1) Instanzen + verwendete Klassen sammeln
      Set<Resource> usedClasses = new HashSet<>();

      ResIterator subjects = modelToSave.listSubjectsWithProperty(RDF.type);
      try {
        while (subjects.hasNext()) {
          Resource subj = subjects.next();

          // nur URI-Subjekte
          if (!subj.isURIResource()) {
            continue;
          }

          StmtIterator stmts = modelToSave.listStatements(subj, null, (RDFNode) null);
          try {
            while (stmts.hasNext()) {
              Statement stmt = stmts.next();

              if (stmt.getSubject().isAnon()) continue;
              if (stmt.getObject().isAnon())  continue;

              // Instanz-Tripel übernehmen
              out.add(stmt);

              // verwendete Klassen merken (rdf:type)
              if (stmt.getPredicate().equals(RDF.type) && stmt.getObject().isURIResource()) {
                Resource cls = stmt.getObject().asResource();
                usedClasses.add(cls);
              }
            }
          } finally {
            stmts.close();
          }
        }
      } finally {
        subjects.close();
      }

      // 2) (optional) Superklassen der verwendeten Klassen mit aufnehmen
      //    falls du auch deren Definitionen brauchst
      boolean changed;
      do {
        changed = false;
        Set<Resource> toAdd = new HashSet<>();
        for (Resource cls : usedClasses) {
          StmtIterator scIt = modelToSave.listStatements(cls, RDFS.subClassOf, (RDFNode) null);
          try {
            while (scIt.hasNext()) {
              Statement scStmt = scIt.next();
              RDFNode superNode = scStmt.getObject();
              if (superNode.isURIResource()) {
                Resource superCls = superNode.asResource();
                if (!usedClasses.contains(superCls)) {
                  toAdd.add(superCls);
                }
              }
            }
          } finally {
            scIt.close();
          }
        }
        if (!toAdd.isEmpty()) {
          usedClasses.addAll(toAdd);
          changed = true;
        }
      } while (changed);

      // 3) TBox-Tripel der verwendeten Klassen in out kopieren
      for (Resource cls : usedClasses) {
        StmtIterator classStmts = modelToSave.listStatements(cls, null, (RDFNode) null);
        try {
          while (classStmts.hasNext()) {
            Statement s = classStmts.next();
            // Blank Nodes je nach Bedarf filtern
            if (s.getSubject().isAnon()) continue;
            if (s.getObject().isAnon())  continue;
            out.add(s);
          }
        } finally {
          classStmts.close();
        }
      }

      // 4) Serialisieren
      String modelToSaveString =
              OntologySHACLConverter_Impl.retrieveStringFromOntModel(out, format);
      this.db.setModelTTL(modelToSaveUri, modelToSaveString);
    } else {
      String modelToSaveString =
              OntologySHACLConverter_Impl.retrieveStringFromOntModel(modelToSave, format);
      this.db.setModelTTL(modelToSaveUri, modelToSaveString);
    }
  }

  @Override
  public String toTTL(OntModel model, boolean onlyInstances){
    if (onlyInstances) {
      Model out = ModelFactory.createDefaultModel();
      out.setNsPrefixes(model.getNsPrefixMap());

      // 1) Instanzen + verwendete Klassen sammeln
      Set<Resource> usedClasses = new HashSet<>();

      ResIterator subjects = model.listSubjectsWithProperty(RDF.type);
      try {
        while (subjects.hasNext()) {
          Resource subj = subjects.next();

          // nur URI-Subjekte
          if (!subj.isURIResource()) {
            continue;
          }

          StmtIterator stmts = model.listStatements(subj, null, (RDFNode) null);
          try {
            while (stmts.hasNext()) {
              Statement stmt = stmts.next();

              if (stmt.getSubject().isAnon()) continue;
              if (stmt.getObject().isAnon())  continue;

              // Instanz-Tripel übernehmen
              out.add(stmt);

              // verwendete Klassen merken (rdf:type)
              if (stmt.getPredicate().equals(RDF.type) && stmt.getObject().isURIResource()) {
                Resource cls = stmt.getObject().asResource();
                usedClasses.add(cls);
              }
            }
          } finally {
            stmts.close();
          }
        }
      } finally {
        subjects.close();
      }

      // 2) (optional) Superklassen der verwendeten Klassen mit aufnehmen
      //    falls du auch deren Definitionen brauchst
      boolean changed;
      do {
        changed = false;
        Set<Resource> toAdd = new HashSet<>();
        for (Resource cls : usedClasses) {
          StmtIterator scIt = model.listStatements(cls, RDFS.subClassOf, (RDFNode) null);
          try {
            while (scIt.hasNext()) {
              Statement scStmt = scIt.next();
              RDFNode superNode = scStmt.getObject();
              if (superNode.isURIResource()) {
                Resource superCls = superNode.asResource();
                if (!usedClasses.contains(superCls)) {
                  toAdd.add(superCls);
                }
              }
            }
          } finally {
            scIt.close();
          }
        }
        if (!toAdd.isEmpty()) {
          usedClasses.addAll(toAdd);
          changed = true;
        }
      } while (changed);

      // 3) TBox-Tripel der verwendeten Klassen in out kopieren
      for (Resource cls : usedClasses) {
        StmtIterator classStmts = model.listStatements(cls, null, (RDFNode) null);
        try {
          while (classStmts.hasNext()) {
            Statement s = classStmts.next();
            // Blank Nodes je nach Bedarf filtern
            if (s.getSubject().isAnon()) continue;
            if (s.getObject().isAnon())  continue;
            out.add(s);
          }
        } finally {
          classStmts.close();
        }
      }

      // 4) Serialisieren
        return OntologySHACLConverter_Impl.retrieveStringFromOntModel(out, "TURTLE");
    } else {
        return OntologySHACLConverter_Impl.retrieveStringFromOntModel(model, "TURTLE");
    }
  }


  @Override
  public void saveNewModel(String modelToSaveUri, Model model, String format) {
    final String modelToSaveString = OntologySHACLConverter_Impl
            .retrieveStringFromOntModel(model, format);
    //TODO check how to path
    Path projectRoot = Path.of("").toAbsolutePath();
    Path dataDir = projectRoot.resolve("data");
    String cleaned = modelToSaveUri.replaceAll(StaticText.REGEX_FILE_NAME, "");
    this.db.putNewEntry(modelToSaveUri, String.valueOf(dataDir)+"\\"+cleaned+".ttl", modelToSaveString);
  }

  private static OntModel wrapAsOntapiOntModel(Model base) {
    if (base instanceof OntModel) {
      return (OntModel) base;
    }
    return OntModelFactory.createModel(base.getGraph(), OntSpecification.OWL2_FULL_MEM_RULES_INF);
  }



}
