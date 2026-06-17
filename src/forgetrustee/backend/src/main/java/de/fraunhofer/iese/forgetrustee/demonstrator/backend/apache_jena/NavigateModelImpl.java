
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

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.helper.StaticText;

//import org.apache.jena.ontology.Individual;
import org.apache.jena.ontapi.model.OntIndividual;
//import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontapi.model.OntClass;
//import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontapi.model.OntModel;
//import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontapi.model.OntProperty;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;


public abstract class NavigateModelImpl {

  public static String getLiteralOfSubject(String subjectUri, String propertyUri, String lang,
      OntModel model) {

    // 1) Alle Statements für subject–property holen
    final StmtIterator iterator = getPropertyStatementsOfSubject(subjectUri, propertyUri, model);

    // 2) Sprache prüfen: leer oder null → kein Filter
    final boolean filterLang = lang != null && !lang.isBlank();

    // 3) Iterator durchlaufen
    while (iterator.hasNext()) {
      final Statement stmt = iterator.next();
      final RDFNode obj = stmt.getObject();

      // nur Literale interessieren uns
      if (!obj.isLiteral()) {
        continue;
      }

      final Literal lit = obj.asLiteral();

      // wenn Sprach-Filter aktiv, nur passende Literale zulassen
      if (filterLang) {
        if (!lang.equals(lit.getLanguage())) {
          continue;
        }
      }
      // gefunden!→ sofort zurückgeben
      return lit.getString();
    }

    // 4) Nichts gefunden
    return "No Literal found";
  }

  public static StmtIterator getPropertyStatementsOfSubject(String subjectUri, String propertyUri,
      OntModel model) {
    Resource subject = model.getResource(subjectUri);
    Property property = model.getProperty(propertyUri);

    return model.listStatements(subject, property, (RDFNode) null);
  }

  public static Property getOntPropertyFromModel(String uri, OntModel model) {
    return model.getProperty(uri);
  }

}
