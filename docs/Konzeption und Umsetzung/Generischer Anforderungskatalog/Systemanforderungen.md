Systemanforderungen
=====================

 SA1. Maschinenregistrierung
-------------------------------

| **ID** | **SA1** |
| --- | --- |
| **Name** | Maschinenregistrierung |
| **Anforderung** | Das System soll Datengebern die Möglichkeit bieten, verschiedene Maschine zu registrieren, die Daten sammeln und einem bestimmten Nutzer gehören um die von allen Maschinen gesammelten Daten berücksichtigen zu können. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | Die Informationen kommen in der Regel von einem angebundenen System eines Herstellers oder Management-Systems und nicht direkt vom Gerät. (Sind also gegebenenfalls aufbereitet)  <br>  <br>\- Besitzer (Data/machine owner(s))  <br>\- Datenherausgeber (Hersteller, Management System,...)  <br>\- Maschinendaten (Id, Hersteller,...)  <br>\- autorisierte Daten (Daten, die gesammelt werden)  <br>\- Bedingungen der Nutzung (prüfen!)  <br>\- Berechtigte Datennutzer |
| **Wichtigkeit** | Optional |
| **Alternativen** |     |

### Zugehörige Quellen

| **ID** | **Name** |
| --- | --- |
| **Q24** | Workshop User Journey |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA58** | Maschinenerfassung |

 SA2. Mitteilung über die gemeinsame Nutzung einer Maschine
--------------------------------------------------------------

| **ID** | **SA2** |
| --- | --- |
| **Name** | Mitteilung über die gemeinsame Nutzung einer Maschine |
| **Anforderung** | Das System soll Nutzern, die eine Maschine mit mehreren Personen teilen, die Möglichkeit bieten, die verschiedenen betroffenen Personen zu registrieren um ihnen eine Einladung zur Teilnahme an den Datentreuhänder zu schicken. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | \- Besitzer (Data owners): alle betroffenen Personen  <br>\- Datenherausgeber (Hersteller, Management System,...)  <br>\- Maschinendaten (Id, Hersteller,...)  <br>\- autorisierte Daten (Daten, die gesammelt werden)  <br>\- Bedingungen der Nutzung (prüfen!)  <br>\- Zweck der Nutzung  <br>\- Berechtigte Datennutzer |
| **Wichtigkeit** | Optional |
| **Alternativen** |     |

### Zugehörige Quellen

| **ID** | **Name** |
| --- | --- |
| **Q24** | Workshop User Journey |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA58** | Maschinenerfassung |

 SA3. Nutzerkontoverwaltung
------------------------------

| **ID** | **SA3** |
| --- | --- |
| **Name** | Nutzerkontoverwaltung |
| **Anforderung** | Das System soll den Nutzern die Möglichkeit bieten, ihre Kontodaten einzusehen und zu bearbeiten um ihren Daten und Einstellungen korrekt und aktuell zu halten. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | \- Person bezogene Daten (Name, Adresse, E-Mail,..) |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** |     |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA14** | Nutzermanagement |

 SA4. Datenverwaltung
------------------------

| **ID** | **SA4** |
| --- | --- |
| **Name** | Datenverwaltung |
| **Anforderung** | Das System soll dem Datengeber die Möglichkeit bieten, sich einen überblick über die Daten zu verschaffen, die der Datentreuhänder über eine betroffene Person / ein betroffenes Unternehmen besitzt um einen bestimmen Datensatz genau einzusehen und/oder eine Entscheidung darüber zu treffen. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | \- Alle Datengeberdaten  <br>\- Status (Einwilligung)  <br>\- Alle Zugreifenden (Datennutzer)  <br>\- Historie |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** |     |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA17** | Angaben zu den bereitgestellten Daten |
| **DTH-FA23** | Rechteverwaltung |

 SA5. An- und Abmeldung
--------------------------

| **ID** | **SA5** |
| --- | --- |
| **Name** | An- und Abmeldung |
| **Anforderung** | Das System soll den Nutzern die Möglichkeit bieten, sich beim Datentreuhänder an- und abzumelden um den Zugang zu seinen Funktionalitäten zu gestatten oder zu beenden. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | Daten für Anmeldung:  <br>\- Benutzer (E-Mail)  <br>\- Passwort  <br>\- 2-Faktor |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** |     |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA14** | Nutzermanagement |

 SA6. Nutzerregistrierung
