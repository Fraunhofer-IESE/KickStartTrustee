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
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelLegal;

public class LegalSectionGenerator {

  public String generate(DataTrusteeModel model) {

    // -------------------------------------------------
    // Zugriff auf Core- und Legal-Bereich
    // -------------------------------------------------

    final DataTrusteeModelCore core =
            model.getCore();

    final DataTrusteeModelLegal legal =
            model.getLegal();

    // -------------------------------------------------
    // Kein Legal-Modell vorhanden
    // -------------------------------------------------

    if (legal == null) {
      return "";
    }

    final StringBuilder sb =
            new StringBuilder();

    // -------------------------------------------------
    // Abschnittsüberschrift
    // -------------------------------------------------

    sb.append("<h3>Rechtliche Grundlage der Datenverarbeitung</h3>\n");

    sb.append("<p>");

    // -------------------------------------------------
    // Rechtsgrundlagen der Verarbeitung
    // -------------------------------------------------

    final String processingBases =
            NarrativeUtils.processingBasesToString(
                    legal.getProcessingBases(),
                    ", "
            );

    /*
     * Nur ausgeben, wenn tatsächlich relevante
     * Rechtsgrundlagen vorhanden sind.
     */
    if (NarrativeUtils.isPresent(processingBases)
            && !"keine Angabe".equals(processingBases)) {

      NarrativeUtils.appendSentence(
              sb,
              "Die Verarbeitung der Daten stützt sich auf "
                      + processingBases
      );
    }

    // -------------------------------------------------
    // Konkretisierung der Rechtsgrundlagen
    // -------------------------------------------------

    final String details =
            NarrativeUtils.processingBasisDetailsToString(
                    legal.getProcessingBases()
            );

    /*
     * Zusätzliche fachliche Beschreibungen
     * der jeweiligen Rechtsgrundlagen.
     */
    if (NarrativeUtils.isPresent(details)) {

      NarrativeUtils.appendSentence(
              sb,
              "Die hinterlegten Konkretisierungen beschreiben "
                      + details
      );
    }

    // -------------------------------------------------
    // Art der Einwilligung
    // -------------------------------------------------

    if (legal.getConsentType() != null
            && NarrativeUtils.isRelevantEnum(
            legal.getConsentType()
    )) {

      NarrativeUtils.appendSentence(
              sb,
              "Die Einwilligung ist als "
                      + NarrativeUtils.enumLabelDe(
                      legal.getConsentType(),
                      "Einwilligung"
              )
                      + " organisiert"
      );
    }

    // -------------------------------------------------
    // Prozess der Einholung und Erfassung
    // der Einwilligung
    // -------------------------------------------------

    appendConsentProcessNarrative(
            sb,
            legal,
            core
    );

    sb.append("</p>\n");

    return sb.toString();
  }

  /**
   * Erzeugt eine natürlich formulierte Beschreibung
   * des Einwilligungsprozesses.
   *
   * Beispiele:
   *
   * "Die Einholung der Einwilligung erfolgt über
   * ein Patientenportal, während die Erfassung
   * durch die Rechteinhaber vorgenommen wird."
   */
  private void appendConsentProcessNarrative(
          StringBuilder sb,
          DataTrusteeModelLegal legal,
          DataTrusteeModelCore core
  ) {

    // -------------------------------------------------
    // Prüfen, ob Angaben vorhanden sind
    // -------------------------------------------------

    final boolean hasObtainingConsentBy =
            NarrativeUtils.isPresent(
                    legal.getObtainingConsentBy()
            );

    final boolean hasConsentEnteredBy =
            legal.getConsentEnteredBy() != null
                    && NarrativeUtils.isRelevantEnum(
                    legal.getConsentEnteredBy()
            );

    /*
     * Keine Angaben vorhanden
     */
    if (!hasObtainingConsentBy
            && !hasConsentEnteredBy) {

      return;
    }

    // -------------------------------------------------
    // Beide Angaben vorhanden:
    // gemeinsame narrative Formulierung
    // -------------------------------------------------

    if (hasObtainingConsentBy
            && hasConsentEnteredBy) {

      NarrativeUtils.appendSentence(
              sb,
              "Die Einholung der Einwilligung erfolgt über "
                      + legal.getObtainingConsentBy()
                      + ", während die Erfassung durch "
                      + NarrativeUtils.safe(
                      NarrativeUtils.mapRoleToLabel(
                              legal.getConsentEnteredBy(),
                              core
                      ),
                      "keine Angabe"
              )
                      + " vorgenommen wird"
      );

      return;
    }

    // -------------------------------------------------
    // Nur Einholung vorhanden
    // -------------------------------------------------

    if (hasObtainingConsentBy) {

      NarrativeUtils.appendSentence(
              sb,
              "Die Einholung der Einwilligung erfolgt über "
                      + legal.getObtainingConsentBy()
      );
    }

    // -------------------------------------------------
    // Nur erfassende Rolle vorhanden
    // -------------------------------------------------

    if (hasConsentEnteredBy) {

      NarrativeUtils.appendSentence(
              sb,
              "Die Erfassung der Einwilligung wird durch "
                      + NarrativeUtils.safe(
                      NarrativeUtils.mapRoleToLabel(
                              legal.getConsentEnteredBy(),
                              core
                      ),
                      "keine Angabe"
              )
                      + " vorgenommen"
      );
    }
  }
}
