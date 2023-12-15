# Datentreuhänder-Landkarte

Aus unserer bewusst recht abstrakten Definition geht bereits hervor, das Datentreuhandmodelle sich stark von einander unterscheiden - je nach der konkreten Domäne und dem konkreten Anwendungsfall. Die verschiedenen Ausprägungen oder Archetypen von Datentreuhändern, die sich in einzelnen Funktionen und Fähigkeiten unterscheiden, wollen wir in dieser "Landkarte" einordnen. Dazu stellen wir die verschiedenen Unterscheidungsmerkmale vor und ordnen reale Datentreuhandmodelle in die jeweiligen Kategorien ein. Die Landkarte soll also einen Überblick ermöglichen, Beispiele aufzeigen und als Grundlage zur Einordnung des eigenen, anvisierten Datentreuhandmodells dienen.

Wir unterscheiden in unserer Landkarte drei Ebenen:

1.  Die  _Modellebene_  ordnet Datentreuhänder anhand ihres grundsätzlichen Aufbaus (_"Welches Modell wird umgesetzt?"_) und der Art der Datenübermittlung ein (_"Wie werden Daten vermittelt?"_).
2.  Die  _Geschäftsebene_ beschäftigt sich mit den Parteien (_"Wer ist beteiligt?"_), deren Motivation (_"Welches Ziel hat der Datentreuhänder?"_) und der Finanzierung (_"Wer bezahlt im Datentreuhandmodell?"_).
3.  Die  _Daten- und Funktionsebene_ kategorisiert Datentreuhänder basierend auf den Schutzbedarfen (_"Warum sind die Daten schützenswert?"_) und den Funktionen, die der Datentreuhänder anbietet (_"Was bietet der Datentreuhänder zustätzlich der eigentlichen Vermittlung?"_).

Wir gehen im Folgenden auf diese drei Ebenen im Detail ein und nennen jeweils Beispiele für konkrete Datentreuhänder in den verschiedenen Kategorien.

Ziel bei der Anwendung des KickStartTrustee-Frameworks ist es, dass sich potentielle Datentreuhänder am Ende in diese Landkarte einordnen können.

# Modellebene

Auf der Modellebene zerlegen wir zunächst den Begriff "Datentreuhandmodell" in seine beiden Kernbestandteile: Die Daten, um die es im Kern geht, und das Treuhandmodell, welches die Aufgabe des Datentreuhänders schlussendlich definiert. Von diesen Kernbestandteilen hängen die Entscheidungen auf den späteren Ebenen ab.

## Daten

Daten sind das zentrale Element eines jeden Datentreuhandmodells. Dabei gibt es - technisch gesehen - verschiedene Arten, wie die Daten vom Datengeber zum Datennutzer gelangen.

### Kategorien

1.  **Zentraler Router:** Die derzeit verbreitetste Variante ist es, dass Datentreuhänder Daten bei Bedarf von Datengebern anfragen, annehmen, an die entsprechenden Datennutzer weitergeben und anschließend wieder löschen. Eine Analogie wäre in dem Fall die Post, welche Briefe vom Absender zum Empfänger zustellt.
    1.  Vorteile für die Datengeber: Daten werden beim Datentreuhänder nicht zustätzlich gespeichert.
    2.  Nachteile für die Datengeber: Der Datengeber muss bei jeder Anfrage die Daten zur Verfügung stellen, also erreichbar sein.
    3.  Vorteile für den Datentreuhänder: Geringere Speicherkosten; ggf. einfachere Umsetzung von Datenschutzauflagen; ggf. Vorteile bei der Korrektheit / Aktualität der Daten.
    4.  Nachteile für den Datentreuhänder: Keine Auswertungen oder Mehrwertdienste auf großen Datenbasen möglich; ggf. Nachteile bei der Verfügbarkeit.
    5.  Vorteile für die Datennutzer: Daten sind so aktuell wie sie sein können, da sie direkt vom Datengeber kommen.
    6.  Nachteile für die Datennutzer: Daten sind ggf. nicht direkt verfügbar; Auswertungen über Daten von mehreren Datengebern muss der Datennutzer selbst vornehmen.
