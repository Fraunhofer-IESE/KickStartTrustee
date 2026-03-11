# Umsetzung von Datengeberrechten und Datentreuhänderpflichten

> **Hinweis:** Dieser Baustein basiert vollständig auf den fachlichen, technischen und rechtlichen Ergebnissen des Projekts *Bridge-Up – Benutzerfreundliche und rechtskonforme Implementierung von Datengeberrechten und Datentreuhänderpflichten*. Er fasst diese Ergebnisse zusammen und stellt sie so dar, dass sie als eigenständiger Baustein neben dem Kapitel **Sicherheits- und Datenschutzanforderungen** verwendet werden können.

Während im Baustein **Sicherheits- und Datenschutzanforderungen** beschrieben wird, welche Sicherheits- und Datenschutzziele ein Datentreuhänder (DTH) erreichen muss und welche BSI-Bausteine dafür relevant sind, konzentriert sich dieser Baustein darauf, wie insbesondere die datenschutzrechtlichen Schutzziele **Transparenz** und **Intervenierbarkeit** sowie die dort beschriebenen Daten-Assets (z. B. „Berechtigungen und Einwilligungen“, „Nutzungs- und Protokolldaten“, „Datenschutzdaten“, „Geschäftsgeheimnisse“) in einem konkreten Datentreuhandmodell umgesetzt werden können.

## Zielsetzung dieses Bausteins

Ziel dieses Bausteins ist es, ein **umsetzbares Zielbild** für Datentreuhänder bereitzustellen, das folgende Aspekte abdeckt:

1. **Datenschutz-Cockpits** als zentrale Nutzeroberflächen, über die Datengeber ihre Rechte wahrnehmen und Einsicht in die Verarbeitung ihrer Daten erhalten.

2. **Prozess- und Architekturanforderungen**, die sicherstellen, dass die Cockpit-Funktionen technisch robust und mit den Sicherheitsanforderungen vereinbar implementiert werden.

3. **Dokumentenvorlagen**, mit denen die rechtlichen und organisatorischen Pflichten eines Datentreuhänders sowie die Rolle der Datennutzer vertraglich und dokumentarisch ausgestaltet werden.

4. **Schutz von Geschäftsgeheimnissen**, der über die klassischen Datenschutzanforderungen hinausgeht und insbesondere für B2B-Kontexte wesentlich ist.

Dieser Baustein soll nicht den Sicherheits-Baustein ersetzen, sondern ihn an zentralen Stellen **konkretisieren** und **operationalisieren**.

## Ausgangspunkt: Rechtsrahmen und Rollen

Die Umsetzung von Datengeberrechten und Datentreuhänderpflichten setzt eine präzise Einordnung der Rollen und Pflichten nach geltendem Recht voraus. Auf Basis der Bridge-Up-Ergebnisse werden insbesondere folgende Normen betrachtet:

- **Datenschutz-Grundverordnung (DSGVO)**  
  - Rollen: Verantwortliche, ggf. gemeinsame Verantwortliche, Auftragsverarbeiter.  
  - Verarbeitungsgrundlagen, insbesondere Einwilligung und vertragliche Erforderlichkeit.  
  - Rechte betroffener Personen (insb. Art. 15–20 DSGVO).  
  - Pflichten der Verantwortlichen (Art. 24 ff. DSGVO).

- **Data Governance Act (DGA)**  
  - Einordnung des Datentreuhänders als möglicher Datenvermittlungsdienst.  
  - Anforderungen an neutrale, transparente und rechtskonforme Vermittlung.

- **Geschäftsgeheimnisschutzgesetz (GeschGehG)**  
  - Maßstab „angemessene Geheimhaltungsmaßnahmen“ für Geschäftsgeheimnisse.  
  - Übertragbarkeit von Datenschutzprinzipien (z. B. Zweckbindung, Datenminimierung, Vertraulichkeit) auf vertrauliche Unternehmensdaten.

Im Zusammenspiel mit den im Sicherheits- und Datenschutzbaustein beschriebenen Kategorien „Betroffene Person“, „Datengeber“, „Datennutzer“ und „Datentreuhänder“ ergibt sich ein Rollenmodell, in dem:

