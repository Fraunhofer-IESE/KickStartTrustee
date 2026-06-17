
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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.apache_jena;
/* Created by chwalek on 02.07.2025 */

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.datamodel.ForgeClass;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.datamodel.ForgeIndividual;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.datamodel.ForgePredicate;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.datamodel.statements.ForgeOntStatement;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper.ConfigurationStrings;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper.StaticText;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
//import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontapi.model.OntModel;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import java.util.ArrayList;

public class JenaTranslater {

  public static ForgeClass toForgeClass(OntClass ontClass, OntModel model) {
    ForgeClass toReturn = new ForgeClass();

    String ontClassUri = ontClass.getURI();
    toReturn.setUri(ontClass.getURI());

    String label = NavigateModelImpl
        .getLiteralOfSubject(ontClassUri, model.expandPrefix(StaticText.RDFS_LABEL),
            ConfigurationStrings.lang_setting, model);
    toReturn.setLabelName(label);



    return toReturn;
  }



  public static ForgePredicate toForgePredicate(Property property) {
    ForgePredicate toReturn = new ForgePredicate();
    toReturn.setShowName(property.getLocalName());
    toReturn.setUri(property.getURI());
    return toReturn;
  }

  public static ForgeIndividual toForgeIndividual(Individual individual) {
    ForgeIndividual toReturn = new ForgeIndividual();
    toReturn.setUri(individual.getURI());
    toReturn.setLabel(individual.getLabel("de"));
    return toReturn;
  }

  public static ArrayList<ForgeOntStatement> toStatementList(OntClass ontClass, OntModel model) {
    ArrayList<ForgeOntStatement> toReturn = new ArrayList<>();
    StmtIterator stmtIterator = model.listStatements();
    while (stmtIterator.hasNext()) {
      Statement statement = stmtIterator.nextStatement();
      //ForgeOntStatement forgeOntStatement = JenaTranslater.toStatement(statement, model);
      //toReturn.add(forgeOntStatement);
    }
    return toReturn;
  }

  /**private static ForgeOntStatement toStatement(Statement statement, OntModel model) {
    ForgeOntStatement toReturn = new ForgeOntStatement();
    Resource subject = statement.getSubject();
    Property predicate = statement.getPredicate();

    ForgeClass subjectOnto = JenaTranslater.toForgeClass((OntClass) subject, model);
    ForgePredicate predicateOnto = JenaTranslater.toForgePredicate(predicate);

    if (statement.getLiteral() != null) {
      toReturn = new ForgeLiteralStatement();
      toReturn.setLiteralStatement(true);
      toReturn.setLiteral(statement.getLiteral().getString());
    } else if (statement.getObject() != null) {
      toReturn = new ForgeObjectStatement();
      toReturn.setObjectStatement(true);
      Resource object = (Resource) statement.getObject();
      ForgeClass objectOnto = JenaTranslater.toForgeClass((OntClass) object, model);
      toReturn.setObject(objectOnto);
    }
    toReturn.setSubject(subjectOnto);
    toReturn.setPredicate(predicateOnto);

    return toReturn;
  }**/
}
