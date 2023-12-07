# Einleitung

Forschung und Wirtschaft versprechen sich großen Nutzen von der Auswertung von Daten. Dabei geht es in der Regel immer um die Optimierung von Prozessen, Diensten oder Produkten und eine damit verbundene Gewinnsteigerung. Dabei birgt gerade die Nutzung von Daten über Grenzen einzelner Anwendungsdomänen oder Unternehmen hinweg (d. h. die Verknüpfung bisher getrennter Daten) ein großes Chancenpotential.

Allerdings ist die Verfügbarkeit qualitativ hochwertiger Daten ein großes Problem. Forschung, Wirtschaft, Verwaltung und Privatpersonen sind häufig sehr zögerlich bei der Freigabe von Daten. Neben technischen Hürden, die mit der Bereitstellung von Daten einhergehen, gibt es diverse, größtenteils nachvollziehbare, Vorbehalte hinsichtlich Transparenz- und Kontrollverlust, Datenschutz, IP-Schutz Rechtskonformität, etc.

Um diese Hürden abzubauen und das Teilen von Daten künftig zu steigern, ist es notwendig, dass zum Teil gegenläufige Interessen, Rechte und Pflichten von Datengebern und Datennutzern abgewogen und ausgeglichen werden. Ein vielversprechender Lösungsansatz sind sogenannte Datentreuhandmodelle (DTM), die Rahmen, Umsetzung und Technologien für den Einsatz von Datentreuhändern vereinen.

Aber was genau sind „Datentreuhänder“? Die Antwort darauf gestaltet sich in der Praxis komplex, wie wir gleich sehen werden.

# Grundlegende Definition

Wenn man sich die Literatur zu Datentreuhändern ansieht, gibt es immer wiederkehrende Kerneigenschaften, die mit Datentreuhändern verbunden werden. Aus diesen lässt sich folgende Definition ableiten:

> **Definition**
> _Ein  Datentreuhänder ist eine **Vertrauensinstanz**,** die  **schützenswerte Daten**  zwischen **Datengebern** und **Datennutzern** unter Wahrung der **Interessen** beider Seiten **digital vermittelt**._

Wie Datengeber, Datennutzer und Datentreuhänder im Verhältnis zueinander stehen, wird in der folgenden Abbildung dargestellt. Sie zeigt anschaulich, dass Datentreuhänder eine Vermittlerrolle zwischen Datengebern und Datennutzern einnehmen. Beim Datengeber kann man darüber hinaus noch unterscheiden zwischen:

1.  der betroffene Person auf die sich die Daten beziehen (Datenschutz) / dem betroffenen Unternehmen, welche die Rechte an den Daten hat (IP-Schutz)
2.  dem Dateninhaber, der die Daten technisch dem Datentreuhänder zur Verfügung stellen kann.

![](Übersicht Datentreuhänder.pdf)

Will man die Definition enger fassen, stößt man schnell an Grenzen und der Konzens in der Literatur endet. Dies liegt daran, dass sich Datentreuhändern je nach Domäne und Anwendungsbeispiel stark in ihren Anforderungen und Funktionen unterscheiden können. Eine zu enge Fassung des Begriffs ist darüber hinaus auch nicht unbedingt zielführend, da es die Entwicklung von Datentreuhandmodellen hemmen würde  [BS21]. Im folgenden werden wir die einzelnen (unterstrichenen) Komponenten der Definition genauer ansehen, welche aus den genannten Gründen mehr als "Interpretationshilfe" denn als "harte" Definition zu verstehen sind.

## Vertrauensinstanz

Ein Datentreuhänder ist eine spezielle Form eines  **Datenmittlers** [Spe22]  zwischen mindestens zwei anderen Parteien, zwischen denen kein oder ein lediglich eingeschränktes **Vertrauensverhältnis** besteht. Er stellt daher eine Vertrauensinstanz (auch Vertrauensanker) dar.

Ob ein Datenmittler von allen Parteien als Vertrauensinstanz akzeptiert wird, hängt von vielen Faktoren und dem konkreten Anwendungsfall ab. Insbesondere ist Vertrauen eine nicht-binäre Eigenschaft und hochgradig subjektiv. Im jeweiligen Anwendungsfall zu diskutieren und zu bewerten sind insbesondere die folgenden Fragestellungen:

-   **Neutralität:** Werden alle Datengeber und alle Datennutzer von der Vertrauensinstanz gleich behandelt?
-   **Unabhängigkeit:** Gibt es organisatorische, politische oder finanzielle Abhängigkeiten der Vertrauensinstanz?
-   **Geschäftsmodell:** Ergeben sich durch das Geschäftsmodell gegebenenfalls Interessenskonflikte?