- der **Datentreuhänder** in der Regel als Verantwortlicher (oder gemeinsam Verantwortlicher) handelt,  
- der **Datengeber** Betroffener sein kann (bei personenbezogenen Daten) oder Inhaber von Geschäftsgeheimnissen,  
- der **Datennutzer** in Abhängigkeit vom Szenario Verantwortlicher oder gemeinsam Verantwortlicher ist.

Dieses Rollenmodell ist Grundlage für die Ausgestaltung der Datenschutz-Cockpits, der Prozesse und der Dokumentenvorlagen.


## Datenschutz-Cockpit als zentrales Steuerungsinstrument

### Rolle des Datenschutz-Cockpits im Sicherheits- und Datenschutzkonzept

Das Datenschutz-Cockpit bildet die zentrale Sicht des Datengebers auf das Datentreuhandmodell. Es bündelt Informationen und Funktionen, die ansonsten über mehrere Systeme, Dokumente und Kommunikationswege verteilt wären, und macht damit die im Sicherheits-Baustein beschriebenen Schutzziele **Transparenz** und **Intervenierbarkeit** konkret erfahrbar.

Es interagiert direkt mit den dort beschriebenen Datenkategorien:

- Stammdaten und Kontoinformationen der Datengeber und Datennutzer,  
- Berechtigungen, Einwilligungen und Freigaben,  
- Nutzungs- und Protokolldaten der Plattform,  
- Datenschutzdaten (z. B. Auskunfts- und Löschersuchen),  
- ggf. klassifizierte Geschäftsgeheimnisse.

### Funktionsbereiche des Datenschutz-Cockpits

Die folgenden Funktionsbereiche haben sich als zentral herauskristallisiert und sind auf die DSGVO-Betroffenenrechte und die Bridge-Up-Anforderungen abgestimmt:

#### 1. Überblick über Daten und Verarbeitungen (Auskunft)

Das Cockpit stellt eine **übersichtliche Darstellung** bereit, welche Daten über den Datengeber im Datentreuhandmodell verarbeitet werden. Dies umfasst insbesondere:

- eine Auflistung der relevanten Datenkategorien (z. B. Stammdaten, Transaktionsdaten, Protokolldaten),  
- die jeweils zugeordneten **Verarbeitungszwecke**,  
- die beteiligten **Datennutzer** und die Art ihrer Nutzung (z. B. einmaliger Abruf, laufende Nutzung),  
- einen Bezug zu den Einträgen im **Verarbeitungsverzeichnis**.

Auf diese Weise wird das Auskunftsrecht nach Art. 15 DSGVO in einer Form umgesetzt, die für Datengeber nachvollziehbar und verständlich ist.

#### 2. Korrektur von Daten (Berichtigung)

Zur Wahrung der Richtigkeit verarbeiteter Daten (Art. 16 DSGVO) bietet das Cockpit Funktionen zur:

- Anzeige einzelner Datensätze, die korrigierbar sind,  
- Anpassung fehlerhafter Angaben direkt in der Oberfläche,  
- Kennzeichnung, ob und in welchem Umfang die Korrektur bereits an Datennutzer weitergegeben wird bzw. werden kann.

Die Berichtigungsprozesse stehen in enger Verbindung mit den im Sicherheits-Baustein beschriebenen Anforderungen an **Integrität** und an die **Fehlerbehandlung in Anwendungen**.

#### 3. Löschung und Einschränkung der Verarbeitung

Das Cockpit ermöglicht es, Löschanforderungen gezielt auszulösen. Hierbei werden:

- die vom Datengeber ausgewählten Datenkategorien oder Verarbeitungen identifiziert,  
- rechtliche und technische Einschränkungen (z. B. Aufbewahrungsfristen, laufende Vertragsverhältnisse) berücksichtigt,  
- der Fortschritt und das Ergebnis der Löschung dokumentiert.

Die Prozesse müssen mit den Konzepten zu **„Löschen und Vernichten“**, **Datensicherung** und **Archivierung** kompatibel sein, damit keine Widersprüche entstehen (z. B. Löschung in operativen Systemen bei gleichzeitiger Aufbewahrungspflicht in Archiven).

#### 4. Datenübertragbarkeit

Das Cockpit stellt Funktionen zur **Datenübertragbarkeit** bereit, etwa:

