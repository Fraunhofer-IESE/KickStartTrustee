# Anleitung zum Anlegen von Metadata (YAML)

Diese Anleitung beschreibt, wie die Metadata-Dateien im Backend aufgebaut sein muessen.

Sie basiert auf den aktuellen DTOs unter `src/main/java/.../dto/metadata/` und auf dem Ladevorgang in `MetadataService`.

## 1. Wofür die Dateien verwendet werden

Die Metadaten beschreiben:

- welche Module es gibt,
- welche Sections/Subsections darin vorkommen,
- welche Felder ein Modul enthält,
- welchen Typ ein Feld hat,
- welche Optionen auswählbar sind,
- wann etwas sichtbar oder verpflichtend ist,
- und welche Texte in der UI angezeigt werden.

Wichtig: Die Metadaten sind aktuell **in technischem Schema und sprachabhängigen UI-Texten getrennt**.

---

## 2. Aktuelle Ordnerstruktur

Unter `backend/metadata/` wird aktuell dieses Layout verwendet:

```text
metadata/
├─ dtm-schema/
│  ├─ dtm.yaml
│  ├─ core.yaml
│  ├─ data.yaml
│  ├─ objectives.yaml
│  ├─ implementation.yaml
│  └─ business.yaml
└─ dtm-i18n/
   └─ de/
      ├─ core.yaml
      ├─ data.yaml
      ├─ objectives.yaml
      ├─ implementation.yaml
      └─ business.yaml
```

### Bedeutung der Dateien

- `dtm-schema/*.yaml`
  - technisches Schema
  - enthält Struktur, Feldtypen, Optionen, Regeln und Sichtbarkeit
- `dtm-i18n/<lang>/*.yaml`
  - sprachabhängige Texte
  - enthält Labels, Tooltips, Help-Texte, Beispiele usw.
- `dtm-schema/dtm.yaml`
  - definiert die Reihenfolge der Module
- `dtm-schema/<module>.yaml` mit `requestDtoClassName`
  - Zuordnung von Request-DTO-Klassenname zu `moduleId`
  - wird benutzt, um z. B. `@Size` und `@Schema(requiredMode=...)` aus den Request-DTOs in die Metadata einzureichern

---

## 3. Welche Formate unterstuetzt werden

Der `MetadataService` unterstuetzt fuer Moduldateien und Modellindex:

- `.yaml`
- `.yml`

Kurzregel fuer den Teamalltag:

- Neue Dateien direkt als YAML anlegen
- Dateiendung konsistent auf `.yaml` halten
- Struktur und Inhalt bei Aenderungen getrennt halten

Beispiel:

- `dtm-schema/core.yaml` enthaelt ein einzelnes `MetadataModuleDto`
- `dtm-i18n/de/core.yaml` enthaelt ebenfalls ein einzelnes `MetadataModuleDto`

---

## 4. Grundprinzip: Schema und i18n werden zusammengeführt

`MetadataService` lädt zuerst das **technische Schema** und merged danach die **i18n-Datei** darüber.

Das bedeutet:

- Im **Schema** stehen Struktur und Logik.
- In **i18n** stehen die anzuzeigenden Texte.
- Beide Dateien müssen dieselbe Struktur spiegeln:
  - gleiche `moduleId`
  - gleiche Section-Keys
  - gleiche Field-Keys
  - gleiche Child-Field-Keys
  - gleiche Option-`value`

Wenn die Struktur nicht zusammenpasst, protokolliert `MetadataConsistencyCheckService` Warnungen.

---

## 5. Aufbau eines Moduls (`MetadataModuleDto`)

Ein Modul ist das oberste Objekt einer Datei.

Beispiel:

```json
{
  "moduleId": "core",
  "order": 10,
  "fields": {},
  "sections": {
    "datatrustee": {
      "fields": {
        "dataTrusteeName": {
          "name": "dataTrusteeName",
          "type": "TEXT"
        }
      }
    }
  }
}
```

### Relevante Felder auf Modulebene

- `moduleId`  
  Eindeutige technische ID des Moduls. Muss stabil bleiben.

