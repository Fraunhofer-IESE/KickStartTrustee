# Generischer Anforderungskatalog

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

Die hier dargestellten Inhalte stellen einen Überblick über verschiedene generische Anforderungen an ein Datentreuhändermodell dar, die im Rahmen des Projektes erhoben wurden. Sie basieren auf den durchgeführten Experteninterviews, aus denen verschiedene Inhalte extrahiert und anschließend verfeinert wurden. Die Anforderungen werden als generisch bezeichnet, da sie für die meisten Arten von Datentreuhändern relevant sind und somit eine Basis für weitere Erhebungen und Workshops darstellen. Vorschläge, wie weitere Inhalte erarbeitet werden können, finden sich unter [Methoden zur Erhebung domänenspezifischer Anforderungen](<../Methoden zur Erhebung domänenspezifischer Anforderungen/index.md>). Bei der Erhebung der Daten kam es vereinzelt zu Widersprüchen zwischen den Experten und den daraus abgeleiteten Anforderungen. Die Daten sind daher mit Vorsicht zu betrachten und für den Anwendungsfall zu validieren.

Folgende Inhalte finden sich auf dieser Seite:

-   Eine PDF und ein Worddokument, welche das volle Spektrum der Anforderungen und ihrer Herkunft beschreiben. Zusätzlich zu den Anforderungen wurden Herausforderungen, Potentiale, Eigenschaften, Anwendungsbeispiele sowie Zugehörigkeiten zu Domänen abgeleitet und entsprechend verknüpft.
-   Eine Bild, das eine Übersicht der Anforderungen zeigt und sie in einen Kontext einbindet. Die Anforderungen werden in dieser Ansicht in verschiedenen Weisen kategorisiert. Zum einen gibt es 6 inhaltliche Kategorien:

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

<a name="inhalte"></a>
## Inhalte

![Alt Komplettübersicht_SVG](<Dateien/Generische Anforderungen Komplettübersicht.svg>)
[PDF](<Dateien/Generische Anforderungen Komplettübersicht.pdf>)


<a name="domaene"></a>
### Domäne

Bei den bei den durchgeführten Erhebung war es zu meist möglich bestimmte Inhalte einer Domäne zuzuordnen. So gibt es beispielsweise bestimmte Herausforderungen, die nur in der Domäne Medizin relevant sind.

Durch diese Zuordnung wird es den Lesern des Dokumentes erleichtert, relevante Inhalte für ihren Datentreuhänder zu identifizieren, die sich einer gewissen Domäne zugehörig fühlen.

Weitere Details finden Sie [hier](Dom%C3%A4nen.md).

<a name="interviews"></a>
### Interviews

Zur Erhebung der Daten wurden Interviews mit verschiedensten Experten unterschiedlicher Domänen durchgeführt. Basierend auf diesen Daten wurden die verschiedenen Kategorien abgeleitet und Zugehörigkeiten bestimmt. Insgesamt wurden 13 Interviews mit ein oder mehreren Experten durchgeführt. Die Struktur der durchgeführten Interviews findet sich hier: 
[Interviewleitfragen_Template](Dateien/Interviewleitfragen_Template.docx)

Weitere Details finden Sie [hier](Interviews.md).

<a name="potentiale"></a>
### Potentiale

Potentiale beschreiben positive Chancen, die die Verwendung eines Datentreuhänders für verschiedenste Parteien bringen kann. Es kann sich dabei um positive Effekte für einzelne Personen handeln oder auch um positive Effekte für ganze Systeme. Potentiale wurden direkt von den Interviews abgeleitet.

Weitere Details finden Sie [hier](Potentiale.md).

<a name="herausforderungen"></a>
### Herausforderungen

Doch genauso wie es Potentiale für Datentreuhänder gibt, entstehen auch viele Herausforderungen, die in verschiedensten Phasen auftreten und das Team vor Probleme stellen. Herausforderungen können bereits bei der Erhebung und Erstellung des Datentreuhänders bestehen, während andere die Einführung und Akzeptanz verkomplizieren. Die dargestellten Inhalte stellen nicht alle möglichen Herausforderungen dar, doch bilden sie einen Überblick über mögliche Problempunkte und können Entwickler und Projektmanager darin unterstützen diese möglichst früh zu erkennen und zu beheben.

Weitere Details finden Sie [hier](Herausforderungen.md).

<a name="eigenschaften"></a>
### Eigenschaften

Die Eigenschaften eines Datentreuhänders bilden sich hauptsächlich aus den durchgeführten Interviews, wo die Experten gebeten wurden Datentreuhänder und ihre Eigenschaften zu beschreiben. Die Uneinigkeiten in gewissen Themenbereichen treten in diesem Kapitel besonders hervor und sollten für jeden konkreten Fall eruiert werden.

Weitere Details finden Sie [hier](Eigenschaften.md).

<a name="qualitaetsanforderungen"></a>
### Qualitätsanforderungen

Wie bei jeder Software, gibt es auch bei einem Datentreuhänder Qualitätsanforderungen, die eingehalten oder zumindest bedacht werden sollten bei der Entwicklung. Datenintegrität ist beispielsweise ein Attribut, dass in jedem Fall gegeben sein sollte, um unrechtmäße Veränderungen zu verhindern.

Weitere Details finden Sie [hier](Qualit%C3%A4tsanforderungen.md).

<a name="anforderungen"></a>
### Anforderungen

Die Anforderungen an einen Datentreuhänder beschäftigen sich in vielen Aspekten mit dem Verwalten von Daten, die sich zum Teil an die Qualitätsanforderungen anschließen, aber auch eigene Themenfelder eröffnen. Weitere Details und Verknüpfungen finden sich im Kapitel Anforderungen dieser Seite in Form eines Bildes und zwei Excel-Dateien.

Weitere Details finden Sie [hier](DTH-Anforderungen.md).

<a name="systemanforderungen"></a>
### Systemanforderungen

Anforderungen haben teilweise nicht genug technische Informationen, um das System ausreichend zu beschreiben. Systemanforderungen begeben sich etwas tiefer in die Materie und machen Anforderungen konkreter. Sie basieren somit in der Regel auf (DTH-)Anforderungen, welche durch diese Systemanforderungen umgesetzt werden.

Weitere Details finden Sie [hier](Systemanforderungen.md).

Die Informationen können Sie sich auch als Word, Excel oder PDF herunterladen:
- [KickStartTrusteeExport.docx](Dateien/KickStartTrustee_AnforderungenExport.docx)
- [KickStartTrusteeExport.pdf](Dateien/KickStartTrustee_AnforderungenExport.pdf)
- [Anforderungs_Details_xlsx](Dateien/Anforderungs_Details.xlsx)
- [Anforderungen_xlsx](Dateien/Anforderungen.xlsx)