----------------------------

| **ID** | **SA6** |
| --- | --- |
| **Name** | Nutzerregistrierung |
| **Anforderung** | Das System soll den Nutzern die Möglichkeit bieten, ein Konto anzulegen um Zugang zu den Funktionalitäten des Datentreuhänders zu erhalten. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | Daten:  <br>\- Name  <br>\- E-Mail  <br>\- Passwort  <br>\- Rolle (betroffene Person / betroffenes Unternehmen, Dateninhaber, Datennutzer)  <br>\- Kontotyp (persönliches oder unternehmensbezogenes Konto) |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** |     |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA14** | Nutzermanagement |
| **DTH-FA39** | Identifizierung |

 SA7. Einwilligung
---------------------

| **ID** | **SA7** |
| --- | --- |
| **Name** | Einwilligung |
| **Anforderung** | Das System soll den betroffenen Personen/Unternehmen die Möglichkeit bieten, der Verwendung bestimmter Daten für bestimmte Zwecke zuzustimmen oder diese zu verweigern sowie eine bereits erteilte Einwilligung zu widerrufen um die Datensouveranität zu gewährleisten. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Zugangstreuhand; Fremdbestimmte Auswertungstreuhand |
| **Beschreibung** | Passiert nach SA11 und SA18  <br>  <br>\- Zeitpunkt der Einwilligung  <br>\- Autorisierter Zeitraum  <br>\- Autorisierte Daten  <br>\- Zweck der Nutzung  <br>\- Berechtigte Datennutzer (bestimmter Datennutzer oder Typ von Datennutzern) |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** | automatisiertes Matching |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA20** | Einwilligung |

 SA8. Angaben zur Nutzung der Daten
--------------------------------------

| **ID** | **SA8** |
| --- | --- |
| **Name** | Angaben zur Nutzung der Daten |
| **Anforderung** | Das System soll den betroffenen Personen und Unternehmen die Möglichkeit bieten, Angaben zur Nutzung ihrer Daten einzusehen um Transparenz über die Aktivitäten der Datennutzer zu gewährleisten. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | -Datennutzer  <br>-Datum der Datennutzung  <br>-Datenbeschreibung (wie in der Einwilligung)  <br>-Nach Bedarf: Datensätze  <br>-Nach Bedarf: Einwilligung zeigen |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** |     |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA47** | Protokollierung |
| **DTH-FA48** | Angaben über die Nutzung der Daten |

 SA9. Datensammlung (Continuous Push)
----------------------------------------

| **ID** | **SA9** |
| --- | --- |
| **Name** | Datensammlung (Continuous Push) |
| **Anforderung** | Das System muss fähig sein, gesendeten Daten von den Datengebern zu empfangen, und falls nötig und zulässig, die empfangenen Daten zu speichern um autorisierten Datennutzern den Zugriff auf die Daten zu ermöglichen. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | \- Nutzer (Von wem?)  <br>\- Daten des Datengebers (z.b. Patientendaten, Zahlungsinformationen,...)  <br>\- Datum und Uhrzeit der Erfassung |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** |     |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA15** | Datensammlung/Datenerfassung |

SA10. Datenanfrage (Pull)
------------------------------

| **ID** | **SA10** |
| --- | --- |
| **Name** | Datenanfrage (Pull) |
| **Anforderung** | Das System muss fähig sein Datenanfragen an Datengeber zu senden und die von Datengebern gesendeten Daten zu empfangen um autorisierten Datennutzern den Zugriff auf die Daten zu ermöglichen. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle; Zugangstreuhand; Fremdbestimmte Auswertungstreuhand |
| **Beschreibung** | \- Datenenanfrager  <br>\- Zweck  <br>\- Autorisierung (verifizieren wer man ist)  <br>\- Datengeber (Von wem?)  <br>\- Daten des Datengebers (z.b. Patientendaten, Zahlungsinformationen,...)  <br>\- Datum und Uhrzeit der Erfassung |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** |     |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA15** | Datensammlung/Datenerfassung |

SA11. Interaktive Aufforderung zur Einwilligung
----------------------------------------------------

