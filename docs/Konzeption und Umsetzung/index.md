# Konzeption und Umsetzung
Dieses Kapitel widmet sich der (technischen) Umsetzung der Datentreuhandlösung. Dazu bietet der Leitfaden grundlegende Bausteine und methodische Unterstützung zur Erhebung, Konzeption und Erstellung von Anforderungen, Softwarearchitektur, Sicherheitsmaßnahmen. Der Leitfaden unterstützt außerdem bei der Auswahl der geeigneten Grundlagentechnologieen und stellt generische Software- und UI-Komponenten bereit.

## Generischer Anforderungskatalog
Datentreuhänder unterscheiden sich natürlich je nach Domäne und konkreter Mission. Dennoch gibt es diverse Gemeinsamkeiten, bzw. wiederkehrende Muster, die sogenannten Varianten. Dieser Katalog generischer Anforderungen ermöglicht es, einen gewissen Teil der Anforderungen direkt zu verwenden und so die Arbeit ihrer Erhebung zu sparen. Die Anforderungen wurden aus der Literaturrecherche, den Anwendungsbeispielen und Interviews abgeleitet.

[Zum Anforderungskatalog](<Generischer Anforderungskatalog>)

## Methoden zur Erhebung domänenspezifischer Anforderungen
Auch wenn der generische Anforderungskatalog bereits viele Anforderungen beinhaltet, muss immer ergänzend eine individuelle Anforderungserhebung durchgeführt werden.  Diese individuelle Anforderungserhebung ermöglicht nicht nur die Berücksichtigung der spezifischen Anforderungen an die DTH-Lösung, sondern ist auch notwendig, um Entscheidung über optionale und alternative Anforderungen zu treffen.

[Zu den Methoden zur Erhebung domänenspezifischer Anforderungen](<Methoden zur Erhebung domänenspezifischer Anforderungen>)

## Sicherheits- und Datenschutzanforderungen
In einer digitalisierten Welt, in der sensible Daten und Informationen tagtäglich produziert, verarbeitet und gespeichert werden, ist ein umfassendes Sicherheitsmanagement unerlässlich. Datentreuhänder (DTH) und DTH betreibende Unternehmen übernehmen dabei eine besondere Verantwortung für die Sicherheit und den Schutz dieser Daten und Informationen. Ein umfassendes Sicherheitsdokument kann hierbei helfen, alle relevanten Aspekte der IT-Sicherheit zu identifizieren, Risiken zu bewerten und Maßnahmen zu ergreifen, um ein hohes Sicherheitsniveau zu gewährleisten. Dieses Dokument bietet einen umfassenden Maßnahmenkatalog für die grundlegenden Konzepte der IT-Sicherheit bei DTH und zeigt, wie den ein Sicherheitskonzept entsprechend der spezifischen Anforderungen von DTH und deren Betreibern umgesetzt werden kann.

Ziel ist es, ein durchgängiges Sicherheitskonzept für DTH und DTH betreibende Unternehmen zu entwickeln. Dieser Baustein beschreibt dahingehend die intendierte Vorgehensweise und zählt die umzusetzenden Sicherheitsanforderungen auf. Diese sind die Grundlage für das Ableiten von technischen und organisatorischen Maßnahmen und eines Sicherheitskonzept, was jedoch außerhalb des Rahmens dieses Bausteins liegt. Dieser Baustein orientiert sich im Wesentlichen am IT-Grundschutz des Bundesamts für Sicherheit und Informationstechnik (BSI). Hierbei werden nicht sämtliche Inhalte des IT-Grundschutzes hervorgehoben, sondern lediglich solche, die für DTH von besonderer Relevanz sind. 

[Zu den Sicherheits- und Datenschutzanforderungen](<Sicherheits- und Datenschutzanforderungen>)

## Security Testing Guide
Angesichts der zunehmenden Bedrohungen durch Cyberkriminalität und Datenschutzverletzungen ist es unerlässlich, dass Betreiber von DTH ihre Sicherheitsvorkehrungen ständig überprüfen und verbessern, um zu gewährleisten, dass die von ihnen verwalteten Daten sicher und geschützt bleiben.

Ein Prüfkatalog kann dabei helfen, sicherzustellen, dass alle relevanten Bereiche der Datensicherheit geprüft werden und dass der DTH die erforderlichen Sicherheitsstandards erfüllt.

In diesem Prüfkatalog werden verschiedene Aspekte der Datensicherheit wie sichere Konfiguration und sicheres Deployment, Authentisierung und Autorisierung, Identity und Session Management, Eingabevalidierung oder Clientsicherheit betrachtet. Dabei orientiert sich der Katalog an dem OWASP Web Security Testing Guide (WSTG). Da dieser jedoch in seiner Art eher deskriptiv ist, wurden konkrete Prüfkriterien abgeleitet, welche die Bewertung des Sicherheitsniveaus eines zu prüfenden DTH erleichtern sollen.

[Zum Security Testing Guide](<Security Testing Guide>)

## UI-Gestaltung
Beim entwickeln von DTH-Plattformen ist das eigentliche Produkt, dass der Nutzer/Mensch sieht das User Interface, also die grafische Benutzeroberfläche. Die Konzepte und Prinzipien des UX- und UI-Design, die für andere Anwendungen gelten, gelten genauso auch für DTH.

Beim entwickeln des UX-Konzepts und den daraus resultierenden User Interfaces ist es wichtig den Anwender und den Kontext ganzheitlich zu betrachten. Ein genaues Verständnis darüber, was für den Anwender entscheidend ist, führt zu einem guten Bedienkonzept. Im Falle einer DTH-Plattform stehen zwei Aspekte dabei stark im Vordergrund: Vertrauen und Sicherheit. Wenn ein Anwender die Plattform verwenden soll, muss er der Plattform vertrauen. Es gilt herauszufinden, wie dieses Vertrauen in der Domäne bzw. dem Nutzungskontext des DTH erzeugt werden kann. Erreicht wird dies über eine ausführliche Nutzer- bzw. Kontextanalyse und den daraus resultierenden, relevanten Funktionen und dem richtigen Einsatz der Usability Heuristiken und Design Prinzipien für den Kontext DTH-Plattform.

[Zur den UI-Konzepten](<UI-Gestaltung>)