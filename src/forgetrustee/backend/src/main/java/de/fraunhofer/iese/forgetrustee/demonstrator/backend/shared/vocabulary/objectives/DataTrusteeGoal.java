
package de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.objectives;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.LabeledConcept;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DataTrusteeGoal implements LabeledConcept {
  SCIENCE_RESEARCH(
          "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#ScienceAndResearch",
          "Science and Research", "Wissenschaft und Forschung"),
  COMPLIANCE_REQUIREMENTS(
          "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#Compliance",
          "Compliance", "Erfüllung von Auflagen"),
  DIGITAL_SOVEREIGNTY(
          "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#DigitalSovereignty",
          "Digital Sovereignty", "Digitale Souveränität"),
  DATA_SOVEREIGNTY("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#DataSovereignty",
          "Data Sovereignty", "Datensouveränität"),
  ECONOMIC_UTILIZATION(
          "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#EconomicUtilization",
          "Economic Utilization", "Wirtschaftliche Verwertung"),
  FAIR_MARKET_COMPETITION(
      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#FairMarketCompetition",
      "Fair Market Competition", "Fairer Wettbewerb"),
  INNOVATION("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#Innovation",
      "Innovation", "Innovation"),
  AMBIGUOUS("https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#Ambiguous", "Ambiguous",
      "Unklar"),
  NONE("https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#None", "None", "Keine");

  private final String uri;

  private final String labelEn;

  private final String labelDe;
}