2.  **Zentrale Datenhaltung:** Alternativ kann der Datentreuhänder die Daten auch "auf Vorrat" bei sich speichern.
    1.  Vorteile für die Datengeber: Der Datengeber muss nicht bei jeder Anfrage die Daten erneut schicken.
    2.  Nachteile für die Datengeber: Der Datentreuhänder hält eine Kopie der Daten.
    3.  Vorteile für den Datentreuhänder: Auswertungen oder Mehrwertdienste auf großen Datenbasen möglich.
    4.  Nachteile für den Datentreuhänder: Hohe Speicherkosten; Datenschutzauflagen und Mandantentrennung komplex.
    5.  Vorteile für die Datennutzer: Daten sind direkt verfügbar; Auswertungen über Daten von mehreren Datengebern kann der Datentreuhänder ggf. übernehmen.
    6.  Nachteile für die Datennutzer: Daten sind ggf. nicht aktuell.
3.  **Peer-to-Peer:** Die dritte Möglichkeit ist das Peer-to-Peer-Modell. Dabei übernimmt der Datentreuhänder lediglich eine vermittelnde Rolle ("Match-Making") - am Datenaustausch selbst ist er aber nicht beteiligt. Stattdessen werden die Daten direkt zwischen den Datengebern und Datennehmern ausgetauscht. Diese Möglichkeit ist in der Praxis selten anzufinden.
    1.  Vorteile für die Datengeber und Datennutzer: Daten müssen nicht über eine dritte Partei laufen.
    2.  Nachteile für die Datengeber und Datennutzer: Der Datengeber muss sich selbst um die sichere Übermittlung und die Einhaltung seiner Datensouveränitätsbedarfe kümmern; Sollen auch technische Maßnahmen zur Sicherstellung der Datensouveränität umgesetzt werden, ist dies in diesem Modell am kompexesten und bedarf spezieller Lösungen wie den IDS-Connectoren.
    3.  Vorteile für den Datentreuhänder: Technisch einfach umzusetzen.
    4.  Nachteile für den Datentreuhänder: Der Datentreuhänder ist am Datenaustausch selbst nicht beteiligt und kann daher keinerlei Dienste hierfür bereitstellen (z. B. Qualitätssicherung, Konvertierung oder Auswertungen)

### Beispiele

| Zentraler Router | Zentrale Datenhaltung | P2P         |
|------------------|-----------------------|-------------|
| BreedFides       | Ddtrust               | BreedFides  |
| Ddtrust          | TRANSIT               |             |
| DeRe             | MobiDataSol           |  MANDAT     |
| S3I-X            |                       |             |
| TreuMoDa         |                       |             |  

## Treuhandmodell

In unserem Framework unterscheiden wir drei grundlegende Modelle. Diese ergeben sich aus zwei Aspekten, nämlich wer über eine konkrete Datenfreigabe entscheidet und ob die Daten in ihrer Ursprungsform weitergegeben werden oder nicht.

### Kategorien

1.  **Zugangstreuhand:** Zugangsdatentreuhänder vermitteln auf Anweisung des Datengebers Daten. Der Datentreuhänder entscheidet also nicht selbst, sondern erhält eine Fremdanweisung, an die er gebunden ist. In diesem Modell werden Daten in der Regel Rohdaten weitergegeben, wobei diese gegebenenfalls vor Weitergabe konvertiert, maskiert, gefiltert oder anonymisiert werden.
    1.  Vorteile für die Datengeber: Hoher Grad an Kontrolle.
    2.  Nachteile für die Datengeber: Hoher Aufwand für manuelle Freigaben.
    3.  Vorteile für den Datentreuhänder: Verantwortlichkeit für die Freigabe liegt nicht beim Datentreuhänder.
    4.  Nachteile für den Datentreuhänder: -
    5.  Vorteile für die Datennutzer: Sicherheit, dass der Datengeber auch wirklich einverstanden ist.
    6.  Nachteile für die Datennutzer: ggf. längere Dauer bis der Zugriff gewährt wurde.
