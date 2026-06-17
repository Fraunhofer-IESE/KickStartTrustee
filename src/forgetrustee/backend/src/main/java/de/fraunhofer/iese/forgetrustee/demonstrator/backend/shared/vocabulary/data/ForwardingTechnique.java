
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
public enum ForwardingTechnique implements LabeledConcept {
  API("https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#ReceptionAPI", "API", "API"),
  UPLOAD("https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#Download", "Upload",
      "Upload"),
  ANALOG("https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#Analog", "Analog", "Analog"),
  AMBIGUOUS("https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#Ambiguous", "Ambiguous",
      "Unklar");

  private final String uri;

  private final String labelEn;

  private final String labelDe;
}
