# Methoden zur Erhebung domänenspezifischer Anforderungen

<a name="nutzen"></a>
## Motivation

Der Inhalt dieser Seite richtet sich primär an Projektmanager und Requirement Engineers und soll bei der Erhebung von Anforderungen eines Datentreuhänders helfen. Insbesondere sollen Ideen und Anreize geschaffen werden, um Workshops und andere Formate aufzusetzen und dadurch domänenspezifische Anforderungen zu identifizieren.

<a name="workshops"></a>
## Workshops (Inhalte und Ziele)

Die folgende Sektion beschreibt mehrere Methodiken zur Erhebung von datentreuhänderrelevanten Daten und den korrelierenden Anforderungen, die je nach Ergebnis, dem Anwendungsfall entsprechend angepasst werden müssen.

Die ersten zwei Sektionen beschreiben potentielle Workshops, mit deren Hilfe ein besseres Verständnis über den zu entwickelnden Datentreuhänder und seine Anforderungen gefördert wird. Das Ziel des ersten Workshops ist ein vollumfänglicher Überblick über den As-Is Zustand zu erlangen und dabei verschiedenste Fragen über Daten und ihren Austausch zu beantworten. Der zweite Workshop dient dazu sich Gedanken über die Zukunft zu machen (To-Be) und dabei diverse Fragen zu verschiedenen Einschränkungen, Modellen und Eigenschaften des neuen Datentreuhänders zu beantworten.  
Beide Sektionen folgen dabei folgender Struktur:

1.  Schritte
    -  Die konkreten Schritte, die in dem Workshop bearbeitet werden sollten und ggf. Vorschläge zur Art der Umsetzung der Schritte
2.  Fragen, die währenddessen beantwortet werden sollen:
    -  Diese Fragen sind besonders wichtig für die Person(en), die den Workshop oder ein anderes Format ausrichten, da sie dabei helfen, den Fokus auf wichtige Themenpunkte zu behalten. Zusätzlich gibt es Vorschläge für Templates, um wichtige Fragen zu erarbeiten und festzuhalten.
3.  Gesamtziel
4.  Korrelierende Anforderungen
    -  Der vierte Punkt bezieht sich auf die Anforderungen des [generischen Anforderungskatalogs](<../Generischer Anforderungskatalog/>) und gibt Hinweise auf mögliche Verknüpfungen und Beziehungen zwischen den Workshopinhalten und den in Interviews erhobenen Anforderungen.

Die dritte Sektion befasst sich mit Features, Funktionalitäten und anderen Inhalten, die dem Datentreuhänder einen Mehrwert bieten können, aber nicht zwangsläufig notwendig sind, um diesen nutzbar zu machen. Beispielhaft werden hierzu ein paar mögliche Anknüpfungspunkte genannt.

In der letzten Sektion geht es um den Vergleich und die Analyse des Marktes. Sowohl andere Datentreuhänder, als auch andere Systeme in der eigenen Domäne sollten in diesem Schritt betrachtet und mit dem zukünftigen System verglichen werden. Dazu werden bestimmte Aspekte hervorgehoben und auf ihre Vor- und Nachteile hin untersucht.

Jede der folgenden Thematiken kann in einem oder mehreren Workshops adressiert werden. Dies sollte je nach Anwendungsfall und den Teilnehmern entschieden werden.
<a name="kategorisierung"></a>
### Erstellung einer Kategorisierung der Daten, die der Datentreuhänder verwalten soll

