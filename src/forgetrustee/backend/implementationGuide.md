# Gesamtüberblick: Wizard-Logik & Extensions

## Ziel des Wizards

Der Wizard dient dazu, **ein Datentreuhand-Szenario vollständig zu beschreiben**, bestehend aus:

1. **Akteuren & Daten** (dtm-core)
2. **Rechtsgrundlage/Rechtlicher Legitimation der Datenweitergabe** (dtm-legal)
3. **Technik/Technischer Umsetzung & Lebenszyklus** (dtm-data)
4. **Finanzen/Business-Einordnung** (dtm-business)
5. **Ziele**
    

Jede Wizard-Antwort erzeugt **konkrete Ontologie-Instanzen oder Relationen** über die bereitgestellten REST-APIs.

---



# 0 Neue Ontologie-Instanz

#### Frage vor dem Wizard

**Titel:** Neue Ontologie anlegen  
**Frage:** Unter welcher URI soll die Ontologie angelegt werden?

#### API

`POST /newOntologyInstance`

**Request**

**RequestParam**
`{   "newOntologyURI": "https://www.example.de/ontology#" }`

**Response**

`{   "uri": "https://www.example.de/ontology#" }`

#### UI

- `newOntologyURI`: Textfeld
	- Pflichtfeld
#### Tooltip

„Geben Sie eine stabile Basis-URI an. Alle späteren Elemente (Akteure, Daten, Prozesse) erhalten automatisch diese URI als Präfix.“


---

# Seite: Akteure & Daten

## Überschrift: Datentreuhand

**Hilfetext:**
 Ein Datentreuhänder verwaltet und vermittelt Daten unter klaren Governance-Regeln, um den Schutz von Rechten Betroffener, die Steuerung durch Dateninhaber und die kontrollierte Nutzung durch Datennutzer sicherzustellen. Das Datentreuhandmodell beschreibt die Betriebs- und Governance-Variante und dient als Leitplanke für Rollen, Prozesse und technische Umsetzung.

### 1.1 Name des Datentreuhänders

#### Wizard-Frage

**Frage:** Wie ist der Name des Datentreuhänders?

#### Required?
Ja

#### API

`POST /newDataTrustee`

**RequestParam** 
`{   "ontologyURI": "https://www.example.de/ontology#",   "dataTrusteeName": "NewDataTrust" }`

#### UI