2.  **Verwaltungstreuhand:**  Verwaltungsdatentreuhänder vermitteln Daten aufgrund eigener Entscheidungen. Sie bedarfen also nicht einer konkreten Fremdanweisung für jede Datenvermittlung, müssen aber die Interessen aller Parteien berücksichtigen. Dazu ist es in der Regel notwendig einen Rahmen abzustecken, in der der Datentreuhänder agieren kann.
    1.  Vorteile für die Datengeber: Geringerer Aufwand, da nicht jede Anfrage selbst bearbeitet werden muss.
    2.  Nachteile für die Datengeber: Definition des Rahmens kann komplex sein; Folgen ggf. nicht gut abschätzbar.
    3.  Vorteile für den Datentreuhänder: Hat Handlungsspielraum und muss den Datengeber nicht jedes mal einbinden.
    4.  Nachteile für den Datentreuhänder: Muss sehr darauf achten, dass der Rahmen klar definiert ist und nachweisen dass er ihn auch einhält.
    5.  Vorteile für die Datennutzer: Verhandlungen finden nur mit einer Stelle (Datentreuhänder) statt, anstatt mit jedem Datennutzer.
    6.  Nachteile für die Datennutzer: -
3.  **Mehrwerttreuhand:**  Bei Mehrwertdatentreuhändern werden in der Regel keine Rohdaten weitergegeben. Stattdessen wertet der Datentreuhänder die Rohdaten aus und teilt das Ergebnis der Auswertung mit den Datennutzern, die die Ergebnisse direkt verwerten können. Mehrwertdatentreuhänder können die Bandbreite der möglichen Auswertungen statisch festlegen oder spezielle Auswertungen im Auftrag der Datengeber durchzuführen ("Algorithm to the Data").
    1.  Vorteile für die Datengeber: Rohdaten werden nicht herausgegeben.
    2.  Nachteile für die Datengeber: Ggf. unattraktiver für Datennutzer, daher weniger Gewinn.
    3.  Vorteile für den Datentreuhänder: Profilierung durch Mehrwertdienste.
    4.  Nachteile für den Datentreuhänder: Hoher Aufwand.
    5.  Vorteile für die Datennutzer: Aufwand wird zum Datentreuhänder verschoben.
    6.  Nachteile für die Datennutzer: Ggf. nicht alle Auswertungen möglich; Kein Zugriff auf die Rohdaten

### Beispiele

| Zugangstreuhand | Verwaltungstreuhand | Mehrwerttreuhand  |
|-----------------|---------------------|-------------------|
| BreedFides      | MobiDataSol 		|                   |
| Ddtrust         |    				    |                   |
| DeRe            |                     |                   |
| FAIRWinDS       |                     |                   |
| MANDAT          |                     |                   |
| S3I-X           |                     |                   |
| TRANSIT         |                     |                   |
| TreuMoDa        |                     |                   |
 
# Geschäftsebene

Auf der auf der Geschäftsebene unterscheiden sich Datentreuhandmodelle anhand der mit dem Modell verfolgten Ziele, der Geschäftsbeziehungen und der Geschäftsmodelle.

## Ziele

Datentreuhänder können mit ihrem Angebot, bzw. dem Ermöglichen eines vertrauensvollen Datenaustauschs, diverse Ziele verfolgen.

### Kategorien

1.  Förderung **digitaler Souveränität:** „Digitale Souveränität“ beschreibt laut Bitkom „die Fähigkeiten und Möglichkeiten von Individuen und Institutionen, ihre Rolle(n) in der digitalen Welt selbstständig, selbstbestimmt und sicher ausüben zu können.
2.  Förderung von  **Datensouveränität:** Unter Datensouveränität verstehen wir die rechtliche Legitimation sowie die organisatorischen und technischen Möglichkeiten für eine größtmögliche Kontrolle, Einfluss- und Einsichtnahme in Bezug auf die Nutzung von Daten durch die Datengebenden / Betroffenen. Sie ist ein Teilgebiet der digitalen Souveränität.
3.  Förderung der  **wirtschaftlichen Verwertung**  von Daten
4.  Förderung von **Innovation**
5.  Förderung von  **Wissenschaft und Forschung**
6.  Förderung von fairem  **Wettbewerb**
7.  Erfüllung von  **Auflagen**

