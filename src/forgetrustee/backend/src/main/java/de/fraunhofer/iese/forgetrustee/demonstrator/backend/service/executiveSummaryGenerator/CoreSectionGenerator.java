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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.executiveSummaryGenerator;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModel;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelCore;

public class CoreSectionGenerator {

  public String generate(DataTrusteeModel model) {
    final DataTrusteeModelCore core = model.getCore();

    if (core == null) {
      return "";
    }

    final StringBuilder sb = new StringBuilder();

    sb.append("<h3>Akteure und Datenbasis</h3>\n");
    sb.append("<p>");

    NarrativeUtils.appendSentence(
            sb,
            "Das Datentreuhandmodell „"
                    + NarrativeUtils.safe(core.getDataTrusteeName(), "Datentreuhandmodell")
                    + "“ wird von "
                    + NarrativeUtils.safe(core.getDataTrusteeOperator(), "dem Betreiber")
                    + " betrieben"
    );

    if (core.getDataTrusteeOperatorAffiliation() != null) {
      NarrativeUtils.appendSentence(
              sb,
              "Der Betreiber ist als "
                      + NarrativeUtils.enumLabelDe(
                      core.getDataTrusteeOperatorAffiliation(),
                      "keine Angabe"
              )
                      + " eingeordnet"
      );
    }

    if (NarrativeUtils.isPresent(core.getDataTrusteeDescription())) {
      NarrativeUtils.appendSentence(
              sb,
              core.getDataTrusteeDescription()
      );
    }

    sb.append("</p>\n");

    sb.append("<p>");

    if (NarrativeUtils.isPresent(core.getRightsHolderName())) {
      NarrativeUtils.appendSentence(
              sb,
              NarrativeUtils.safe(core.getRightsHolderName(), "Die Rechteinhaber")
                      + " treten im Modell als Rechteinhaber auf"
      );
    }

    NarrativeUtils.appendSentence(
            sb,
            NarrativeUtils.booleanNarrative(
                    core.getRightsHolderIsRepresented(),
                    "Die Rechteinhaber werden innerhalb des Modells durch Dritte vertreten",
                    "Die Rechteinhaber werden innerhalb des Modells nicht durch Dritte vertreten",
                    "Zur Vertretung der Rechteinhaber liegt keine Angabe vor"
            )
    );

    if (NarrativeUtils.isPresent(core.getDataOwnerName())) {
      NarrativeUtils.appendSentence(
              sb,
              NarrativeUtils.safe(core.getDataOwnerName(), "Der Datenhalter")
                      + " fungiert innerhalb des Modells als Datenhalter"
      );
    }

    if (NarrativeUtils.isPresent(core.getDataConsumerName())) {
      NarrativeUtils.appendSentence(
              sb,
              NarrativeUtils.safe(core.getDataConsumerName(), "Der Datennutzer")
                      + " ist als Datennutzer eingebunden"
      );
    }

    if (core.getDataConsumerAffiliation() != null) {
      NarrativeUtils.appendSentence(
              sb,
              "Der Datennutzer ist als "
                      + NarrativeUtils.enumLabelDe(
                      core.getDataConsumerAffiliation(),
                      "keine Angabe"
              )
                      + " eingeordnet"
      );
    }

    sb.append("</p>\n");

    if (NarrativeUtils.isPresent(core.getDataCategoryName())
            || NarrativeUtils.isPresent(core.getDataCategoryDescription())) {

      sb.append("<p>");

      NarrativeUtils.appendSentence(
              sb,
              "Die Datenkategorie „"
                      + NarrativeUtils.safe(core.getDataCategoryName(), "Datenkategorie")
                      + "“ umfasst "
                      + NarrativeUtils.safe(core.getDataCategoryDescription(), "keine nähere Beschreibung")
      );

      NarrativeUtils.appendSentence(
              sb,
              NarrativeUtils.dataSensitivityNarrative(
                      core.getContainPersonalInformation(),
                      core.getSpecialPersonalInformation(),
                      core.getContainTradeSecrets()
              )
      );

      sb.append("</p>");
    }

    return sb.toString();
  }
}
