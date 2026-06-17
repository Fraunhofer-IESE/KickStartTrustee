
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
/* Created by chwalek on 12.08.2025 */

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper.PrefixUtil;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper.StaticText;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.DatabaseConnection;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.ServiceRunDatabase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.atlas.lib.NotImplemented;
//import org.apache.jena.ontology.Individual;
import org.apache.jena.ontapi.model.OntIndividual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontapi.model.OntObjectProperty;
//import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontapi.model.OntClass;
//import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontapi.model.OntModel;
import org.apache.jena.ontology.Ontology;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper.StaticText.OWL_CLASS_URI;

@Service
@RequiredArgsConstructor
@Slf4j
public class OntologyNavigation_Impl implements OntologyNavigation {

  private final DatabaseConnection db;
  private final ServiceRunDatabase serviceRunDatabase;

  @Override
  public List<OntClass> getAllOntologyClasses() {
    return List.of();
  }

  @Override
  public List<OntClass> getAllOntologyClasses(String... extensionPrefix) {
    // 2. Core-Vokabular, das wir überspringen möchten
    final Set<String> CORE_CLASSES = Set
        .of(StaticText.RDF_PROPERTY, StaticText.RDF_LIST, StaticText.RDFS_CLASS,
            StaticText.RDFS_RESOURCE, StaticText.OWL_CLASS, StaticText.OWL_THING,
            StaticText.OWL_NOTHING, StaticText.OWL_ONTOLOGY, StaticText.OWL_OBJECT_PROPERTY,
            StaticText.OWL_DATATYPE_PROPERTY);

    // 4. Ergebnis-Liste vorbereiten
    final List<OntClass> userClasses = new ArrayList<>();

    // 5. Über alle benannten Klassen iterieren (Iterator statt toList() spart Speicher)
    final String baseOntology_String = this.db.getBaseModelTTL();
    final OntModel baseOntology = OntologySHACLConverter_Impl
        .retrieveOntModelFromText(baseOntology_String, StaticText.TURTLE);
    List<OntClass.Named> classes = baseOntology.classes()
            .filter(c -> c.canAs(OntClass.Named.class))
            .map(c -> c.as(OntClass.Named.class))
            .filter(cls -> !cls.isAnon())
            .filter(cls -> cls.getURI() != null)
            .filter(cls -> !CORE_CLASSES.contains(cls.getURI()))
            .filter(cls -> cls.getURI().startsWith(StaticText.KICKSTARTTRUSTEE_ONTOLOGY_NAMESPACE))
            .toList();

    userClasses.addAll(classes);

    if (extensionPrefix == null || extensionPrefix.length == 0) {
      return userClasses;
    }

    // 5. Andernfalls: filtere nur die Klassen, deren URI mit einem der
    //    angegebenen Prefixe + "#" beginnt
    final List<String> prefixesWithHash = Arrays
        .stream(extensionPrefix)
        .map(p -> p.endsWith("#") ? p : p + "#")
        .toList();

    final List<OntClass> filteredClasses = new ArrayList<>();
    for (final OntClass cls : userClasses) {
      final String uri = cls.getURI();
      log.debug("▶ Prüfe OntClass: {}", uri);

      boolean keep = false;
      for (final String prefix : prefixesWithHash) {
        log.debug("   - vergleiche mit Prefix „{}", prefix);
        if (uri.contains(prefix)) {
          log.debug("     ✔ match: {}", prefix);
          keep = true;
          break;
        } else {
          log.debug("     ✘ kein match");
        }
      }

      if (keep) {
        log.debug("   => behalte {}", uri);
        filteredClasses.add(cls);
      } else {
        log.debug("   => verwerfe {}", uri);
      }
    }
    log.debug("Ergebnis: {} Klassen behalten.", filteredClasses.size());

    return filteredClasses;
  }

  @Override
  public OntClass getOntologyClass(String uri) {
    final String baseOntology_String = this.db
        .getModelTTL(StaticText.KICKSTARTTRUSTEE_ONTOLOGY_NAMESPACE);
    final OntModel baseOntology = OntologySHACLConverter_Impl
        .retrieveOntModelFromText(baseOntology_String, StaticText.TURTLE);
    if (!uri.startsWith(StaticText.KICKSTARTTRUSTEE_ONTOLOGY_NAMESPACE)) {
      uri = baseOntology.expandPrefix(uri);
    }
    final OntClass cls = baseOntology.getOntClass(baseOntology.expandPrefix(uri));
    return cls;
  }

  @Override
  public List<OntIndividual> getAllIndividuals(String ontologyUri) {
    return List.of();
  }

