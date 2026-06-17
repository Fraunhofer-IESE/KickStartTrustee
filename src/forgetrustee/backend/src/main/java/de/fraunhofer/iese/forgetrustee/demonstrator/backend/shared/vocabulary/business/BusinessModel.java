
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
public enum BusinessModel implements LabeledConcept {
  REGISTERED_ASSOCIATION(
      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#RegisteredAssociation",
      "Registered Association", "Eingetragener Verein (e.V.)"),
  LIMITED_LIABILITY_COMPANY(
      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#LimitedLiabilityCompany",
      "Limited Liability Company", "Gesellschaft mit beschränkter Haftung (GmbH)"),
  GENERAL_PARTNERSHIP(
      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#GeneralPartnership",
      "General Partnership", "Offene Handelsgesellschaft (OHG)"),
  CIVIL_LAW_PARTNERSHIP(
      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#CivilLawPartnership",
      "Civil-Law Partnership", "Gesellschaft bürgerlichen Rechts (GbR)"),
  ENTREPRENEURIAL_COMPANY(
      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#EntrepreneurialCompany",
      "Entrepreneurial Company", "Unternehmergesellschaft (UG) (haftungsbeschränkt)"),
  SOCIETAS_EUROPAEA(
      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#EuropeanCompany",
      "Societas Europaea (SE)", "Societas Europaea (SE)"),
  COOPERATIVE("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#CooperativeSociety",
      "Cooperative", "Eingetragene Genossenschaft (eG)"),
  NONPROFIT_LLC(
      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#NonprofitLimitedLiabilityCompany",
      "Nonprofit LLC", "Gemeinnützige Gesellschaft mit beschränkter Haftung (gGmbH)"),
  PUBLIC_LAW_CORPORATION(
      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#PublicLawCorporation",
      "Public Law Corporation", "Körperschaft des öffentlichen Rechts (KdöR)"),
  PUBLIC_LAW_INSTITUTION(
      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#PublicLawInstitution",
      "Public Law Institution", "Anstalt des öffentlichen Rechts (AöR)"),
  STOCK_CORPORATION(
      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#StockCorporation",
      "Stock Corporation", "Aktiengesellschaft (AG)"),
  FOUNDATION("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#Foundation",
      "Foundation", "Stiftung"),
  PARTNERSHIP_LIMITED_BY_SHARES(
      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#ShareLimitedPartnership",
      "Partnership Limited by Shares", "Kommanditgesellschaft auf Aktien (KGaA)"),
  LIMITED_PARTNERSHIP(
      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#LimitedPartnership",
      "Limited Partnership", "Kommanditgesellschaft (KG)"),
  AMBIGUOUS("https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#Ambiguous", "Ambiguous",
      "Unklar"),
  NONE("https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#None", "None", "Keine");

  private final String uri;

  private final String labelEn;

  private final String labelDe;
}