- `order`  
  Optionale numerische Sortierung. Niedrigere Werte kommen zuerst.

- `label`, `heading`, `menuPoint`, `tooltip`, `helpText`, `callToAction`, `examples`  
  Typische UI-Texte. Diese gehören in der Praxis in die i18n-Datei.

- `hints`  
  Zusätzliche Hinweise auf Modulebene.

- `fields`  
  Top-Level-Felder des Moduls.

- `sections`  
  Fachliche Gruppierung innerhalb eines Moduls.

### Empfehlung

Im aktuellen Bestand werden die meisten Felder innerhalb von `sections` gepflegt. Top-Level-`fields` können aber ebenfalls verwendet werden.

---

## 6. Aufbau einer Section (`MetadataSectionDto`)

Eine Section liegt innerhalb von `sections` oder `subsections`.

Beispiel:

```json
"legalBasis": {
  "heading": "Verarbeitungsgrundlage",
  "fields": {
    "processingBases": {
      "name": "processingBases",
      "type": "TAGS"
    }
  },
  "subsections": {
    "consentTypeSubSection": {
      "fields": {
        "consentType": {
          "name": "consentType",
          "type": "TAGS"
        }
      }
    }
  }
}
```

### Relevante Felder

- `id`  
  Im DTO vorhanden, in den JSON-Dateien wird praktisch meist der Map-Key verwendet.  
  **Empfehlung:** Wenn gesetzt, sollte `id` identisch zum JSON-Key sein.

- `label`, `heading`, `tooltip`, `helpText`, `callToAction`, `examples`  
  UI-Texte.

- `hints`  
  Hinweise auf Section-Ebene.

- `visibleBy`  
  Regeln, wann die Section sichtbar sein soll.

- `fields`  
  Felder innerhalb der Section.

- `subsections`  
  Weitere Untergliederungen. Diese können rekursiv verschachtelt werden.

---

## 7. Aufbau eines Felds (`MetadataFieldDto`)

Ein Feld ist der zentrale Baustein in `fields`.

Beispiel:

```json
"dataTrusteeName": {
  "name": "dataTrusteeName",
  "type": "TEXT",
  "required": true,
  "visible": true
}
```

### Wichtige technische Felder

- `name`  
  Technischer Feldname. Sollte dem JSON-Key entsprechen.

- `type`  
  Feldtyp. Zulässige Werte sind aktuell:
  - `TEXT`
  - `LONGTEXT`
  - `SELECT`
  - `TAGS`
  - `TAGS_WITH_DESCRIPTION_LIST`
  - `TAGS_WITH_TWO_FIELDS`
  - `TAGS_WITH_TEXT_AND_DESCRIPTION_LIST`
  - `TEXT_WITH_DESCRIPTION`
  - `TEXT_WITH_DESCRIPTION_LIST`

- `multiple`  
  Ob mehrere Werte zulässig sind.