  public List<OntIndividual> getAllIndividuals_OLD(String ontologyUri, String... extensionPrefix) {
    final String baseOntology_String = this.db.getModelTTL(ontologyUri);
    final OntModel modelToLookIn = OntologySHACLConverter_Impl
        .retrieveOntModelFromText(baseOntology_String, StaticText.TURTLE);

    // Core-Vokabular, das wir überspringen möchten
    final Set<String> CORE_CLASSES = Set
        .of(StaticText.RDF_PROPERTY, StaticText.RDF_LIST, StaticText.RDFS_CLASS,
            StaticText.RDFS_RESOURCE, StaticText.OWL_CLASS, StaticText.OWL_THING,
            StaticText.OWL_NOTHING, StaticText.OWL_ONTOLOGY, StaticText.OWL_OBJECT_PROPERTY,
            StaticText.OWL_DATATYPE_PROPERTY);

    //  Ergebnis-Liste vorbereiten
    final List<OntIndividual> userClasses = new ArrayList<>();

    //  Über alle benannten Klassen iterieren (Iterator statt toList() spart Speicher)
    userClasses.addAll(
            modelToLookIn.individuals()
                    .filter(OntIndividual::isURIResource)
                    .filter(ind -> !CORE_CLASSES.contains(ind.getURI()))
                    .toList()
    );


    // Falls keine Prefix-Filter angegeben sind → alles zurückgeben
    if (extensionPrefix == null || extensionPrefix.length == 0) {
      return userClasses;
    }

    // Andernfalls: filtere nur die Klassen, deren URI mit einem der
    //    angegebenen Prefixe + "#" beginnt
    final List<String> prefixesWithHash = Arrays
        .stream(extensionPrefix)
        .map(p -> p.endsWith("#") ? p : p + "#")
        .toList();

    final List<OntIndividual> filteredIndividuals = new ArrayList<>();
    for (final OntIndividual cls : userClasses) {
      final String uri = cls.getURI();
      log.debug("▶ Prüfe Individual: {}", uri);

      boolean keep = false;
      for (final String prefix : prefixesWithHash) {
        log.debug("   - vergleiche mit Prefix „{}", prefix);
        if (uri.contains(prefix)) {
          log.debug("     ✔ match: {}", prefix);
          keep = true;
          break;
        } else {
          log.debug("     ✘ kein match");
        }
      }

      if (keep) {
        log.debug("   => behalte {}", uri);
        filteredIndividuals.add(cls);
      } else {
        log.debug("   => verwerfe {}", uri);
      }
    }
    log.debug("Ergebnis: {} Individual behalten.", filteredIndividuals.size());

    return filteredIndividuals;
  }

  @Override
  public List<OntIndividual.Named> getAllIndividuals(String ontologyUri, String... extensionPrefix) {
    final String ontologyString = this.db.getModelTTL(ontologyUri);
    final OntModel modelToLookIn = OntologySHACLConverter_Impl
        .retrieveOntModelFromText(ontologyString, StaticText.TURTLE);

    final String ontologyBaseString = this.db.getBaseModelTTL();
    final OntModel baseModel = OntologySHACLConverter_Impl
        .retrieveOntModelFromText(ontologyBaseString, StaticText.TURTLE);

    // Core-Vokabular, das wir überspringen möchten
    final Set<String> CORE_CLASSES = Set
        .of(StaticText.RDF_PROPERTY, StaticText.RDF_LIST, StaticText.RDFS_CLASS,
            StaticText.RDFS_RESOURCE, StaticText.OWL_CLASS, StaticText.OWL_THING,
            StaticText.OWL_NOTHING, StaticText.OWL_ONTOLOGY, StaticText.OWL_OBJECT_PROPERTY,
            StaticText.OWL_DATATYPE_PROPERTY

        );

    // Core-Vokabular, das wir überspringen möchten
    final Set<String> PREFIXES = Set
        .of(OWL.getURI(), RDF.getURI(), RDFS.getURI(), XSD.getURI(), baseModel.getNsPrefixURI(""));

    //  Ergebnis-Liste vorbereiten
    final List<OntIndividual.Named> individuals = new ArrayList<>();

    //  Über alle benannten Klassen iterieren (Iterator statt toList() spart Speicher)
    individuals.addAll(
            modelToLookIn.individuals()
                    .filter(OntIndividual::isURIResource)
                    .filter(ind -> !CORE_CLASSES.contains(ind.getURI()))
                    .map(ind -> ind.as(OntIndividual.Named.class))
                    .toList()
    );

    // Falls keine Prefix-Filter angegeben sind → alles zurückgeben
    if (extensionPrefix == null || extensionPrefix.length == 0) {
      return individuals;
    }

    // 5) Prefix-Normalisierung:
    //    Wir akzeptieren sowohl "…/ns" als auch "…/ns#" als Eingabe und matchen robust
    //    gegen URIs, die ggf. mit '#' oder '/' getrennt sind (Jena-Ontologien variieren hier).
    final String baseNameSpace = baseModel.getNsPrefixURI("");
    final List<String> extensionPrefixesWithNamespace = Arrays
        .stream(extensionPrefix)
        .map(p -> baseNameSpace + p)   // baseNameSpace davor setzen
        .toList();
    final List<String> normalizedPrefixes = this
        .normalizePrefixes(extensionPrefixesWithNamespace.toArray(new String[0]));

    // 6) Filtern: Behalte Individuals, deren EIGENE URI ODER eine der (Super-)Klassen-URIs
    //    mit einem der Prefixe beginnt.
    final List<OntIndividual.Named> result = new ArrayList<>();
    for (final OntIndividual.Named ind : individuals) {
      final String indUri = ind.getURI();
      log.debug("▶ Prüfe Individual: {}", indUri);

      // 6a) Schneller Check: Individual-URI direkt gegen Prefixe
      if (this.matchesAnyPrefix(indUri, normalizedPrefixes)) {
        log.debug("   ✔ Match über Individual-URI");
        result.add(ind);
        continue;
      }

      // 6b) Erweiterter Check: rdf:type + Superklassen
      //     (macht die "extension Zugehörigkeit" über Klassen-Prefix sichtbar)
      final Set<String> typeAndSuperUris = this.collectTypeAndSuperUris(ind, modelToLookIn);

      boolean keep = false;
      for (final String classUri : typeAndSuperUris) {
        if (this.matchesAnyPrefix(classUri, normalizedPrefixes)) {
          log.debug("   ✔ Match über Klassenhierarchie: {}", classUri);
          keep = true;
          break;
        }
      }

      if (keep) {
        result.add(ind);
      } else {
        log.debug("   ✘ Kein Match für {}", indUri);
      }
    }

    log.debug("Ergebnis: {} Individuals behalten.", result.size());
    return result;
  }

