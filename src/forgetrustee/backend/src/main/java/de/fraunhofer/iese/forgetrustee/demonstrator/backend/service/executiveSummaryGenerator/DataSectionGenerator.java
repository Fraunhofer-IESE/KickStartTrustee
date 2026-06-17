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
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelData;

public class DataSectionGenerator {

    public String generate(DataTrusteeModel model) {

        // -------------------------------------------------
        // Zugriff auf technischen Datenbereich
        // -------------------------------------------------

        final DataTrusteeModelData data =
                model.getData();

        // -------------------------------------------------
        // Kein Daten-/Implementierungsmodell vorhanden
        // -------------------------------------------------

        if (data == null) {
            return "";
        }

        final StringBuilder sb =
                new StringBuilder();

        // -------------------------------------------------
        // Abschnittsüberschrift
        // -------------------------------------------------

        sb.append("<h3>Technische Ausgestaltung und Datenlebenszyklus</h3>\n");

        sb.append("<p>");

        // -------------------------------------------------
        // Grundlegende technische Einordnung
        // -------------------------------------------------

        if (NarrativeUtils.isRelevantEnum(data.getDataTrusteeCategory())
                || NarrativeUtils.isRelevantEnum(data.getArchitectureType())) {

            NarrativeUtils.appendSentence(
                    sb,
                    buildArchitectureNarrative(data)
            );
        }

        // -------------------------------------------------
        // Datenempfang
        // -------------------------------------------------

        appendReceptionNarrative(sb, data);

        // -------------------------------------------------
        // Datenaufbereitung
        // -------------------------------------------------

        appendPreparationNarrative(sb, data);

        // -------------------------------------------------
        // Datenspeicherung
        // -------------------------------------------------

        appendStorageNarrative(sb, data);

        // -------------------------------------------------
        // Datenauswertung
        // -------------------------------------------------

        appendAnalysisNarrative(sb, data);

        // -------------------------------------------------
        // Datenweitergabe
        // -------------------------------------------------

        appendForwardingNarrative(sb, data);

        sb.append("</p>\n");

        return sb.toString();
    }

    /**
     * Beschreibt die grundlegende technische Einordnung
     * des Datentreuhandmodells, also Kategorie und Architektur.
     */
    private String buildArchitectureNarrative(
            DataTrusteeModelData data
    ) {

        final String category =
                NarrativeUtils.enumLabelDe(
                        data.getDataTrusteeCategory(),
                        ""
                );

        final String architecture =
                NarrativeUtils.enumLabelDe(
                        data.getArchitectureType(),
                        ""
                );

        if (NarrativeUtils.isPresent(category)
                && NarrativeUtils.isPresent(architecture)) {

            return "Technisch ist das Modell als "
                    + category
                    + " mit der Architektur "
                    + architecture
                    + " umgesetzt";
        }

        if (NarrativeUtils.isPresent(category)) {

            return "Technisch ist das Modell als "
                    + category
                    + " umgesetzt";
        }

        if (NarrativeUtils.isPresent(architecture)) {

            return "Technisch nutzt das Modell die Architektur "
                    + architecture;
        }

        return "";
    }

