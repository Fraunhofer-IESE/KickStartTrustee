
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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.data;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.LabeledConcept;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AnalysisTechnique implements LabeledConcept {
  PET("https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#PrivacyEnhancingTechnology",
      "Privacy Enhancing Technologies", "Privacy Enhancing Technologies"),
  STATISTICAL_ANALYSIS(
      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#StatisticalAnalysis",
      "Statistical Analysis", "Statistische Analyse"),
  AI_ANALYSIS("https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#AIPoweredAnalysis",
      "AI powered analysis", "KI-gestützte Auswertung"),
  NONE("https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#None", "None", "Keine"),
  AMBIGUOUS("https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#Ambiguous", "Ambiguous",
          "Unklar"),
  CUSTOM("https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#Custom", "Custom",
          "Nutzerspezifisch");

  private final String uri;

  private final String labelEn;

  private final String labelDe;
}