  @Override
  public OntIndividual getIndividual(String individualURI, String ontologyURI) {
    final String baseOntology_String = this.db.getModelTTL(ontologyURI);
    final OntModel modelToLookIn = OntologySHACLConverter_Impl
        .retrieveOntModelFromText(baseOntology_String, StaticText.TURTLE);
    //if (!individualURI.startsWith(ontologyURI)||!individualURI.startsWith("http")) {
    //  individualURI = modelToLookIn.expandPrefix(":" + individualURI);
    //}
    final OntIndividual individual = modelToLookIn.getIndividual(individualURI);
    return individual;
  }

  @Override
  public Property getPropertyByURI(String propertyURI, String ontologyURI) {
    String ttl = db.getModelTTL(ontologyURI);
    OntModel model = OntologySHACLConverter_Impl.retrieveOntModelFromText(ttl, StaticText.TURTLE);

    String expanded = model.expandPrefix(propertyURI);
    return model.getProperty(expanded);
  }

  @Override
  public String getOntologyIRI(Model model) {
    ResIterator it = model.listResourcesWithProperty(RDF.type, OWL2.Ontology);
    try {
      while (it.hasNext()) {
        Resource ont = it.nextResource();
        if (ont.isURIResource()) {
          return ont.getURI();
        }
      }
      return null;
    } finally {
      it.close();
    }
  }

  @Override
  public String getOntologyIRI(boolean fromBase, Model model) {
    throw new NotImplemented("Not yet implemented");
  }

  @Override
  public boolean isSkosConcept(String uri, String ontologyURI) {
    final String baseOntology_String = this.db.getModelTTL(ontologyURI);
    final OntModel modelToLookIn = OntologySHACLConverter_Impl
            .retrieveOntModelFromText(baseOntology_String, StaticText.TURTLE);
    Resource resource = modelToLookIn.getResource(uri);
    Resource skosConcept = modelToLookIn.getResource(StaticText.SKOS_CONCEPT_URI);
    boolean toReturn = modelToLookIn.contains(resource, RDF.type, skosConcept);

    return toReturn;
  }

  @Override
  public Resource getSkosConcept(String uri, String ontologyURI) {
    final String baseOntology_String = this.db.getModelTTL(ontologyURI);
    final OntModel modelToLookIn = OntologySHACLConverter_Impl
            .retrieveOntModelFromText(baseOntology_String, StaticText.TURTLE);

    Resource resource = modelToLookIn.getResource(uri);
    Resource skosConcept = modelToLookIn.getResource(StaticText.SKOS_CONCEPT_URI);

    if (!modelToLookIn.contains(resource, RDF.type, skosConcept)) {
      throw new IllegalArgumentException("SKOS Concept not found: " + uri);
    }

    return resource;
  }

