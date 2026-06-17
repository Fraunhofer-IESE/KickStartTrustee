
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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.apache_jena.translater;
/* Created by chwalek on 11.08.2025 */

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.apache_jena.NavigateModelImpl;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.apache_jena.navigation.OntologySHACLConverter_Impl;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.datamodel.ForgeClass;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.datamodel.ForgeIndividual;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.datamodel.ForgePredicate;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.datamodel.statements.ForgeIndividualStatement;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.datamodel.statements.ForgeLiteralStatement;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.datamodel.statements.ForgeObjectStatement;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper.ConfigurationStrings;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper.StaticText;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.DatabaseConnection;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.apache.jena.ontology.Individual;
import org.apache.jena.ontapi.model.OntIndividual;
//import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontapi.model.OntClass;
//import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontapi.model.OntModel;
//import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontapi.OntSpecification;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class OntologyTranslater_Impl implements OntologyTranslater {

  private final DatabaseConnection db;

  @Override
  public ForgeClass translateToForgeClass(OntClass cls) {
    final ForgeClass toReturn = new ForgeClass();

    final String ontClassUri = cls.getURI();
    toReturn.setUri(cls.getURI());

    final String modelString = this.db.getModelTTL(StaticText.KICKSTARTTRUSTEE_ONTOLOGY_NAMESPACE);
    final OntModel baseModel = OntologySHACLConverter_Impl
        .retrieveOntModelFromText(modelString, StaticText.TURTLE);

    final String label = NavigateModelImpl
        .getLiteralOfSubject(ontClassUri, baseModel.expandPrefix(StaticText.RDFS_LABEL),
            ConfigurationStrings.lang_setting, baseModel);

    toReturn.setLabelName(label);

    ExtendedIterator<Statement> statements = cls.listProperties();
    while(statements.hasNext()){
      Statement st = statements.next();
      if(st.getObject().isLiteral()){
        ForgeLiteralStatement newStatement = toForgeLiteralStatement(ontClassUri, st);
        toReturn.getLiteralStmnts().add(newStatement);
      }
      else if (st.getObject().isResource()){
        ForgeObjectStatement newStatement = toForgeObjectStatement(ontClassUri, st);
        toReturn.getObjectStmnts().add(newStatement);
      }
    }


    return toReturn;
  }

  private ForgeObjectStatement toForgeObjectStatement(String ontClassUri, Statement st) {
    ForgeObjectStatement newStatement = new ForgeObjectStatement();

    newStatement.setSubjectUri(ontClassUri);
    newStatement.setObjectUri(st.getObject().asResource().getURI());
    newStatement.setObjectLabel(st.getObject().asResource().getLocalName());
    ForgePredicate forgePredicate = new ForgePredicate();
    forgePredicate.setUri(st.getPredicate().getURI());
    forgePredicate.setShowName(st.getPredicate().getLocalName());
    newStatement.setPredicate(forgePredicate);

    return newStatement;
  }


  private static ForgeLiteralStatement toForgeLiteralStatement(String subjectUri, Statement literalStatement) {
    ForgeLiteralStatement newStatement = new ForgeLiteralStatement();

    newStatement.setLiteral(literalStatement.getLiteral().getString());
    newStatement.setLang(literalStatement.getLanguage());
    newStatement.setSubjectUri(subjectUri);
    ForgePredicate forgePredicate = new ForgePredicate();
    forgePredicate.setUri(literalStatement.getPredicate().getURI());
    forgePredicate.setShowName(literalStatement.getPredicate().getLocalName());
    newStatement.setPredicate(forgePredicate);

    return newStatement;
  }


  //===========================

  @Override
  public ForgeIndividual translateToForgeIndividual(OntIndividual.Named ind) {
    if (ind == null || ind.isAnon()) {
      throw new IllegalArgumentException("Individual ist null oder anonym (Blank Node).");
    }

    // OntModel aus dem Individual ableiten (fallback: wrap base Model)
    final OntModel model = this.extractOntModel(ind);

    final ForgeIndividual out = new ForgeIndividual();
    out.setUri(ind.getURI());
    out.setLabel(this.getBestLabel(ind, model));

    // 1) „spezifischste“ Klasse bestimmen und flach mappen
    final OntClass type = this.getMostSpecificClass(ind, model);
    if (type != null) {
      out.setInstanceOf(this.toForgeClassShallow(type, model));
    }

    // 2) Ausgehende Statements des Individuals mappen
    final StmtIterator sit = ind.listProperties();
    while (sit.hasNext()) {
      final Statement st = sit.nextStatement();
      final Property p = st.getPredicate();

      // rdf:type nicht als Statement exportieren
      if (RDF.type.equals(p)) {
        continue;
      }

      final ForgePredicate pred = this.toForgePredicate(p, model);
      final RDFNode obj = st.getObject();

      if (obj.isLiteral()) {
        final ForgeLiteralStatement ls = new ForgeLiteralStatement();
        ls.setSubjectUri(ind.getURI());
        ls.setPredicate(pred);
        ls.setLiteral(obj.asLiteral().getString());
        out.getLiteralStmnts().add(ls);

      } else if (obj.isResource()) {
        final Resource res = obj.asResource();
        if (res.isAnon()) {
          continue;
        }

        final ForgeIndividualStatement is = new ForgeIndividualStatement();
        is.setId(UUID.randomUUID().toString());
        is.setIndividualSubjectUri(ind.getURI());
        is.setProperty(pred);

        // Ziel-Individual „flach“ (URI+Label) einbetten, ohne Rekursion
        final ForgeIndividual target = new ForgeIndividual();
        target.setUri(res.getURI());
        target.setLabel(this.getBestLabel(res, model));
        is.setFTIndividual(target);

        out.getIndividualStmnts().add(is);
      }
    }

    return out;
  }

  /* ===================== Hilfen ===================== */

  private org.apache.jena.ontapi.model.OntModel extractOntModel(org.apache.jena.ontapi.model.OntIndividual ind) {
    org.apache.jena.ontapi.model.OntModel om = ind.getModel();
    if (om != null) return om;

    // Wenn das wirklich jemals passiert, hast du vermutlich ein Individual aus einem "plain Model"
    // Dann musst du an der Stelle, wo du das Individual erzeugst, schon ein OntModel benutzen.
    throw new IllegalStateException("ONTAPI Individual ohne OntModel – bitte Erzeugung/Loading auf OntModel umstellen.");
  }

  /** Flaches Mapping einer Klasse (nur URI + Label). */
  private ForgeClass toForgeClassShallow(OntClass cls, OntModel model) {
    final ForgeClass fc = new ForgeClass();
    // Falls keine Setter vorhanden: fc.uri = cls.getURI(); fc.labelName = getBestLabel(cls, model);
    try {
      fc.getClass().getMethod("setUri", String.class).invoke(fc, cls.getURI());
      fc
          .getClass()
          .getMethod("setLabelName", String.class)
          .invoke(fc, this.getBestLabel(cls, model)); // falls vorhanden
    } catch (final ReflectiveOperationException e) {
      // fallback auf direkten Zugriff, wenn im selben Paket (sonst bitte Setter hinzufügen)
      // fc.uri = cls.getURI();
      // fc.labelName = getBestLabel(cls, model);
    }
    return fc;
  }

  /** Prädikat (URI + Anzeigename). */
  private ForgePredicate toForgePredicate(Property p, OntModel model) {
    final ForgePredicate pred = new ForgePredicate();
    pred.setUri(p.getURI());
    pred.setShowName(this.getBestLabel(p, model));
    return pred;
  }

  /** „Bestes“ Label: rdfs:label (egal welche Sprache) -> localName -> URI -> Fallback. */
  private String getBestLabel(Resource r, OntModel model) {
    final Statement lab = r.getProperty(RDFS.label);
    if (lab != null && lab.getObject().isLiteral()) {
      final String s = lab.getString();
      if (s != null && !s.isBlank()) {
        return s;
      }
    }
    if (r.getURI() != null) {
      final String ln = r.getLocalName();
      if (ln != null && !ln.isBlank()) {
        return ln;
      }
      return r.getURI();
    }
    return "(unlabeled)";
  }

  /** Wählt eine „spezifischste“ Klasse aus den rdf:type-Werten. */
  private OntClass getMostSpecificClass(OntIndividual ind, OntModel model) {
    final List<OntClass.Named> types = ind
            .classes(true)
            .filter(c -> c.canAs(OntClass.Named.class))
            .map(c -> c.as(OntClass.Named.class))
            .toList();

    if (types.isEmpty()) {
      return null;
    }

    final List<OntClass> mostSpecific = new ArrayList<>(types);
    for (final OntClass c1 : types) {
      for (final OntClass c2 : types) {
        final boolean c1IsSubClassOfc2 = c1.hasSuperClass(c2, true);
        final boolean c2IsSubClassOfc1 = c2.hasSuperClass(c1, true);
        if (!Objects.equals(c1, c2) && c1.hasSuperClass(c2, true)) {
          // c2 ist allgemeiner → aus Kandidaten entfernen
          mostSpecific.remove(c2);
        }
      }
    }
    return mostSpecific.isEmpty() ? types.get(0) : mostSpecific.get(0);
  }
}
