
package de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.objectives;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.LabeledConcept;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MotivationDataConsumer implements LabeledConcept {

  INNOVATION("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#Innovation",
      "Innovation", "Innovation"),
  COMPLIANCE_REQUIREMENTS("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#Compliance",
      "Compliance", "Erfüllen von Auflagen"),
  INTEROPERABILITY(
      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#Interoperability",
      "Interoperability", "Interoperabilität"),
  VALUE_CREATION("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#ValueCreation",
      "Value Creation", "Wertschöpfung"),
  OPTIMIZATION("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#Optimization",
      "Optimization", "Optimierung"),
  SCIENCE_AND_RESEARCH(
      "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#ScienceAndResearch",
      "Science and Research", "Wissenschaft und Forschung"),
  SOCIAL_VALUE("https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#SocietalValue",
      "Societal Value", "Gesellschaftlicher Mehrwert"),
  AMBIGUOUS("https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#Ambiguous", "Ambiguous",
      "Unklar"),
  NONE("https://forgetrustee.kickstarttrustee.de/ontology/dtm-misc#None", "None", "Keine");

  private final String uri;

  private final String labelEn;

  private final String labelDe;
}