| **ID** | **SA11** |
| --- | --- |
| **Name** | Interaktive Aufforderung zur Einwilligung |
| **Anforderung** | Das System muss fähig sein, eine interaktive Aufforderung zur Einwilligung von einem Dritten (Datengeber oder potenzieller Datennutzer) entgegenzunehmen und sie an die betroffenen Personen / Unternehmen weiterzuleiten um die erforderlichen Einwilligungen einzuholen. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Fremdbestimmte Auswertungstreuhand; Zugangstreuhand |
| **Beschreibung** | \- Absender (Dateninhaber oder Datennutzer)  <br>\- Zeitpunkt der gesendeten Anfrage  <br>\- Betroffene Person / Betroffenes Unternehmen  <br>\- Angeforderter Zeitraum  <br>\- Angeforderte Daten  <br>\- Bedingungen der Nutzung (prüfen!)  <br>\- Zweck der Nutzung  <br>\- Berechtigte Datennutzer (bestimmter Datennutzer oder Typ von Datennutzern) |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** |     |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA20** | Einwilligung |

SA12. Einladung zur Teilnahme am Datentreuhänder
-----------------------------------------------------

| **ID** | **SA12** |
| --- | --- |
| **Name** | Einladung zur Teilnahme am Datentreuhänder |
| **Anforderung** | Das System muss fähig sein, Teilnahmeeinladungen an Personen zu schicken, die sich die Nutzung einer Maschine mit einem Nutzer des Datentreuhänders teilen, um den Datennutzern vollständigere Datensätze anbieten zu können. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | \- Maschine  <br>\- Nutzer  <br>\- Zweck  <br>\- Einladung |
| **Wichtigkeit** | Optional |
| **Alternativen** |     |

SA13. Visualisierung der Daten
-----------------------------------

| **ID** | **SA13** |
| --- | --- |
| **Name** | Visualisierung der Daten |
| **Anforderung** | Das System soll dem Nutzer die Möglichkeit bieten die Details eines Datensatzes einzusehen um auf Wunsch detaillierte Informationen abzurufen. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | \- Datennutzer  <br>\- Zweck  <br>\- Autorisierung  <br>\- autorisierte Daten  <br>\- Datengeber  <br>\- Datum für Anfrage |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** |     |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA17** | Angaben zu den bereitgestellten Daten |
| **DTH-FA18** | Zugang zu Daten |

SA14. Abfrage von historischen Daten (Pull)
------------------------------------------------

| **ID** | **SA14** |
| --- | --- |
| **Name** | Abfrage von historischen Daten (Pull) |
| **Anforderung** | Das System muss fähig sein, eine Abfrage von historischen Daten zu empfangen und zu beantworten um den Datennutzern bei Bedarf Zugang zu den historischen Daten zu gewähren. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | klar definierte Schnittstellen  <br>\- Datennutzer  <br>\- Zweck  <br>\- Autorisierung  <br>\- autorisierte Daten  <br>\- Datengeber  <br>\- Datum/Zeitraum für Anfrage |
| **Wichtigkeit** | Optional |
| **Alternativen** |     |

SA16. Datenweitergabe neuer Daten (Push)
---------------------------------------------

| **ID** | **SA16** |
| --- | --- |
| **Name** | Datenweitergabe neuer Daten (Push) |
| **Anforderung** | Das System muss fähig sein, neue Daten oder Datenänderungen (selbstständig) an den Datennutzer zu senden, um regelmäßige Abfragen zu vermeiden und Daten so schnell wie möglich verwendbar zu machen. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | \- Dateninhaber  <br>\- Betroffene Person oder betroffenes Unternehmen  <br>\- Datum der Datenweitergabe  <br>\- Neue Daten  <br>\- Datennutzer |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** |     |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA18** | Zugang zu Daten |
| **DTH-FA44** | Datenvermittlung im Auftrag des Datengebers |
| **DTH-FA49** | aktive Datenvermittlung |

SA17. Benachrichtigungen an Datennutzer
--------------------------------------------

