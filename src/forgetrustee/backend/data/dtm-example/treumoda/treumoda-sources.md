TreuMoDa – kommentierte Modellierungsdatei mit stärkeren fachlichen Implikationen

Hinweis:
Diese Version erlaubt bewusst stärkere fachliche Ableitungen aus den bereitgestellten Projektinformationen,
ohne neue Akteure, Technologien oder Geschäftsmodelle zu halluzinieren.

Wesentliche Anpassungen gegenüber der konservativen Variante:
- Akteure wurden konkreter benannt
- Geschäftsmodell als gemeinnützige Struktur interpretiert
- Datenarten stärker am automatisierten Fahren orientiert
- Ziele wirtschaftlicher Nutzung expliziter modelliert
- Quell- und Zielsysteme konkreter beschrieben

{
  "core": {
    // Quelle: Projektname / Nutzerinput
    "dataTrusteeName": "TreuMoDa",

    // Quelle: Projektziele + Beschreibung der TMD
    "dataTrusteeDescription": "TreuMoDa ist als unabhängige und gemeinnützige Treuhandstelle für Mobilitätsdaten konzipiert. Die Treuhandstelle soll die datenschutzkonforme Bereitstellung, Aufbereitung und Nutzung von Mobilitätsdaten für Wissenschaft, Wirtschaft und öffentliche Akteure ermöglichen und dadurch Innovation, Forschung und den sicheren Datenaustausch fördern.",

    // Keine explizite Betreiberorganisation genannt → Nutzung des Projekt-/Treuhandnamens
    "dataTrusteeOperator": "TreuMoDa",

    "dataTrusteeOperatorAffiliation": "ORGANIZATION",

    // Stärkere Ableitung aus B2B-/Mobilitätskontext
    "rightsHolderName": "mobilitätsdatenbereitstellende Unternehmen",

    "rightsHolderAffiliation": "ORGANIZATION",

    // Keine Hinweise auf Stellvertretung
    "rightsHolderIsRepresented": "false",

    // Quelle: Projektbeschreibung nennt Wissenschaft und Wirtschaft als Datenbereitsteller/Nutzer
    "dataOwnerName": "Unternehmen und Forschungseinrichtungen",

    // Quelle: Projekttext nennt Wirtschaft, Wissenschaft und öffentliche Hand
    "dataConsumerName": "Unternehmen, Forschungseinrichtungen und öffentliche Stellen",

    "dataConsumerAffiliation": "ORGANIZATION"
  },

  "data": {
    "dataCategory": {
      // Quelle: Mobilitätskontext + automatisiertes Fahren
      "dataCategoryName": "Mobilitäts- und Verkehrsdaten",

      // Quelle: Projektbeschreibung zu Sensoren in Fahrzeugen und Verkehrsinfrastrukturen
      "dataCategoryDescription": "Mobilitätsdaten aus Fahrzeugen und Verkehrsinfrastrukturen, insbesondere Sensor-, Verkehrs- und Umfelddaten aus dem Kontext automatisierten Fahrens."
    },

    // Quelle: Projekttext „häufig gegebener Personenbezug“
    "containPersonalInformation": "true",

    // Keine Hinweise auf besondere Kategorien personenbezogener Daten
    "specialPersonalInformation": "false",

    // B2B-/Mobilitäts-/Fahrzeugdaten implizieren potenzielle Betriebs- und Entwicklungsgeheimnisse
    "containTradeSecrets": "true",

    "processingBases": {
      // Juristische Modellierungsannahme auf Basis des beschriebenen Nutzungszwecks
      "LEGITIMATE_INTEREST": [
        {
          "title": "Datenschutzkonforme Datennutzung",
          "description": "Die Verarbeitung dient der rechtssicheren Bereitstellung und Nutzung von Mobilitätsdaten für Forschung, Innovation und die Entwicklung datenbasierter Mobilitätslösungen."
        }
      ]
    },

    "consentType": "NOT_PROVIDED",
    "obtainingConsentBy": "",
    "consentEnteredBy": "NOT_PROVIDED"
  },

  "implementation": {
    // Quelle: Nutzerinput „Fremdbestimmte Auswertungstreuhand“
    "dataTrusteeCategory": "EXTERNALLY_CONTROLLED_VALUE_ADDED_DATA_TRUSTEE",

    // Ableitung aus Datenvermittlung und Transferfunktion
    "architectureType": "CENTRAL_ROUTER",

    // Quelle: digitale Schnittstellen und Datentransfer
    "receptionTechnologies": [
      "API",
      "DOWNLOAD"
    ],

    // Quelle: „bei Bedarf / auf Anweisung“
    "receptionFrequency": "MANUAL",

    // Quelle: Modul 2
    "receptionSecurityMeasures": [
      "PSEUDONYMISATION",
      "ANONYMISATION",
      "TRANSPORT_ENCRYPTION"
    ],

    "customReceptionSecurityTechniques": [
      {
        // Quelle: Modul 2 „Eingangsprüfung“
        "title": "Vorprüfung eingehender Datensätze",
        "description": "Eingehende Datensätze werden vor der Weitergabe technisch und organisatorisch geprüft."
      }
    ],

    // Konkretisierung aus Fahrzeug-/Verkehrskontext
    "sourceSystem": "Fahrzeuge, Verkehrsinfrastrukturen und Systeme der Datengeber",

    "preparationTechniques": [
      "TRANSFORMATION",
      "CUSTOM"
    ],

    "customPreparationTechniques": [
      {
        // Quelle: Modul 2
        "title": "Datenschutzkonforme Datenaufbereitung",
        "description": "Die Daten werden vor der Bereitstellung formatiert, strukturiert, pseudonymisiert oder anonymisiert."
      }
    ],

    // Kurzfristige zentrale Verarbeitung
    "storageTechnique": "CENTRAL_STORAGE",

    // Quelle: Nutzerinput + Modul 2
    "storageRetention": "TRANSMISSION_ONLY",

    // Keine explizite Analysefunktion beschrieben
    "analysisTechniques": [
      "NONE"
    ],

    "customAnalysisTechniques": [],

    "forwardingTechniques": [
      "API"
    ],

    "forwardingFrequency": "MANUAL",

    "forwardingSecurityMeasures": [
      "PSEUDONYMISATION",
      "ANONYMISATION",
      "TRANSPORT_ENCRYPTION"
    ],

    "customForwardingSecurityTechniques": [],

    // Konkretisierte Zielsysteme aus Projektkontext
    "targetSystem": "Forschungs-, Analyse- und Verwaltungssysteme der Datennutzer"
  },

  "business": {
    "businessDomains": [
      "MOBILITY"
    ],

    // Gemeinnützigkeit im Projekttext → stärkere Interpretation
    "businessModel": "NONPROFIT_LLC",

    "fundingSources": [
      "OTHER"
    ],

    "financingTypes": {
      "PUBLIC_FUNDING": [
        {
          "title": "Projektförderung",
          "description": "Die Konzeptionierung und prototypische Erprobung der Treuhandstelle erfolgt im Rahmen öffentlich unterstützter Projektförderung."
        }
      ],

      "OTHER_FUNDINGS": [
        {
          "title": "Nachhaltige Finanzierungsmodelle",
          "description": "Es werden Finanzierungsoptionen zur langfristigen und neutralen Bereitstellung der Treuhandstelle erarbeitet."
        }
      ]
    },

    // Keine Vergütung der Datengeber beschrieben
    "paymentMethodDataOwner": "FREE",

    // Nutzerinput „Datennutzer pro Transaktion“
    "paymentMethodDataConsumer": "PAY_PER_USE"
  },

  "objectives": {
    // Quelle: Projektziele + Nutzerinput
    "dataTrusteeGoals": [
      "SCIENCE_RESEARCH",
      "INNOVATION",
      "COMPLIANCE_REQUIREMENTS",
      "ECONOMIC_UTILIZATION"
    ],

    // Gesellschaftlicher und wirtschaftlicher Nutzen
    "motivationRightsHolder": [
      "SOCIAL_VALUE",
      "VALUE_CREATION"
    ],

    "motivationDataHolder": [
      "COMPLIANCE_REQUIREMENTS",
      "INTEROPERABILITY",
      "SCIENCE_AND_RESEARCH"
    ],

    "motivationDataConsumer": [
      "SCIENCE_AND_RESEARCH",
      "INNOVATION",
      "VALUE_CREATION"
    ]
  }
}