### Beispiele

| Förderung digitaler Souveränität | Förderung von Datensouveränität | Förderung der wirtschaftlichen Verwertung von Daten | Förderung von Innovation | Förderung von Wissenschaft und Forschung | Förderung von fairem Wettbewerb | Erfüllung von Auflagen  |
|----------------------------------|---------------------------------|-----------------------------------------------------|--------------------------|------------------------------------------|---------------------------------|-------------------------|
| BreedFides                       | BreedFides                      | BreedFides                                          |                          | BreedFides                               | BreedFides                      | MobiDataSol             |
| MANDAT                           | Ddtrust                         | FAIRWinDS                                           |                          | Ddtrust                                  | MobiDataSol                     |                         |
| MobiDataSol                      | MANDAT                          | S3I-X                                               |                          | DeRe                                     | TRANSIT                         |                         |
| TRANSIT                          | MobiDataSol                     | TreuMoDa                                            |                          | FAIRWinDS                                |                                 |                         |
|                                  | TRANSIT                         |                                                     |                          | MobiDataSol                              |                                 |                         |
|                                  |                                 |                                                     |                          | TreuMoDa                                 |                                 |                         

## Geschäftsbeziehungen

In vielen Datentreuhandmodellen sind die Zielgruppen recht klar und es ist schnell ersichtlich, wer die Datengeber sind und wer die Datennutzer sind. Das muss jedoch nicht zwangsläufig so sein. Daher ist es ratsam, sich zumindest darüber klar zu sein, ob sich das Datentreuhandmodell an Unternehmen, Privatpersonen, und / oder andere Stakeholder richtet. Zur groben Orientierung geben wir vier Kategorien vor. Diese lassen sich sowohl auf die Datengeber als auch die Datennutzer anwenden, sodass wir mit 16 möglichen Arten von Geschäftsbeziehungen arbeiten. Dabei ist jedoch zu erwähnen, dass ein Treuhandmodell mehr als eine Beziehungsart bedienen kann, wie auch an den Beispielen deutlich wird.

### Kategorien

Datengeber und Datennutzer können in folgende Kategorien fallen:

1.  **Geschäftliche Nutzer / Unternehmen (Business)**
2.  **Private Nutzer / Verbraucher (Customer)**
3.  **Öffentliche Verwaltung und Behörden (Administration)**
4.  **Forschung (Research)**

### Beispiele

|                                     | Geschäftliche Nutzer | Private Nutzer und Verbraucher | Öffentliche Verwaltung und Behörden | Forschung    |
|-------------------------------------|----------------------|--------------------------------|-------------------------------------|--------------|
| **Geschäftliche Nutzer**                | **B2B:**                 | **C2B:**                           | **A2B:**                                | **R2B:**         |
|                                     | BreedFides           | Ddtrust                        | BreedFides                          | BreedFides   |
|                                     | Ddtrust              | TRANSIT                        | MobiDataSol                         | MobiDataSol  |
|                                     | MANDAT               | MobiDataSol   	              |                                     | TreuMoDa     |
|                                     | MobiDataSol          |                                |                                     | Ddtrust      |
|                                     | S3I-X                |                                |                                     |              |
|                                     | TRANSIT              |                                |                                     |              |
|                                     | TreuMoDa             |                                |                                     |              |
| **Private Nutzer und Verbraucher**      | **B2C:**                 | **C2C:**                           | **A2C:**                                | **R2C:**         |
|                                     | Ddtrust              | Ddtrust                        |                                     | Ddtrust      |
|                                     | TRANSIT              | TRANSIT                        |                                     |              |
| **Öffentliche Verwaltung und Behörden** | **B2A:**                 | **C2A:**                           | **A2A:**                               | **R2A:**         |
|                                     | MANDAT               | MobiDataSol                    | MobiDataSol                         | MobiDataSol  |
|                                     | MobiDataSol          |                                |                                     | TreuMoDa     |
|                                     | S3I-X                |                                |                                     |              |
|                                     | TreuMoDa             |                                |                                     |              |
| **Forschung** | **B2R:**        | **C2R:**        | **A2R:**        | **R2R:**         |
|           | BreedFides  | Ddtrust     | BreedFides  | BreedFides   |
|           | Ddtrust     | MobiDataSol | MobiDataSol | Ddtrust      |
|           | FAIRWinDS   |             |             | MobiDataSol  |
|           | MobiDataSol |             |             | TreuMoDa     |
|           | S3I-X       |             |             |              |
|           | TreuMoDa    |             |             |              |


