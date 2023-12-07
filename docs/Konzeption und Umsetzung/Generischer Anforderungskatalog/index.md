> [!NOTE]
> Es fehlen Links und die zugehörigen Dateien.
# Inhaltsverzeichnis
- [Generischer Anforderungskatalog](#ueberschrift)
    - [Motivation und Ziel](#motivation-und-ziel)
    - [Relevante Stakeholder](#stakeholder)
    - [Inhalte](#inhalte)
        - [Domäne](#domaene)
        - [Interviews](#interviews)
        - [Potentiale](#potentiale)
        - [Herausforderungen](#herausforderungen)
        - [Eigenschaften](#eigenschaften)
        - [Qualitätsanforderungen](#qualitaetsanforderungen)
        - [Anforderungen](#anforderungen)
        - [Systemanforderungen](#systemanforderungen)

<a name="ueberschrift"></a>
# Generischer Anforderungskatalog

Die hier dargestellten Inhalte stellen einen Überblick über verschiedene generische Anforderungen an ein Datentreuhändermodell dar, die im Rahmen des Projektes erhoben wurden. Sie basieren auf den durchgeführten Experteninterviews, aus denen verschiedene Inhalte extrahiert und anschließend verfeinert wurden. Die Anforderungen werden als generisch bezeichnet, da sie für die meisten Arten von Datentreuhändern relevant sind und somit eine Basis für weitere Erhebungen und Workshops darstellen. Vorschläge, wie weitere Inhalte erarbeitet werden können, finden sich unter  [Methoden zur Erhebung domänenspezifischer Anforderungen](Methoden%20zur%20Erhebung%20dom%C3%A4nenspezifischer%20Anforderungen.md). Bei der Erhebung der Daten kam es vereinzelt zu Widersprüchen zwischen den Experten und den daraus abgeleiteten Anforderungen. Die Daten sind daher mit Vorsicht zu betrachten und für den Anwendungsfall zu validieren.

Folgende Inhalte finden sich auf dieser Seite:

-   Eine PDF und ein Worddokument, welche das volle Spektrum der Anforderungen und ihrer Herkunft beschreiben. Zusätzlich zu den Anforderungen wurden Herausforderungen, Potentiale, Eigenschaften, Anwendungsbeispiele sowie Zugehörigkeiten zu Domänen abgeleitet und entsprechend verknüpft.
-   Eine PNG, die eine Übersicht der Anforderungen zeigt und sie in einen Kontext einbindet. Die Anforderungen werden in dieser Ansicht in verschiedenen Weisen kategorisiert. Zum einen gibt es 6 inhaltliche Kategorien:

    1.  Onboarding/Konto
    2.  Datenerfassung/Einbindung
    3.  Datenvermittlung
    4.  Datenverwaltung
    5.  Andere funktionale Anforderungen
    6.  Nicht-funktionale Anforderungen (Qualitätsanforderungen wurden hier ebenso aufgeführt)
        - Rahmenbedingungen
    7.  Zusätzlich dazu gibt es manche Anforderungen, die als obligatorisch für einen Datentreuhänder angesehen werden, sowie optionale oder alternative Anforderungen, die abhängig von der Art des Datentreuhänders nützlich oder notwendig sein könnten. Die nicht obligatorischen Anforderungen sind somit nicht mehr Teil der generischen Anforderungen, wurden aber als wichtig genug eingestuft, dass diese ebenso aufgenommen wurden.

-   Zwei Excel-Dateien, wobei die erste Excel-Datei konsolidierte Daten der Anforderungen in ihren drei Kategorien: Systemanforderungen, DTH-Anforderungen und DTH-Qualitätsanforderungen sowie eine Matrix der verschiedenen Verknüpfungen der Anforderungen enthält. So kann es zum Beispiel sein, dass aus einer DTH-Anforderung ein oder mehrere Systemanforderungen abgeleitet wurden, die dann verknüpft sind. Die zweite Excel-Datei gibt etwas mehr Informationen über die einzelnen Anforderungen, ihre Wichtigkeit und für welche Modelle von Datentreuhändern diese relevant sein könnten.

<a name="motivation-und-ziel"></a>
## Motivation und Ziel

Das Ziel des generischen Anforderungskatalogs ist es ein Verständnis über verschiedene Anforderungen eines Datentreuhänders zu geben, die so formuliert sind, dass sie für alle Modelle eine Relevanz bilden. Mit Hilfe dieser Anforderungen ist es möglich ein einen Überblick über noch zu entwickelnde Datentreuhändersysteme zu schaffen, ohne dabei zusätzlichen Aufwand für die Erhebung von Anforderungen zu verwenden und potentielle Risiken, Herausforderungen, aber auch Potentiale früh zu erkennen.

Durch dieses Verständnis werden wichtige Stakeholder darin unterstützt relevante Inhalte für ihren Datentreuhänder zu identifizieren und so zu vermeiden, dass wichtige oder essentielle Dinge vergessen werden. Es bietet somit einen Rahmen der Orientierung. Es ist aber zu beachten, dass je nach Anwendungsfall verschiedene Prioritäten oder Funktionen gefordert sind, weshalb eine weitere Erhebung und Konzeptionierung unabdingbar ist.

<a name="stakeholder"></a>
## Stakeholder für die die Inhalte relevant sind

Die Inhalte sind besonders interessant für Projektmanager und -Leiter sowie für alle Stakeholder, die an der Entwicklung und der Erhebung von Anforderungen und Konzeptionen teilhaben.

&uarr; [zurück zur Übersicht](#top)
<a name="inhalte"></a>
## Inhalte

Im folgenden finden sich Dateien, die die gesammelten Informationen der durchgeführten Interviews darstellen.

Die nachfolgenden PDF- und Worddokumente sind wie folgt strukturiert:

1.  Betrachtete Domänen
    1.  Name
    2.  Beschreibung
    3.  Zugehörige Lösungsansätze
2.  Interview-Ergebnisse
    1.  Datum
    2.  Domänenzugehörigkeit
    3.  Hervorgegangene Lösungsansätze
    4.  Hervorgegangene Herausforderungen
    5.  Zugehörige Potentiale
    6.  Hervorgegangene Eigenschaften
3.  Potentiale  
    1.  Name
    2.  Beschreibung
    3.  Zugehörige Anwendungsbeispiele
    4.  Zugehörige Domänen
4.  Herausforderungen
    1.  Name
    2.  Art (Organisatorisch, Rechtlich, Technisch)
    3.  Beschreibung
    4.  Zugehörige Anwendungsbeispiele
    5.  Zugehörige Domänen
5.  Eigenschaften
    1.  Name
    2.  Art
    3.  Beschreibung
    4.  zugehörige Domänen
    5.  Zugehörige Anwendungsbeispiele
    6.  Zugehörige Quellen
6.  DTH-Qualitätsanforderungen
    1.  Name
    2.  Kategorie
    3.  Relevanz für welche DTH-Art
    4.  Herausforderungen, die die Umsetzung verkomplizieren
    5.  Wichtigkeit
    6.  Alternativen
    7.  Zugehörige Eigenschaften
    8.  Zugehörige Herausforderungen
    9.  Zugehörige Potentiale
    10.  Zugehörige DTH-Anforderungen
    11.  Bezogene DTH-Qualitätsanforderungen
7.  DTH-Anforderungen  
    1.  Name
    2.  Beschreibung
    3.  Kategorie
    4.  Umsetzungsart
    5.  Interpretation
    6.  Umzusetzende Qualitätseigenschaft
    7.  Relevanz für welche DTH-Art
    8.  Herausforderungen, die die Umsetzung verkomplizieren
    9.  Wichtigkeit
    10.  Alternativen
    11.  Zugehörige Qualitätsanforderungen
    12.  Zugehörige Potentiale
    13.  Bezogene DTH-Anforderungen
8.  Systemanforderungen
    1.  Name
    2.  Anforderung
    3.  Umzusetzende Qualitätseigenschaften
    4.  Relevanz für welche DTH-Art
    5.  Beschreibung
    6.  Wichtigkeit
    7.  Alternativen
    8.  Zugehörige Quellen
    9.  Zugehörige DTH-Anforderungen

<a name="domaene"></a>
### Domäne

Bei den bei den durchgeführten Erhebung war es zu meist möglich bestimmte Inhalte einer Domäne zuzuordnen. So gibt es beispielsweise bestimmte Herausforderungen, die nur in der Domäne Medizin relevant sind.

Durch diese Zuordnung wird es den Lesern des Dokumentes erleichtert, relevante Inhalte für ihren Datentreuhänder zu identifizieren, die sich einer gewissen Domäne zugehörig fühlen.

<a name="interviews"></a>
### Interviews

Zur Erhebung der Daten wurden Interviews mit verschiedensten Experten unterschiedlicher Domänen durchgeführt. Basierend auf diesen Daten wurden die verschiedenen Kategorien abgeleitet und Zugehörigkeiten bestimmt. Insgesamt wurden 13 Interviews mit ein oder mehreren Experten durchgeführt. Die Struktur der durchgeführten Interviews findet sich hier: 
[Interviewleitfragen_Template](https://github.com/Fraunhofer-IESE/KickStartTrustee/files/13601177/Interviewleitfragen_Template.1.docx)


<a name="potentiale"></a>
### Potentiale

Potentiale beschreiben positive Chancen, die die Verwendung eines Datentreuhänders für verschiedenste Parteien bringen kann. Es kann sich dabei um positive Effekte für einzelne Personen handeln oder auch um positive Effekte für ganze Systeme. Potentiale wurden direkt von den Interviews abgeleitet.

<a name="herausforderungen"></a>
### Herausforderungen

Doch genauso wie es Potentiale für Datentreuhänder gibt, entstehen auch viele Herausforderungen, die in verschiedensten Phasen auftreten und das Team vor Probleme stellen. Herausforderungen können bereits bei der Erhebung und Erstellung des Datentreuhänders bestehen, während andere die Einführung und Akzeptanz verkomplizieren. Die dargestellten Inhalte stellen nicht alle möglichen Herausforderungen dar, doch bilden sie einen Überblick über mögliche Problempunkte und können Entwickler und Projektmanager darin unterstützen diese möglichst früh zu erkennen und zu beheben.

<a name="eigenschaften"></a>
### Eigenschaften

Die Eigenschaften eines Datentreuhänders bilden sich hauptsächlich aus den durchgeführten Interviews, wo die Experten gebeten wurden Datentreuhänder und ihre Eigenschaften zu beschreiben. Die Uneinigkeiten in gewissen Themenbereichen treten in diesem Kapitel besonders hervor und sollten für jeden konkreten Fall eruiert werden.

<a name="qualitaetsanforderungen"></a>
### Qualitätsanforderungen

Wie bei jeder Software, gibt es auch bei einem Datentreuhänder Qualitätsanforderungen, die eingehalten oder zumindest bedacht werden sollten bei der Entwicklung. Datenintegrität ist beispielsweise ein Attribut, dass in jedem Fall gegeben sein sollte, um unrechtmäße Veränderungen zu verhindern.

<a name="anforderungen"></a>
### Anforderungen

Die Anforderungen an einen Datentreuhänder beschäftigen sich in vielen Aspekten mit dem Verwalten von Daten, die sich zum Teil an die Qualitätsanforderungen anschließen, aber auch eigene Themenfelder eröffnen. Weitere Details und Verknüpfungen finden sich im Kapitel Anforderungen dieser Seite in Form eines Bildes und zwei Excel-Dateien.

<a name="systemanforderungen"></a>
### Systemanforderungen

Anforderungen haben teilweise nicht genug technische Informationen, um das System ausreichend zu beschreiben. Systemanforderungen begeben sich etwas tiefer in die Materie und machen Anforderungen konkreter. Sie basieren somit in der Regel auf (DTH-)Anforderungen, die durch die Systemanforderungen umgesetzt werden.

[KickStartTrusteeExport_docx](https://github.com/Fraunhofer-IESE/KickStartTrustee/files/13601196/KickStartTrusteeExport.1.docx)
[KickStartTrusteeExport__PDF](https://github.com/Fraunhofer-IESE/KickStartTrustee/files/13601198/KickStartTrusteeExport.1.pdf)
[Generische Anforderungen Übersicht - Komplettübersicht ohne Kommentare_pdf](https://github.com/Fraunhofer-IESE/KickStartTrustee/files/13601200/Generische.Anforderungen.Ubersicht.-.Komplettubersicht.ohne.Kommentare.pdf)
[Anforderungen_xlsx](https://github.com/Fraunhofer-IESE/KickStartTrustee/files/13601202/Anforderungen.1.xlsx)
[Anforderungs_Details_xlsx](https://github.com/Fraunhofer-IESE/KickStartTrustee/files/13601203/Anforderungs_Details.1.xlsx)


&uarr; [zurück zur Übersicht](#top)

> Bei den Lösungsansätzen fehlen noch Beschreibungen, bei denen mir
> Informationen oder Hintergründe fehlen, um diese Aufzufüllen.

*[DTH]:Datentreuhänder
