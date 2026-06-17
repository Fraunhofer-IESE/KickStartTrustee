
package de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.legal;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.ImplementedConcept;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConsentType implements ImplementedConcept {
  INDIVIDUAL_CONSENT(
      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#IndividualConsent",
      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#IndividualConsentWorkflow",
      "Individual Consent", "Individuelle Einwilligung"),
  GENERAL_CONSENT("https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#GeneralConsent",
      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#GeneralConsentWorkflow",
      "General Consent", "Allgemeine Einwilligung"),
  NOT_PROVIDED("https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#NotProvided", "",
      "Keine Angabe", "Keine Angabe");

  private final String uri;

  private final String implementationUri;

  private final String labelEn;

  private final String labelDe;
}