  @Override
  public boolean isSelectionStatus(String uri, String ontologyURI) {
    final String baseOntology_String = this.db.getModelTTL(ontologyURI);
    final OntModel modelToLookIn = OntologySHACLConverter_Impl
            .retrieveOntModelFromText(baseOntology_String, StaticText.TURTLE);
    Resource resource = modelToLookIn.getResource(uri);
    Resource selectionStatus = modelToLookIn.getResource(StaticText.SELECTION_STATUS_URI);
    boolean toReturn = modelToLookIn.contains(resource, RDF.type, selectionStatus);

    return toReturn;
  }

  @Override
  public Resource getSelectionStatus(String uri, String ontologyURI) {
    final String baseOntology_String = this.db.getModelTTL(ontologyURI);
    final OntModel modelToLookIn = OntologySHACLConverter_Impl
            .retrieveOntModelFromText(baseOntology_String, StaticText.TURTLE);

    Resource resource = modelToLookIn.getResource(uri);
    Resource selectionStatus = modelToLookIn.getResource(StaticText.SELECTION_STATUS_URI);

    if (!modelToLookIn.contains(resource, RDF.type, selectionStatus)) {
      throw new IllegalArgumentException("SELECTION STATUS not found: " + uri);
    }

    return resource;
  }

  @Override
  public boolean isOntologyClass(String uri, String ontologyURI) {
    final String baseOntology_String = this.db.getModelTTL(ontologyURI);
    final OntModel modelToLookIn = OntologySHACLConverter_Impl
            .retrieveOntModelFromText(baseOntology_String, StaticText.TURTLE);

    Resource resource = modelToLookIn.getResource(uri);
    Resource owlClass = modelToLookIn.getResource(OWL_CLASS_URI);

    return modelToLookIn.contains(resource, RDF.type, owlClass);
  }

  /* ===================== Hilfsfunktionen ===================== */

  /**
   * Sammelt URIs der direkten rdf:type-Klassen eines Individuals sowie (transitiv) deren
   * Superklassen. Achtung: nutzt ein Set, um Zyklen zu vermeiden.
   */
  private Set<String> collectTypeAndSuperUris(OntIndividual ind, OntModel model) {
    final Set<String> uris = new LinkedHashSet<>();

    // direkte Typen (OntAPI liefert bereits OntClass)
    final java.util.List<OntClass> directTypes = ind.classes(true)
            .filter(Objects::nonNull)
            .toList();

    for (final OntClass type : directTypes) {
      this.addClassAndSupers(type, uris, new HashSet<>(), true);
    }

    return uris;
  }


  /**
   * Fügt die URI der Klasse und (transitiv) die URIs ihrer Superklassen hinzu.
   * 
   * @param directOnly true → nur direkte Superklassen (Jena-Reasoner berücksichtigt inferences),
   *                     false → nicht genutzt hier, aber leicht erweiterbar.
   */
  private void addClassAndSupers(OntClass cls, Set<String> acc, Set<OntClass> seen,
      boolean directOnly) {
    if (cls == null || cls.isAnon() || !seen.add(cls)) {
      return;
    }

    final String uri = cls.getURI();
    if (uri != null) {
      acc.add(uri);
    }

    // directOnly == true  -> nur direkte Superklassen
    // directOnly == false -> inkl. transitiver Superklassen
    cls.superClasses(directOnly)
            .filter(sup -> sup != null && !sup.isAnon())
            .forEach(sup -> {
              String supUri = sup.getURI();
              if (supUri != null) {
                acc.add(supUri);
              }
              // rekursiv weiter nach oben
              addClassAndSupers(sup, acc, seen, directOnly);
            });
  }

  /**
   * Normalisiert die gegebenen Prefixe: - entfernt überflüssige Leerzeichen - fügt Varianten mit
   * und ohne abschließendem '#' bzw. '/' hinzu (z. B. "…/dtm-data" → ["…/dtm-data", "…/dtm-data#",
   * "…/dtm-data/"])
   */
  private List<String> normalizePrefixes(String[] raw) {
    final Set<String> out = new LinkedHashSet<>();
    for (final String p : raw) {
      if (p == null) {
        continue;
      }
      String base = p.trim();
      if (base.isEmpty()) {
        continue;
      }

      // Entferne mehrfaches '#', '/', Whitespace am Ende
      while (base.endsWith(" ") || base.endsWith("#") || base.endsWith("/")) {
        base = base.substring(0, base.length() - 1);
      }

      // Füge robuste Varianten hinzu
      out.add(base);
      out.add(base + "#");
      out.add(base + "/");
    }
    return new ArrayList<>(out);
  }

  /**
   * "StartsWith"-basiertes Matching gegen mehrere, bereits normalisierte Prefixe. (bewusst
   * startsWith statt contains, um echte Namespace-Grenzen zu wahren)
   */
  private boolean matchesAnyPrefix(String uri, List<String> prefixes) {
    if (uri == null) {
      return false;
    }
    for (final String px : prefixes) {
      if (uri.contains(px)) {
        return true;
      }
    }
    return false;
  }
}
