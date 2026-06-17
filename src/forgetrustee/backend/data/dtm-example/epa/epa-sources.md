# ePA – kommentierte Modellierungsdatei mit Quellenbezug

## Hinweis

Diese Modellierung basiert ausschließlich auf den bereitgestellten Informationen des Bundesministeriums für Gesundheit (BMG) sowie den ergänzenden Beschreibungen zur elektronischen Patientenakte (ePA).

---

# Core

## `dataTrusteeName`

### Quelle

* „Elektronische Patientenakte (ePA)“
* „ePA für alle“

### Modellierungsentscheidung

Für die Modellierung wurde die prägnante Bezeichnung:

```text
ePA
```

verwendet.

---

## `dataTrusteeDescription`

### Quelle

* „digitale Infrastruktur“
* „Schaufenster in die Behandlungshistorie“
* „Cockpit zur Wahrnehmung der Datensouveränität“
* sichere Verwaltung medizinischer Informationen
* kontrollierte Verwaltung von Zugriffsrechten
* interoperable Gesundheitsversorgung

### Zusammengeführt zu

Narrativer Beschreibung einer sicheren digitalen Gesundheitsinfrastruktur mit Fokus auf Datensouveränität und kontrollierten Zugriff.

---

## `dataTrusteeOperator`

### Quelle

* Krankenkassen stellen die ePA bereit
* Bereitstellung innerhalb der Telematikinfrastruktur
* gesetzlich regulierte Gesundheitsinfrastruktur

### Modellierungsentscheidung

Die organisatorische Rolle wurde abstrahiert als:

```text
Krankenkassen
```

---

## `dataTrusteeOperatorAffiliation`

### Quelle

Krankenkassen und beteiligte Organisationen des Gesundheitswesens.

### Modellierungsentscheidung

```text
ORGANIZATION
```

Da kein `STATE`-Enum existiert und die Akteure organisatorisch handeln.

---

## `rightsHolderName`

### Quelle

* Versicherte
* Patientinnen und Patienten

### Modellierungsentscheidung

Verallgemeinert zu:

```text
Versicherte
```

---

## `rightsHolderAffiliation`

### Quelle

Natürliche Personen bzw. Bürgerinnen und Bürger.

### Modellierungsentscheidung

```text
INDIVIDUAL
```

---

## `rightsHolderIsRepresented`

### Quelle

* Möglichkeit von Stellvertretungen
* Verwaltung der ePA durch bevollmächtigte Personen

### Deshalb modelliert als

```text
true
```

---

## `dataOwnerName`

### Quelle

* Arztpraxen
* Krankenhäuser
* Labore
* weitere Leistungserbringer

### Modellierungsentscheidung

Verallgemeinert zu:

```text
Leistungserbringer
```

---

## `dataConsumerName`

### Quelle

* behandelnde Leistungserbringer
* Zugriff auf Behandlungsdaten innerhalb der Versorgung

### Modellierungsentscheidung

Ebenfalls abstrahiert zu:

```text
Leistungserbringer
```

---

## `dataConsumerAffiliation`

### Quelle

Institutionen des Gesundheitswesens.

### Modellierungsentscheidung

```text
ORGANIZATION
```

---

# Data

## `dataCategoryName`

### Quelle

* Gesundheitsdaten
* medizinische Daten
* Medikationsdaten
* Befunde

### Modellierungsentscheidung

```text
Gesundheitsdaten
```

---

## `dataCategoryDescription`

### Quelle

* Krankheiten
* Medikationslisten
* Allergien
* Arztbriefe
* Befunde
* Behandlungshistorien

### Zusammengeführt zu

Medizinischen Behandlungs-, Medikations- und Befunddaten.

---

## `containPersonalInformation`

### Quelle

Gesundheitsdaten mit Personenbezug.

### Deshalb modelliert als

```text
true
```

---

## `specialPersonalInformation`

### Quelle

Gesundheitsdaten gehören gemäß DSGVO zu besonderen Kategorien personenbezogener Daten.

