
package de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.legal;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.LabeledConcept;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProcessingBasis implements LabeledConcept {
  CONSENT("https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#Consent", "Consent",
      "Einwilligung"),
  LEGAL_OBLIGATION("https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#LegalObligation",
      "Legal Obligation", "Gesetzliche Verpflichtung"),
  LEGITIMATE_INTEREST(
          "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#LegitimateInterest",
          "Legitimate Interest", "Öffentliches Interesse"),
  PUBLIC_INTEREST("https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#PublicInterest",
      "Public Interest", "Datenqualitätsmanagement"),
  NOT_PROVIDED("https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#NotProvided",
      "Keine Angabe", "Keine Angabe"),
  NONE("https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#None", "None", "Keine"),
  AMBIGUOUS("https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#Ambiguous", "Ambiguous",
      "Unklar");

  private final String uri;

  private final String labelEn;

  private final String labelDe;
}
