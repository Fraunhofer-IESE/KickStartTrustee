
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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.business;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.LabeledConcept;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FinancingType implements LabeledConcept {
  PUBLIC_FUNDING("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#PublicFunding", "Public funding", "Öffentliche Finanzierung"),
  USAGE_FEES("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#UsageFees", "Usage fee", "Nutzungsgebühr"),
  COMMUNITY_FUNDING("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#CommunityFunding", "Community Funding", "Gemeinschaftliche Finanzierung"),
  BUSINESS_RELATED_FUNDING("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#BusinessRelatedFunding", "Business-Related Funding", "Betriebsbezogene Finanzierung"),
  OTHER_FUNDINGS("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#OtherFunding", "Other fundings", "Weitere Finanzierungen"),
  NOT_PROVIDED("https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#NotProvided", "Keine Angabe",
          "Keine Angabe"),
  AMBIGUOUS("https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#Ambiguous", "Ambiguous",
          "Unklar");

  private final String uri;

  private final String labelEn;

  private final String labelDe;
}