### Deshalb modelliert als

```text
true
```

---

## `containTradeSecrets`

### Quelle

Keine Hinweise auf Geschäfts- oder Betriebsgeheimnisse.

### Deshalb modelliert als

```text
false
```

---

## `processingBases`

---

### `CONSENT`

#### Quelle

* Zugriffsmanagement
* Einwilligungsmanagement
* individuelle Freigaben
* Versicherte entscheiden über Zugriffsrechte

#### Modellierungsentscheidung

Einwilligungsbasierter Zugriff auf Gesundheitsdaten.

---

### `LEGAL_OBLIGATION`

#### Quelle

* verpflichtende Nutzung der ePA durch Leistungserbringer
* gesetzliche Befüllungspflichten

#### Modellierungsentscheidung

Gesetzlich regulierte Verarbeitung innerhalb des Gesundheitssystems.

---

## `consentType`

### Quelle

Versicherte verwalten individuelle Freigaben und Zugriffsrechte.

### Deshalb modelliert als

```text
INDIVIDUAL_CONSENT
```

---

## `obtainingConsentBy`

### Quelle

* ePA-App
* Krankenkassen

### Modellierungsentscheidung

```text
ePA-App und Krankenkassen
```

---

## `consentEnteredBy`

### Quelle

Versicherte verwalten selbst ihre Zugriffsrechte.

### Deshalb modelliert als

```text
RIGHTS_OWNER
```

---

# Implementation

## `dataTrusteeCategory`

### Quelle

* Zugriffsmanagement
* Berechtigungsmanagement
* kontrollierter Datenzugriff
* Identitätsmanagement

### Deshalb modelliert als

```text
ACCESS_DATA_TRUSTEE
```

---

## `architectureType`

### Quelle

* „virtuelle ePA“
* verteilte Datenhaltung
* Zugriff auf verteilte medizinische Datenquellen

### Deshalb modelliert als

```text
P2P
```

---

## `receptionTechnologies`

### Quelle

* digitale Schnittstellen
* interoperable Kommunikation
* Anbindung an die Telematikinfrastruktur

### Deshalb modelliert als

```text
API
```

---

## `receptionFrequency`

### Quelle

Fortlaufende Nutzung und Aktualisierung medizinischer Informationen.

### Deshalb modelliert als

```text
CONTINUOUS
```

---

## `receptionSecurityMeasures`

### Quelle

* Verschlüsselung
* Vertrauensdienste
* Zertifikate
* Integritätsschutz
* Datenschutzanforderungen

### Deshalb modelliert als

* `END_TO_END_ENCRYPTION`
* `TRANSPORT_ENCRYPTION`
* `PSEUDONYMISATION`

---

## `customReceptionSecurityTechniques`

### Quelle

* eIDAS-Verordnung
* elektronische Signaturen
* Webseitenzertifikate
* Vertrauensdienste

---

## `sourceSystem`

### Quelle

* Arztpraxissoftware
* Krankenhausinformationssysteme
* Laborsysteme
* Systeme der Leistungserbringer

### Modellierungsentscheidung

```text
Systeme der Leistungserbringer
```

---

## `preparationTechniques`

### Quelle

* interoperable Standards
* strukturierte medizinische Inhalte
* standardisierte Datenformate

### Deshalb modelliert als

```text
TRANSFORMATION
```

---

## `customPreparationTechniques`

### Quelle

* interoperable medizinische Inhalte
* standardisierte Medikationslisten
* strukturierte Versorgung

---

## `storageTechnique`

### Quelle

* „virtuelle ePA“
* verteilte Speicherung
* Zugriff auf bestehende Datenquellen

### Deshalb modelliert als

```text
DECENTRAL_STORAGE
```

---

## `storageRetention`

### Quelle

* langfristige Behandlungshistorie
* dauerhafte Verfügbarkeit medizinischer Informationen

### Deshalb modelliert als

```text
WITH_RETENTION
```

---

