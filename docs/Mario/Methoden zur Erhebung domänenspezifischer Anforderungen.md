# Inhaltsverzeichnis
- [Überschrift](#ueberschrift)
  - [Nutzen](#nutzen)
  - [Workshops](#workshops)
    - [Erstellung einer Kategorisierung der Daten](#kategorisierung)
    - [Den Rahmen des Datentreuhänders (-Modell) bestimmen](#rahmen)
    - [Zusätzliche Features/Funktionen/Bedingungen erfassen und bewerten](#zusatz)
    - [Marktvergleich/-analyse - was macht den neuen Datentreuhänder besonders?](#marktvergleich)
<a name="ueberschrift"></a>
# Methoden zur Erhebung domänenspezifischer Anforderungen

<a name="nutzen"></a>
## Nutzen

Der Inhalt dieser Seite richtet sich primär an Projektmanager und Requirement Engineers und soll bei der Erhebung von Anforderungen eines Datentreuhänders helfen. Insbesondere sollen Ideen und Anreize geschaffen werden, um Workshops und andere Formate aufzusetzen und dadurch domänenspezifische Anforderungen zu identifizieren.

<a name="workshops"></a>
## Workshops (Inhalte und Ziele)

Die folgende Sektion beschreibt mehrere Methodiken zur Erhebung von datentreuhänderrelevanten Daten und den korrelierenden Anforderungen, die je nach Ergebnis, dem Anwendungsfall entsprechend angepasst werden müssen.

Die ersten zwei Sektionen beschreiben potentielle Workshops, mit deren Hilfe ein besseres Verständnis über den zu entwickelnden Datentreuhänder und seine Anforderungen gefördert wird. Das Ziel des ersten Workshops ist ein vollumfänglicher Überblick über den As-Is Zustand zu erlangen und dabei verschiedenste Fragen über Daten und ihren Austausch zu beantworten. Der zweite Workshop dient dazu sich Gedanken über die Zukunft zu machen (To-Be) und dabei diverse Fragen zu verschiedenen Einschränkungen, Modellen und Eigenschaften des neuen Datentreuhänders zu beantworten.  
Beide Sektionen folgen dabei folgender Struktur:

1.  Schritte
    1.  Die konkreten Schritte, die in dem Workshop bearbeitet werden sollten und ggf. Vorschläge zur Art der Umsetzung der Schritte
2.  Fragen, die währenddessen beantwortet werden sollen:
    1.  Diese Fragen sind besonders wichtig für die Person(en), die den Workshop oder ein anderes Format ausrichten, da sie dabei helfen, den Fokus auf wichtige Themenpunkte zu behalten. Zusätzlich gibt es Vorschläge für Templates, um wichtige Fragen zu erarbeiten und festzuhalten.
3.  Gesamtziel
4.  Korrelierende Anforderungen
    1.  Der vierte Punkt bezieht sich auf die Anforderungen des [generischen Anforderungskatalog](Generischer%20Anforderungskatalog.md) und gibt Hinweise auf mögliche Verknüpfungen und Beziehungen zwischen den Workshopinhalten und den in Interviews erhobenen Anforderungen.

Die dritte Sektion befasst sich mit Features, Funktionalitäten und anderen Inhalten, die dem Datentreuhänder einen Mehrwert bieten können, aber nicht zwangsläufig notwendig sind, um diesen nutzbar zu machen. Beispielhaft werden hierzu ein paar mögliche Anknüpfungspunkte genannt.

In der letzten Sektion geht es um den Vergleich und die Analyse des Marktes. Sowohl andere Datentreuhänder, als auch andere Systeme in der eigenen Domäne sollten in diesem Schritt betrachtet und mit dem zukünftigen System verglichen werden. Dazu werden bestimmte Aspekte hervorgehoben und auf ihre Vor- und Nachteile hin untersucht.

Jede der folgenden Thematiken kann in einem oder mehreren Workshops adressiert werden. Dies sollte je nach Anwendungsfall und den Teilnehmern entschieden werden.
<a name="kategorisierung"></a>
### Erstellung einer Kategorisierung der Daten, die der Datentreuhänder verwalten soll

1.  **_Schritte_**:
    1.  Prozess(e) As-Is aufmalen
        1.  Falls es mehrere relevante Parteien gibt, die unterschiedliche Prozesse (und unterschiedliche Daten) beschreiben, sollten diese jeweils einzeln erfasst werden, aber auch als Gesamtheit betrachtet werden.
        2.  Beispiele für Modellierungstechniken:
            1.  Business Model Processing Notation (BPMN)  [https://www.bpmn.de/lexikon/bpmn/](https://www.bpmn.de/lexikon/bpmn/)
            2.  Unified Modeling Language (UML) Aktivitätsdiagramme
2.  _**Fragen, die währenddessen beantwortet werden sollen**_:
    1.  Welche Daten fließen und welche Art von Daten sind es?
    2.  Wie werden Daten in der Regel gespeichert und von wem?  
        1.  Datentreuhänder selbst
        2.  Dritte Partei
        3.  Nur der Provider und der DTH fungiert als Mittelsmann
    3.  Wer nutzt die Daten und wozu?
    4.  Wer gibt die Daten weiter und warum?
    5.  Gibt es existierende standardisierte Formate der Daten?
    6.  <img src='Erhebung%20dom%C3%A4nenspezifischer%20Anforderungen/Erhebung%20dom%C3%A4nenspezifischer%20Anforderungen%20-%20Daten.jpg' width=500> [PDF](Erhebung%20dom%C3%A4nenspezifischer%20Anforderungen/Erhebung%20dom%C3%A4nenspezifischer%20Anforderungen%20-%20Daten.pdf)
3.  _**Gesamtziel**_:
    1.  Es soll ein klares Verständnis der Daten, die in dem Datentreuhänder fließen, geschaffen und alle Rahmenbedingungen geklärt oder zumindest erfasst werden.
4.  _**Korrelierende Anforderungen**_:
    1.  Anpassung der Schnittstellen (FA54, SA32, QA12)
        1.  Interoperabilität (QA1)
    2.  Anpassung der Datensammlung (FA15, SA9, SA33, SA10, SA34, QA13, QA14)
    3.  Anpassung der Daten- und Maschinenerfassung (FA58, FA55, SA28, SA1)
    4.  Anpassung der Datendarstellung (FA17, SA13, SA4)
    5.  Anpassung der Aggregation der Daten (FA28, SA39, SA40)
<a name="rahmen"></a>
### Den Rahmen des Datentreuhänders (-Modell) bestimmen

1.  _**Schritte**_;
    1.  Hauptanwendungsfälle als Beispiele um To-Be zu bestimmen
        1.  Beispielmethode zum Entwerfen des Systems:
            -   Tangible Ecosystem Design-Workshop
                1.  Mögliche Anwendungsszenarien durchgehen und die Aktivitäten der Nutzer und des Systems an verschiedenen Stellen bestimmen
                2.  Interaktionen und Flüsse der verschiedenen Parteien bestimmen
                    1.  Geldfluss, Assetfluss, Datenfluss, Vertragsfluss, Softwarefluss,
                3.  Nutzen für die Partizipation der einzelnen Parteien bestimmen
                    1.  Wer kriegt was von wem?
2.  **_Fragen, die währenddessen beantwortet werden sollen_**:
    1.  Wer trifft die Entscheidungen über den Datenaustausch? (Datengeber, Datentreuhänder)
        1.  Wie sollen Daten zu dem Datentreuhänder kommen?
        2.  Wie sollen Daten zu dem Datennutzer kommen?
    2.  Welchen gesetzlichen, betrieblich, organisatorischen Einschränkungen unterlieg(t)[en]
        1.  die Daten?
        2.  das Unternehmen?
        3.  die Datengeber und betroffenen Personen/Unternehmen?
        4.  der Datentreuhänder?
    3.  Welches Geschäftsmodell soll verfolgt werden? :warning:(siehe auch [Geschäftsmodellalternativen](https://ras.iese.de/confluence/pages/viewpage.action?pageId=499417109)) :warning:
        1.  Ertragsmodell
            -   Bsp.: Konsumenten zahlen einen festen wiederkehrenden Beitrag an die Plattform (50€/Monat)
            -   Eine Hilfestellung bietet dabei die Taxonomie für Ertragsmodelle  [https://www.researchgate.net/publication/374869140_A_Taxonomy_for_Platform_Revenue_Models_An_Empirical-to-Conceptual_Development_Approach](https://www.researchgate.net/publication/374869140_A_Taxonomy_for_Platform_Revenue_Models_An_Empirical-to-Conceptual_Development_Approach)  bzw.  [https://link.springer.com/chapter/10.1007/978-3-031-46587-1_11                                                                                                                                                                                                   ](https://link.springer.com/chapter/10.1007/978-3-031-46587-1_11)                                                                                                                                                                                                    
        2.  Wertversprechen
            -   Bsp.: Aggregierte Statistiken zur Nutzung von (smarten) Fitnessgeräten und deren Nutzer, zentral, schnell und von überall
        3.  Wertschöpfung
            -   Bsp.: Konsumenten erhalten Daten zu Nutzern und können damit bessere Dienste für die Nutzer anbieten und attraktive Angebote bereitstellen
    4.  Welchem Modell kann der Datentreuhänder zugeordnet werden?
        1.  Zugangstreuhand
            -   Speicherung von Daten zentral oder dezentral
            -   Ausführung von Fremdweisungen
            -   Bsp.: Data Escrow
        2.  Verwaltungstreuhand
            -   Speicherung von Daten zentral oder dezentral
            -   Datenumgang und Vermittlung ist eigene Entscheidung der Datentreuhand
            -   Bsp.: Selbstbeschränkungstreuhand
        3.  Auswertungstreuhand
            -   Speicherung von Daten zentral oder dezentral
            -   Verarbeitung der Daten z.b. Anonymisierung, Pseudonymisierung, Aufbereitung und Analyse, ...
                -   in der Regel keine Rohdatenweitergabe, sondern nur verarbeitete Daten
            -   Selbstbestimmt
                -   Datenumgang und Vermittlung ist eigene Entscheidung der Datentreuhand
            -   Fremdbestimmt
                -   Datengeber entscheiden selbst über den Umgang ihrer Daten
        4. <img src='Erhebung%20dom%C3%A4nenspezifischer%20Anforderungen/Erhebung%20dom%C3%A4nenspezifischer%20Anforderungen%20-%20Vorstellung%20konkretisieren.jpg' width=500>[PDF](Erhebung%20dom%C3%A4nenspezifischer%20Anforderungen/Erhebung%20dom%C3%A4nenspezifischer%20Anforderungen%20-%20Vorstellung%20konkretisieren.pdf)
3.  _**Gesamtziel**_:
    1.  Einen Überblick über das zu entstehende System erhalten und etwaige rechtliche und ökonomische Fragestellungen und Anforderungen identifizieren
4.  _**Korrelierende Anforderungen**_:
    1.  Gesetzliche Einschränkungen
        1.  Je nach Domäne können die gesetzlichen Regularien den Datentreuhänder noch weiter einschränken. Dies gilt insbesondere für kritische Daten wie Gesundheitsdaten oder Finanzdaten
            1.  Vertragsvorlagen (FA50, FA51)
            2.  Speicherung (Wer speichert Daten und wo?)
                1.  1.  Redundanz (FA37, SA24)
            3.  Protokollierung (FA47, FA48, SA8, SA46)
            4.  Patches (FA13)
            5.  automatische Datenlöschung (FA52, SA38)
    2.  Datenvermittlung
        1.  Je nach Domäne kann sich die Art, wie Daten vermittelt werden und wer über diese bestimmt ändern
            1.  (FA19, FA44, FA45, FA49, SA14, SA16, SA17, SA25, SA26, SA31, SA41, SA51)
    3.  Verarbeitung und Auswertung
        1.  Je nach Anwendungsfall soll der Datentreuhänder gewisse Anpassungen oder Auswertungen auf den Daten ausführen
            1.  Anonymisierung, Pseudonymisierung (FA41, FA42)
            2.  Datentransformation (FA36, QA13)
            3.  Datenaggregation (FA28, SA39, SA40)

<a name="zusatz"></a>
### Zusätzliche Features/Funktionen/Bedingungen erfassen und bewerten
<img src='Erhebung%20dom%C3%A4nenspezifischer%20Anforderungen/Erhebung%20dom%C3%A4nenspezifischer%20Anforderungen%20-%20Additional%20Feature.jpg' width=300>[PDF](Erhebung%20dom%C3%A4nenspezifischer%20Anforderungen/Erhebung%20dom%C3%A4nenspezifischer%20Anforderungen%20-%20Additional%20Feature.pdf)

1.  **Bezahlung**
    1.  Zum Beispiel: Der Datentreuhänder bietet (Online-)Zahlungen über die Plattform an
2.  **Verfügbarkeit (QA8)**
    1.  Der Datentreuhänder ist über einen Zeitraum von X Monaten Y% der Zeit erreichbar
        1.  Bsp. Der DTH ist über einen Zeitraum von 24 Monaten 99% der Zeit erreichbar
3.  **Transparenz (QA16)**
    1.  Geldflüsse des Datentreuhänders werden offen gelegt
    2.  Nutzungsstatistiken
    3.  Zweck der Nutzungen
    4.  Art der Nutzer
    5.  ...
4.  **Skalierbarkeit (QA17)**
    1.  Der Datentreuhänder kann X viele Provider und Y viele Konsumenten zuverlässig unterstützen
5.  Konformität mit GaiaX (QA20)

<a name="marktvergleich"></a>
### Marktvergleich/-analyse - was macht den neuen Datentreuhänder besonders?
<img src='Erhebung%20dom%C3%A4nenspezifischer%20Anforderungen/Erhebung%20dom%C3%A4nenspezifischer%20Anforderungen.jpg' width=500>[PDF](Erhebung%20dom%C3%A4nenspezifischer%20Anforderungen/Erhebung%20dom%C3%A4nenspezifischer%20Anforderungen.jpdf)

1.  Bsp. für Besonderheiten:
    1.  Ist die erste (Datentreuhänder-)Plattform für diese Art von Daten
    2.  Besitzt extra Funktionalitäten, die den Treuhänder hervorstechen lassen (z.B. eine Aufbereitung der Daten in einer Übersicht und eine Handlungsempfehlung basierend darauf)
    3.  Hat Attribute mit besonderer Ausprägung z.B. besonders günstig
2.  Auflistung der
    1.  bekanntesten Kompetitoren und relevante Systemen der Domäne
    2.  Vorteile und Nachteile gegenüber diesen
    3.  abgeleitete Anforderungen, Probleme und Herausforderungen
