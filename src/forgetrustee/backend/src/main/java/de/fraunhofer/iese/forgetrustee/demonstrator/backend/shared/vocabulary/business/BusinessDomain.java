
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
public enum BusinessDomain implements LabeledConcept {
  FINANCE("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#FinancialSector",
      "Finance", "Finanzwirtschaft"),
  SMART_LIVING("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#SmartLivingSector",
      "Smart Living", "Smart Living"),
  HEALTHCARE("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#HealthcareSector",
      "Healthcare", "Gesundheitswesen"),
  INDUSTRY_40("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#Industrie40",
      "Industry 4.0", "Industrie 4.0"),
  PUBLIC_SECTOR("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#PublicSector",
      "Public Sector", "Öffentlicher Sektor"),
  CULTURE_CREATIVE_INDUSTRY(
      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#CulturalCreativeSector",
      "Culture / Creative Industry", "Kultur- / Kreativwirtschaft"),
  SMART_CITY_REGION(
      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#SmartCityRegionSector",
      "Smart City / Region", "Smart City / Smart Region"),
  AEROSPACE("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#AerospaceSector",
      "Aerospace", "Luft- / Raumfahrt"),
  AGRICULTURE("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#AgricultureSector",
      "Agriculture", "Landwirtschaft"),
  LOGISTICS("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#LogisticSector",
      "Logistics", "Logistik"),
  ENERGY("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#EnergySector", "Energy",
      "Energie"),
  CROSS_DOMAIN("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#CrossDomainSector",
      "Cross-Domain", "Domänenübergreifend"),
  GEOINFORMATION(
      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#GeoinformationSector",
      "Geoinformation", "Geoinformation"),
  MOBILITY("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#MobilitySector",
      "Mobility", "Mobilität"),
  EDUCATION("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#EducationSector",
      "Education", "Bildung"),
  CONSTRUCTION_OPERATION_MAINTENANCE(
      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#ConstructionSector",
      "Construction / Operation / Maintenance", "Planen / Bauen / Betreiben"),
  AMBIGUOUS("https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#Ambiguous", "Ambiguous",
          "Unklar"),
  NONE("https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#None", "None", "Keine");;

  private final String uri;

  private final String labelEn;

  private final String labelDe;
}
