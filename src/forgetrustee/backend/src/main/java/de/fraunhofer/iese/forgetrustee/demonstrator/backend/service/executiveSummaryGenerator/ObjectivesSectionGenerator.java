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
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelObjectives;

public class ObjectivesSectionGenerator {

    public String generate(DataTrusteeModel model) {

        // -------------------------------------------------
        // Zugriff auf Ziel-/Motivationsbereich
        // -------------------------------------------------

        final DataTrusteeModelObjectives objectives =
                model.getObjectives();

        // -------------------------------------------------
        // Kein Objectives-Modell vorhanden
        // -------------------------------------------------

        if (objectives == null) {
            return "";
        }

        final StringBuilder sb =
                new StringBuilder();

        // -------------------------------------------------
        // Abschnittsüberschrift
        // -------------------------------------------------

        sb.append("<h3>Ziele und Mehrwert</h3>\n");

        sb.append("<p>");

        // -------------------------------------------------
        // Ziele des Datentreuhandmodells
        // -------------------------------------------------

        appendGoalsNarrative(
                sb,
                objectives
        );

        // -------------------------------------------------
        // Motivation der Rechteinhaber
        // -------------------------------------------------

        appendRightsHolderMotivationNarrative(
                sb,
                objectives
        );

        // -------------------------------------------------
        // Motivation der Datengeber
        // -------------------------------------------------

        appendDataHolderMotivationNarrative(
                sb,
                objectives
        );

        // -------------------------------------------------
        // Motivation der Datennutzer
        // -------------------------------------------------

        appendDataConsumerMotivationNarrative(
                sb,
                objectives
        );

        // -------------------------------------------------
        // Abschließende Einordnung
        // -------------------------------------------------

        appendConclusionNarrative(
                sb,
                model
        );

        sb.append("</p>\n");

        return sb.toString();
    }

    /**
     * Beschreibt die zentralen Ziele
     * des Datentreuhandmodells.
     */
    private void appendGoalsNarrative(
            StringBuilder sb,
            DataTrusteeModelObjectives objectives
    ) {

        if (!NarrativeUtils.hasRelevantValues(
                objectives.getDataTrusteeGoals()
        )) {

            return;
        }

        NarrativeUtils.appendSentence(
                sb,
                "Das Datentreuhandmodell verfolgt die Ziele "
                        + italicize(NarrativeUtils.enumSetLabelsDeOrEmpty(
                        objectives.getDataTrusteeGoals(),
                        ", "
                ))
        );
    }

    /**
     * Beschreibt die Motivation der Rechteinhaber.
     */
    private void appendRightsHolderMotivationNarrative(
            StringBuilder sb,
            DataTrusteeModelObjectives objectives
    ) {

        if (!NarrativeUtils.hasRelevantValues(
                objectives.getMotivationRightsHolder()
        )) {

            return;
        }

        NarrativeUtils.appendSentence(
                sb,
                "Aus Sicht der Rechteinhaber stehen "
                        + italicize(NarrativeUtils.enumSetLabelsDeOrEmpty(
                        objectives.getMotivationRightsHolder(),
                        ", "
                )
                        + " im Vordergrund"
        ));
    }

    /**
     * Beschreibt die Motivation der Datengeber.
     */
    private void appendDataHolderMotivationNarrative(
            StringBuilder sb,
            DataTrusteeModelObjectives objectives
    ) {

        if (!NarrativeUtils.hasRelevantValues(
                objectives.getMotivationDataHolder()
        )) {

            return;
        }

        NarrativeUtils.appendSentence(
                sb,
                "Für die Datengeber sind "
                        + italicize(NarrativeUtils.enumSetLabelsDeOrEmpty(
                        objectives.getMotivationDataHolder(),
                        ", "
                )
                        + " relevant"
        ));
    }

    /**
     * Beschreibt die Motivation der Datennutzer.
     */
    private void appendDataConsumerMotivationNarrative(
            StringBuilder sb,
            DataTrusteeModelObjectives objectives
    ) {

        if (!NarrativeUtils.hasRelevantValues(
                objectives.getMotivationDataConsumer()
        )) {

            return;
        }

        NarrativeUtils.appendSentence(
                sb,
                "Die Datennutzer verfolgen Ziele wie "
                        + italicize(NarrativeUtils.enumSetLabelsDeOrEmpty(
                        objectives.getMotivationDataConsumer(),
                        ", "
                )
        ));
    }

    /**
     * Ergänzt eine abschließende,
     * leicht narrative Einordnung des Modells.
     *
     * Es werden ausschließlich Informationen verwendet,
     * die bereits implizit im Modell enthalten sind.
     */
    private void appendConclusionNarrative(
            StringBuilder sb,
            DataTrusteeModel model
    ) {

        final String trusteeName =
                model.getCore() != null
                        ? model.getCore().getDataTrusteeName()
                        : null;

        if (!NarrativeUtils.isPresent(trusteeName)) {

            NarrativeUtils.appendSentence(
                    sb,
                    "Insgesamt unterstützt das Modell eine strukturierte und vertrauenswürdige Nutzung von Daten"
            );

            return;
        }

        NarrativeUtils.appendSentence(
                sb,
                "Insgesamt unterstützt das Modell „"
                        + trusteeName
                        + "“ eine strukturierte und vertrauenswürdige Nutzung von Daten"
        );
    }

    private String italicize(
            String value
    ) {

        if (value == null || value.isBlank()) {
            return "";
        }

        return "<i>" + value + "</i>";
    }
}