| **ID** | **SA17** |
| --- | --- |
| **Name** | Benachrichtigungen an Datennutzer |
| **Anforderung** | Das System muss fähig sein eine Benachrichtigung an die Datennutzer senden, wenn eine betroffene Person / Unternehmen eine Entscheidung trifft oder eine frühere Entscheidung in Bezug auf die Aufforderung zur Einwilligung ändert um die Einstellungen der Datenvermittlung definieren/aktualisieren zu können. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Zugangstreuhand; Fremdbestimmte Auswertungstreuhand |
| **Beschreibung** | \- Datennutzer  <br>\- betroffene Person/Unternehmen  <br>\- Beschreibung der angefragten Daten  <br>\- Einwilligung  <br>\- (Daten)  <br>\- Datum/Zeitraum |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** |     |

SA18. Nicht-interaktive Aufforderung zur Einwilligung
----------------------------------------------------------

| **ID** | **SA18** |
| --- | --- |
| **Name** | Nicht-interaktive Aufforderung zur Einwilligung |
| **Anforderung** | Das System soll fähig sein, eine nicht interaktive Aufforderung zur Einwilligung von einem Dritten (Datengeber oder potenzieller Datennutzer) entgegenzunhemen und sie an die betroffenen Personen / Unternehmen weiterzuleiten um eine Einwillgung von betroffenen Personen / Unternehmen zu ermöglichen. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Fremdbestimmte Auswertungstreuhand; Zugangstreuhand |
| **Beschreibung** | \- Absender (Dateninhaber oder Datennutzer)  <br>\- Zeitpunkt der gesendeten Anfrage  <br>\- Betroffene Personen oder Unternehmen  <br>\- Angeforderter Zeitraum  <br>\- Angeforderte Daten  <br>\- Bedingungen der Nutzung (prüfen!)  <br>\- Zweck der Nutzung  <br>\- Berechtigte Datennutzer (bestimmter Datennutzer oder Typ von Datennutzern)  <br>  <br>Die betroffene Person / das betroffene Unternehmen ist nicht online. Der Dateninhaber sendet die Aufforderung zur Einwilligung an den Datentreuhänder. |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** |     |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA20** | Einwilligung |

SA19. Ausstehende Einwilligungen
-------------------------------------

| **ID** | **SA19** |
| --- | --- |
| **Name** | Ausstehende Einwilligungen |
| **Anforderung** | Das System muss den betroffenen Personen/Unternehmen die Möglichkeit bieten, ausstehende Einwilligungen einzusehen um Entscheidungen über die Aufforderung zur Einwilligung zu treffen. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Fremdbestimmte Auswertungstreuhand; Zugangstreuhand |
| **Beschreibung** | \- Status der Einwilligung  <br>\- Zweck der Nutzung/Anfrage  <br>\- Datennutzer  <br>\- angefragte Daten  <br>\- Datum für Anfrage |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** | automatisiertes Matching |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA20** | Einwilligung |

SA20. Benachrichtigung an betroffene Personen/Unternehmen
--------------------------------------------------------------

| **ID** | **SA20** |
| --- | --- |
| **Name** | Benachrichtigung an betroffene Personen/Unternehmen |
| **Anforderung** | Das System soll fähig sein, Benachrichtigungen an die betroffenen Personen/Unternehmen zu senden um sie über die austehenden Verfahren zu informieren. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | \- Benachrichtigungsnachricht (Thema, Schritte)  <br>\- betroffene Person/Unternehmen  <br>\- Eintrittsdatum |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** |     |

SA22. Bereits erteilte Einwilligungen
------------------------------------------

| **ID** | **SA22** |
| --- | --- |
| **Name** | Bereits erteilte Einwilligungen |
| **Anforderung** | Das System soll den betroffenen Personen/Unternehmen die Möglichkeit bieten, bereits erteilte Einwillungen einzusehen um um einen überblick und die Kontrolle über die mögliche Verwendung ihrer Daten zu ermöglichen. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Zugangstreuhand; Fremdbestimmte Auswertungstreuhand |
| **Beschreibung** | \- Status der Einwilligung  <br>\- Zweck der Nutzung/Anfrage  <br>\- Datennutzer  <br>\- angefragte Daten  <br>\- Datum für Anfrage |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** |     |

### Zugehörige Quellen

| **ID** | **Name** |
| --- | --- |
| **Q24** | Workshop User Journey |

SA23. Integration mit einem Abrechnungssystem
--------------------------------------------------