    /**
     * Beschreibt den Empfang der Daten.
     *
     * Dabei werden Quelle, Empfangstechnologien,
     * Empfangsfrequenz, Sicherheitsmaßnahmen und Custom-Eingaben
     * zu einem flüssigen Absatz verbunden.
     */
    private void appendReceptionNarrative(
            StringBuilder sb,
            DataTrusteeModelData data
    ) {

        final boolean hasSource =
                NarrativeUtils.isPresent(
                        data.getSourceSystem()
                );

        final boolean hasTechnologies =
                NarrativeUtils.hasRelevantValues(
                        data.getReceptionTechnologies()
                );

        final boolean hasFrequency =
                NarrativeUtils.isRelevantEnum(
                        data.getReceptionFrequency()
                );

        final String customSecurity =
                NarrativeUtils.titleDescriptionListToString(
                        data.getCustomReceptionSecurityTechniques()
                );

        final boolean hasSecurity =
                NarrativeUtils.hasRelevantValues(
                        data.getReceptionSecurityMeasures()
                );

        if (!hasSource
                && !hasTechnologies
                && !hasFrequency
                && !hasSecurity
                && NarrativeUtils.isBlank(customSecurity)) {

            return;
        }

        final StringBuilder sentence =
                new StringBuilder();

        sentence.append("Der Datenempfang");

        if (hasSource) {
            sentence
                    .append(" erfolgt aus ")
                    .append(data.getSourceSystem());
        } else {
            sentence.append(" ist im Modell vorgesehen");
        }

        if (hasFrequency) {
            sentence
                    .append(" ")
                    .append(
                            NarrativeUtils.enumLabelDe(
                                    data.getReceptionFrequency(),
                                    ""
                            ).toLowerCase()
                    );
        }

        if (hasTechnologies) {
            sentence
                    .append(" über ")
                    .append(
                            NarrativeUtils.enumSetLabelsDeOrEmpty(
                                    data.getReceptionTechnologies(),
                                    ", "
                            )
                    );
        }

        NarrativeUtils.appendSentence(
                sb,
                sentence.toString()
        );

        if (hasSecurity) {
            NarrativeUtils.appendSentence(
                    sb,
                    "Zur Absicherung des Empfangs werden "
                            + NarrativeUtils.enumSetLabelsDeOrEmpty(
                            data.getReceptionSecurityMeasures(),
                            ", "
                    )
                            + " eingesetzt"
            );
        }

        if (NarrativeUtils.isPresent(customSecurity)) {
            NarrativeUtils.appendSentence(
                    sb,
                    "Ergänzend ist für den Datenempfang "
                            + customSecurity
                            + " hinterlegt"
            );
        }
    }

    /**
     * Beschreibt die Aufbereitung der Daten.
     *
     * Custom-Eingaben werden nur ausgegeben,
     * wenn sie tatsächlich im Modell vorhanden sind.
     */
    private void appendPreparationNarrative(
            StringBuilder sb,
            DataTrusteeModelData data
    ) {

        final boolean hasPreparation =
                NarrativeUtils.hasRelevantValues(
                        data.getPreparationTechniques()
                );

        final String customPreparation =
                NarrativeUtils.titleDescriptionListToString(
                        data.getCustomPreparationTechniques()
                );

        if (!hasPreparation
                && NarrativeUtils.isBlank(customPreparation)) {

            return;
        }

        if (hasPreparation) {
            NarrativeUtils.appendSentence(
                    sb,
                    "Für die Datenaufbereitung werden "
                            + NarrativeUtils.enumSetLabelsDeOrEmpty(
                            data.getPreparationTechniques(),
                            ", "
                    )
                            + " eingesetzt"
            );
        }

        if (NarrativeUtils.isPresent(customPreparation)) {
            NarrativeUtils.appendSentence(
                    sb,
                    "Zusätzlich ist für die Aufbereitung "
                            + customPreparation
                            + " beschrieben"
            );
        }
    }

    /**
     * Beschreibt die Speicherung der Daten.
     */
    private void appendStorageNarrative(
            StringBuilder sb,
            DataTrusteeModelData data
    ) {

        final boolean hasStorageTechnique =
                NarrativeUtils.isRelevantEnum(
                        data.getStorageTechnique()
                );

        final boolean hasRetention =
                NarrativeUtils.isRelevantEnum(
                        data.getStorageRetention()
                );

        if (!hasStorageTechnique && !hasRetention) {
            return;
        }

        if (hasStorageTechnique && hasRetention) {
            NarrativeUtils.appendSentence(
                    sb,
                    "Die Speicherung erfolgt über "
                            + NarrativeUtils.enumLabelDe(
                            data.getStorageTechnique(),
                            ""
                    )
                            + " mit der Aufbewahrungsstrategie "
                            + NarrativeUtils.enumLabelDe(
                            data.getStorageRetention(),
                            ""
                    )
            );

            return;
        }

        if (hasStorageTechnique) {
            NarrativeUtils.appendSentence(
                    sb,
                    "Die Speicherung erfolgt über "
                            + NarrativeUtils.enumLabelDe(
                            data.getStorageTechnique(),
                            ""
                    )
            );
        }

        if (hasRetention) {
            NarrativeUtils.appendSentence(
                    sb,
                    "Für die Aufbewahrung ist "
                            + NarrativeUtils.enumLabelDe(
                            data.getStorageRetention(),
                            ""
                    )
                            + " vorgesehen"
            );
        }
    }