Gerade die Neutralität und Unabhängigkeit wird häufig gefordert. Dennoch ist eine pauschale Forderung nicht zielführend, da zum einen die Notwendigkeit vom Anwendungsfall abhängt und zum anderen auch die Wirtschaftlichkeit des Datentreuhänders berücksichtigt werden muss  [BS21].

## Interessensvertretung

### Interessen des Datengebers

Bei den Interessen des Datengebers sind primär zwei Faktoren durch den Datentreuhänder zu berücksichtigen und abzuwägen:

1.  Der **Nutzen,** den sich der Datengeber durch die Bereitstellung seiner Daten erhofft.  
    Neben monetären Anreizen gibt es auch eine ganze Reihe von nicht-monetären Anreizen, wie zum Beispiel Bequemlichkeit oder der Beitrag zum Gemeinwohl.
2.  Die **Risiken** die durch die Bereitstellung der Daten bestehen.  
    → siehe hierzu den Abschnitt "Schützenswerte Daten".

### Interessen der Datennutzer

Bei den Interessen der Datennutzer müssen verschiedene Faktoren beachtet werden:

1.  Der erwartete **Nutzen aus den Daten**:  
    Datennutzer möchten Daten dann verwenden, wenn sich dadurch ein Vorteil für sie ergibt. Dieser kann sowohl monetärer als auch nicht-monetärer Natur sein und ist je nach Domäne und Anwendungsbeispiel individuell.
2.  Die **Qualität der Daten**:  
    Um Nutzen aus den Daten ziehen zu können, ist es essentiell, dass diese gewissen Qualitätsstandards genügen. Darunter fallen beispielsweise die generelle Verfügbarkeit der Daten, deren Aktualität und Genauigkeit sowie Anforderungen struktureller Art (z. B. standardisierte Datenformate).
3.  Die **Kosten der Daten**:  
    Die Kosten für Datennutzer setzen sich zusammen aus dem von den Datengebern verlangten Preis, möglichen Vermittlungsgebühren des Datentreuhänders sowie Aufwänden für die Nutzbarmachung der Daten (z. B. das Konvertieren von Datenformaten).

### Legitimation der Interessensvertretung

Damit ein Datentreuhänder den Datengeber gegenüber Datennutzern überhaupt vertreten kann, bedarf es einer Legitimation, die ihm entsprechende Rechte und ggf. Handlungsspielräume einräumt. Dies kann zum Einen eine **gesetzliche Grundlage** **oder Verpflichtung** zur Vermittlung von Daten über einen Datentreuhänder sein (Beispiel: Australischer Energiesektor). In diesem Fall ist der Austausch von Daten nur über einen Datentreuhänder erlaubt, wobei gegebenenfalls Freiheit bei der Wahl des Datentreuhänders besteht. Alternativ muss es einen **Vertrag** zwischen Datengeber und Datentreuhänder geben, der den Datentreuhänder  zur Vermittlung von Daten berechtigt und verpflichtet  [Spe22]. In diesem Fall ist Nutzung eines Datentreuhänders für den Datengeber in der Regel freiwillig.

### Umsetzung der Interessensvertretung

Wichtigste Anforderung ist, dass der Datentreuhänder **gesetzeskonform handelt** (z. B. dem Datengeber die Betroffenenrechte der DSGVO einräumt und diese umsetzt) und stets die **Interessen des Datengebers wahrt**. Er darf also nichts unternehmen oder zulassen, was dem Datengeber „schadet“. Dazu gehört in der Regel auch, dass der Datengeber stets selbst darüber entscheiden kann, wer seine Daten nutzen darf.

### Eigeninteresse des Datentreuhänders

Neben wirtschaftlichen Interessen, gibt es eine ganze Reihe weiterer Interessen, welche Datentreuhänder  verfolgen können  [BBK+20]

1.  Förderung digitaler Souveränität
2.  Förderung von Datensouveränität
3.  Förderung der wirtschaftlichen Verwertung von Daten
4.  Förderung von Innovation
5.  Förderung von Wissenschaft und Forschung
6.  Förderung von fairem Wettbewerb

## Schützenswerte Daten