| **ID** | **SA23** |
| --- | --- |
| **Name** | Integration mit einem Abrechnungssystem |
| **Anforderung** | Das System soll eine Schnittstelle zu Abrechnungssystemen bereitstellen um Bezahlungen zu ermöglichen. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | Zahlungsmethode  <br>Zahlungsmbetrag  <br>Kostenstellen  <br>Zahlungs-ID  <br>... |
| **Wichtigkeit** | Optional |
| **Alternativen** |     |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA27** | Abrechnung |

SA24. Redundanz an anderer Stelle
--------------------------------------

| **ID** | **SA24** |
| --- | --- |
| **Name** | Redundanz an anderer Stelle |
| **Anforderung** | Das System muss fähig sein regelmäßig (Alle 30 min.?) Sicherungen aller Daten an einem anderen Ort zu erstellen oder diese direkt während jedem Schreibvorgang zu duplizieren (unklar was davon) um bei Ausfällen oder Defekten keinen kompletten Datenverlust zu erleben. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | \- Daten (der Datengeber)  <br>\- Zuordnung (zu dem Datengeber/Eintrag in der Datenbank)  <br>\- Aktualität der Daten (Datum) |
| **Wichtigkeit** | Optional |
| **Alternativen** |     |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA37** | Redundanz |
| **DTH-FA38** | Ausfalllösung |

SA25. Auswahl von Datennutzern
-----------------------------------

| **ID** | **SA25** |
| --- | --- |
| **Name** | Auswahl von Datennutzern |
| **Anforderung** | Das System soll Datengebern die Möglichhkeit bieten eine Liste von Datennutzern und ihren Nutzungszwecken ansehen und auswählen zu können um Datengeber dabei zu unterstützen geeignete Datennutzer zu finden. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Fremdbestimmte Auswertungstreuhand; Zugangstreuhand |
| **Beschreibung** | \- Suchmaske mit Suche nach Name, Kategorie, Datenanforderung, Zweck...  <br>\- Daten des Datengebers  <br>\- nützliche Daten (Angabe der Datengeber welche Daten sie gerne hätten)  <br>\- Zweck der Nutzung |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** | Öffentliche Datenanfrage |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA44** | Datenvermittlung im Auftrag des Datengebers |

SA26. Öffentliche Datenanfrage
-----------------------------------

| **ID** | **SA26** |
| --- | --- |
| **Name** | Öffentliche Datenanfrage |
| **Anforderung** | Das System muss fähig sein öffentliche Datenanfragen zu erstellen und zu verarbeiten um die Anfragen des Datennutzers einem breiten Spektrum zugänglich zu machen und so diese bei ihrer Datensammlung zu unterstützen. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Fremdbestimmte Auswertungstreuhand; Zugangstreuhand |
| **Beschreibung** | (Z.b. eine Forschung gegen Krebs brauch anonymisierte Informationen von Leuten, die schon Krebs hatten und bittet daher öffentlich nach "Datenspenden".)  <br>  <br>Daraus werden Einwilligungen für die Datengeber abgeleitet. (Oder direkt Verbindungen geknüpft, falls der DTH das Brokering selbstständig übernimmt)  <br>  <br>\- Zweck der Nutzung  <br>\- Angefragte Daten  <br>\- Format der Daten (anonymisiert, pseudonymisiert,...)  <br>\- Zeitraum |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** | Auswahl von Datenfreigaben |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA45** | Datenvermittlung auf Anfrage des Datennutzers |

SA28. Erfassung analoger Daten
-----------------------------------

| **ID** | **SA28** |
| --- | --- |
| **Name** | Erfassung analoger Daten |
| **Anforderung** | Das System soll fähig sein Technologien wie OCR und Natural Language Processing zu verwenden um Bilder von Dokumenten und Ähnlichem zu digitalisieren und in das entsprechende Format zu bringen |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | \- Bilder  <br>\- Metadaten zu den Bildern  <br>\- Datengeber  <br>\- Datum |
| **Wichtigkeit** | Optional |
| **Alternativen** |     |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA55** | Digitalisierung von Daten |

SA29. Erneutes Senden
--------------------------

