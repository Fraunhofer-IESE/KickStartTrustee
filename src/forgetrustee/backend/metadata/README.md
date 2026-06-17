# Metadata

Die Dateien in diesem Ordner beschreiben die Metadaten des Forgetrustee-Backends für die Wizard- und UI-Darstellung.

Sie definieren insbesondere:

- welche Module im Metadata-Modell existieren,
- wie diese Module strukturiert sind,
- welche Sections, Subsections und Felder angezeigt werden,
- welche Feldtypen und Optionen verfügbar sind,
- welche Sichtbarkeits- und Pflichtregeln gelten,
- und welche sprachabhängigen UI-Texte dazu ausgeliefert werden.

## Kurzbeschreibung

Die Metadaten sind in zwei Schichten getrennt:

- **technisches Schema**: Struktur, Typen, Optionen, Validierungs- und Sichtbarkeitslogik
- **i18n-Texte**: Labels, Tooltips, Hilfetexte, Platzhalter und Beispiele

Beim Laden führt das Backend beide Schichten zusammen. Dadurch bleibt die technische Struktur stabil, während die UI-Texte sprachabhängig gepflegt werden können.

## Technik

Die Metadata-Verarbeitung ist Teil des Spring-Boot-Backends.

Relevante technische Bausteine im Projekt:

- **Java 25**
- **Spring Boot 4.0.5**
- **Jackson** für JSON-Deserialisierung und Merging
- **Spring Validation / Jakarta Validation** zur Anreicherung von Feld-Constraints
- **OpenAPI / springdoc** zur Bereitstellung der API-Beschreibung

Für die Metadaten selbst sind besonders diese Klassen wichtig:

- `de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.MetadataService`
- `de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.MetadataConsistencyCheckService`
- `de.fraunhofer.iese.forgetrustee.demonstrator.backend.config.BackendProperties`
- DTOs unter `src/main/java/de/fraunhofer/iese/forgetrustee/demonstrator/backend/dto/metadata/`

## Ordnerstruktur

Standardmäßig erwartet das Backend die Metadaten unter dem in `backend.metadata.base-path` konfigurierten Verzeichnis.

Mit der aktuellen Default-Konfiguration aus `src/main/resources/application.yml` ist das:

- `metadata/`

Erwartete Struktur:

```text
metadata/
├─ dtm-ids.json
├─ dtm-schema/
│  ├─ dtm.json
│  └─ <module>.json
└─ dtm-i18n/
   └─ <lang>/
      └─ <module>.json
```

### Bedeutung der Dateien

- `dtm-ids.json`
  - Zuordnung von Java-Request-DTOs zu `moduleId`
  - wird zur Anreicherung von `minLength`, `maxLength` und teilweise `required` genutzt
- `dtm-schema/<module>.json`
  - technisches Schema pro Modul
- `dtm-schema/dtm.json`
  - optionale Modulreihenfolge
- `dtm-i18n/<lang>/<module>.json`
  - sprachabhängige Texte pro Modul
- optional: `dtm-schema.json`
  - kombinierte Sammeldatei statt einzelner Moduldateien

## Wie die Verarbeitung funktioniert

Beim Zugriff auf die Metadata-API läuft die Verarbeitung grob so ab:

1. Laden des technischen Schemas aus `dtm-schema/`
2. Laden der sprachabhängigen Texte aus `dtm-i18n/<lang>/`
3. Optionaler Strukturabgleich zwischen Schema und i18n
4. Merge von Schema und i18n anhand von
   - `moduleId`
   - Section-Keys
   - Field-Keys
   - Child-Field-Keys
   - Option-`value`
5. Anreicherung mit Constraints aus den zugeordneten Java-DTOs über `dtm-ids.json`

Wichtige Hinweise:

- Jede `moduleId` muss stabil und eindeutig sein.
- Schema und i18n müssen strukturell zueinander passen.
- Optionseinträge werden über `value` zusammengeführt.
- Für Child-Felder innerhalb eines Feldes ist im aktuellen Service effektiv nur eine Ebene vorgesehen.

## Konfiguration

Die relevanten Properties liegen unter dem Prefix `backend.metadata`.

Aktuell bekannte Konfigurationsschlüssel:

- `backend.metadata.base-path`
  - Pfad zum Metadata-Basisordner
- `backend.metadata.domain-base-package`
  - optionales Java-Basispaket zur DTO-Auflösung für Constraint-Anreicherung
- `backend.metadata.consistency-check-enabled`
  - aktiviert den Strukturabgleich zwischen Schema und i18n
- `backend.metadata.preload-on-startup`
  - vorbereitet für Vorladen der Metadaten beim Start

Beispiel aus `application.yml`:

```yaml
backend:
  metadata:
    base-path: "metadata/"
    consistency-check-enabled: true
```

## Lokaler Start

Die Metadaten selbst werden nicht separat gestartet, sondern mit dem Backend ausgeliefert.

### Voraussetzungen

- Java 25
- Maven
- gültige Backend-Konfiguration in `src/main/resources/application.yml` oder per Environment/CLI-Overrides

### Backend lokal starten

Im Ordner `backend/`:

```bat
mvn spring-boot:run
```

Das Backend ist laut aktueller `application.yml` standardmäßig unter Port `8080` konfiguriert.

### Alternative: JAR bauen und starten

```bat
mvn clean package
java -jar target\backend-0.0.1-SNAPSHOT.jar
```

## Container-Hinweis

Es gibt ein `Dockerfile` für das Backend.

Dabei sind für Metadaten und Daten diese Volumes vorgesehen:

- `${APP_HOME}/data`
- `${APP_HOME}/metadata`


## Nutzung über die API

Die Metadaten werden über den `MetadataController` bereitgestellt.

Wichtige Endpunkte:

- `GET /api/metadata`
  - liefert das Modell mit Modulreihenfolge
- `GET /api/metadata/modules`
  - liefert alle Module
- `GET /api/metadata/modules/summary`
  - liefert eine kompakte Modulübersicht
- `GET /api/metadata/modules/{moduleId}`
  - liefert ein einzelnes Modul
- `GET /api/metadata/modules/{moduleId}/fields/{fieldPath}`
  - liefert Feldmetadaten für einen konkreten Feldpfad
- `GET /api/metadata/module-ids`
  - liefert die DTO-zu-`moduleId`-Zuordnung

Beispiele:

```text
GET /api/metadata?lang=de
GET /api/metadata/modules/core?lang=de
GET /api/metadata/modules/core/fields/dataTrusteeName?lang=de
```

## Pflegehinweise

- Neue Module immer in Schema **und** i18n anlegen
- Struktur in beiden Schichten exakt spiegeln
- `dtm.json` ergänzen, wenn die Reihenfolge relevant ist
- `dtm-ids.json` ergänzen, wenn das Modul zu einem Java-DTO gehört

## Weiterführende Dokumentation

- Siehe `METADATA_AUTHORING.md` für eine ausführliche Anleitung zum Aufbau einzelner Metadata-Objekte.