- Export bestimmter Datensätze in einem strukturierten, gängigen und maschinenlesbaren Format,  
- Darstellung, welche Daten im Export enthalten sind und welche nicht,  
- Hinweise zur weiteren Verwendung exportierter Daten (z. B. beim Wechsel zu einem anderen Dienstleister).

Diese Funktion muss auf die technische Architektur (z. B. Datenbanken, Schnittstellen) abgestimmt sein und sich in die Sicherheitsmaßnahmen (z. B. Zugriffskontrolle, Protokollierung) einfügen.

#### 5. Einwilligungen und Freigaben

Ein eigenes Modul im Cockpit dient der Verwaltung von **Einwilligungen** und sonstigen Freigaben, z. B.:

- Übersichtsseite aller erteilten Einwilligungen und aktiven Freigaben,  
- Funktionen zum Erteilen, Ablehnen oder Widerrufen von Einwilligungen,  
- Anzeige der Konsequenzen eines Widerrufs für laufende Verarbeitungen.

Hier greifen die im Sicherheitskapitel genannten Aspekte „Berechtigungen und Einwilligungen“ und „Datenschutzdaten“ unmittelbar. Die im Cockpit sichtbaren Zustände müssen konsistent mit den technischen Berechtigungen und Zugriffsrechten sein.

#### 6. Historie und Nachweisbarkeit

Eine Historienansicht dokumentiert wichtige Vorgänge, etwa:

- erteilte und widerrufene Einwilligungen,  
- gestellte Auskunfts-, Berichtigungs- oder Löschanfragen,  
- Entscheidungen über Datennutzungsanfragen,  
- relevante Benachrichtigungen (z. B. Hinweise auf Datenschutzvorfälle oder Regelwerksänderungen).

Diese Historie dient sowohl der **Nachvollziehbarkeit für den Datengeber** als auch der **Nachweisführung** gegenüber Aufsichtsbehörden und internen Prüfinstanzen. Sie ergänzt damit die im Sicherheitskapitel vorgesehenen Maßnahmen zur Protokollierung und Detektion sicherheitsrelevanter Ereignisse.

## Prozess- und Architekturanforderungen

Die beschriebenen Cockpit-Funktionen lassen sich nur dann sicher betreiben, wenn sie durch klare Prozesse und eine geeignete Architektur gestützt werden. Aus den Bridge-Up-Ergebnissen ergeben sich insbesondere folgende Anforderungen:

### Klare Rollen und Zuständigkeiten

Für jede Funktion im Cockpit (z. B. Bearbeitung von Auskunftsersuchen, Durchführung von Löschungen) müssen Rollen, Verantwortlichkeiten und Vertretungsregelungen definiert werden. Dies ist mit den organisatorischen Vorgaben aus den Bausteinen:

- „Organisation“,  
- „Personal“,  
- „Ordnungsgemäße IT-Administration“ und  
- „Compliance Management“

abzustimmen. Die Zuständigkeiten sollten sowohl in internen Richtlinien als auch in den Dokumentenvorlagen (siehe unten) reflektiert werden.

### Anbindung an Identitäts- und Berechtigungsmanagement

Die sichere Nutzung des Cockpits setzt voraus, dass:

- Datengeber eindeutig identifiziert werden,  
- Zugriff nur im Rahmen definierter Berechtigungen möglich ist,  
- administrative Tätigkeiten besonders geschützt und nachvollziehbar sind.

Hierfür sind die Anforderungen aus dem Baustein „Identitäts- und Berechtigungsmanagement“ maßgeblich. Die Bridge-Up-Ergebnisse sehen eine konsequente Trennung von Ansichten und Funktionen je nach Rolle vor (z. B. Datengeber vs. Datennutzer).

### Konsistentes Protokollierungs- und Archivierungskonzept

Die in der Historie des Cockpits angezeigten Informationen müssen auf ein konsistentes Protokollierungskonzept zurückgreifen. Das bedeutet:

- Protokolle werden systematisch und manipulationssicher erfasst,  
- relevante Ereignisse werden für eine angemessene Dauer archiviert,  
- Zugriffe auf Protokolldaten sind geregelt und geprüft.