| **ID** | **SA29** |
| --- | --- |
| **Name** | Erneutes Senden |
| **Anforderung** | Das System muss bei fehlgeschlagener übermittlung fähig sein den Fehlschlag zu erkennen und die übermittlung neu zu starten (in Intervallen bzw. sinnvollen Zeitabständen) um bei Internetausfällen und anderweitigen Problemen den Datentransfer trotzdem zu gewährleisten |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | \- Daten  <br>\- Datengeber  <br>\- Datum der Erstsendung  <br>\- Aktualität der Daten  <br>\- Zweck der Nutzung |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** |     |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA53** | Ausfallnetz |

SA31. Öffentliche Freigaben
--------------------------------

| **ID** | **SA31** |
| --- | --- |
| **Name** | Öffentliche Freigaben |
| **Anforderung** | Das System soll Datengebern die Möglichkeit bieten festgelegte Daten anonymisiert/pseudonymisiert öffentlich verfügbar zu machen um Datennutzern den Zugang zu den Daten zu gewähren. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Zugangstreuhand; Fremdbestimmte Auswertungstreuhand |
| **Beschreibung** | \- Daten (in einem spezifizierten Format)  <br>\- (optional) Zeitraum |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** | Auswahl von Datennutzern |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA45** | Datenvermittlung auf Anfrage des Datennutzers |

SA32. Schnittstellen zur Anbindung verschiedener Datenquellen
------------------------------------------------------------------

| **ID** | **SA32** |
| --- | --- |
| **Name** | Schnittstellen zur Anbindung verschiedener Datenquellen |
| **Anforderung** | Das System soll die Möglichkeit der Anbindung von verschiedenen Datenquellen über Schnittstellen bereitstellen um es Datengebern zu ermöglichen ihre Daten einfach zusammenzustellen. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | \- Quelle  <br>\- Datum  <br>\- Datengeber  <br>\- Metadaten darüber was für Daten geschickt werden  <br>\- Daten (die eigentlichen Daten, die über die Quelle kommen) |
| **Wichtigkeit** | Optional |
| **Alternativen** |     |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA54** | Einfache Möglichkeit zur Datenzusammenstellung |

SA33. Datenerfassung (Discrete Push)
-----------------------------------------

| **ID** | **SA33** |
| --- | --- |
| **Name** | Datenerfassung (Discrete Push) |
| **Anforderung** | Das System muss fähig sein event-basierte Daten von einem Datengeber zu empfangen um die Datenerfassung zu ermöglichen. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | \- Art der Daten  <br>\- Datennutzer  <br>\- Autorisierung  <br>\- Datum/Zeitraum für Anfrage |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** |     |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA15** | Datensammlung/Datenerfassung |

SA34. Kontinuierliche Datenanfrage (Continuous Pull)
---------------------------------------------------------

| **ID** | **SA34** |
| --- | --- |
| **Name** | Kontinuierliche Datenanfrage (Continuous Pull) |
| **Anforderung** | Das System soll fähig sein kontinuierliche Datenanfragen an Datengeber zu senden und die von Dategebern gesendeten Daten zu empfangen um den Datenstand zu aktualisieren und diese neuen Daten dem Datennutzer zur Verfügung zu stellen. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | \- Nutzer (Von wem?)  <br>\- Daten des Datengebers (z.b. Patientendaten, Zahlungsinformationen,...)  <br>\- Datum und Uhrzeit der Erfassung |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** |     |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA15** | Datensammlung/Datenerfassung |

SA35. Änderung der Rechte
------------------------------

| **ID** | **SA35** |
| --- | --- |
| **Name** | Änderung der Rechte |
| **Anforderung** | Das System muss fähig sein die Zugriffsrechte von den Daten der Datengebern zu aktualisieren um die Datenhoheit dieser gewährleisten zu können. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Fremdbestimmte Auswertungstreuhand; Zugangstreuhand |
| **Beschreibung** | \- Zugriffsrecht  <br>\- Datenkonsumer |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** |     |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA23** | Rechteverwaltung |

SA36. Datenlöschung
------------------------

| **ID** | **SA36** |
| --- | --- |
| **Name** | Datenlöschung |
| **Anforderung** | Das System soll die Möglichkeit bieten Datengebern ihre Daten zu löschen um dem Nutzer die Kontrolle über seine Daten zu ermöglichen. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | \- zu löschende Daten |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** |     |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA25** | Datenlöschung durch den Datengeber |

SA37. Nutzungsvereinbarung
-------------------------------