## `analysisTechniques`

### Quelle

Keine explizite eigenständige Analysefunktion der ePA beschrieben.

### Deshalb modelliert als

```text
NONE
```

---

## `forwardingTechniques`

### Quelle

* digitale Kommunikation
* interoperable Schnittstellen
* Übermittlung innerhalb der TI

### Deshalb modelliert als

```text
API
```

---

## `forwardingFrequency`

### Quelle

Kontinuierlicher Datenaustausch innerhalb der Versorgung.

### Deshalb modelliert als

```text
CONTINUOUS
```

---

## `forwardingSecurityMeasures`

### Quelle

* Verschlüsselung
* Zertifikate
* Vertrauensdienste
* Integritätsschutz

### Deshalb modelliert als

* `END_TO_END_ENCRYPTION`
* `TRANSPORT_ENCRYPTION`
* `PSEUDONYMISATION`

---

## `customForwardingSecurityTechniques`

### Quelle

* individuelles Berechtigungsmanagement
* Zugriffssteuerung
* Einwilligungsmanagement

---

## `targetSystem`

### Quelle

* Arztpraxen
* Krankenhäuser
* Labore
* medizinische Versorgungssysteme

### Modellierungsentscheidung

```text
Systeme der Leistungserbringer
```

---

# Business

## `businessDomains`

### Quelle

Gesundheitswesen und medizinische Versorgung.

### Deshalb modelliert als

```text
HEALTHCARE
```

---

## `businessModel`

### Quelle

- Es wird keine konkrete Rechtsform oder Geschäftsmodellform der ePA genannt.
- Die ePA ist gesetzlich reguliert und wird durch Krankenkassen bereitgestellt, aber dein Enum enthält hierfür keine passende öffentliche Institutionsform.

### Deshalb modelliert als

`AMBIGUOUS`

---

## `fundingSources`

### Quelle

Öffentliche Gesundheitsversorgung und gesetzliche Krankenversicherung.

### Deshalb modelliert als

```text
PUBLIC
```

---

## `financingTypes`

### Quelle

Die ePA wird als öffentliche Infrastruktur bereitgestellt.

### Deshalb modelliert als

```text
PUBLIC_FUNDING
```

---

## `paymentMethodDataOwner`

### Quelle

Versicherte erhalten die ePA kostenfrei.

### Deshalb modelliert als

```text
FREE
```

---

## `paymentMethodDataConsumer`

### Quelle

Keine transaktionsbasierte Vergütung beschrieben.

### Deshalb modelliert als

```text
FREE
```

---

# Objectives

## `dataTrusteeGoals`

### Quelle

* Datensouveränität
* sichere digitale Gesundheitsversorgung
* Interoperabilität
* Forschung mit Gesundheitsdaten
* digitale Transformation

### Deshalb modelliert als

* `DATA_SOVEREIGNTY`
* `COMPLIANCE_REQUIREMENTS`
* `SCIENCE_RESEARCH`
* `DIGITAL_SOVEREIGNTY`

---

## `motivationRightsHolder`

### Quelle

* bessere Transparenz
* bessere medizinische Versorgung
* Vermeidung von Doppeluntersuchungen
* Kontrolle über eigene Daten

### Deshalb modelliert als

* `QUALITY_IMPROVEMENT`
* `SOCIAL_VALUE`

---

## `motivationDataHolder`

### Quelle

* interoperable Versorgung
* effizientere Prozesse
* gesetzliche Anforderungen

### Deshalb modelliert als

* `COMPLIANCE_REQUIREMENTS`
* `INTEROPERABILITY`
* `QUALITY_IMPROVEMENT`

---

## `motivationDataConsumer`

### Quelle

* schnellere Verfügbarkeit medizinischer Informationen
* bessere Behandlungsqualität
* strukturierte Versorgung

### Deshalb modelliert als

* `QUALITY_IMPROVEMENT`
* `INTEROPERABILITY`
* `SCIENCE_AND_RESEARCH`
