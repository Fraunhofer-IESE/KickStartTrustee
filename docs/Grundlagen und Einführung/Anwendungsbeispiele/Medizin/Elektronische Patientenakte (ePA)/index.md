# Elektronische Patientenakte (ePA)

-   [Problemstellung](#problemstellung)
-   [Ziele und Werteversprechen](#ziele)
-   [DTH-Satzschablone](#schablone)
-   [Domäne](#domaene)
-   [Involvierte Personen/Unternehmen](#involv)
-   [Modell](#modell)
-   [Art der Daten](#daten)
-   [Speicherung](#speicherung)
-   [Übermittlung](#uebermittlung)
-   [Finanzierung](#finanzierung)

https://www.bundesgesundheitsministerium.de/elektronische-patientenakte

<a name="problemstellung"></a>
## Problemstellung

**Eine ePA kann nur dann wie eine „kooperative Gesundheitsmanagement-Plattform“ wirken, wenn sie in ein komplexes System aus Kommunikations- und Zugriffskomponenten eingebettet ist.**

**Digitale Identitäten** spielen eine wesentliche Rolle für ein sicheres digitales Gesundheitswesen. Dafür muss in einem ersten Schritt organisatorisch gewährleistet sein, dass Personen, die eine digitale Identität erhalten sollen, überhaupt existieren.

Bei der Einführung einer elektronischen Patientenakte (ePA) steht der Gesetzgeber vor einem Zielkonflikt. Einerseits erscheint es sinnvoll, dass so viele Versicherte wie möglich an der TI teilnehmen und eine ePA anlegen. Denn nur mit einer hohen Nutzerzahl werden sich die Kosten und der aufwendige Betrieb der TI auf Dauer rentieren. Andererseits könnten es viele Menschen als Übergriff in ihre Selbstbestimmung und Privatsphäre auffassen, wenn der Staat sie zwingt, eine ePA zu nutzen. Das hat der Gesetzgeber auch nicht vor: Bislang begnügt er sich damit, **den Patienten einen Rechtsanspruch darauf zu geben, dass die Krankenkasse eine ePA bereitstellt. Wer eine ePA nicht ausdrücklich beantragt, erhält also auch nicht die**  
**erforderliche Infrastruktur.**

<a name="ziele"></a>
## Ziele und Werteversprechen

Die elektronische Patientenakte dient den Versicherten  
sowohl als Schaufenster in ihre  **Behandlungshistorie**  als auch als ,,Cockpit", um ihre  **Datensouveränität**  wahrzunehmen.

So lassen sich Doppeluntersuchungen vermeiden,  
wenn Befunde unterschiedlicher Leistungserbringer oder die Behandlungshistorie digital abrufbar sind.

Hinzu kommen schnellere Wege beim Austausch medizinischer  
Informationen: Laborbefunde, MRT- oder Röntgenbilder können in Sekundenschnelle vom Fach- zum Hausarzt gelangen und dort maschinenlesbar direkt in eine Datenanalyse einfließen.

Ein elektronischer Medikationsplan kann dazu beitragen, Unverträglichkeiten früh zu bemerken und ungewünschte Nebenwirkungen zu vermeiden.

Hinzu kommt, dass Patienten beim Wechsel des Hausarzts ihre kompletten Behandlungsinhalte mitnehmen könnten.

In Notfallsituationen könnte eine ePA sogar Leben retten, wenn Informationen, etwa über Allergien oder Medikamente, digital hinterlegt sind.

Sie sollte unterschiedliche Funktionen eines Datentreuhänders erfüllen. Dazu gehören insbesondere ein  **Identitäts und Berechtigungsmanagement**, sowie ein  **Einwilligungs- und Zugriffsmanagement**.

Jede ePA-Lösung sollte die folgenden Merkmale aufweisen:  
• ein nutzerfreundliches Frontend,  
• ein hohes Maß an Datensicherheit,  
• ein zuverlässiges Rechte-, Zugriffs- und Einwilligungsmanagement und  
• eine nahtlose standardisierte Anbindung an die Vertrauensarchitektur TI (Darüber hinaus müssen sie mit den  **sonstigen Bestandteilen der Telematikinfrastruktur kompatibel sein und über interoperable Schnittstellen verfügen**).

<a name="schablone"></a>
## DTH-Satzschablone

ePA  **bietet** **vertraulichen und digitalen** Zugang  **zu**  Gesundheitsdaten  **der** Patienten  **zur Verfügung gestellt von**  Arztpraxen, Laboren und Krankenhäusern  **für**  Arztpraxen, Labore und Krankenhäuser.

<a name="domaene"></a>
## Domäne

Medizin

<a name="involv"></a>
## Involvierte Personen/Unternehmen

-   Datengeber, Datennutzer:
    -   Arztpraxen, Labore, Krankenhäuser
-   betroffene Person/Unternehmen:
    -   Patienten (Bürger)

<a name="modell"></a>
## Modell

-   Selbstbestimmte Auswertungstreuhand
    -   Wie werden Daten weiterverarbeitet?
-   Fremdbestimmte Auswertungstreuhand
    -   Wie werden Daten weiterverarbeitet?
-   Zugangstreuhand
-   Verwaltungstreuhand

<a name="daten"></a>
## Art der Daten

Gesundheitsdaten

-   Gesundheitlicher Zustand
-   Krankheiten
-   Behandlungen
-   Medikamente
-   Allergien
-   ...

<a name="speicherung"></a>
## Speicherung

"virtuelle“ ePA: In der ePA selbst sind keine medizinischen Daten gespeichert. Vielmehr steuert der Einzelne über sie nur den **Zugriff auf die im Gesundheitssystem verteilten Daten**.

<a name="uebermittlung"></a>
## Übermittlung

Vertrauensdienste und **Verschlüsselung**: In einem digitalen System bedarf es hoher Standards, um die Kommunikationswege abzusichern und zu überprüfen, ob elektronisch zirkulierende Dokumente authentisch sind. Dafür **müssen die jeweiligen Empfänger über vertrauensvolle Zertifikate verfügen**. Durch die **eIDAS-Verordnung** ist es gelungen, einen EU-weiten hohen Standard für Vertrauensdienste zu etablieren. Ziel ist es, die Integrität zu übermittelnder Daten zu schützen, Manipulation zu verhindern und nicht zuletzt ein Online-Pendant für die Unterschrift auf Papier zu bieten. Die Studie empfiehlt eine stärkere Verankerung der eIDAS-Verordnung in den Sozialgesetzbüchern,um elektronische Signaturen, Siegel und Webseitenzertifikate standardisiert und rechtssicher im deutschen Gesundheitswesen nutzen zu können. Darüber hinaus sollten bei der Datenablage und der Anbindung an die TI neueste Verschlüsselungstechnologien zum Einsatz kommen.

<a name="finanzierung"></a>
## Finanzierung

Aus Sicht der Versicherten muss ihnen die elektronische Patientenakte **als staatliche Leistung kostenlos zur Verfügung stehen**.