| **ID** | **SA37** |
| --- | --- |
| **Name** | Nutzungsvereinbarung |
| **Anforderung** | Das System soll Datennutzern die Möglichkeit bieten die Datennutzungsvereinbarung zu bestätigen um den Schutz der Daten zu gewährleisten. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | Vereinbarung |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** |     |

SA38. automatische Datenlöschung
-------------------------------------

| **ID** | **SA38** |
| --- | --- |
| **Name** | automatische Datenlöschung |
| **Anforderung** | Das System soll fähig sein automatisierte Löschung von Daten zu unterstützen um Zweckbindung und Datensouveränität für den Nutzer sicherzustellen. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | \- Nutzer  <br>\- Daten  <br>\- Zeitintervall |
| **Wichtigkeit** | Optional |
| **Alternativen** |     |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA52** | automatische Datenlöschung |

SA39. Auswahl von Daten
----------------------------

| **ID** | **SA39** |
| --- | --- |
| **Name** | Auswahl von Daten |
| **Anforderung** | Das System soll den Datennutzern die Möglichkeit bieten ihre benötigten Daten auszuwählen um dem Datennutzer die Spezifikation der benötigten Daten einfach zu gewährleisten. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | Ansonsten ungefiltert alle Daten |
| **Wichtigkeit** | Optional |
| **Alternativen** |     |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA28** | Datenaggregation |

SA40. Aggregation anhand ausgewählter Daten
------------------------------------------------

| **ID** | **SA40** |
| --- | --- |
| **Name** | Aggregation anhand ausgewählter Daten |
| **Anforderung** | Das System soll fähig sein Daten anhand der von dem Datennutzer festgelegten Auswahl zu aggregieren um dem Datennutzer eine Zusammenstellung seiner benötigten Daten zu liefern. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Fremdbestimmte Auswertungstreuhand |
| **Beschreibung** | Daten |
| **Wichtigkeit** | Optional |
| **Alternativen** |     |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA28** | Datenaggregation |

SA41. automatisiertes Matching
-----------------------------------

| **ID** | **SA41** |
| --- | --- |
| **Name** | automatisiertes Matching |
| **Anforderung** | Das System soll fähig sein Datengeber und Datennutzer basierend auf ihren Freigaben und Anfragen zu matchen um Datenvermittlung aktiv zu betreiben. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Verwaltungstreuhand; Selbstbestimmte Auswertungstreuhand |
| **Beschreibung** | \- Datengeber  <br>\- Datennutzer  <br>\- Freigaben  <br>\- Anfragen |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** | Auswahl von Datennutzern; Öffentliche Datenanfrage |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA49** | aktive Datenvermittlung |

SA42. Daten Ex- und Import
-------------------------------

| **ID** | **SA42** |
| --- | --- |
| **Name** | Daten Ex- und Import |
| **Anforderung** | Das System muss fähig sein Ex- und Import von Daten zu unterstützen um den Datentreuhänder (leicht) austauschbar zu machen. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | \- Nutzer  <br>\- Daten(-Verknüpfung)  <br>\- Konsument |
| **Wichtigkeit** | Optional |
| **Alternativen** |     |

SA43. Konsumentenliste Ex- und Import
------------------------------------------

| **ID** | **SA43** |
| --- | --- |
| **Name** | Konsumentenliste Ex- und Import |
| **Anforderung** | Das System soll Datengebern die Möglichkeit bieten ihre Verbindungen zu extrahieren und bei einem anderen Datentreuhänder anzubinden um einen leichten Wechsel der Datentreuhänder zu ermöglichen |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | \- Konsumenten  <br>\- zugehörige Daten(-verknüpfung)  <br>\- Nutzer |
| **Wichtigkeit** | Optional |
| **Alternativen** |     |

SA44. Einwilligung und Rechte Ex- und Import
-------------------------------------------------

| **ID** | **SA44** |
| --- | --- |
| **Name** | Einwilligung und Rechte Ex- und Import |
| **Anforderung** | Das System soll Datengebern die Möglichkeit bieten, ihre Einwilligungen und Rechte bezüglich der Nutzung ihrer Daten in gängige Formate wie Json zu ex- und importieren um eine leichte Austauschbarkeit der Datentreuhänder zu gewährleisten. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Zugangstreuhand; Fremdbestimmte Auswertungstreuhand |
| **Beschreibung** | Siehe QA-19 |
| **Wichtigkeit** | Optional |
| **Alternativen** |     |