1.  **_Schritte_**:
    1.  Prozess(e) As-Is aufmalen
        -  Falls es mehrere relevante Parteien gibt, die unterschiedliche Prozesse (und unterschiedliche Daten) beschreiben, sollten diese jeweils einzeln erfasst werden, aber auch als Gesamtheit betrachtet werden.
        -  Beispiele für Modellierungstechniken:
            -  Business Model Processing Notation (BPMN)  [https://www.bpmn.de/lexikon/bpmn/](https://www.bpmn.de/lexikon/bpmn/)
            -  Unified Modeling Language (UML) Aktivitätsdiagramme
2.  _**Fragen, die währenddessen beantwortet werden sollen**_:
    -  Welche Daten fließen und welche Art von Daten sind es?
    -  Wie werden Daten in der Regel gespeichert und von wem?  
        -  Datentreuhänder selbst
        -  Dritte Partei
        -  Nur der Provider und der DTH fungiert als Mittelsmann
    -  Wer nutzt die Daten und wozu?
    -  Wer gibt die Daten weiter und warum?
    -  Gibt es existierende standardisierte Formate der Daten?
    -  <img src='Bilder/Daten.jpg' width=500> [PDF](Bilder/Daten.pdf)
3.  _**Gesamtziel**_:
    -  Es soll ein klares Verständnis der Daten, die in dem Datentreuhänder fließen, geschaffen und alle Rahmenbedingungen geklärt oder zumindest erfasst werden.
4.  _**Korrelierende Anforderungen**_:
    -  Anpassung der Schnittstellen (FA54, SA32, QA12)
        -  Interoperabilität (QA1)
    -  Anpassung der Datensammlung (FA15, SA9, SA33, SA10, SA34, QA13, QA14)
    -  Anpassung der Daten- und Maschinenerfassung (FA58, FA55, SA28, SA1)
    -  Anpassung der Datendarstellung (FA17, SA13, SA4)
    -  Anpassung der Aggregation der Daten (FA28, SA39, SA40)
<a name="rahmen"></a>
### Den Rahmen des Datentreuhänders (-Modell) bestimmen

1.  _**Schritte**_;
    1.  Hauptanwendungsfälle als Beispiele um To-Be zu bestimmen
        -  Beispielmethode zum Entwerfen des Systems:
            -   Tangible Ecosystem Design-Workshop
                1.  Mögliche Anwendungsszenarien durchgehen und die Aktivitäten der Nutzer und des Systems an verschiedenen Stellen bestimmen
                2.  Interaktionen und Flüsse der verschiedenen Parteien bestimmen
                    -  Geldfluss, Assetfluss, Datenfluss, Vertragsfluss, Softwarefluss,
                3.  Nutzen für die Partizipation der einzelnen Parteien bestimmen
                    -  Wer kriegt was von wem?
2.  **_Fragen, die währenddessen beantwortet werden sollen_**:
    -  Wer trifft die Entscheidungen über den Datenaustausch? (Datengeber, Datentreuhänder)
        - Wie sollen Daten zu dem Datentreuhänder kommen?
        - Wie sollen Daten zu dem Datennutzer kommen?
    -  Welchen gesetzlichen, betrieblich, organisatorischen Einschränkungen unterlieg(t)[en]
        -  die Daten?
        -  das Unternehmen?
        -  die Datengeber und betroffenen Personen/Unternehmen?
        -  der Datentreuhänder?
    -  Welches Geschäftsmodell soll verfolgt werden? :warning: (siehe auch [Geschäftsmodellalternativen](<>../..Angebotsentwicklung/Geschäftsmodellalternativen>)) :warning:
        -  Ertragsmodell
            -   Bsp.: Konsumenten zahlen einen festen wiederkehrenden Beitrag an die Plattform (50€/Monat)
            -   Eine Hilfestellung bietet dabei die Taxonomie für Ertragsmodelle  [https://www.researchgate.net/publication/374869140_A_Taxonomy_for_Platform_Revenue_Models_An_Empirical-to-Conceptual_Development_Approach](https://www.researchgate.net/publication/374869140_A_Taxonomy_for_Platform_Revenue_Models_An_Empirical-to-Conceptual_Development_Approach)  bzw.  [https://link.springer.com/chapter/10.1007/978-3-031-46587-1_11                                                                                                                                                                                                   ](https://link.springer.com/chapter/10.1007/978-3-031-46587-1_11)                                                                                                                                                                                                    
        -  Wertversprechen
            -   Bsp.: Aggregierte Statistiken zur Nutzung von (smarten) Fitnessgeräten und deren Nutzer, zentral, schnell und von überall
        -  Wertschöpfung
            -   Bsp.: Konsumenten erhalten Daten zu Nutzern und können damit bessere Dienste für die Nutzer anbieten und attraktive Angebote bereitstellen
    -  Welchem Modell kann der Datentreuhänder zugeordnet werden?
        -  Zugangstreuhand
            -   Speicherung von Daten zentral oder dezentral
            -   Ausführung von Fremdweisungen
            -   Bsp.: Data Escrow
        -  Verwaltungstreuhand
            -   Speicherung von Daten zentral oder dezentral
            -   Datenumgang und Vermittlung ist eigene Entscheidung der Datentreuhand
            -   Bsp.: Selbstbeschränkungstreuhand
        -  Auswertungstreuhand
            -   Speicherung von Daten zentral oder dezentral
            -   Verarbeitung der Daten z.b. Anonymisierung, Pseudonymisierung, Aufbereitung und Analyse, ...
                -   in der Regel keine Rohdatenweitergabe, sondern nur verarbeitete Daten
            -   Selbstbestimmt
                -   Datenumgang und Vermittlung ist eigene Entscheidung der Datentreuhand
            -   Fremdbestimmt
                -   Datengeber entscheiden selbst über den Umgang ihrer Daten
        - <img src='Bilder/Vorstellung konkretisieren.jpg' width=500>[PDF](<Bilder/Vorstellung konkretisieren.pdf>)
3.  _**Gesamtziel**_:
    -  Einen Überblick über das zu entstehende System erhalten und etwaige rechtliche und ökonomische Fragestellungen und Anforderungen identifizieren
4.  _**Korrelierende Anforderungen**_:
    -  Gesetzliche Einschränkungen
        -  Je nach Domäne können die gesetzlichen Regularien den Datentreuhänder noch weiter einschränken. Dies gilt insbesondere für kritische Daten wie Gesundheitsdaten oder Finanzdaten
            -  Vertragsvorlagen (FA50, FA51)
            -  Speicherung (Wer speichert Daten und wo?)
                -  Redundanz (FA37, SA24)
            -  Protokollierung (FA47, FA48, SA8, SA46)
            -  Patches (FA13)
            -  automatische Datenlöschung (FA52, SA38)
    -  Datenvermittlung
        -  Je nach Domäne kann sich die Art, wie Daten vermittelt werden und wer über diese bestimmt ändern
            -  (FA19, FA44, FA45, FA49, SA14, SA16, SA17, SA25, SA26, SA31, SA41, SA51)
    -  Verarbeitung und Auswertung
        -  Je nach Anwendungsfall soll der Datentreuhänder gewisse Anpassungen oder Auswertungen auf den Daten ausführen
            -  Anonymisierung, Pseudonymisierung (FA41, FA42)
            -  Datentransformation (FA36, QA13)
            -  Datenaggregation (FA28, SA39, SA40)

<a name="zusatz"></a>
### Zusätzliche Features/Funktionen/Bedingungen erfassen und bewerten
<img src='Bilder/Additional Feature.jpg' width=300>[PDF](<Bilder/Additional Feature.pdf>)

1.  **Bezahlung**
    -  Zum Beispiel: Der Datentreuhänder bietet (Online-)Zahlungen über die Plattform an
2.  **Verfügbarkeit (QA8)**
    -  Der Datentreuhänder ist über einen Zeitraum von X Monaten Y% der Zeit erreichbar
        -  Bsp. Der DTH ist über einen Zeitraum von 24 Monaten 99% der Zeit erreichbar
3.  **Transparenz (QA16)**
    -  Geldflüsse des Datentreuhänders werden offen gelegt
    -  Nutzungsstatistiken
    -  Zweck der Nutzungen
    -  Art der Nutzer
    -  ...
4.  **Skalierbarkeit (QA17)**
    -  Der Datentreuhänder kann X viele Provider und Y viele Konsumenten zuverlässig unterstützen
5.  Konformität mit GaiaX (QA20)

<a name="marktvergleich"></a>
### Marktvergleich/-analyse - was macht den neuen Datentreuhänder besonders?
<img src='Bilder/Marktanalyse.jpg' width=500>[PDF](Bilder/Marktanalyse.pdf)

1.  Bsp. für Besonderheiten:
    -  Ist die erste (Datentreuhänder-)Plattform für diese Art von Daten
    -  Besitzt extra Funktionalitäten, die den Treuhänder hervorstechen lassen (z.B. eine Aufbereitung der Daten in einer Übersicht und eine Handlungsempfehlung basierend darauf)
    -  Hat Attribute mit besonderer Ausprägung z.B. besonders günstig
2.  Auflistung der
    -  bekanntesten Kompetitoren und anderen relevanten Systeme der Domäne
    -  Vorteile und Nachteile gegenüber diesen
    -  abgeleitete Anforderungen, Probleme und Herausforderungen
