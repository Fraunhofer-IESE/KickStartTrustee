
package de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.legal;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.LabeledConcept;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConsentEnteredBy implements LabeledConcept {
  RIGHTS_OWNER("", "", ""),
  DATA_OWNER("", "", ""),
  NOT_PROVIDED("https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#NotProvided",
      "Keine Angabe", "Keine Angabe");

  private final String uri;

  private final String labelEn;

  private final String labelDe;
}