SA45. Authentifizierung
----------------------------

| **ID** | **SA45** |
| --- | --- |
| **Name** | Authentifizierung |
| **Anforderung** | Das System muss fähig sein Nutzer anhand von Authentifizierungsmechanismen zu identifizieren (Passwort, SmartCard, Fingerabdruck,...) um die Authentizität des Nutzers zu gewährleisten. |
| **Umzusetztende Qualitätseigenschaften** | Authenzität |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | \- identifikaition |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** |     |

SA46. Logging
------------------

| **ID** | **SA46** |
| --- | --- |
| **Name** | Logging |
| **Anforderung** | Das System muss fähig sein Aktionen, Events und Interaktionen zu protokollieren um im Falle eines Streitfalls, diese nachweisen zu können. |
| **Umzusetztende Qualitätseigenschaften** | Nachweisbarkeit/Nicht-Abstreitbarkeit (Non-repudiation); Verantwortlichkeit/Rechenschaftspflicht (Accountability) |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | \- Aktion/Event  <br>\- Datum + Zeit  <br>\- Identifikation  <br>\- Alter Stand  <br>\- Neuer Stand |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** |     |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA47** | Protokollierung |

SA47. Mean Time Between Failure
------------------------------------

| **ID** | **SA47** |
| --- | --- |
| **Name** | Mean Time Between Failure |
| **Anforderung** | Das System muss fähig sein eine Verfügbarkeit von X% im Jahresmittel bei einer maximalen Ausfalldauer von jeweils Y Stunden zu haben um eine Ausgereiftheit und Zuverlässigkeit des Systems zu gewährleisten. |
| **Umzusetztende Qualitätseigenschaften** | Ausgereifheit |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | keine Daten. |
| **Wichtigkeit** | Optional |
| **Alternativen** |     |

SA48. Verschlüsselte übertragung
-------------------------------------

| **ID** | **SA48** |
| --- | --- |
| **Name** | Verschlüsselte übertragung |
| **Anforderung** | Das System soll Daten verschlüsselt übertragen um es keinen unberechtigten Personen zu ermöglichen auf die Daten zuzugreifen. |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | Bei der Verschlüsselung wird der Datentreuhänder als vertrauenswürdige Instanz angesehen werden, er könnte somit die Schlüssel verwalten. |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** |     |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA43** | Verschlüsselung |

SA49. Verschlüsselte Speicherung
-------------------------------------

| **ID** | **SA49** |
| --- | --- |
| **Name** | Verschlüsselte Speicherung |
| **Anforderung** | Das System soll die Daten der Nutzer in verschlüsselter Form speichern um den Zugriff und das Auslesen der Daten nur festgelegten Parteien zu erlauben |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Alle |
| **Beschreibung** | Potentiell gibt es Datentreuhänder, die die Daten selbst nicht einsehen können (=> Keine Auswertungstreuhand). In der Regel wird der Datentreuhänder aber als vertrauenswürdige Instanz angesehen, der die Daten sehen kann. |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** |     |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA43** | Verschlüsselung |

SA51. Auswahl von Datenfreigaben
-------------------------------------

| **ID** | **SA51** |
| --- | --- |
| **Name** | Auswahl von Datenfreigaben |
| **Anforderung** | Das System soll Datennutzern ermöglichen einen oder mehrere Datengeber anhand öffentlicher Freigaben auszuwählen um zu ermöglichen Daten von diesen zu erhalten |
| **Umzusetztende Qualitätseigenschaften** |     |
| **Relevant für diese DTH-Arten** | Fremdbestimmte Auswertungstreuhand; Zugangstreuhand |
| **Beschreibung** | Ein Datennutzer durchsucht die Liste öffentlicher Freigaben (SA-31) und sucht sich passende Datengeber/Daten aus. |
| **Wichtigkeit** | Obligatorisch |
| **Alternativen** | Öffentliche Datenanfrage |

### Zugehörige DTH-Anforderungen

| **ID** | **Name** |
| --- | --- |
| **DTH-FA45** | Datenvermittlung auf Anfrage des Datennutzers |