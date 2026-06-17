# FAIRWinDS – kommentierte Modellierungsdatei mit Quellenbezug

Hinweis:
Diese Modellierung basiert ausschließlich auf den bereitgestellten Projektinformationen und bewusst abgeleiteten fachlichen Implikationen.
Es wurden keine zusätzlichen Akteure, Technologien oder Geschäftsmodelle halluziniert.

---

```json
{
  "core": {
    // Quelle: Projektname FAIRWinDS
    "dataTrusteeName": "FAIRWinDS",

    // Quelle: Projektbeschreibung + Zielsetzung
    "dataTrusteeDescription": "FAIRWinDS ist ein Datentreuhandmodell für den souveränen und standardisierten Austausch von Betriebsdaten aus Windenergieanlagen. Ziel ist es, den kontrollierten Datenaustausch zwischen Windparkbetreibern und Forschenden zu ermöglichen und dadurch Innovation, Forschung und wirtschaftliche Potenziale im Bereich Windenergie zu fördern.",

    // Quelle:
    // „Das projektkoordinierende Fraunhofer IWES ist dabei unter anderem
    // für die Rolle des Datentreuhänders vorgesehen.“
    "dataTrusteeOperator": "Fraunhofer IWES",

    "dataTrusteeOperatorAffiliation": "ORGANIZATION",

    // Quelle:
    // Nutzerinput + Projekttext
    "rightsHolderName": "Windparkbetreiber",

    "rightsHolderAffiliation": "ORGANIZATION",

    // Keine Hinweise auf Stellvertretung
    "rightsHolderIsRepresented": "false",

    // Quelle:
    // Windparkbetreiber stellen Betriebsdaten bereit
    "dataOwnerName": "Windparkbetreiber",

    // Quelle:
    // Forschende / Forschungseinrichtungen
    "dataConsumerName": "Forschungseinrichtungen",

    "dataConsumerAffiliation": "ORGANIZATION"
  },

  "data": {
    "dataCategory": {

      // Quelle:
      // „Betriebsdaten von Windenergieanlagen“
      "dataCategoryName": "Betriebsdaten von Windenergieanlagen",

      // Quelle:
      // Felddaten / Betriebsdaten / Windparks
      "dataCategoryDescription": "Betriebs-, Zustands- und Felddaten aus Windenergieanlagen und Windparks zur Analyse, Standardisierung und wissenschaftlichen Auswertung."
    },

    // Keine expliziten personenbezogenen Daten beschrieben
    "containPersonalInformation": "false",

    "specialPersonalInformation": "false",

    // Quelle:
    // Sorge vor Kontrollverlust
    // Geschäftsgeheimnisse/Unternehmenswerte
    "containTradeSecrets": "true",

    "processingBases": {
      // Modellierungsentscheidung:
      // legitimes Interesse am kontrollierten Datenaustausch
      "LEGITIMATE_INTEREST": [
        {
          "title": "Souveräner Datenaustausch",
          "description": "Die Verarbeitung dient dem kontrollierten und standardisierten Austausch von Betriebsdaten zwischen Unternehmen und Forschungseinrichtungen."
        }
      ]
    },

    "consentType": "NOT_PROVIDED",
    "obtainingConsentBy": "",
    "consentEnteredBy": "NOT_PROVIDED"
  },

  "implementation": {

    // Quelle:
    // Nutzerinput „Fremdbestimmte Auswertungstreuhand“
    "dataTrusteeCategory": "EXTERNALLY_CONTROLLED_VALUE_ADDED_DATA_TRUSTEE",

    // Quelle:
    // IDS / dezentrale Infrastruktur / Data Space
    "architectureType": "P2P",

    "receptionTechnologies": [
      "API"
    ],

    // Regelmäßiger Datenaustausch zwischen Akteuren
    "receptionFrequency": "PERIODIC",

    // Quelle:
    // Anonymisierung / Pseudonymisierung
    "receptionSecurityMeasures": [
      "PSEUDONYMISATION",
      "ANONYMISATION",
      "TRANSPORT_ENCRYPTION"
    ],

    "customReceptionSecurityTechniques": [
      {
        // Quelle:
        // IDS-basierte dezentrale Infrastruktur
        "title": "IDS-basierte Dateninfrastruktur",

        "description": "Die Bereitstellung und Übertragung der Daten erfolgt innerhalb einer dezentralen Dateninfrastruktur auf Basis der International Data Spaces."
      }
    ],

    // Quelle:
    // Windparks / Windenergieanlagen
    "sourceSystem": "Windenergieanlagen, Windparks und Systeme der Windparkbetreiber",

    "preparationTechniques": [
      "TRANSFORMATION",
      "AGGREGATION",
      "CUSTOM"
    ],

    "customPreparationTechniques": [
      {
        // Quelle:
        // Standardisierung / IEC 61400-25
        "title": "Standardisierung von Betriebsdaten",

        "description": "Die Daten werden in ein einheitliches Format überführt und unter Nutzung von Branchenstandards beschrieben."
      },
      {
        // Quelle:
        // Qualitätsprüfung / Anreicherung
        "title": "Qualitätsprüfung und Datenanreicherung",

        "description": "Die Daten werden geprüft, bewertet und durch zusätzliche Verarbeitungsschritte ergänzt."
      }
    ],

    // Speicherung nicht vollständig ausgeschlossen
    "storageTechnique": "CENTRAL_STORAGE",

    // Quelle:
    // variabel je nach Datengeber-Wunsch
    "storageRetention": "WITH_RETENTION",

    // Quelle:
    // unternehmensübergreifende Auswertung
    "analysisTechniques": [
      "STATISTICAL_ANALYSIS",
      "CUSTOM"
    ],

    "customAnalysisTechniques": [
      {
        "title": "Unternehmensübergreifende Datenauswertung",

        "description": "Die Betriebsdaten werden aggregiert ausgewertet, um Potenziale zur Senkung von Stromgestehungskosten zu identifizieren."
      }
    ],

    "forwardingTechniques": [
      "API"
    ],

    "forwardingFrequency": "PERIODIC",

    "forwardingSecurityMeasures": [
      "PSEUDONYMISATION",
      "ANONYMISATION",
      "TRANSPORT_ENCRYPTION"
    ],

    "customForwardingSecurityTechniques": [],

    // Quelle:
    // Forschende / Forschungseinrichtungen
    "targetSystem": "Forschungs- und Analysesysteme der beteiligten Forschungseinrichtungen"
  },

  "business": {

    // Quelle:
    // Windenergie / Energiebranche
    "businessDomains": [
      "ENERGY"
    ],

    // Modellierungsentscheidung:
    // gemeinschaftlicher Datenraum / mehrere Partner
    "businessModel": "COOPERATIVE",

    // Quelle:
    // BMBF + EU NextGenerationEU
    "fundingSources": [
      "BMBF",
      "EU"
    ],

    "financingTypes": {
      "PUBLIC_FUNDING": [
        {
          "title": "Projektförderung",

          "description": "Das Projekt wird durch das BMBF und die Europäische Union im Rahmen von NextGenerationEU gefördert."
        }
      ],

      "OTHER_FUNDINGS": [
        {
          // Quelle:
          // Querfinanzierung
          "title": "Querfinanzierung",

          "description": "Langfristige Finanzierungsoptionen sollen unter anderem durch ergänzende Dienste des Datentreuhänders unterstützt werden."
        }
      ]
    },

    // Keine Vergütung der Datengeber beschrieben
    "paymentMethodDataOwner": "FREE",

    // Kein konkretes Gebührenmodell beschrieben
    "paymentMethodDataConsumer": "FREE"
  },

  "objectives": {

    // Quelle:
    // Innovation / Forschung / wirtschaftliche Verwertung
    "dataTrusteeGoals": [
      "SCIENCE_RESEARCH",
      "INNOVATION",
      "ECONOMIC_UTILIZATION",
      "COMPLIANCE_REQUIREMENTS"
    ],

    "motivationRightsHolder": [
      "VALUE_CREATION",
      "SOCIAL_VALUE"
    ],

    "motivationDataHolder": [
      "INTEROPERABILITY",
      "SCIENCE_AND_RESEARCH",
      "COMPLIANCE_REQUIREMENTS"
    ],

    "motivationDataConsumer": [
      "SCIENCE_AND_RESEARCH",
      "INNOVATION",
      "VALUE_CREATION"
    ]
  }
}
```