- `ontologyURI`durch instanzierte Ontology; kein UI Element -> ermittelt durch `dataTrusteeName`
- `dataTrusteeName`: TextInput ("Name hinzufügen)
	- Pflichtfeld
#### Tooltip

„Der Datentreuhänder ist der zentrale Akteur, der Daten verwaltet und weitergibt. 
Der Name wird Teil der URI. Verwenden Sie einen sprechenden, eindeutigen Bezeichner.
[Link Framework]“

**Neuer Vorschlag** Kurz, eindeutig, domänen- und regionsbezogen. Beispiele (fiktiv, Health): „Administrative Trustee – Klinische Studien DE“, „Access Trustee – Patientenakten EU“. [Link Framework]


---

### 1.2 Beschreibung des Datentreuhänders

#### Wizard-Frage

**Frage:** Beschreibung des Datentreuhandmodels

#### Required?
Nein

#### API

`POST /addComment`

**@RequestBody**
`{   "ontologyURI": "https://www.example.de/ontology#",   "subjectURI": "https://www.example.de/ontology#NewDataTrust",   "literal": "Datentreuhand für Fahrzeugtelemetrie im Versicherungsumfeld.",   "lang": "de" }`

#### UI

- `ontologyURI`: Kein UI-Element, implizit durch gesetzte Instanz-Ontologie
- `subjectURI`: Kein UI-Element, Platzierung im Wizard impliziert Verknüpfung zur vorangegangenen Datentreuhand-Instanz
- `literal`: TextArea
	- (Placeholder: "Optional: Beschreibung hinzufügen")
- `lang`: Kein UI-Element, momentan im Code hinterlegt

#### Tooltip

Beschreiben Sie optional Zweck, Kontext und Besonderheiten des Modells in 1–3 Sätzen. Beispiele: „Empfängt elektronische Patientenakten via API, pseudonymisiert sie, speichert dezentral mit definierter Aufbewahrung und gewährt Zugriff an akkreditierte Forschungsteams.“


### 1.3 Datentreuhandbetreiber 

#### Wizard Frage
**Frage:** Wer betreibt das Datentreuhandmodell?

#### API

!TODO!

#### UI
!TODO!

#### Tooltip
Vollständiger juristischer Name der betreibenden Einheit. Beispiele: „GesundData gGmbH“, „HealthyTrust Services SE“

---

## Überschrift: Datengeber (anstatt Rechte- und Dateninhaber)

**Hilfetext:**
Hier erfassen Sie den Datengeber (natürliche Person oder juristische Person) und, falls vorhanden, die Zwischeninstanz zwischen Datengeber und Datentreuhänder, die ihn vertritt. Geben Sie den Namen des Datengebers an, wählen Sie den Typ (Individuum/Organisation) und markieren Sie, ob der Datengeber vertreten wird; nennen Sie in diesem Fall die vertretende Zwischeninstanz. Diese Angaben stellen die Beziehung zwischen Datengeber und Zwischeninstanz her und bilden die Grundlage für nachgelagerte Schritte wie delegierte Einwilligungen und die Verwaltung der zugehörigen Daten.

### 1.4 Datengeber (Betroffene Partei (AffectedParty))

#### Wizard-Frage

**Frage:** Wer ist der Datengeber?

#### API

`POST /newActor`

**@RequestParam**
`{   "ontologyURI": "https://www.example.de/ontology#",   "actorName": "Fahrzeughalter",   "roleURI": "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#AffectedParty" }`

#### UI

-  `ontologyURI`: Kein UI-Element, implizit durch gesetzte Instanz-Ontologie
- `actorName`: TextInput
	- Pflichtfeld
- `roleURI`: Kein UI-Element, durch Platzierung impliziert `--> AffectedParty (https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#AffectedParty)`

#### Tooltip

„Geben Sie einen sprechenden Namen für einen Datengeber ein, z.B. _Patienten_. 
[Link Framework]“

---

### 1.5 Typ der betroffenen Partei (dtm-business)

#### Wizard-Frage

**Frage:** Beim Datengeber handelt es sich um …

#### API

`POST /addRelation`

**@RequestParam**
`{   "ontologyURI": "https://www.example.de/ontology#" }

**@RequestBody**
   `` {"subject_URI": "https://www.example.de/ontology#Fahrzeughalter",   "property_URI": "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#affiliationType",   "object_URI": "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#Individual", "relationBetweenIndividuals": false }

#### UI

- `subjectURI`: Kein UI-Element, durch Platzierung impliziert
- `propertyURI`: Kein UI-Element, durch Platzierung impliziert
- `objectURI`: Einfache Chipauswahl
	- Natürliche Person --> `dtm-business#Individual`
	- Juristische Person --> `dtm-business#Organisation`
	- Unklar --> Methode wird nicht gecallt
- `relationBetweenIndividuals`: Kein UI-Element, durch Platzierung impliziert

#### Tooltip

nicht gebraucht

---

### 1.6 Wird der Rechteinhaber durch eine andere Organisation vertreten?

#### Wizard-Frage

**Frage:** Gibt der Datengeber die Daten direkt an die Datentreuhand weiter oder übernimmt das eine Zwischeninstanz?
    
#### API

`POST /addSubtype`

**@RequestParam**

`{   "ontologyURI": "https://www.example.de/ontology#",   "subjectURI": "https://www.example.de/ontology#Fahrzeughalter",   "subtypeURI": "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#DataOwner" }`

#### UI

Einfache Chipauswahl
- Direkte Weitergabe --> `AffectedPerson` bekommt Subtype `DataOwner`
- Unklar --> Nichts passiert, kein API call
- Weitergabe durch Zwischeninstanz --> Einblendung der Frage 1.7
    
### Tooltip

"_Weitergabe durch anderen Akteur_ bedeutet: Ein anderer Akteur übernimmt faktisch oder rechtlich die 
Verwaltung/Verfügung über die Daten für die betroffene Partei., z.B. ein _Krankenhaus_
agiert für _Patienten_ in der Datentreuhand."

---

### 1.7 Dateninhaber (DataOwner)

#### Wizard-Frage

**Frage:** Wer ist die Zwischeninstanz, der die Daten für den Datengeber verwaltet?

#### API

`POST /newActor`

`{   "ontologyURI": "https://www.example.de/ontology#",   "actorName": "OEMBavaria",   "roleURI": "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#DataOwner" }`

und

`POST /addRelation`

**@RequestParam**

`{   "ontologyURI": "https://www.example.de/ontology#" }`

**@RequestBody**

` {"subject_URI": "https://www.example.de/ontology#OEMBavaria",   "property_URI": "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#affiliationType",   "object_URI": "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#Organisation" }`

--> implizierte Organization, kann keine natürliche Person sein, in den allermeisten Fällen ist es aber so
#### UI

siehe [[#1.5 Typ der betroffenen Partei (dtm-business)]]

#### Tooltip
"Dieser Akteur übernimmt faktisch oder rechtlich die 
Verwaltung/Verfügung über die Daten für die betroffene Partei., z.B. ein _Krankenhaus_
agiert für _Patienten_ in der Datentreuhand."

---

## Überschrift: Datennutzer
**Hilfetext:**
Der Datennutzer ist der Akteur, der unter den Regeln des Datentreuhänders Zugang zu Daten erhält und sie für definierte Ziele nutzt (z. B. Datenzugang, Analyse, Qualitätssicherung oder Teilen). Er kann eine natürliche oder juristische Person sein und wird im Modell mit seinen Nutzungszielen verknüpft sowie als Empfänger von Datenzugang ausgewiesen. [Link Framework]“

### 1.8 Datennutzer (DataConsumer)

#### Wizard-Frage

**Frage:** Wie heißt der Datennutzer?

#### API

`POST /newActor`

`{   "ontologyURI": "https://www.example.de/ontology#",   "actorName": "DienstleisterVersicherung",   "roleURI": "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#DataConsumer" }`

#### UI

siehe [[#1.5 Typ der betroffenen Partei (dtm-business)]]  [[]]
#### Tooltip

Verwenden Sie den vollständigen Namen: bei Organisationen die juristische Bezeichnung (z. B. „Forschungszentrum Musterstadt gGmbH“), bei Personen Vor- und Nachnamen (z. B. „Dr. Max Beispiel“)

---
### 1.9 Typ der begünstigten Partei (dtm-business)

#### Wizard-Frage

**Frage:** Beim Datennutzer handelt es sich um …

#### API

`POST /addRelation`


**@RequestParam**

`{   "ontologyURI": "https://www.example.de/ontology#" }`

**@RequestBody**

` {"subject_URI": "https://www.example.de/ontology#DienstleisterVersicherung",   "property_URI": "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#affiliationType",   "object_URI": "https://forgetrustee.kickstarttrustee.de/ontology/dtm-business#Organisation" }`

#### UI

Einfache Chipauswahl
- Natürliche Person --> `dtm-business#Individual`
- Juristische Person --> `dtm-business#Organisation`
- Unklar --> Methode wird nicht gecallt

#### Tooltip
nicht gebraucht

----

## Überschrift: Daten
**Hilfetext:**
Vermittelte Daten sind die Informationsbestände, die über den Datentreuhänder kontrolliert bereitgestellt oder weitergegeben werden. Sie können personenbezogene Daten (inklusive besonderer Kategorien wie Gesundheits‑ oder biometrische Daten), nicht personenbezogene Daten oder Geschäftsgeheimnisse umfassen. Wesentliche Merkmale sind Herkunft/Provenienz, Struktur und Format, Qualitätsgrad, Nutzungsbedingungen sowie Aufbewahrungsregeln; zusätzlich prägen Rollenbezüge die Daten: rechtliche Bindung an den Datengeber, Verwaltung durch die Zwischeninstanz und technische Formen der Haltung bzw. Übergabe.

### 1.10 Datenkategorie

#### Wizard-Frage

**Frage:** Welche Kategorie von Daten wird vermittelt?

#### API [TODO, hinzufügen der Beschreibung]

`POST /newData`

**@RequestParam**
`{   "ontologyURI": "https://www.example.de/ontology#",   "dataName": "TelemetrieDaten" }`

und

**@RequestBody**
`POST /newComment`
```json
{
  "ontologyURI": "https://www.example.de/ontology#",
  "subjectURI": "https://www.example.de/ontology#TelemetrieDaten",
  "literal": "TestComment",
  "lang": "de"
}
```

#### UI
- TextInput --> `\newData` `dataName`
- TextInput --> `\newComment` `literal`

#### Tooltip
Für Kategorie Textinput: "Geben Sie eine Oberkategorie der zu vermittelten Daten im Datentreuhandmodell, z.B. _Gesundheitsdaten_"
Für optionale Beschreibung: "Kurze Charakterisierung mit Quelle/Format/Zeitraum, z. B. „EPA (FHIR), pseudonymisiert, 2019–2024“."

---

### 1.10 Schutzklassifikation der Daten (BUG)

#### Personenbezogen?

`POST /addSubtype`

**@RequestParam**
`{   "ontologyURI": "https://www.example.de/ontology#",   "subjectURI": "https://www.example.de/ontology#TelemetrieDaten",   "subtypeURI": "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#PersonalData" }`

#### Besondere Kategorien?

`POST /addSubtype`

**@RequestParam**
`{   "ontologyURI": "https://www.example.de/ontology#",   "subjectURI": "https://www.example.de/ontology#TelemetrieDaten",   "subtypeURI": "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#SpecialPersonalData" }`

#### Geschäftsgeheimnisse?

`POST /addSubtype`

**@RequestParam**
`{   "ontologyURI": "https://www.example.de/ontology#",   "subjectURI": "https://www.example.de/ontology#TelemetrieDaten",   "subtypeURI": "https://forgetrustee.kickstarttrustee.de/ontology/dtm-core#TradeSecret" }`

##### Tooltip
nicht nötig, im Hilfetext integriert

#### UI

jeweils einfache Chipauswahl
- Ja --> `addSubtype`
- Nein/Unklar --> Nichts


---

# Seite: Rechtsgrundlage (TODO)

## Überschrift: 2. Rechtliches (dtm-legal) (TODO Januar)

## 2.1 Verarbeitungsgrundlage der Datenweitergabe

### Wizard-Frage

**Frage:** Auf welche Verarbeitungsgrundlage beruht die Datentreuhand?

### Auswahl (SKOS Concepts)

- Vertragserfüllung
    
- Gesetzliche Verpflichtung
    
- Berechtigtes Interesse
    
- Öffentliches Interesse
    
- Lebenswichtiges Interesse
    
- Einwilligung
    

### API (Beispiel)

`POST /addRelation`

`{   "ontologyURI": "https://www.example.de/ontology#",   "subject_URI": "https://www.example.de/ontology#DTForwarding",   "property_URI": "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#hasProcessingBasis",   "object_URI": "https://forgetrustee.kickstarttrustee.de/ontology/dtm-legal#LegalObligation" }`

### Tooltip

„Wählen Sie die rechtliche Grundlage nach DSGVO Art. 6 für die Datenweitergabe via Datentreuhand.“


---

## 2.2 Beschreibung der Verarbeitungsgrundlage

### Wizard-Frage

**Frage:** Warum trifft diese Verarbeitungsgrundlage zu?

### API

`POST /addComment`

`{   "ontologyURI": "https://www.example.de/ontology#",   "subjectURI": "https://www.example.de/ontology#DTForwarding",   "literal": "Weitergabe erfolgt zur Erfüllung gesetzlicher Meldepflichten.",   "lang": "de" }`

---

## 2.3 Einwilligung (Consent-Workflow, falls relevant)

### Wizard-Fragen (hierarchisch)

1. Wird eine Einwilligung eingeholt?
    
2. Allgemein oder individuell?
    
3. Wer initiiert den Workflow?
    
4. Wer gibt die Einwilligung (direkt / über Dateninhaber)?
    

### API-Objekte

- `dtm-legal:GeneralConsentWorkflow`
    
- `dtm-legal:IndividualConsentWorkflow`
    
- `generalConsentWorkflowInitiatedBy`
    
- `getsConsentDirectly`
    
- `getsConsentThroughDataOwner`
    

_(API-Calls analog zu vorheriger Antwort, vollständig kompatibel mit dtm-legal)_

---

# Seite: Technik

## Überschrift: Art der Datentreuhand
**Hilfetext:** 
Der Datengeber entscheidet fallbezogen, typischerweise per Einwilligung oder Freigabe, ob und in welchem Umfang eine Weitergabe erfolgt; der Datentreuhänder setzt diese Entscheidungen technisch und organisatorisch durch und vermittelt den Zugriff. Verwaltungstreuhand: Der Datentreuhänder entscheidet regel- und policybasiert im Rahmen gesetzlicher und vertraglicher Vorgaben, ob und wie Daten verwaltet oder weitergegeben werden; individuelle Einwilligungen sind dabei nicht der primäre Steuerungsmechanismus. Mehrwerttreuhand: Der Datentreuhänder erbringt zusätzlich wertschöpfende Leistungen (z. B. Aufbereitung, Analyse, Schutzmaßnahmen); die Governance folgt je nach Ausgestaltung entweder dem Zugangsprinzip (Entscheidung bei des Datengebers) oder dem Verwaltungsprinzip (regelbasierte Entscheidung durch den Datentreuhänder), in selbstbestimmter oder fremdbestimmter Variante.

### 3.1 Datentreuhandmodell (dtm-core) (TODO: Eventeull in "Akteure & Daten" schieben)

#### Wizard-Frage

**Frage:** Welche Oberklasse der Datentreuhand trifft zu?

#### API

`POST /newDataTrusteeModel`

Und

`POST /addSubtype`

**@RequestParam**
`{   "ontologyURI": "https://www.example.de/ontology#",   "subjectURI": "https://www.example.de/ontology#NewDataTrustModel",   "subtypeURI": "https://tool.kickstarttrustee.de/ontology/dtm-core#SelfManagedValueAddedDataTrustee" }`

Weiter Alternativen für subtype:
- https://tool.kickstarttrustee.de/ontology/dtm-core#ValueAddedDataTrustee (Mehrwerttreuhand)
	- https://tool.kickstarttrustee.de/ontology/dtm-core#SelfManagedValueAddedDataTrustee
	- https://tool.kickstarttrustee.de/ontology/dtm-core#ExternallyControlledValueAddedDataTrustee
- https://tool.kickstarttrustee.de/ontology/dtm-core#AccessDataTrustee
- https://tool.kickstarttrustee.de/ontology/dtm-core#AdministrativeDataTrustee

#### UI

einfache Chipauswahl (hierarchisch)
- Mehrwerttreuhand
	- Selbstbestimmte Mehrwerttreuhand
	- Fremdbestimmte Mehrwerttreuhand
- Zugangstreuhand
- Verwaltungstreuhand


#### langer Tooltip (deprecated)
Wähle das Modell nach der Hauptlogik, wer die Entscheidung über die Datenweitergabe trifft und welche Rolle der Datentreuhänder einnimmt: Zugangstreuhand bedeutet, dass die betroffene Partei fallbezogen entscheidet, ob und wie eine Weitergabe erfolgt – typischerweise über Einwilligungen (Consent) bzw. Freigaben; der Datentreuhänder setzt diese Entscheidungen um und vermittelt den Zugang. Verwaltungstreuhand bedeutet, dass der Datentreuhänder selbst entscheidet, ob und wie Daten verwaltet/weitergegeben werden – gebunden an gesetzliche Regeln und formale Vorgaben (nicht fallbezogen durch individuelle Consents gesteuert). Mehrwerttreuhand bedeutet, dass der Datentreuhänder zusätzlich weitergehende Funktionen anbietet (z. B. Aufbereitung, Auswertung, Schutzmaßnahmen) und die Governance dabei je nach Ausgestaltung entweder wie bei der Zugangstreuhand (Entscheidung bei der betroffenen Partei) oder wie bei der Verwaltungstreuhand (Entscheidung beim Datentreuhänder nach Regeln) organisiert sein kann.

---

## Überschrift: Technische Implementierung/Architektur

### 3.2 Technische Implementierung
**Hilfetext:**
Die technische Implementierung/Architektur beschreibt die Systemlandschaft, auf der der Datentreuhänder betrieben wird (z. B. zentralisiert, föderiert oder als Microservice-Architektur)

#### Wizard-Frage
**Frage:** Welche Architektur der Datentreuhand wird implementiert?

#### API
`POST /addRelation`

`{   "ontologyURI": "https://www.example.de/ontology#",   "subject_URI": "https://www.example.de/ontology#NewDataTrustModel",   "property_URI": "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#implementedOn",   "object_URI": "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#CentralisedArchitecture" }`

#### UI (TODO, nutze die Kategorien aus der Ontology)
Chipauswahl:
- Zentraler Router
- Peer-to-Peer
- Zentral
- Unklar

#### Tooltip
"Wählen Sie die technische Topologie der Datentreuhand. Zentral: ein System speichert/verarbeitet; Zentraler Router: ein Vermittler steuert/verteilt Datenflüsse; Peer-to-Peer: direkter Austausch zwischen Teilnehmern. Bei Unsicherheit: Unklar wählen und später präzisieren."

## Überschrift Datenfluss

**Hilfetext Datenfluss:**
Der Datenfluss beschreibt den Weg der Daten vom Eingang über die Aufbereitung bis zur (ggf.) Weitergabe in klar abgegrenzten Phasen; Ziel ist Transparenz darüber, welche Daten warum verarbeitet werden, wer verantwortlich ist, in welcher Häufigkeit dies geschieht und unter welchen Schutz- und Governance-Regeln dies erfolgt.

**Hilfetext Empfang:**
Der Empfang beschreibt, wie Daten in die Treuhandumgebung gelangen, typischerweise über definierte Techniken (API, Download oder analog) und mit einer angegebenen Frequenz (periodisch, kontinuierlich oder manuell); er wird auf ein Quellsystem bezogen und durch geeignete Sicherheitsmaßnahmen wie Transport‑ oder Ende‑zu‑Ende‑Verschlüsselung sowie Pseudonymisierung/Anonymisierung abgesichert.

**Hilfetext Verarbeitung:**
Die Verarbeitung umfasst die Aufbereitung der Daten (z. B. Bereinigung, Transformation, Aggregation) zur Sicherung von Qualität und Kompatibilität, die Auswertung (z. B. statistisch oder KI‑gestützt) sowie die Art der Speicherung und die Aufbewahrungsdauer. Dabei wird festgelegt, ob Daten zentral oder dezentral gespeichert werden, und wie lange sie vorgehalten werden (nur zur Übermittlung, mit definierten Aufbewahrungsregeln oder dauerhaft), einschließlich der passenden Schutzmaßnahmen für ruhende Daten.

**Hilfetext Weitergabe:**
Die Weitergabe bezeichnet die kontrollierte Bereitstellung von Daten an berechtigte Empfänger (z. B. via API oder Upload) mit festgelegter Häufigkeit; sie setzt Freigaben/Policies um, protokolliert Empfänger und Umfang des Zugriffs und stellt geeignete Schutzmaßnahmen während der Übertragung sicher.

### Phasen
Phasen, wie sie in der Ontologie hinterlegt sind.

- Data Reception (Empfang)
    
- Data Preparation
    
- Data Forwarding
    
- Data Storage
    
- Data Analysis
    

Für **jede Phase**:

#### 1. Phase erstellen (`/newIndividual`)

**@RequestBody**
`POST /newIndividual`

```json
{
  "ontologyURI": "https://www.example.de/ontology#",
  "classURI": "https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#DataReception",
  "individualName": "DTReception",
  "langSetting": "de"
}
```
Alternativ für `classURI`:
- https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#DataReception
- https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#DataPreparation
- https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#DataForwarding
- https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#DataStorage
- https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#DataAnalysis

#### 2. Technik erstellen (`/newIndividual`)
**@RequestBody**
`POST /newIndividual`

```json
{
  "ontologyURI": "https://www.example.de/ontology#",
  "classURI": "[Subklasse der Techniken der jeweiligen Phasen]",
  "individualName": "Test",
  "langSetting": "de"
}

```
Alternativ für `classURI`:
- Oberklasse: https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#ReceptionTechnique
	- https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#Analog
	- https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#Download
	- https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#API
- Oberklasse: https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#ProcessingTechnique
	- https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#Cleaning
	- https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#Transformation
	- https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#Aggregation
	- https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#Digitization
- Oberklasse: https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#ForwardingTechnique
	- https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#Upload
	- https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#ForwardingAPI
- Oberklasse: https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#StorageTechnique
	- https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#CentralStorage
	- https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#DecentralStorage
- Oberklasse: https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#AnalysisTechnique
	- https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#PrivacyEnhancingTechnology
	- https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#StatisticalAnalysis
	- https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#AIPoweredAnalysis

##### Für Empfang zusätzliche benutzerdefinierte Techniken
**@RequestBody** TODO

**Tooltip Sicherheitsmaßnahmen:**
- Tooltip „Name der Sicherheitsmaßnahme“: Prägnanter, eindeutiger Maßnahmenname, z. B. Zugriffskontrolle (RBAC/ABAC), Schlüsselmanagement (KMS/HSM), Confidential Computing (TEE/SGX), Tokenisierung, Sichere Mehrparteienberechnung (MPC), Homomorphe Verschlüsselung, Data Loss Prevention (DLP), unveränderliche Backups, Zero‑Trust‑Segmentierung, SIEM/IDS/IPS.
- Tooltip „Optionale Beschreibung der Sicherheitsmaßnahme“: Kurze Charakterisierung mit Zweck/Umsetzung/Scope, z. B. Schlüsselrotation monatlich via KMS, Tokenisierung sensibler Felder, Analysen in TEE ausgeführt, DLP‑Regeln gegen Exfiltration, Zero‑Trust für Partnerzugriffe, SIEM‑Korrelation von Ereignissen.

##### Für Verarbeitung zusätzliche benutzerdefinierte Techniken
**@RequestBody Datenaufbereitung** TODO

**Tooltip Datenaufbereitung:**
- Tooltip „Name der Aufbereitungstechnik“: Prägnanter Technikname, z. B. Normalisierung – Z‑Score/Min‑Max, Imputation – MICE/kNN, Dublettenabgleich – Fingerprint/Hash, Enrichment – ICD‑10/LOINC‑Mapping, Textaufbereitung – Tokenisierung/Stemming, Zeitreihen – Resampling/Glättung, Feature Engineering – abgeleitete Kennzahlen.
- Tooltip „Optionale Beschreibung der Aufbereitungstechnik“: Kurze Charakterisierung mit Zweck/Verfahren, z. B. Standardisierung von Einheiten (mg/dL→mmol/L), Auffüllen fehlender Werte (MICE), Entfernen von Dubletten per Hash‑Vergleich, Anreicherung um ICD‑10 Codes, Tokenisierung klinischer Texte, Resampling auf 5‑Min‑Intervalle.

**@RequestBody Datenauswertung** TODO

**Tooltip Datenauswertung:**
- Weitere Technik zur Datenauswertung: Prägnanter, eindeutiger Technikname; Beispiele: „Zeitreihenprognose – ARIMA/Prophet“, „Graphanalyse – Community Detection“, „Kausalinferenz – Difference‑in‑Differences“, „Anomalieerkennung – Isolation Forest“, „Themenmodellierung – LDA“, „Überlebensanalyse – Cox PH“.
- Optionale Beschreibung der Auswertungstechnik: Kurze Charakterisierung mit Methode/Framework/Zweck, z. B. „Python/Scikit-Learn, Klassifikation von Befunden“, „R/GLM, Regressionsanalyse“, „DP-SGD für anonymisierte Trainingsläufe“.

##### Für Weitergabe zusätzliche benutzerdefinierte Techniken
**@RequestBody** TODO

**Tooltip Sicherheitsmaßnahmen:**
- Tooltip „Name der Sicherheitsmaßnahme“: Prägnanter, eindeutiger Maßnahmenname, z. B. Zugriffskontrolle (RBAC/ABAC), Schlüsselmanagement (KMS/HSM), Confidential Computing (TEE/SGX), Tokenisierung, Sichere Mehrparteienberechnung (MPC), Homomorphe Verschlüsselung, Data Loss Prevention (DLP), unveränderliche Backups, Zero‑Trust‑Segmentierung, SIEM/IDS/IPS.
- Tooltip „Optionale Beschreibung der Sicherheitsmaßnahme“: Kurze Charakterisierung mit Zweck/Umsetzung/Scope, z. B. Schlüsselrotation monatlich via KMS, Tokenisierung sensibler Felder, Analysen in TEE ausgeführt, DLP‑Regeln gegen Exfiltration, Zero‑Trust für Partnerzugriffe, SIEM‑Korrelation von Ereignissen.
    
#### 3. Phase ↔ Technik verknüpfen (`/addRelation`)

- Nur Datentreuhänder:
	- Data Reception
	- Data Preparation
	- Data Forwarding
- Alle Akteure:
	- Data Storage
	- Data Analysis

`POST /addRelation`
**@RequestParam**
`{   "ontologyURI": "https://www.example.de/ontology#" }`

**@RequestBody**
   ` {"subject_URI": "[Phase]",   "property_URI": "[passende Property]",   "object_URI": "[jeweilige Technik]", "relationBetweenIndividuals": true }`

- Phasenproperty
	- Data Reception: `https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#hasReceptionTechnique`
	- Data Preparation: ``https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#hasPreparationTechnique``
	- Data Forwarding: `https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#hasForwardingTechnique`
	- Data Storage: `https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#hasStorageTechnique`
	- Data Analysis: `https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#hasDataAnalysisTechnique`
	
#### 4. Trustee ↔ Phase verknüpfen

`POST /addRelation`
**@RequestParam**
`{   "ontologyURI": "https://www.example.de/ontology#" }

**@RequestBody**
   `` {"subject_URI": "[DataTrustee]",   "property_URI": "[passende Property]",   "object_URI": "[jeweilige Phase]", "relationBetweenIndividuals": true }

- Verknüpfungsproperty
	- Data Reception: https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#triggersReception
	- Data Preparation: https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#triggersPreparation
	- Data Forwarind: https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#triggersForwarding
	- Data Storage: https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#triggersStorage
	- Data Analysis:
    
#### 5. Zusatzattribute (Frequenz, Systeme, Retention)

##### Frequenz
**Für Datenempfang und Datenweitergabe Frequenz erfragen**
**Frage:** Wie oft werden Daten empfangen/weitergegeben?

**API**

`POST /addRelation`
**@RequestParam**
`{   "ontologyURI": "https://www.example.de/ontology#" }`

**@RequestBody**
   ` {"subject_URI": "[DTReception/DTForwarding]",   "property_URI": "[passende Property]",   "object_URI": "[jeweilige Dauer]", "relationBetweenIndividuals": true }`

- property_URI:
	- für Empfang: https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#hasReceptionFrequency
	- für Forwarding: https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#hasForwardingFrequency

- object_URI (Skos Concept)
	- Manual: https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#Manual
	- Continuous: https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#Continuous
	- Periodic: https://forgetrustee.kickstarttrustee.de/ontology/dtm-data#Periodic

**UI**
Einfache Chipauswahl:
- Kontinuierlich
- Periodisch
- Manuell
- Unklar -> kein API Call

**Tooltip**
"Geben Sie an, wie oft Daten empfangen bzw. weitergegeben werden. Kontinuierlich = laufender Stream; Periodisch = feste Intervalle (z. B. täglich/wöchentlich); Manuell = bei Bedarf. Wenn noch unklar, dies wählen und später präzisieren."

##### System
**Für Datenempfang und Datenweitergabe Frequenz erfragen**
**Frage:** Wie oft werden Daten empfangen/weitergegeben?

**API**

[TODO]

**UI**
- Textinput

**Tooltip**
- **Quellsystem (Empfang):** Eindeutiger Systemname mit Quellenrolle, z. B. „EPA-Quellsystem“, „Sensor-Hub Edge 01“.
- **Zielsystem (Weitergabe):** Eindeutiger Systemname mit Zielrolle, z. B. „Forschungsdatenplattform“, „Analyse-Cluster“, „Data Warehouse Klinikum“

---

# Seite: Finanzen
**Hilfetext:**
Finanzielle Aspekte von Datentreuhändern umfassen die sektorale Domäne (z. B. Gesundheit, Energie, Mobilität), die rechtliche Geschäftsform (z. B. GmbH, gGmbH, e.V., Stiftung), Förderquellen und Finanzierer (etwa BMBF, EU, BMWK) sowie die Preis- und Entgeltmodelle: angeboten werden typischerweise Kostenlos, Freemium, Flatrate, Pay‑per‑Use, Zugangs‑, Service‑ oder Supportgebühren; tatsächlich genutzt werden je nach Rolle und Zusammenarbeit unterschiedliche Modelle durch die Zwischeninstanz und den Datennutzer (z. B. Flatrate für Plattformbetrieb, Pay‑per‑Use für Datenabrufe). Diese Ausgestaltung prägt Kostenverteilung, Wirtschaftlichkeit und Anreizstruktur der Teilnahme.

## 4. Finanzielles (dtm-business) (TODO Januar)

## 4.1 Domäne

`POST /addRelation`

`hasDomain`

## 4.2 Bezahlmodelle

`POST /addRelation`

`offersPayModel`, `usesPayModel`

## 4.3 Geschäftsform & Finanzierung

`POST /addRelation`

`hasBusinessForm`, `hasFundingSource`

# 5 Ziele (dtm-business)

`POST /addRelation`

`hasDataTrusteeGoal`, `hasUsageGoal`

### Tooltip

„Ziele beschreiben Motivation und erwarteten Mehrwert der Datentreuhand.“

---

# Seite: Ziele
**Hilfetext:**
Datentreuhandziele setzen die strategische Leitlinie, wie Datensouveränität und digitale Souveränität, Unterstützung von Wissenschaft und Forschung, wirtschaftliche Verwertung, fairer Wettbewerb und Compliance, und bestimmen die Governance für Zugang, Verarbeitung und Weitergabe. Die Zwischeninstanz verfolgt operativ die gleichen Nutzungsziele wie der Datennutzer. Diese sind Zugang zu Daten, Datenteilen, Qualitätsmanagement und Datenanalyse, jedoch mit Schwerpunkt auf policykonformer Umsetzung, Transparenz, Nachweisbarkeit und Schutz. Der Datennutzer verfolgt dieselben Ziele zweckgebunden, ergebnisorientiert und innerhalb freigegebener Zugriffe, dokumentierter Datenflüsse und definierten Aufbewahrungsregeln.

[TODO]