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
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelBusiness;

public class BusinessSectionGenerator {

  public String generate(DataTrusteeModel model) {

    // -------------------------------------------------
    // Zugriff auf Business-Bereich
    // -------------------------------------------------

    final DataTrusteeModelBusiness business =
            model.getBusiness();

    // -------------------------------------------------
    // Kein Business-Modell vorhanden
    // -------------------------------------------------

    if (business == null) {
      return "";
    }

    final StringBuilder sb =
            new StringBuilder();

    // -------------------------------------------------
    // Abschnittsüberschrift
    // -------------------------------------------------

    sb.append("<h3><b>Geschäftsmodell und Finanzierung</b></h3>\n");

    sb.append("<p>");

    // -------------------------------------------------
    // Domänen und Rechts-/Organisationsform
    // -------------------------------------------------

    appendBusinessContextNarrative(
            sb,
            business
    );

    // -------------------------------------------------
    // Finanzierung und Förderquellen
    // -------------------------------------------------

    appendFundingNarrative(
            sb,
            business
    );

    // -------------------------------------------------
    // Zahlungs- und Vergütungslogik
    // -------------------------------------------------

    appendPaymentNarrative(
            sb,
            business
    );

    sb.append("</p>\n");

    return sb.toString();
  }

  /**
   * Beschreibt die fachliche Domäne und die
   * organisatorisch-rechtliche Ausgestaltung des Modells.
   */
  private void appendBusinessContextNarrative(
          StringBuilder sb,
          DataTrusteeModelBusiness business
  ) {

    final boolean hasDomains =
            NarrativeUtils.hasRelevantValues(
                    business.getBusinessDomains()
            );

    final boolean hasBusinessModel =
            NarrativeUtils.isRelevantEnum(
                    business.getBusinessModel()
            );

    if (!hasDomains && !hasBusinessModel) {
      return;
    }

    if (hasDomains && hasBusinessModel) {

      NarrativeUtils.appendSentence(
              sb,
              "Das Modell ist in den Anwendungsdomänen "
                      + NarrativeUtils.enumSetLabelsDeOrEmpty(
                      business.getBusinessDomains(),
                      ", "
              )
                      + " verortet und organisatorisch als "
                      + NarrativeUtils.enumLabelDe(
                      business.getBusinessModel(),
                      ""
              )
                      + " ausgestaltet"
      );

      return;
    }

    if (hasDomains) {

      NarrativeUtils.appendSentence(
              sb,
              "Das Modell ist in den Anwendungsdomänen "
                      + NarrativeUtils.enumSetLabelsDeOrEmpty(
                      business.getBusinessDomains(),
                      ", "
              )
                      + " verortet"
      );
    }

    if (hasBusinessModel) {

      NarrativeUtils.appendSentence(
              sb,
              "Die organisatorisch-rechtliche Ausgestaltung erfolgt als "
                      + NarrativeUtils.enumLabelDe(
                      business.getBusinessModel(),
                      ""
              )
      );
    }
  }

  /**
   * Beschreibt die Finanzierung des Modells.
   *
   * FundingSources werden nur ausgegeben,
   * wenn relevante Werte vorhanden sind.
   */
  private void appendFundingNarrative(
          StringBuilder sb,
          DataTrusteeModelBusiness business
  ) {

    final boolean hasFundingSources =
            NarrativeUtils.hasRelevantValues(
                    business.getFundingSources()
            );

    if (!hasFundingSources) {
      return;
    }

    NarrativeUtils.appendSentence(
            sb,
            "Die Finanzierung des Modells wird durch "
                    + NarrativeUtils.enumSetLabelsDeOrEmpty(
                    business.getFundingSources(),
                    ", "
            )
                    + " unterstützt"
    );
  }

  /**
   * Beschreibt die Zahlungs- und Vergütungslogik für
   * Datengeber und Datennutzer.
   */
  private void appendPaymentNarrative(
          StringBuilder sb,
          DataTrusteeModelBusiness business
  ) {

    final boolean hasDataOwnerPayment =
            NarrativeUtils.isRelevantEnum(
                    business.getPaymentMethodDataOwner()
            );

    final boolean hasDataConsumerPayment =
            NarrativeUtils.isRelevantEnum(
                    business.getPaymentMethodDataConsumer()
            );

    if (!hasDataOwnerPayment && !hasDataConsumerPayment) {
      return;
    }

    if (hasDataOwnerPayment && hasDataConsumerPayment) {

      NarrativeUtils.appendSentence(
              sb,
              "Für Datengeber ist "
                      + NarrativeUtils.enumLabelDe(
                      business.getPaymentMethodDataOwner(),
                      ""
              )
                      + " vorgesehen, während für Datennutzer "
                      + NarrativeUtils.enumLabelDe(
                      business.getPaymentMethodDataConsumer(),
                      ""
              )
                      + " gilt"
      );

      return;
    }

    if (hasDataOwnerPayment) {

      NarrativeUtils.appendSentence(
              sb,
              "Für Datengeber ist "
                      + NarrativeUtils.enumLabelDe(
                      business.getPaymentMethodDataOwner(),
                      ""
              )
                      + " vorgesehen"
      );
    }

    if (hasDataConsumerPayment) {

      NarrativeUtils.appendSentence(
              sb,
              "Für Datennutzer gilt "
                      + NarrativeUtils.enumLabelDe(
                      business.getPaymentMethodDataConsumer(),
                      ""
              )
      );
    }
  }
}