---

# Zusätzliche Modellierungsentscheidungen

## Warum P2P?

Im Projekttext wird mehrfach eine dezentrale Dateninfrastruktur beschrieben:

* International Data Spaces (IDS)
* verteilter Datenraum
* Datenbrücke
* dezentrale Bereitstellung

Deshalb wurde `P2P` als passendste Architektur gewählt.

---

## Warum EXTERNALLY_CONTROLLED_VALUE_ADDED_DATA_TRUSTEE?

Der Datentreuhänder übernimmt:

* Aufbereitung
* Standardisierung
* Qualitätsprüfung
* Aggregation
* Auswertung
* Datenanreicherung

Die Nutzungszwecke werden jedoch überwiegend durch Datengeber und Forschende bestimmt.

---

## Warum containTradeSecrets = true?

Im Text wird explizit erwähnt:

* Sorge vor Kontrollverlust
* Unternehmensdaten
* Betriebsdaten
* wirtschaftliche Potenziale

Dadurch ist die Verarbeitung geschäftskritischer Informationen stark impliziert.

---

## Warum storageRetention = WITH_RETENTION?

Im Nutzerinput steht:

„Variabel, je nach Datengeber-Wunsch“

Dadurch kann keine reine Durchleitung (`TRANSMISSION_ONLY`) angenommen werden.

---

## Warum COOPERATIVE als BusinessModel?

Das Projekt beschreibt:

* kollaborativen Datenraum
* gemeinschaftliche Nutzung
* mehrere Industrie- und Forschungspartner
* kein klassisches Plattformgeschäft

Die Modellierung ist daher eine fachliche Annäherung, keine explizite Aussage des Projekts.

Alternative Interpretationen wären ebenfalls möglich.
