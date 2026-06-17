
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
public enum FundingSource implements LabeledConcept {
  GWK("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#GWK",
      "GWK - Joint Science Conference", "GWK - Gemeinsame Wissenschaftskonferenz"),
  BMDV("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#BMDV",
      "BMDV - Federal Ministry for Digital and Transport",
      "BMDV - Bundesministerium für Digitales und Verkehr"),
  EU("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#EU", "EU - European Union",
      "EU - Europäische Union"),
  BMFTR("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#BMFTR",
      "BMFTR - Federal Ministry of Research, Technology and Space",
      "BMFTR - Bundesministerium für Forschung, Technologie und Raumfahrt"),
  BMWK("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#BMWK",
      "BMWK - Federal Ministry for Economic Affairs and Climate Action",
      "BMWK - Bundesministerium für Wirtschaft und Klimaschutz"),
  BMBF("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#BMBF",
      "BMBF - Federal Ministry of Education and Research",
      "BMBF - Bundesministerium für Bildung und Forschung"),
  OTHER("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#AndereFoerderung",
      "Other Funding Source", "Andere Förderung"),
  AMBIGUOUS("https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#Ambiguous", "Ambiguous",
      "Unklar"),
  NONE("https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#None", "None", "Keine");

  private final String uri;

  private final String labelEn;

  private final String labelDe;
}