Daten können für unterschiedliche **Parteien,** aus verschiedenen **Gründen** und hinsichtlich verschiedener **Schutzziele** schützenswert sein. Häufig haben die zu vermittelnden Daten einen Bezug zum Datengeber als Person, weshalb der **Datenschutz** greift. Hieraus ergeben sich beispielsweise die Schutzziele **Transparenz, Intervenierbarkeit** und **Nichtverkettbarkeit** (vgl. Standard-Datenschutzmodell). Darüber hinaus kann für den Datengeber aber auch der Schutz von **geistigem Eigentum** (insbesondere wenn es sich beim Datengeber um Unternehmen handelt) oder die Einhaltung des **Urheberrechts** von Interesse sein. Die Schutzziele der Datennutzer ergeben sich häufig aus der für sie notwendigen Datenqualität. Beispielsweise muss die **Integrität** der Daten geschützt werden um deren Korrektheit sicherzustellen. Auch die Aktualität der Daten ist für viele Anwendungsfälle essenziell, was hohe Anforderungen an die **Verfügbarkeit** stellt.

Um die angesprochenen Schutzziele zu erreichen, ist für die Vermittlung unter anderem ein entsprechendes **Berechtigungs- und Zugriffsmanagement** umzusetzen, geeignete **Anonymisierungsverfahren** zu implementieren und Transaktionen revisionssicher zu **protokollieren** und transparent zu machen. Falls die Nutzung der Daten an gewisse **Auflagen** gebunden ist, muss der Datentreuhänder deren Einhaltung durch die Datennutzer außerdem **organisatorisch oder technisch sicherstellen**.

## Digitale Vermittlung

Die Vermittlung selbst lässt sich in zwei Schritte teilen, wie im Folgenden dargestellt.

### Datengeber → Datentreuhänder

Im ersten Schritt gibt der Datengeber dem Datentreuhänder Zugriff auf die Daten. Dabei können technisch oder organisatorisch weitere Parteien involviert sein. Beispielsweise stellen Krankenhäuser oder Ärzte Gesundheitsdaten von Patienten für die medizinische Forschung und mit deren Einwilligung (siehe "Legitimation der Interessensvertretung") Datentreuhändern zur Verfügung.

Je nach Ansatz und Anwendungsfall können die Daten entweder **dem Datentreuhänder übergeben und zentral bei ihm gespeichert werden** oder lediglich **bei Bedarf von den Datengebern angefragt** werden. In beiden Fällen hat der Datentreuhänder jedoch vollen Zugang zu den Daten.

Damit die erhaltenen Daten nutzbar gemacht werden können, muss der Datentreuhänder sie anschließend gegebenenfalls zunächst digitalisieren, aggregieren oder anonymisieren. Hieran wird deutlich, dass unter Vermittlung **mehr als nur ein reines Übertragen** von Daten zu verstehen ist.

### Datentreuhänder → Datennutzer

Im zweiten Schritt gibt der Datentreuhänder dem oder den Datennutzer(n) Zugriff auf die Daten. Dieser Schritt wird - je nach Anwendungsfall - angestoßen, wenn

1.  der **Datengeber** die Vermittlung explizit anweist,
2.  **Datennutzer** bestimmte Daten anfordern oder
3.  der **Datentreuhänder** die Vermittlung im Interesse des Datengebers für sinnvoll erachtet.

In den ersten beiden Fällen nimmt der Datentreuhänder eine eher **passive Rolle** ein und ermöglicht im Wesentlichen die sichere Übermittlung der Daten. In vielen Fällen entsteht dadurch bereits der gewünschte Mehrwert für den Datengeber und die Datennutzer. Darüber hinaus kann der Datentreuhänder auch eine **aktivere Rolle** einnehmen. Dies ist beispielsweise dann notwendig, wenn sich der Datengeber einen Nutzen durch die Auswertung oder Veredlung der Daten durch den Datentreuhänder selbst erhofft (beispielsweise zum Matching mit anderen Partnern). Im Projekt beschränken wir uns auf solche Datentreuhänder, bei denen die Vermittlung zum Datennutzer ausschließlich digital erfolgt.

# Fazit

Eine einheitliche Definition oder Eingrenzung von Datentreuhändern ist aufgrund der Diversität der konkreten Szenarien, Anwendungsfälle und gesetzlichen Anforderungen schwer möglich. Unsere Definition verfolgt daher den Ansatz, grundsätzliche und wesentliche Aspekte einer Datentreuhandschaft herauszuarbeiten, die für die Idee eines Datentreuhänders erfüllt sein müssen.

# Quellen

 - [BS21] Blankertz, A., & Specht, L. (2021). Wie eine Regulierung für Datentreuhänder aussehen sollte. _Policy-Brief. Juli. Berlin: Stiftung Neue Verantwortung eV.
 - [Spe22] Specht-Riemenschneider, L. (2022). Datentreuhänder-Gesellschaftlich nützlich, rechtlich größere Anforderungen erforderlich.
 - [BBK+20] Blankertz, A., Braunmühl, P. V., Kuzev, P., Richter, F., Richter, H., & Schallbruch, M. (2020). Datentreuhandmodelle-Themenpapier.