    /**
     * Beschreibt die Analyse der Daten.
     */
    private void appendAnalysisNarrative(
            StringBuilder sb,
            DataTrusteeModelData data
    ) {

        final boolean hasAnalysis =
                NarrativeUtils.hasRelevantValues(
                        data.getAnalysisTechniques()
                );

        final String customAnalysis =
                NarrativeUtils.titleDescriptionListToString(
                        data.getCustomAnalysisTechniques()
                );

        if (!hasAnalysis
                && NarrativeUtils.isBlank(customAnalysis)) {

            return;
        }

        if (hasAnalysis) {
            NarrativeUtils.appendSentence(
                    sb,
                    "Für die Datenauswertung werden "
                            + NarrativeUtils.enumSetLabelsDeOrEmpty(
                            data.getAnalysisTechniques(),
                            ", "
                    )
                            + " genutzt"
            );
        }

        if (NarrativeUtils.isPresent(customAnalysis)) {
            NarrativeUtils.appendSentence(
                    sb,
                    "Ergänzend ist für die Analyse "
                            + customAnalysis
                            + " angegeben"
            );
        }
    }

    /**
     * Beschreibt die Weitergabe der Daten.
     *
     * Zielsystem, Weitergabetechnik, Frequenz,
     * Sicherheitsmaßnahmen und Custom-Eingaben werden
     * konditional zu natürlich lesbaren Sätzen verbunden.
     */
    private void appendForwardingNarrative(
            StringBuilder sb,
            DataTrusteeModelData data
    ) {

        final boolean hasTarget =
                NarrativeUtils.isPresent(
                        data.getTargetSystem()
                );

        final boolean hasTechniques =
                NarrativeUtils.hasRelevantValues(
                        data.getForwardingTechniques()
                );

        final boolean hasFrequency =
                NarrativeUtils.isRelevantEnum(
                        data.getForwardingFrequency()
                );

        final boolean hasSecurity =
                NarrativeUtils.hasRelevantValues(
                        data.getForwardingSecurityMeasures()
                );

        final String customSecurity =
                NarrativeUtils.titleDescriptionListToString(
                        data.getCustomForwardingSecurityTechniques()
                );

        if (!hasTarget
                && !hasTechniques
                && !hasFrequency
                && !hasSecurity
                && NarrativeUtils.isBlank(customSecurity)) {

            return;
        }

        final StringBuilder sentence =
                new StringBuilder();

        sentence.append("Die Datenweitergabe");

        if (hasTarget) {
            sentence
                    .append(" erfolgt an ")
                    .append(data.getTargetSystem());
        } else {
            sentence.append(" ist im Modell vorgesehen");
        }

        if (hasFrequency) {
            sentence
                    .append(" ")
                    .append(
                            NarrativeUtils.enumLabelDe(
                                    data.getForwardingFrequency(),
                                    ""
                            ).toLowerCase()
                    );
        }

        if (hasTechniques) {
            sentence
                    .append(" über ")
                    .append(
                            NarrativeUtils.enumSetLabelsDeOrEmpty(
                                    data.getForwardingTechniques(),
                                    ", "
                            )
                    );
        }

        NarrativeUtils.appendSentence(
                sb,
                sentence.toString()
        );

        if (hasSecurity) {
            NarrativeUtils.appendSentence(
                    sb,
                    "Zur Absicherung der Weitergabe werden "
                            + NarrativeUtils.enumSetLabelsDeOrEmpty(
                            data.getForwardingSecurityMeasures(),
                            ", "
                    )
                            + " eingesetzt"
            );
        }

        if (NarrativeUtils.isPresent(customSecurity)) {
            NarrativeUtils.appendSentence(
                    sb,
                    "Ergänzend ist für die Weitergabe "
                            + customSecurity
                            + " hinterlegt"
            );
        }
    }
}