## Geschäftsmodell

Auch wenn häufig auf Attribute wie Neutralität und Unabhängigkeit wert gelegt wird, müssen Datentreuhänder schlussendlich wirtschaftlich agieren. Dafür gibt es verschiedenste Ansätze, die je nach Domäne und Anwendungsbeispiel mehr oder weniger tragfähig sind.

### Kategorien

Wir unterscheiden die folgenden möglichen Arten der Finanzierung:

1.  **Datengeber** zahlt pro  **Transaktion** (Pay per Use)
2.  **Datengeber** zahlt einen  **Pauschalbetrag** (z. B. Grundgebühr / Flatrate)
3.  **Datennutzer** zahlt pro  **Transaktion** (Pay per Use)
4.  **Datennutzer**  zahlt einen  **Pauschalbetrag** (z. B. Grundgebühr / Flatrate)
5.  **Dritte** finanzieren den Datentreuhänder (z. B. durch Subventionierung)
6.  **Shareholder** finanzieren den Datentreuhänder (z. B. durch einen Zweckverband, der zum Betrieb eines Datentreuhänders von mehreren Shareholdern gegründet wurde)
7.  **Querfinanzierung** durch andere Dienste des Datentreuhänders

### Beispiele

| Datengeber zahlt pro Transaktion | Datengeber zahlt einen Pauschalbetrag | Datennutzer zahlt pro Transaktion | Datennutzer zahlt einen Pauschalbetrag | Shareholder | Dritte    | Querfinanzierung  |
|----------------------------------|---------------------------------------|-----------------------------------|----------------------------------------|-------------|-----------|-------------------|
| MANDAT                           | MANDAT                                | TreuMoDa                 		   | TRANSIT                                | MobiDataSol | FAIRWinDS | FAIRWinDS         |
|                                  | TRANSIT                               | DeRe                              | MobiDataSol                            |             | S3I-X     | TRANSIT           |
|                                  |                                       | S3I-X                             | S3I-X                                  |             | TreuMoDa  |                   |

# Daten- und Funktionsebene

Auf der auf der Daten- und Funktionsebene schauen wir uns an welche Daten vermittelt werden, warum sie schutzwürdig sind, was mit ihnen passiert und welche Zusatzdienste der Datentreuhänder abseits der reinen Vermittlung noch anbieten kann.

## Schutzgründe