- `required`  
  Ob das Feld immer verpflichtend ist. Sollte über entsprechende Annotation gesetzt werden. Siehe [12. Besonderheiten durch automatische Anreicherung](#12-besonderheiten-durch-automatische-anreicherung)

- `requiredBy`  
  Regeln, wann das Feld verpflichtend wird.

- `visible`  
  Statische Sichtbarkeit.

- `visibleBy`  
  Regeln, wann das Feld sichtbar ist.

- `disabled`, `readonly`  
  UI-/Interaktionssteuerung.

- `allowUnknown`  
  Ob neben vorgegebenen Optionen auch der Wert `AMBIGUOUS` (Unklar) erlaubt ist.

- `allowNoAnswer`  
  Ob neben vorgegebenen Optionen auch der Wert `NONE` (Keine Angaben) erlaubt ist.

- `allowUnknownLabel` / `allowNoAnswerLabel`  
  Vom Backend aus i18n abgeleitete Anzeigetexte fuer die vom Frontend erzeugten Spezialwerte `AMBIGUOUS` und `NONE`. Diese Werte werden nicht als regulaere `options` materialisiert.

- `minLength`, `maxLength`  
  Längenbeschränkung. Sollte über entsprechende Anootation gesetzt werden. Siehe [12. Besonderheiten durch automatische Anreicherung](#12-besonderheiten-durch-automatische-anreicherung)

- `options`  
  Auswahlmöglichkeiten.

- `fields`  
  Child-Felder für komplexe Feldtypen.

- `allowedValuesMode`, `allowedValuesBy`  
  Zusätzliche Einschränkung erlaubter Werte über Regeln.

### Wichtig zu `fields` innerhalb eines Felds

`MetadataFieldDto.fields` enthält **Child-Felder** (`MetadataFieldChildDto`).

Diese sind für komplexe Typen gedacht, z. B.:

- `TEXT_WITH_DESCRIPTION`
- `TEXT_WITH_DESCRIPTION_LIST`
- `TAGS_WITH_TEXT_AND_DESCRIPTION_LIST`

Wichtig: Im aktuellen Backend wird für Child-Felder **nur eine Ebene** unterstützt.

Also erlaubt:

```json
"dataCategory": {
  "name": "dataCategory",
  "type": "TEXT_WITH_DESCRIPTION",
  "fields": {
    "dataCategoryName": {
      "name": "dataCategoryName",
      "type": "TEXT"
    },
    "dataCategoryDescription": {
      "name": "dataCategoryDescription",
      "type": "LONGTEXT"
    }
  }
}
```

Nicht vorgesehen ist eine weitere Verschachtelung innerhalb dieser Child-Felder.

---

## 8. Optionen (`MetadataFieldOption`)

Für Auswahlfelder werden Optionen über `options` gepflegt.

Beispiel:

```json
"options": [
  {
    "value": "INDIVIDUAL"
  },
  {
    "value": "ORGANIZATION"
  }
]
```

### Felder einer Option

- `value`  
  Technischer Wert. Muss stabil sein.

- `label`  
  Sichtbarer Text. Gehört normalerweise in i18n.

- `tooltip`  
  Optionaler Hilfetext zur Option.

- `placeholder`  
  Optionaler Placeholder für Zusatzangaben.

- `disableAdditionalDetails`  
  Wenn `true`, sollen Zusatzfelder/Details für diese Option deaktiviert sein.

- `extraArea`  
  Optionaler Marker fuer das Frontend. Wenn `true`, darf diese Option bzw. ihr zusaetzlicher Detailbereich in einem optisch abgesetzten Extra-Bereich dargestellt werden.

### Wichtig

Die Option-`value` muss in Schema und i18n identisch sein, weil die Merge-Logik Optionen über `value` zusammenführt.

---

## 9. Hinweise (`MetadataHintDto`)

Hints können auf Modul- oder Section-Ebene gepflegt werden.

Schema-Beispiel:

```json
"hints": [
  {
    "name": "objectivesGeneralHint"
  }
]
```

i18n-Beispiel:

```json
"hints": [
  {
    "name": "objectivesGeneralHint",
    "text": "Alle Angaben sind optional, sollten für ein vollständiges Modell aber ausgefüllt werden."
  }
]
```

### Felder

- `name`  
  Technischer Schlüssel des Hinweises.

- `text`  
  Anzuzeigender Text.

- `visibleBy`  
  Optionale Regeln für die Sichtbarkeit des Hinweises.

Wichtig: Hints werden beim Mergen über `name` zusammengeführt.

---

## 10. Regeln für Sichtbarkeit, Pflicht und erlaubte Werte

Drei Regeltypen haben aktuell denselben technischen Aufbau:

- `VisibilityRule`
- `RequiredRule`
- `AllowedValuesRule`

Beispiel:

```json
{
  "name": "processingBases",
  "operator": "IN",
  "value": "CONSENT"
}
```

### Felder

- `name`  
  Name des steuernden Felds.

- `operator`  
  Aktuell unterstützte Operatoren:
  - `EQUALS`
  - `NOT_EQUALS`
  - `IS_TRUE`
  - `IS_FALSE`
  - `NOT_EMPTY`
  - `EXISTS`
  - `IN`
  - `MATCHES`

- `value`  
  Optionaler Vergleichswert.

### Typische Beispiele

#### Feld nur sichtbar, wenn ein anderes Feld `true` ist

```json
"visibleBy": [
  {
    "name": "rightsHolderIsRepresented",
    "operator": "EQUALS",
    "value": "true"
  }
]
```

#### Feld nur erforderlich, wenn Option `CONSENT` ausgewählt wurde

```json
"requiredBy": [
  {
    "name": "processingBases",
    "operator": "IN",
    "value": "CONSENT"
  }
]
```

---

## 11. Was gehört ins Schema und was in i18n?

### Schema (`dtm-schema/...`)

Hier gehören vor allem hinein:

- `moduleId`
- `order`
- `fields`, `sections`, `subsections`
- `name`, `type`
- `multiple`
- `required`, `requiredBy`
- `visible`, `visibleBy`
- `disabled`, `readonly`
- `allowUnknown`
- `allowNoAnswer`
- `options[].value`
- `options[].disableAdditionalDetails`
- `minLength`, `maxLength`
- `allowedValuesMode`, `allowedValuesBy`
- technische `hints[].name`

### i18n (`dtm-i18n/<lang>/...`)

Hier gehören vor allem hinein:

- `label`
- `heading`
- `menuPoint`
- `tooltip`
- `helpText`
- `callToAction`
- `placeholder`
- `examples`
- `hints[].text`
- `options[].label`
- `options[].tooltip`
- `options[].placeholder`

### Praktische Regel

Wenn ein Feld die **Struktur oder Logik** bestimmt, gehört es ins Schema.  
Wenn ein Feld nur den **Anzeigetext** betrifft, gehört es in i18n.

---

## 12. Besonderheiten durch automatische Anreicherung

`MetadataService` reichert Metadaten zusätzlich aus den Java-Request-DTOs an, wenn im Schema je Modul `requestDtoClassName` gepflegt ist.

Dabei können automatisch ergänzt werden:

- `minLength` / `maxLength` aus `@Size`
- `required` aus `@Schema(requiredMode = ...)`

### Voraussetzung

Die Zuordnung erfolgt direkt im jeweiligen Schema-Modul, z. B.:

```yaml
moduleId: core
requestDtoClassName: DataTrusteeWizardCoreRequestDto
...
```

### Empfehlung

- Wenn Constraints bereits im DTO sauber gepflegt sind, können `minLength`, `maxLength` und teilweise `required` im Schema leer bleiben.
- Wenn Metadaten bewusst vom DTO abweichen sollen, können die Werte explizit im Schema gesetzt werden.

---

## 13. Minimales Beispiel für ein neues Modul

### 13.1 Schema-Datei `dtm-schema/example.json`

```json
{
  "moduleId": "example",
  "order": 60,
  "fields": {},
  "sections": {
    "general": {
      "fields": {
        "exampleName": {
          "name": "exampleName",
          "type": "TEXT",
          "required": true
        },
        "exampleType": {
          "name": "exampleType",
          "type": "TAGS",
          "multiple": false,
          "allowUnknown": true,
          "options": [
            {
              "value": "A"
            },
            {
              "value": "B"
            }
          ]
        },
        "exampleDetail": {
          "name": "exampleDetail",
          "type": "TEXT",
          "visibleBy": [
            {
              "name": "exampleType",
              "operator": "EQUALS",
              "value": "B"
            }
          ]
        }
      }
    }
  }
}
```

### 13.2 i18n-Datei `dtm-i18n/de/example.json`

```json
{
  "moduleId": "example",
  "label": "Beispielmodul",
  "heading": "Beispiel",
  "menuPoint": "Beispiel",
  "fields": {},
  "sections": {
    "general": {
      "heading": "Allgemein",
      "helpText": "Beschreibt ein minimales Beispielmodul.",
      "fields": {
        "exampleName": {
          "label": "Name",
          "placeholder": "Bitte Namen eingeben"
        },
        "exampleType": {
          "label": "Typ",
          "options": [
            {
              "value": "A",
              "label": "Typ A"
            },
            {
              "value": "B",
              "label": "Typ B"
            }
          ]
        },
        "exampleDetail": {
          "label": "Zusatzinformation",
          "helpText": "Nur relevant, wenn Typ B ausgewählt wurde."
        }
      }
    }
  }
}
```

---

## 14. Vorgehen beim Anlegen eines neuen Moduls

### Pflichtschritte

1. Neue Schema-Datei unter `dtm-schema/` anlegen
2. Passende i18n-Datei unter `dtm-i18n/de/` anlegen
3. `moduleId` in beiden Dateien identisch setzen
4. Struktur in beiden Dateien identisch spiegeln
5. Modul in `dtm-schema/dtm.yaml` ergänzen
6. Falls das Modul zu einem Java-Request-DTO gehört: `requestDtoClassName` im Schema-Modul ergänzen

### Checkliste für Konsistenz

- [ ] Dateiname und `moduleId` passen zusammen
- [ ] Alle Section-Keys existieren in Schema und i18n
- [ ] Alle Field-Keys existieren in Schema und i18n
- [ ] Alle Child-Field-Keys existieren in Schema und i18n
- [ ] Alle Optionen haben dieselbe `value` in Schema und i18n
- [ ] Regeln (`visibleBy`, `requiredBy`) referenzieren existierende Feldnamen
- [ ] Bei komplexen Feldern ist nur eine Child-Ebene unter `fields` genutzt

---

## 15. Häufige Fehler

### Falsche oder fehlende Spiegelung zwischen Schema und i18n

Beispiel: Ein Field existiert nur im Schema, aber nicht in i18n.  
Folge: Es gibt Warnungen im Konsistenzcheck und in der UI fehlen Texte.

### Unterschiedliche Option-Werte

Beispiel:

- Schema: `"value": "PUBLIC_INTEREST"`
- i18n: `"value": "PUBLICINTEREST"`

Folge: Optionstexte werden nicht korrekt gemerged.

### JSON-Key und `name` passen nicht zusammen

Technisch wird häufig über den Map-Key gearbeitet, logisch aber auch über `name`.  
Empfehlung: Immer identisch halten.

### Zu tiefe Feldverschachtelung

Child-Felder unter `MetadataFieldDto.fields` sind nur für **eine Ebene** gedacht.

### Fehlende Zuordnung in `requestDtoClassName`

Dann können Validierungsinformationen aus den DTOs nicht automatisch angereichert werden.

---

## 16. Kurzreferenz

### Modul

```json
{
  "moduleId": "...",
  "order": 10,
  "label": "...",
  "heading": "...",
  "menuPoint": "...",
  "tooltip": "...",
  "helpText": "...",
  "callToAction": "...",
  "examples": [],
  "hints": [],
  "fields": {},
  "sections": {}
}
```

### Section

```json
{
  "id": "...",
  "label": "...",
  "heading": "...",
  "tooltip": "...",
  "helpText": "...",
  "callToAction": "...",
  "examples": [],
  "hints": [],
  "visibleBy": [],
  "subsections": {},
  "fields": {}
}
```

### Feld

```json
{
  "name": "...",
  "label": "...",
  "heading": "...",
  "placeholder": "...",
  "tooltip": "...",
  "helpText": "...",
  "callToAction": "...",
  "examples": [],
  "minLength": 0,
  "maxLength": 100,
  "type": "TEXT",
  "multiple": false,
  "required": false,
  "requiredBy": [],
  "disabled": false,
  "readonly": false,
  "allowUnknown": false,
  "allowNoAnswer": false,
  "options": [],
  "visible": true,
  "visibleBy": [],
  "allowedValuesMode": "UNION",
  "allowedValuesBy": [],
  "fields": {}
}
```

---

## 17. Empfehlung für die tägliche Pflege

Für neue oder geänderte Metadaten ist diese Reihenfolge am sichersten:

1. Zuerst das technische Schema anlegen
2. Danach die deutsche i18n-Datei spiegeln
3. Danach `dtm.json` und ggf. `dtm-ids.json` ergänzen
4. Danach prüfen, ob alle Keys, Namen und Optionen exakt zusammenpassen