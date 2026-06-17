
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
public enum PayMethods implements LabeledConcept {
  FLATRATE("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#Flatrate", "Flatrate",
      "Flatrate"),
  SERVICE_FEE("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#ServiceFee",
      "Service Fee", "Servicegebühr"),
  FREEMIUM("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#Freemium", "Freemium",
      "Freemium"),
  PAY_PER_USE("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#PayPerUse",
      "Pay-Per-Use", "Bezahlen pro Nutzung"),
  FREE("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#Free", "Free", "Kostenlos"),
  SUPPORT_FEE("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#SupportFee",
      "Support Fee", "Supportgebühr"),
  ACCESS_FEE("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#AccessFee",
      "Access Fee", "Zugangsgebühr"),
  AMBIGUOUS("https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#Ambiguous", "Ambiguous",
      "Unklar"),
  NONE("https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#None", "None", "Keine");

  private final String uri;

  private final String labelEn;

  private final String labelDe;
}
