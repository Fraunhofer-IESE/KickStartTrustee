
package de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.objectives;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.LabeledConcept;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MotivationRightsHolder implements LabeledConcept {
  QUALITY_IMPROVEMENT(
          "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#QualityImprovement",
          "Quality Improvement", "Qualitätsverbesserung"),
  COST_REDUCTION("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#CostReduction",
      "Cost Reduction", "Kostenreduktion"),
  CONTRACT_FULFILLMENT(
      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#ContractFulfillment",
      "Contract Fulfillment", "Vertragserfüllung"),
  SOCIAL_VALUE("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#SocietalValue",
      "Societal Value", "Gesellschaftlicher Mehrwert"),
  AMBIGUOUS("https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#Ambiguous", "Ambiguous",
      "Unklar"),
  NONE("https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#None", "None", "Keine");

  private final String uri;

  private final String labelEn;

  private final String labelDe;
}