Wie schon in der Definition beschrieben, sind Daten in Datentreuhandmodellen immer auf die ein oder andere Weise schützenswert - ansonsten bräuchte es kein Vertrauen zum Datennutzer und keinen Treuhänder. Dabei können sich die Daten selbst, die  [Schutzziele](https://ras.iese.de/confluence/display/KIC/Sicherheits-+und+Datenschutzanforderungen)  für die Daten, die Herkunft der Schutzziele und die daraus abgeleiteten technischen und organisatorischen Sicherheitsmaßnahmen signifikant voneinander unterscheiden.

### Kategorien

Wir unterscheiden folgende Kategorien, da sie diese sich aufgrund unterschiedlicher Rechtsgrundlagen von einander abgrenzen lassen. Dabei ist zu beachten, dass ein konkretes Datum auch in alle vier Kategorien gleichzeitig fallen kann.

1.  "Einfache" personenbezogene Daten nach DSGVO Art. 4 (1)
2.  Besondere Kategorien personenbezogener Daten nach DSGVO Art. 9
3.  Geschäftsgeheimnisse nach Gesetz zum Schutz von Geschäftsgeheimnissen (GeschGehG)
4.  Individuell schützenswerte Daten bei denen die Anforderungen direkt vom Datengeber kommen.

### Beispiele

| Personenbezogene Daten | Besondere Kategorien personenbezogener Daten | Geschäftsgeheimnisse | Individueller Schutzbedarf  |
|------------------------|----------------------------------------------|----------------------|-----------------------------|
| Ddtrust                | Ddtrust                                      | Ddtrust              | S3I-X                       |
| MANDAT                 |                                              | FAIRWinDS            | TRANSIT                     |
| S3I-X                  |                                              | MANDAT               | TreuMoDa                    |
| TRANSIT                |                                              | S3I-X                |                             |
| TreuMoDa               |                                              | TRANSIT              |                             |
|                        |                                              | BreedFides           |  MobiDataSol                |

# Datenbehandlung & Transformation

Grundlegende Aufgabe des Datentreuhänders ist die vertrauensvolle Übermittlung von Daten vom Datengeber zu den Datennutzern. Darüber hinaus können Datentreuhänder jedoch eine Vielzahl weiterer Funktionen anbieten.

### Kategorien

Die folgende Liste ist nicht notwendigerweise vollständig, aber umfasst "Zusatzfunktionen", die in der Praxis häufig gefunden werden.

1.  **Digitalisierung:** Der Datentreuhänder überführt die Daten des Datengebers zur Vermittlung in ein digitales Format.
2.  **Konvertierung:**  Der Datentreuhänder konvertiert die Daten in ein einheitliches Format, damit Datennutzer sich lediglich mit diesem Format auseinandersetzen müssen.
3.  **Anonymisierung:**  Der Datentreuhänder entfernt den Personenbezug aus den Daten.
4.  **Prüfung:**  Der Datentreuhänder prüft die Daten auf Qualitätseigenschaften wie Aktualität, Korrektheit, Konsistenz etc.
5.  **Aggregation:**  Der Datentreuhänder führt Daten (ggf. verschiedener Datengeber) in einen Datensatz zusammen.
6.  **Anreicherung:**  Der Datentreuhänder ergänzt Daten um deren Nutzen für die Datennutzer zu erhöhen.

### Beispiele

| Digitalisierung | Konvertierung | Anonymisierung | Prüfung     | Aggregation | Anreicherung  |   
|-----------------|---------------|----------------|-------------|-------------|---------------|
| MobiDataSol     | FAIRWinDS     | Ddtrust        | BreedFides  | FAIRWinDS   | BreedFides    |
| TRANSIT         | MobiDataSol   | DeRe           | FAIRWinDS   | MobiDataSol | FAIRWinDS     |
|                 | TRANSIT       | FAIRWinDS      | MobiDataSol | S3I-X       | TRANSIT       |
|                 |               | S3I-X          |             | TRANSIT     |               |
|                 |               | TRANSIT        |             |             |               |
|                 |               | TreuMoDa       |             |             |               |

  
# Zusammenfassung und Beispiele

Die folgende Abbildung fasst alle Ebenen und Kategorien zusammen.

<img src='Landkarte.png' width=500>[PDF](Landkarte.pdf)

## Beispiel TreuMoDa:
TreuMoDa setzt eine Zugangstreuhand als zentralen Router um mit dem Ziel die Daten wirtschaftlich und wissenschaftlich verwertbar zu machen. Dabei werden Daten von Unternehmen und Forschungseinrichtungen an Unternehmen, die öffentliche Verwaltung und andere Forschungseinrichtungen vermittelt. Für diese Vermittlung zahlt der Datennutzer sowie Dritte. Die Daten sind individuell schützenswert und personenbezogen und werden daher bei der Vermittlung anonymisiert.

<img src='Landkarte%20Beispiel.png' width=500>[PDF](Landkarte%20Beispiel.pdf)