Dies entspricht den im Sicherheitskapitel dargestellten Anforderungen an Protokollierung, Archivierung sowie Detektion von sicherheitsrelevanten Ereignissen. Die Bridge-Up-Funktionen für Historie und Nachweis greifen auf diese Mechanismen zurück und machen einen Teil davon für Datengeber sichtbar.

### Abstimmung mit Lösch-, Sicherungs- und Archivierungskonzepten

Löschfunktionen im Cockpit müssen:

- konsistent mit dem Datensicherungskonzept sein (keine unbeabsichtigte Wiederherstellung gelöschter Daten),  
- mit gesetzlichen und vertraglichen Aufbewahrungspflichten vereinbar sein,  
- sauber dokumentiert werden (was wurde wann und in welchem Umfang gelöscht).

Die zugehörigen Regelungen sind in den Konzepten zu „Datensicherungskonzept“, „Löschen und Vernichten“ sowie „Archivierung“ zu verankern und in den Dokumentenvorlagen transparent zu machen.

## Dokumentenvorlagen als verbindender Governance-Baustein

Die Bridge-Up-Ergebnisse enthalten eine Reihe von **Dokumentenvorlagen**, die unmittelbar mit den beschriebenen Cockpit-Funktionen und Prozessen verknüpft sind. Sie dienen dazu, die Pflichten und Rechte der Beteiligten verbindlich festzuhalten und die technische Umsetzung im Sicherheitskonzept abzusichern.

### Datentreuhandvertrag

Der Datentreuhandvertrag zwischen Datengeber und Datentreuhänder legt u. a. fest:

- welche Daten der Datengeber dem Datentreuhänder anvertraut,  
- zu welchen Zwecken und auf welcher Rechtsgrundlage die Verarbeitung erfolgt,  
- welche Rechte der Datengeber hat (einschließlich Auskunft, Berichtigung, Löschung, Datenübertragbarkeit, Widerruf) und wie diese Rechte wahrgenommen werden können (z. B. über das Datenschutz-Cockpit),  
- welche technischen und organisatorischen Maßnahmen der Datentreuhänder zur Sicherung der Daten und zur Umsetzung der Rechte ergreift.

Die Formulierungen sind so angelegt, dass sie an unterschiedliche Datentreuhandmodelle und Domänen angepasst werden können.

### Datennutzungsvertrag

Der Datennutzungsvertrag zwischen Datentreuhänder und Datennutzer regelt u. a.:

- welche Daten der Datennutzer zu welchen Zwecken nutzen darf,  
- welche Beschränkungen gelten (z. B. keine Weitergabe an Dritte, Zweckbindung, Löschpflichten),  
- welche Pflichten der Datennutzer im Zusammenhang mit der Umsetzung von Betroffenenrechten hat (z. B. Mitwirkung bei Löschungen oder Auskunftsersuchen),  
- wie mit vertraulichen Informationen und Geschäftsgeheimnissen umzugehen ist.

Die im Vertrag festgelegten Pflichten entsprechen den im Cockpit und in den Prozessen vorgesehenen Interaktionen (z. B. Informationsflüsse bei Widerrufen).

### Vereinbarung über gemeinsame Verantwortlichkeit

Soweit Datentreuhänder und Datennutzer gemeinsam als Verantwortliche auftreten, wird eine Vereinbarung über die gemeinsame Verantwortlichkeit benötigt. Diese beschreibt:

- welche Partei welche Informationspflichten übernimmt,  
- wie die Wahrnehmung der Betroffenenrechte organisiert ist,  
- wie die Umsetzung der technischen und organisatorischen Maßnahmen koordiniert wird.

Die Vereinbarung ist dabei eng mit den Rollen- und Prozessmodellen verknüpft, die im Rahmen von Bridge-Up erarbeitet wurden.

### Verarbeitungsverzeichnis

Die Vorlage für ein Verarbeitungsverzeichnis dient dazu, die im Datentreuhandmodell stattfindenden Verarbeitungstätigkeiten strukturiert zu dokumentieren. Dabei werden insbesondere:

- Verarbeitungszwecke,  
- betroffene Datenkategorien,  
- Empfänger bzw. Kategorien von Empfängern,  
- eingesetzte technisch-organisatorische Maßnahmen

