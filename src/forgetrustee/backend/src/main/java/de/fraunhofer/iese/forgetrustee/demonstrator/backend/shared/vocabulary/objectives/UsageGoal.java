
package de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.objectives;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.LabeledConcept;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UsageGoal implements LabeledConcept {
  DATA_ANALYSIS("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#DataAnalysis",
      "Data Analysis", "Datenanalyse"),
  DATA_ACCESS("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#DataAccess",
      "Data Access", "Datenzugang"),
  DATA_SHARING("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#DataSharing",
      "Data Sharing", "Datenteilen"),
  DATA_QUALITY_MANAGEMENT(
      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#DataQualityManagement",
      "Data Quality Management", "Datenqualitätsmanagement"),
  AMBIGUOUS("https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#Ambiguous", "Ambiguous",
      "Unklar"),
  NONE("https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#None", "None", "Keine");

  private final String uri;

  private final String labelEn;

  private final String labelDe;
}