erfasst. Teile dieser Informationen können für Datengeber aufbereitet und über das Cockpit zugänglich gemacht werden, um Transparenz herzustellen.

### Datenschutz-Folgenabschätzung und Risikoanalyse

Die Vorlagen für Datenschutz-Folgenabschätzung und Risikoanalyse unterstützen die systematische Bewertung, ob bestimmte Verarbeitungen ein hohes Risiko für die Rechte und Freiheiten natürlicher Personen mit sich bringen, und helfen bei der Ableitung zusätzlicher Schutzmaßnahmen. Sie ergänzen die im Sicherheitskapitel beschriebenen Risiko- und Gefährdungsanalysen um eine stärker datenschutzbezogene Perspektive.

## Schutz von Geschäftsgeheimnissen

Neben personenbezogenen Daten berücksichtigt dieser Baustein auch den Schutz von **Geschäftsgeheimnissen** und anderen nicht-personenbezogenen, aber schützenswerten Daten, wie er im Sicherheitskapitel unter „Daten ohne Personenbezug“ und „individuell besonders schützenswerte Daten“ beschrieben ist. 

Auf Basis der Bridge-Up-Ergebnisse werden hierfür:

- vertragliche Regelungen in Datentreuhand- und Datennutzungsverträgen vorgesehen, die die zulässige Nutzung, Offenlegung und Weitergabe solcher Daten einschränken,  
- Schutzmaßnahmen aus dem Datenschutz (z. B. Zweckbindung, Datenminimierung, Zugriffsbeschränkung, Protokollierung) entsprechend auf Geschäftsgeheimnisse übertragen,  
- diese Anforderungen in den Prozessen (z. B. bei Datennutzungsanfragen, Exporten, Löschungen) berücksichtigt.

Auf diese Weise werden die im Sicherheitskapitel bezeichneten Schutzbedarfe für nicht-personenbezogene, jedoch kritische Unternehmensdaten in konkrete Prozesse, Funktionen und vertragliche Verpflichtungen überführt.

## Zusammenführung mit dem Sicherheits- und Datenschutzkapitel

Die hier beschriebenen Ergebnisse lassen sich wie folgt in das bestehende Sicherheits- und Datenschutzkonzept eines Datentreuhänders integrieren:

1. **Anforderungsabgleich**  

   Die im Sicherheitskapitel beschriebenen Schutzziele, Assets, Gefährdungen und BSI-Bausteine werden mit den Funktionsanforderungen des Datenschutz-Cockpits und den damit verbundenen Prozessen abgeglichen. Ziel ist eine lückenlose Abdeckung sowohl aus Sicherheits- als auch aus Datenschutzperspektive.

2. **Prozessdefinition**  

   Die für die Cockpit-Funktionen erforderlichen Prozesse (z. B. Bearbeitung von Auskunftsersuchen, Löschprozessen, Widerrufen) werden in das bestehende Prozess- und ISMS-Framework eingebettet und mit Rollen, Zuständigkeiten und Kontrollmechanismen versehen.

3. **Architektur- und Betriebsintegration**  

   Die erforderlichen Dienste, Schnittstellen und Datenflüsse werden in der Systemarchitektur verankert und mit den im Sicherheitskapitel beschriebenen Bausteinen (Webanwendungen, Datenbanken, Protokollierung, Identitäts- und Berechtigungsmanagement, Cloud-Nutzung) abgestimmt.

4. **Dokumentengovernance**  

   Die bereitgestellten Dokumentenvorlagen (Datentreuhandvertrag, Datennutzungsvertrag, Vereinbarung gemeinsamer Verantwortlichkeit, Verarbeitungsverzeichnis, DSFA/Risikoanalyse) werden an das konkrete Datentreuhandmodell angepasst und als verbindlicher Bestandteil des Governance- und Compliance-Rahmens etabliert.

In dieser Kombination bieten der Baustein **„Sicherheits- und Datenschutzanforderungen“** und der vorliegende Baustein zur **Umsetzung von Datengeberrechten und Datentreuhänderpflichten** eine konsistente Grundlage, um Datentreuhandmodelle sicher, rechtskonform und für Datengeber nachvollziehbar zu betreiben.