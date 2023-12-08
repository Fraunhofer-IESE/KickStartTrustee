

# Motivation und Ziele

Ein Datentreuhänder (DTH) verwaltet Daten von Datengebenden und stellt diese Datennutzenden zur Verfügung. Um dabei das Vertrauen zwischen beiden Stakeholdern zu stärken, ist die Einhaltung gängiger Sicherheitsstandards womöglich eine der wichtigsten Anforderungen, die ein DTH umzusetzen hat.

Angesichts der zunehmenden Bedrohungen durch Cyberkriminalität und Datenschutzverletzungen ist es unerlässlich, dass Betreiber von DTH ihre Sicherheitsvorkehrungen ständig überprüfen und verbessern, um zu gewährleisten, dass die von ihnen verwalteten Daten sicher und geschützt bleiben.

Ein Prüfkatalog kann dabei helfen, sicherzustellen, dass alle relevanten Bereiche der Datensicherheit geprüft werden und dass der DTH die erforderlichen Sicherheitsstandards erfüllt.

In diesem Prüfkatalog werden verschiedene Aspekte der Datensicherheit wie sichere Konfiguration und sicheres Deployment, Authentisierung und Autorisierung, Identity und Session Management, Eingabevalidierung oder Clientsicherheit betrachtet. Dabei orientiert sich der Katalog an dem OWASP Web Security Testing Guide (WSTG). Da dieser jedoch in seiner Art eher deskriptiv ist, wurden konkrete Prüfkriterien abgeleitet, welche die Bewertung des Sicherheitsniveaus eines zu prüfenden DTH erleichtern sollen.

Eine gründliche Überprüfung der oben genannten Bereiche kann dazu beitragen, dass die Betreiber von DTH ihre Sicherheitsmaßnahmen verbessern und somit das Vertrauen der Kunden und Nutzer in die Sicherheit ihrer Daten stärken.

# Der OWASP Web Security Testing Guide

Aus technischer Sicht sind DTH in den meisten Fällen als Webanwendung realisiert. Daher gelten für DTH auch viele Sicherheitsanforderungen, wie sie auch für Webanwendungen gelten.

Für das Security-Testing von Webanwendungen hat sich der OWASP Web Security Testing Guide (WSTG) unter weiteren Best Practices, Leitfäden und Guidelines als wegweisend etabliert. Er ist ein umfassender Leitfaden für die Durchführung von Sicherheitstests an Webanwendungen. Er wurde entwickelt, um Entwicklern, Testern und Sicherheitsprofis einen umfassenden Überblick über die Methoden, Techniken und Tools zu geben, die bei der Durchführung von Web-Sicherheitstests angewendet werden können.

Der Leitfaden wurde von einem Team von Fachleuten aus der Web-Sicherheitsbranche zusammengestellt und enthält eine detaillierte Beschreibung von Techniken und Methoden, die bei der Identifizierung von Sicherheitslücken und Schwachstellen in Webanwendungen eingesetzt werden können. Der OWASP WSTG ist eine praxisorientierte Ressource, die darauf abzielt, Sicherheitsprobleme in Webanwendungen aufzudecken und entsprechende Maßnahmen zur Behebung dieser Probleme zu empfehlen.

Der Leitfaden ist in verschiedene Abschnitte unterteilt, die sich auf verschiedene Aspekte der Web-Sicherheit konzentrieren, wie z.B. Identifizierung von Schwachstellen, Risikobewertung, Testplanung und Testdurchführung. Jeder Abschnitt enthält eine detaillierte Beschreibung der Techniken, Methoden und Tools, die zur Durchführung von Tests in diesem Bereich eingesetzt werden können.

Insgesamt ist der OWASP Web Security Testing Guide ein unverzichtbares Werkzeug für alle, die mit der Entwicklung und dem Testen von Webanwendungen betraut sind, da er dazu beitragen kann, die Sicherheit von Webanwendungen zu erhöhen und das Risiko von Angriffen und Datenschutzverletzungen zu minimieren.

# Prüfkatalog für Datentreuhänder

Dieses Kapitel bildet den Hauptteil des Prüfkatalogs. Gemäß den einzelnen Themenbereichen des OWASP WSTG werden hier die Prüfpunkte aufgeführt. Das Schema eines Prüfpunktes ist dabei stets gleich: Das  _Threat scenario_  beschreibt einen oder mehrere Techniken von Angreifern zum Ausnutzen bestimmter Schwachstellen. Das  _Objective_  beschreibt das Ziel, das Betreiber von DTH verfolgen sollten, um das Risiko für das Threat scenario zu minimieren. Die  _Definition of done_  beschreibt die eigentlichen Prüfpunkte, die seitens der Betreiber, Entwickler und Administratoren umgesetzt werden müssen, um dem Threat scenario entgegenzuwirken.

Die Definition of Done ist hier als Minimum zu verstehen und hat genauso wenig Anspruch auf Vollständigkeit, wie es einen Anspruch auf vollständige IT-Sicherheit gibt. Dieser Prüfkatalog ist als lebendes Dokument zu verstehen, das permanent weiterentwickelt und aktualisiert werden muss, um mittel- bis langfristig ein hohes Sicherheitsniveau für DTH-Implementierungen zu erreichen.

An einigen Stellen sind die Prüfpunkte in diesem Dokument relativ generalistisch gehalten. Hier liegen die tatsächliche Umsetzung und insbesondere der Umfang der Prüfung im Ermessen des DTH-Betreibers. In manchen Fällen kann es ausreichen, stichprobenhaft zu überprüfen, in manchen muss automatisiert oder gar zusätzlich manuell überprüft werden.

Das Kapitel „Testing for Business Logic“, das ursprünglich im OWASP WSTG vorkommt, wurde aus dem Prüfkatalog gestrichen wurde, da es auf konzeptioneller Ebene alles behandelt, was bereits in den anderen Kapiteln aus technischer Sicht beschrieben wird. Die Betrachtung der Business Logic liegt nicht im Scope dieses Dokuments.

Abschließend sei erwähnt, dass dieser Katalog die Sicht von Entwickler und Betreiber annimmt, der OWASP WSTG jedoch häufig die Perspektive von Testern, die in Black- oder Graybox-Szenarien keine oder nur beschränkte Kenntnis über den Code haben. Da hier jedoch eine vollständige Kenntnis über den Code angenommen wird, können viele Schwachstellen zusammengefasst werden, da ihre Ursache häufig die gleiche ist, (z.B. fehlende oder fehlerhafte Inputvalidierung bei vielen Injection-Schwachstellen) und die Präventionsmaßnahmen entsprechend auch übereinstimmen (z.B. ordnungsgemäße Eingabevalidierung).

## Informationsbeschaffung

Die Sammlung von Informationen über das System oder die Anwendung, um mögliche Angriffsvektoren oder Schwachstellen zu identifizieren, ist typischerweise der erste Schritt bei einem Angriffsversuch auf ein System.

Die Informationsbeschaffung kann verschiedene Methoden umfassen, wie beispielsweise die Durchführung von Portscans, Netzwerkscans, Recherche von öffentlichen Informationen über das System oder die Anwendung, Durchführung von Angriffen mit geringer Auswirkung, wie etwa Fingerprinting, und Social Engineering-Techniken.

Ziel der Informationsbeschaffung im Security Testing ist es, das System oder die Anwendung aus Sicht eines potenziellen Angreifers zu betrachten und zu identifizieren, welche Informationen zugänglich sind. Durch diese Art von Tests können frühzeitig und minimalinvasiv Schwachstellen aufgedeckt werden, die sonst möglicherweise übersehen werden könnten.

Der Großteil der Prüfpunkte in diesem Abschnitt setzt voraus, dass der DTH bereits aktiv in Betrieb und öffentlich erreichbar ist. Sofern der zu untersuchende DTH noch nicht in diesem Stadium ist, kann die Prüfung der entsprechenden Punkte bis zum Go-Live vorübergehend ausgesetzt werden.

Die Prüfpunkte aus diesem Prüfabschnitt wurden vom OWASP Web Security Testing Guide, Kapitel 4.1 („Information Gathering“) abgeleitet. Die folgenden Unterkapitel wurden nicht berücksichtigt:

-   _Fingerprint Web Application (WSTG-INFO-09)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/01-Information_Gathering/09-Fingerprint_Web_Application): Zusammengefasst mit Abschnitt 1.2 (Informationsbeschaffung → Webserver-Fingerprinting)
-   _Map Application Architecture (WSTG-INFO-10)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/01-Information_Gathering/10-Map_Application_Architecture): Außerhalb des Scopes für dieses Dokument

### Leaks durch gezielte Suchmaschinenanfragen  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/01-Information_Gathering/01-Conduct_Search_Engine_Discovery_Reconnaissance_for_Information_Leakage)

_Threat scenario:_

Angreifer können sensible Design- und Konfigurationsinformationen des DTH und dessen betreibender Organisation mittels gängiger Suchmaschinen ausfindig machen. Durch gezielte Manipulation der Suchqueries (site, inurl, intitle, intext, inbody, filetype) lassen sich hier gezielt Informationen aus einzelnen Webseiten filtern. Dabei ist es unerheblich, ob die Informationen direkt (auf der Website der Organisation) oder indirekt (über Dienste von Drittanbietern) bezogen werden können. Sensible Inhalte können beispielsweise betriebsinterne oder vertrauliche Dokumente, Konfigurationsdaten von Systemen oder Credentials sein.

_Objective:_

Die Betreiber von DTH müssen sicherzustellen, dass Angreifer keine kritischen Daten und Dateien über gezielte Suchmaschinensuche identifizieren können. Zur Überprüfung sind explizit mehrere Suchmaschinen zu verwenden, da verschiedene Anbieter auch verschiedene Suchmaschinenergebnisse produzieren können.

_Definition of Done:_

* Die gezielte Suchmaschinenanfrage an den DTH mit filetype durch mindestens zwei Suchmaschinen liefert keine sensiblen Konfigurationen zurück (z.B. .asa, .inc oder .config). Die Wahl der zu testenden Dateiendungen sollte sich an den Technologie-Stack des DTH orientieren.

* Die gezielte Suchmaschinenanfrage an den DTH mit filetype durch mindestens zwei Suchmaschinen liefert nur Textdokumente ohne sensiblen Inhalt zurück (z.B. .txt, .pdf, .doc, .docx sowie sämtliche weiteren Office-Dateitypen). Die Wahl der zu testenden Dateiendungen sollte sich an die typischen Dokumenttypen des DTH betreibenden Unternehmens orientieren.

### Webserver-Fingerprinting  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/01-Information_Gathering/02-Fingerprint_Web_Server)

_Threat scenario:_

Gezielte Requests an Webserver liefern häufig eine Antwort, die Rückschlüsse auf die verwendete Applikation, einschließlich deren Version, erlauben. Diese Informationen werden je nach Anwendung unter Umständen in den Response-Header geschrieben und können so unmittelbar ausgelesen werden („Banner-Grabbing“). Hierzu helfen häufig einfachste Kommandozeilentools zum Senden von HTTP-Requests, beispielsweise Netcat (nc).

_Objective:_

Beim Deployment eines DTH müssen die Betreiber darauf achten, dass keine der nach außen bereitgestellten Dienste die verwendeten Anwendungen und Versionen preisgibt. Das Obfuszieren der Responses kann hierbei meist in den Konfigurationen der jeweiligen Anwendung vorgenommen werden.

_Definition of Done:_

* Response-Header von Anfragen an den DTH liefern keine Informationen über die verwendeten Dienste und deren Versionen. Dies ist für alle Dienste auf allen Ports umzusetzen, die der DTH nach außen bereitstellt. Das Obfuszieren von Response-Headern kann beispielsweise mit dem mod_headers-Modul beim Apache-HTTP-Server umgesetzt werden.

* Error-Responses von Anfragen an den DTH liefern ebenfalls keine Informationen über die verwendeten Dienste und deren Versionen.

### Webserver-Metadaten  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/01-Information_Gathering/03-Review_Webserver_Metafiles_for_Information_Leakage)

_Threat scenario:_

Standardmäßig stellen die meisten Webseiten Dateien zur Verfügung, aus denen beispielsweise Suchmaschinen oder andere automatisierte Tools Metadaten extrahieren können.

_Objective:_

Die Betreiber von DTH müssen daher sicherstellen, dass Standarddateien keine sensiblen Informationen enthalten.

_Definition of Done:_

* Die Datei robots.txt verweist auf keinen Pfad, der nicht für die Öffentlichkeit bestimmt ist.

* Die Datei sitemap.xml verweist auf keine Webseite, die nicht für die Öffentlichkeit bestimmt ist.

* Die Datei security.txt lässt, falls vorhanden, keine Rückschlüsse auf sensitive Daten oder Dienste zu, die der DTH anbietet.

* Die Datei humans.txt lässt, falls vorhanden, keine Rückschlüsse auf sensitive Daten oder Dienste zu, die der DTH anbietet.

* Die meta-Tags der Webseite lassen keine Rückschlüsse auf sensitive Daten oder Dienste zu, die der DTH anbietet.

### Identifikation von Anwendungen  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/01-Information_Gathering/04-Enumerate_Applications_on_Webserver)

_Threat scenario:_

Port- und Versionsscans gehören zu typischen Aktivitäten innerhalb der Reconnaissance-Phase eines Angriffs. Angreifer können mit Portscannern wie Nmap Services enumerieren und dadurch Anwendungen identifizieren, die möglicherweise nicht für die Öffentlichkeit bestimmt sind, beispielsweise Ports für Debugging oder Remotezugriffe.

_Objective:_

Beim Deployment müssen Betreiber von DTH darauf zu achten, dass nur die für den Betrieb notwendigen Services bereitgestellt werden. Der Zugriff auf Remote-Protokolle wie etwa SSH sollte auf ein Minimum reduziert werden, sodass sich nur ein ausgewählter Personenkreis in die betroffenen Systeme einloggen kann.

_Definition of Done:_

* Der DTH bietet ausschließlich für die Aufrechterhaltung des Betriebs notwendige offene Dienste nach außen an. Entsprechend sind nur die für den Betrieb des DTH notwendigen Ports offen.

* Der DTH verwendet keine Domains, die nicht für die Aufrechterhaltung des Betriebs notwendig sind.

* Es existieren keine DTH-Unteranwendungen (z.B. über Subpfade wie  [https://example.com/app1](https://example.com/app1)), die nicht für die Öffentlichkeit bestimmt und entsprechend über die Nutzeraktion des Frontends erreichbar sind.

* Es existieren keine Fernzugriffsmöglichkeiten (z.B. SSH oder RDP) ohne Zugangsbeschränkung via Access Control Lists.

### Leaks aus Webseiteninhalten  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/01-Information_Gathering/05-Review_Webpage_Content_for_Information_Leakage)

_Threat scenario:_

In vielen Release Candidates verbleiben Codefragmente aus der Entwicklungsphase, die ungewollt der Öffentlichkeit zugänglich gemacht werden. Typische Beispiele hierfür sind Kommentare in Code, die Passwörter oder Access Tokens enthalten. Angreifer können diese Informationen einfach auslesen, indem sie beispielsweise den Seitenquelltext mit Browsern analysieren.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass Angreifer keine sensiblen Inhalte in den öffentlichen Teilen des Quelltextes auslesen können.

_Definition of Done:_

* Der öffentlich einsehbare HTML-Quelltext der Webseite enthält keine Kommentare mit sensiblen Informationen, insbesondere Benutzernamen, Passwörter, Access-Keys, Tokens oder Kontaktdaten.

* Die öffentlich einsehbaren JavaScript-Dateien enthalten keine sensiblen Informationen.

* Die öffentlich einsehbaren Source-Map-Dateien enthalten keine sensiblen Informationen.

* Sofern ein öffentliches Git-Repository existiert, enthält dies keine Dateien, die nicht für die Öffentlichkeit bestimmt sind. Für die Entwicklung notwendige, hartkodierte Access-Tokens, Passwörter oder Hashes sind in eine separate Config auszulagern, die in der .gitignore-Datei indexiert sind.

### Initial Footholds in Applikationen  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/01-Information_Gathering/06-Identify_Application_Entry_Points)

_Threat scenario:_

Sämtliche Endpunkte, in denen clientseitig Daten an das Backend übergeben werden, stellen eine potenzielle Angriffsfläche dar. Für den Fall, dass die Eingabe an den jeweiligen Endpunkten nicht ordnungsgemäß validiert wird (z.B. bei GET-Query- oder POST-Body-Parametern), können Angreifer aus dem vorgesehenen Scope der Eingabe ausbrechen und im schlechtesten Falle serverseitigen Code ausführen. Bekannte Beispiele hierfür sind SQL und Command Injections sowie Cross-site Scripting; allesamt Schwachstellen, die nach wie vor zu den am häufigsten vorkommenden in Webapplikationen gehören.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass die Parameter aller Request-Endpunkte nach Vorgaben von Best Practices bereinigt werden und vor Missbrauch geschützt sind.

_Definition of Done:_

* Die automatisierte Gray-Box-Analyse mit mindestens einem Endpoint-Analyzer (z.B. OWASP Attack Surface Detector) identifiziert keine kritischen Endpunkte.

### Identifikation kritischer Pfade  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/01-Information_Gathering/07-Map_Execution_Paths_Through_Application)

_Threat scenario:_

Kritische Pfade bilden den geläufigsten Eintrittsvektor beim Angriff von Webapplikationen. Ein Angreifer kann hier auf vielfältige Weise tätig werden, beispielsweise durch die Identifikation von kritischen Pfaden, die nicht für die Öffentlichkeit bestimmt sind oder das Ausnutzen von fehlerhafter Eingabevalidierung, wie sie bei SQL-Injections oder Cross-site Scripting Verwendung findet.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass es keine kritischen Pfade gibt, die von Angreifern ausgenutzt werden können.

_Definition of Done:_

* Die automatisierte Black-Box-Analyse mit mindestens einem Spidering-Tool (z.B. OWASP Zed Attack Proxy) identifiziert keine Pfade, die nicht für die Öffentlichkeit bestimmt sind.

* Die automatisierte Black-Box-Analyse mit mindestens einem Taint-Analyse-Tool (z.B. Scanner von BurpSuite Professional à Scanner) identifiziert keinen unbereinigten User-Input.

* Die automatisierte Black-Box-Analyse mit mindestens einem Tool zum Testen von Race-Conditions (z.B. BurpSuite Professional à Intruder) identifiziert keine Race-Condition.

### Web-Framework-Fingerprinting  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/01-Information_Gathering/08-Fingerprint_Web_Application_Framework)

_Threat scenario:_

Die Kenntnis eines Angreifers über mögliche Web-Frameworks, zum Beispiel von Drittanbietern, erhöht deren Kenntnisstand über das Zielsystem und erlaubt ihnen, gezielter nach Schwachstellen oder Exploits zu suchen.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass keine sensiblen Informationen über (3rd-Party-) Software nach außen gelangen, die der DTH verwendet.

_Definition of Done:_

* Die HTTP-Response-Header des DTH liefern keinerlei Rückschlüsse über verwendete Frameworks (z.B. via X-Powered-By oder X-Generator).

* Die Cookies des DTH liefern keinerlei Rückschlüsse über verwendete Technologien.

* Der HTML-Seitenquelltext des DTH liefert keinerlei Rückschlüsse über verwendete Technologien (z.B. src-Attribut in HTML-Tags).

* Es existieren keine Standard-Pfade oder Dateien, die Rückschlüsse auf eine verwendete Technologie zulassen (z.B. wp-content für WordPress-Applikationen).

## Konfiguration und Deployment

Das Ziel von Konfigurations- und Deployment-Tests im Security Testing ist es, sicherzustellen, dass das System oder die Anwendung sicher und geschützt ist, bevor oder spätestens während das System in einer produktionsnahen Umgebung eingesetzt wird. Durch diesen Prozess können potenzielle Schwachstellen und Angriffspunkte identifiziert werden, bevor sie von böswilligen Angreifern ausgenutzt werden können.

Zusätzlich zu der Überprüfung von Konfigurationseinstellungen und Deployment-Tests können auch andere Tests durchgeführt werden, wie etwa Penetrationstests, um Schwachstellen im System oder in der Anwendung aufzudecken.

Die Prüfpunkte aus diesem Prüfabschnitt wurden vom OWASP Web Security Testing Guide, Kapitel 4.2 („Configuration and Deployment Management Testing“) abgeleitet. Die folgenden Unterkapitel wurden nicht berücksichtigt:

-   _Review Old Backup and Unreferenced Files for Sensitive Information (WSTG-CONF-04)_ [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/02-Configuration_and_Deployment_Management_Testing/04-Review_Old_Backup_and_Unreferenced_Files_for_Sensitive_Information)_:_  Zusammengefasst mit Abschnitt 2.3 (Konfiguration und Deployment → Dateiendungen)
-   _Enumerate Infrastructure and Application Admin Interfaces (WSTG-CONF-05)_ [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/02-Configuration_and_Deployment_Management_Testing/10-Test_for_Subdomain_Takeover): Zusammengefasst mit Abschnitt 3.1 (Identity Management → Rollendefinitionen)
-   _Test File Permission (WSTG-CONF-09)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/02-Configuration_and_Deployment_Management_Testing/09-Test_File_Permission): Zusammengefasst mit Abschnitt 3.1 (Identity Management → Rollendefinitionen)
-   _Test Cloud Storage (WSTG-CONF-11)_ [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/02-Configuration_and_Deployment_Management_Testing/11-Test_Cloud_Storage): Außerhalb des Scopes für DTH

### Netzwerkinfrastruktur  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/02-Configuration_and_Deployment_Management_Testing/01-Test_Network_Infrastructure_Configuration)

_Threat scenario:_

Die Suche nach Schwachstellen in der gesamten Netzwerkinfrastruktur gehört zum Standardvorgehen von Angreifern. Nach erfolgreichem Enumerieren der Systeme werden diese in aller Regel auf bekannte Schwachstellen überprüft. Dazu gehören nicht nur öffentlich bekannte Exploits, sondern auch das Identifizieren von Standardbenutzernamen und -passwörtern, die im durch die Verbreitung im Internet mittlerweile in einer Vielzahl von Wordlists gesammelt werden.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass die Netzwerkinfrastruktur so wenig Angriffsfläche wie möglich bietet.

_Definition of Done:_

* Es ist mindestens ein Schwachstellenscanner zur Durchführung regelmäßiger Scans der Netzwerkinfrastruktur eingerichtet (z.B. Qualys, Tenable Nessus oder Greenbone OpenVAS).

* Die Durchführung der Schwachstellenscans ergab keine kritischen Befunde.

* Es existieren keine Standardbenutzernamen (z.B. admin) für Administratoren bei Netzwerkkomponenten und -diensten. Hierzu sollte gegen eine Blacklist / Wordlist gegengeprüft werden.

* Es werden keine Standardpasswörter für Administratoren bei Netzwerkkomponenten und -diensten verwendet. Hierzu sollte gegen eine Blacklist / Wordlist gegengeprüft werden.

### Konfiguration der Applikationsplattform  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/02-Configuration_and_Deployment_Management_Testing/02-Test_Application_Platform_Configuration)

_Threat scenario:_

Angreifer können durch gezielte Suche nach Dateinamen von Standarddateien einerseits Rückschlüsse auf die verwendeten Frameworks und Libraries eines DTH ziehen und einerseits bei ihrer Suche auf sensible Daten stoßen.

_Objective:_

Die Betreiber von DTH müssen, soweit es ihnen möglich ist, auf die Verwendung von Standarddateinamen verzichten. Diese sind nach Möglichkeit so zu Obfuszieren, dass Angreifer nicht automatisiert nach ihnen suchen können.

_Definition of Done:_

* Es existieren keine Standarddateien (z.B. CodeBrws.asp oder sendmail.jsp. Sämtliche Standarddateien wurden durch Umbenennung „personalisiert“.

* Es existiert kein Debugging-Code in der produktiven Umgebung.

* Es existiert ein zentraler Logging-Server, der für jeden Dienst des DTH Daten loggt.

* Logs werden auf einem separaten Server unabhängig vom DTH gespeichert

* Der Logserver implementiert Maßnahmen gegen Denial-of-Service-Angriffe.

* Die Logdateien enthalten keine sensiblen Daten.

### Dateiendungen  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/02-Configuration_and_Deployment_Management_Testing/03-Test_File_Extensions_Handling_for_Sensitive_Information)

_Threat scenario:_

Angreifer können bei Webseiten gezielt nach kritischen Dateiendungen suchen. Darunter fallen beispielsweise vertrauliche Konfigurationen oder Backups, die nur für interne Zwecke vorgesehen sind. Darüber hinaus können Angreifer eine zu lockere Policy für Dateiendungen auch missbrauchen, um serverseitig ausführbare Dateien hochzuladen.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass Dateien mit kritischem Dateiformat weder gelesen noch geschrieben werden können.

_Definition of Done:_

* Es existieren keine öffentlich erreichbaren Konfigurationsdateien (z.B. .asa, .inc oder .config).

* Es existieren keine öffentlich erreichbaren Backupdateien (z.B. .bak, .old oder ~<dateiname>).

* Es existieren keine öffentlich erreichbaren Dokumente mit sensiblem Inhalt (z.B. .zip, .tar, .txt, .pdf, .doc, .docx und weitere Office-Dateien).

* Das Hochladen von ausführbarem Code ist in keinem Upload-Feld möglich. Hierzu ist eine ausführliche Blacklist erstellt worden, die auch Sonderfälle wie .phtml oder .pht abdeckt.

### HTTP-Methoden  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/02-Configuration_and_Deployment_Management_Testing/06-Test_HTTP_Methods)

_Threat scenario:_

Der Missbrauch von HTTP-Methoden kann zu vielen verschiedenen Bedrohungsszenarien für DTH werden. Einer hiervon kann beispielsweise ein Cross-site Tracing (XST)-Angriff sein, der durch die Zweckentfremdung der TRACE-Methode ermöglicht wird. Ein anderes Beispiel ist ein Authentication-Bypass-Angriff durch Überschreiben von existierenden Credentials mittels PUT oder POST.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass nicht pauschal alle HTTP-Methoden für alle Endpunkte erlaubt sind. Vielmehr müssen Endpunkte kontextspezifisch sein und nur die nötigsten HTTP-Methoden erlauben. Dies gilt insbesondere für die Methoden PUT und DELETE.

_Definition of Done:_

* Die HTTP-Methode PUT ist für keinen Endpunkt erlaubt, sofern nicht ausdrücklich für die Funktionalität des Endpunkts benötigt.

* Die HTTP-Methode DELETE ist für keinen Endpunkt erlaubt, sofern nicht ausdrücklich für die Funktionalität des Endpunkts benötigt.

* DIE HTTP-Methode TRACE ist für keinen Endpunkt erlaubt.

* Die HTTP-Header X-HTTP-Method, X-HTTP-Method-Override und X-Method-Override sind für keinen Endpunkt erlaubt.

### HTTP Strict Transport Security  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/02-Configuration_and_Deployment_Management_Testing/07-Test_HTTP_Strict_Transport_Security)

_Threat scenario:_

Im Falle von unverschlüsselten Verbindungen können Angreifer möglicherweise den Datenverkehr zwischen Nutzer und DTH abfangen und somit die Vertraulichkeit des DTH beeinträchtigen.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass der DTH HTTP Strict Transport Security (HSTS) verwendet. Dadurch wird erzwungen, dass Clients ausschließlich über HTTPS mit dem DTH kommunizieren.

_Definition of Done:_

* Es existiert bei der Nutzung des DTH keine Möglichkeit, HSTS zu umgehen.

* Sämtliche HTTP-Anfragen werden auf HTTPS umgeleitet

### RIA Cross-Domain-Policy  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/02-Configuration_and_Deployment_Management_Testing/08-Test_RIA_Cross_Domain_Policy)

_Threat scenario:_

Wenn der DTH einen zu freizügigen Cross-Domain-Zugriff von anderen Webseiten erlaubt, können Angreifer dies als Grundlage für einen Cross-Site-Request-Forgery (CSRF)-Angriff nutzen.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass der erlaubte Zugriff von anderen Domänen auf ein Minimum reduziert wird.

_Definition of Done:_

* Es existiert kein Eintrag in der Cross-Domain-Policy-Datei, die Zugriff von einer beliebigen Domain erlaubt (via Wildcard *).

### Subdomain Takeover  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/02-Configuration_and_Deployment_Management_Testing/10-Test_for_Subdomain_Takeover)

_Threat scenario:_

Besitzt der DTH Subdomain-Einträge im Domain Name Server (DNS), die nicht mehr aktiv verwendet werden, können Angreifer bei einem externen Dienst die Subdomain für sich beanspruchen. Im schlechtesten Fall prüft der externe Dienst den Besitz der Subdomain nicht ordnungsgemäß und der Angreifer kann die Subdomain auf eine eigene, schädliche Anwendung zeigen lassen.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass keine unbenutzten Subdomain-Einträge im DNS vorliegen.

_Definition of Done:_

* Es existiert keine Subdomain, die nicht in aktiver Verwendung ist und auf eine Applikation oder Unterapplikation des DTH zeigt („Altlasten“). Dies ist für sämtliche DNS-Record-Typen zu prüfen, beispielsweise: A, CNAME, MX, NS oder TXT:

## Identity Management

Im Rahmen von Identity Management-Tests soll sichergestellt werden, dass Benutzerkonten- und Berechtigungen ordnungsgemäß konfiguriert sind. Insbesondere soll in diesem Abschnitt überprüft werden, ob Rollendefinitionen sowie Registrierungs- und Accountbereitstellungsprozesse widerspruchsfrei definiert sind und keinen Spielraum für Authentication Bypass-Angriffe lassen.

Die Prüfpunkte aus diesem Prüfabschnitt wurden vom OWASP Web Security Testing Guide, Kapitel 4.3 („Identity Management Testing“) abgeleitet.

### Rollendefinitionen  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/03-Identity_Management_Testing/01-Test_Role_Definitions)

_Threat scenario:_

Die verschiedenen Rollen und Rechte, die in einem DTH umgesetzt werden, können eine erhöhte Angriffsfläche bieten, wenn sie nicht korrekt umgesetzt werden. Ein Angreifer könnte, ausgehend von einer unsachgemäßen Konfiguration des Rollen- und Rechtekonzeptes, eine Privilege Escalation durchführen und sich damit seinen technischen Handlungsspielraum deutlich erhöhen.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass Nutzer ihre eigenen Rollen und Berechtigungen nicht unrechtmäßig manipulieren können.

_Definition of Done:_

* Es ist keinem Nutzer möglich, durch Manipulation von Request-Parametern eine andere Rolle einzunehmen, z.B. durch Ändern von Parametern wie role=admin oder isAdmin=true. Dies ist für sämtliche möglichen Rollenkombinationen zu prüfen.

* Es ist keinem Nutzer möglich, durch Manipulation von Accountvariablen eine andere Rolle einzunehmen, z.B. durch Ändern in Role: manager. Dies ist für sämtliche möglichen Rollenkombinationen zu prüfen.

* Es ist keinem Nutzer ohne administrative Rechte möglich, auf privilegierte Pfade wie /admin, /mod oder /backups zuzugreifen. Dies ist für sämtliche nichtadministrative Nutzerrollen zu prüfen.

### Nutzerregistrierung  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/03-Identity_Management_Testing/02-Test_User_Registration_Process)

_Threat scenario:_

Die Registrierung neuer Nutzer ist im Idealfall ein strenger Prozess, der klar definierte Eingabewerte erwartet und in jedem Schritt der Prozesskette ein eindeutiges Vorgehen und eindeutige Verantwortliche benennt. Dabei ist es unerheblich, ob der Registrierungsprozess manuell, teilautomatisiert oder automatisiert erfolgt: In jedem Fall können Angreifer versuchen, aus dem Standardprozess auszubrechen.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass Nutzer nicht unrechtmäßig aus dem Scope des Registrierungsprozesses ausbrechen können.

_Definition of Done:_

* Es besteht keine Möglichkeit, sich zu registrieren, ohne die verpflichtenden Felder des Registrierungsformular auszufüllen. Das beinhaltet auch die allgemeinen Geschäftsbedingungen sowie die Datenschutzerklärung. Für API-Requests gelten die gleichen Voraussetzungen wie für das Registrierungsformular.

* Es besteht für den gewöhnlichen Registrierungsprozess keine Möglichkeit, einen Account mit administrativen Rechten anzulegen.

* Es besteht keine Möglichkeit, sich unter dem gleichen primären Identifier (z.B. die Mail-Adresse) mehrfach zu registrieren.

* Die Bestätigungsmail enthält keine Nutzernamen oder Passwörter im Klartext.

### Accountbereitstellung  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/03-Identity_Management_Testing/03-Test_Account_Provisioning_Process)

_Threat scenario:_

Eine unsachgemäße Konfiguration der Accountbereitstellung kann zur Folge haben, dass Angreifer möglicherweise andere Nutzer sich selbst oder andere Nutzer auf- oder abzuwerten, um ihnen entweder a) mehr Berechtigungen zu gewähren als vorgesehen, oder ihnen b) Berechtigungen zu entziehen, um ihren Handlungsspielraum bewusst zu beschneiden.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass Nutzer die Rollen und Berechtigungen anderer Nutzer nicht unrechtmäßig manipulieren können.

_Definition of Done:_

* Es gibt keine Möglichkeit, den letzten Administrator zu löschen, ohne dass zuvor ein neuer bestimmt wurde.

* Keine Nutzer können anderen Nutzern Rollen und Rechte zuweisen, die höher sind als die eigenen.

* Keine Nutzer können anderen Nutzern Rollen und Rechte entziehen, die höher sind als die eigenen.

* Es gibt keine Möglichkeit, den Validierungsprozess bei der Erstellung von Nutzern zu umgehen, beispielsweise durch Crafting von Bestätigungsmails.

### Enumerieren von Accounts  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/03-Identity_Management_Testing/04-Testing_for_Account_Enumeration_and_Guessable_User_Account)

_Threat scenario:_

Für einen erfolgreichen Brute-Force-Angriff genügt in den seltensten Fällen die alleinige Kenntnis über ein Passwort. In den meisten Fällen ist zusätzlich die Kenntnis über den Benutzernamen erforderlich. Angreifer können die Rückmeldung von fehlgeschlagenen Loginversuchen nutzen, um Benutzernamen zu identifizieren.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass Angreifer nicht durch bewusste, falsche Benutzer- oder Passworteingaben auf den Benutzernamen schließen können.

_Definition of Done:_

* Das Feedback aller Login-Masken lässt keine Rückschlüsse auf die falsche Eingabe eines Benutzernamens zu (z.B. „Falscher Benutzername“). Entsprechend gilt auch für die HTTP-Fehlercodes.

### Schwache Policies  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/03-Identity_Management_Testing/05-Testing_for_Weak_or_Unenforced_Username_Policy)

_Threat scenario:_

Schwache Passwörter bilden nach wie vor einer der häufigsten Angriffsvektoren in Webapplikationen. In modernen Webapplikationen ist daher eine zeitgemäße Password-Policy notwendig. Nicht vorhandene, schwache oder umgehbare Policies sorgen dafür, dass Nutzer potenziell schwache Passwörter verwenden können, die sich einfacher erraten lassen.

_Objective:_

Die Betreiber von DTH sind (mit) dafür verantwortlich, ihren Nutzern die Verwendung von einfach zu erratenden Benutzernamen oder Passwörter zu verbieten.

_Definition of Done:_

* Es gibt keine Möglichkeit, die Policy für Benutzernamen bei der Registrierung oder der Änderung des Profils zu umgehen

* Es gibt keine Möglichkeit, die Policy für Passwörter bei der Registrierung oder der Änderung des Profils zu umgehen

## Authentisierung

Authentisierung bezieht sich auf den Prozess der Überprüfung der Identität einer Person oder eines Systems, um sicherzustellen, dass sie tatsächlich die Person oder das System sind, die sie vorgeben zu sein. Die Authentisierung ist ein wichtiger Bestandteil der Informationssicherheit, da sie sicherstellt, dass nur autorisierte Benutzer auf bestimmte Ressourcen zugreifen können.

Die Sicherheit der Authentisierung hängt von verschiedenen Faktoren ab, wie z.B. der Stärke der Authentisierungsmethoden, der Sicherheit der Übertragung von Authentisierungsinformationen, der Überwachung von fehlgeschlagenen Authentisierungsversuchen sowie der serverseitigen Vorgabe von Passwortrichtlinien. Alle diese Aspekte werden in den folgenden Unterabschnitten behandelt.

Die Prüfpunkte aus diesem Prüfabschnitt wurden vom OWASP Web Security Testing Guide, Kapitel 4.4 („Authentication Testing“) abgeleitet. Die folgenden Unterkapitel wurden nicht berücksichtigt:

-   _Testing for Weak Password Change or Reset Functionalities (WSTG-ATHN-08)_ [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/04-Authentication_Testing/08-Testing_for_Weak_Security_Question_Answer): Zusammengefasst mit Abschnitt 4.7 (Authentisierung → Schwache Passwort-Policies)

### Verschlüsselte Übertragung von Credentials  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/04-Authentication_Testing/01-Testing_for_Credentials_Transported_over_an_Encrypted_Channel)

_Threat scenario:_

Ein Angreifer kann durch einen Man-in-the-Middle-Angriff versuchen, unerlaubt Credentials von Nutzern auszulesen. Dies kann insbesondere dann erfolgreich sein, wenn die Zugangsdaten unverschlüsselt übertragen werden, wie dies beispielsweise bei Basic Authentication der Fall ist.

_Objective:_

Die Betreiber eines DTH müssen sicherstellen, dass Zugangsdaten stets verschlüsselt von Client zu Server übertragen werden.

_Definition of Done:_

* Die Übertragung von Credentials erfolgt in jedem Fall verschlüsselt. Basic Authentication wird generell nicht verwendet.

### Standard-Credentials  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/04-Authentication_Testing/02-Testing_for_Default_Credentials)

_Threat scenario:_

Die gezielte Suche nach häufig verwendeten oder Standard-Credentials kann einen Angreifer unter Umständen schneller zum Ziel führen als reines Bruteforcing. Es existiert eine Vielzahl an öffentlich verfügbaren Wordlists mit Standard-Credentials, wie etwa rockyou.txt, die Angreifer automatisiert durchiterieren können.

_Objective:_

Die Betreiber von DTH müssen die Verwendung von Standard-Credentials durch Nutzer aktiv unterbinden.

_Definition of Done:_

* In der Benutzerdatenbank des DTH existieren keine Standardbenutzernamen (z.B. admin, root, system oder guest).

* Gängige Standardbenutzernamen werden bei der Registrierung gegen eine Blacklist geprüft und unterbunden.

* Gängige Passwörter werden bei der Registrierung gegen eine Blacklist geprüft und die Nutzung von Standardpasswörtern wird unterbunden.

* Es gibt keine Credentials in den Datenbanken, die im Klartext gespeichert sind. Credentials sind zu hashen. Bei der Wahl des Hashverfahrens ist auf gängige Sicherheitsrichtlinien zu achten (z.B. BSI).

* In der Benutzerdatenbank des DTH stimmt kein Passworthash mit den Passworthashes gängiger Wordlists (z.B. rockyou.txt) überein.

### Schwache Lock-Out-Mechanismen  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/04-Authentication_Testing/03-Testing_for_Weak_Lock_Out_Mechanism)

_Threat scenario:_

Mittels Bruteforcing können Angreifer mit Kenntnis des Benutzernamens versuchen, das Passwort (gezielt) zu erraten.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass Bruteforce-Angriffe aktiv verhindert werden.

_Definition of Done:_

* Jede Loginmaske ist mit einem Bruteforce-Schutz versehen (z.B. Zeitbasierter Lockout oder CAPTCHAs). Bei einem zeitbasierten Lockout sollte die maximale Anzahl an fehlgeschlagenen Login-Versuchen 10 nicht überschreiten und die minimale Zeit des Lockouts 5 Minuten betragen.

### Bypass von Authentisierung  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/04-Authentication_Testing/04-Testing_for_Bypassing_Authentication_Schema)

_Threat scenario:_

Je nach Konfiguration eines DTH können Angreifer die vorgesehene Authentisierung umgehen, indem sie beispielsweise die Flags von HTTP-Requests manipulieren, wenn dies nicht aktiv unterbunden wird. Ein Beispiel hierfür ist das Ändern von authenticated=no zu authenticated=yes. Sofern Session-IDs nicht zufallsgeneriert sind, können sie unter Umständen berechnet werden. Das einfachste Beispiel hierfür ist eine inkrementierende Ganzzahl.

_Objective:_

Die Betreiber von DTH müssen aktiv verhindern, dass Angreifer oder unberechtigte Nutzer die vorgegebenen Authentisierungsmaßnahmen umgehen.

_Definition of Done:_

* Kein Nutzer hat die Möglichkeit, die Authentisierung durch einfaches Ändern von Parametern oder Flags zu umgehen.

* Sämtliche password-Felder werden eingabevalidiert. Die Eingabe des password-Felds muss gemäß der Passwort-Policy gefiltert werden.

### „Passwort merken“-Funktionalität  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/04-Authentication_Testing/05-Testing_for_Vulnerable_Remember_Password)

_Threat scenario:_

Angreifer können durch Stehlen der Session-Cookies im schlechtesten Fall Rückschlüsse auf die ursprünglichen Zugangsdaten des Benutzers schließen und ihren Angriff dadurch persistieren.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass Session-IDs nach gängigen Best-Practices erzeugt werden und keine sensiblen Informationen enthalten.

_Definition of Done:_

* Sofern ein „Passwort merken“-Feature vorgesehen ist, sollte eine Cookie-Expiration-Time von einem Monat nicht überschritten werden.

### Browser-Cache-Schwachstellen  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/04-Authentication_Testing/06-Testing_for_Browser_Cache_Weaknesses)

_Threat scenario:_

Sobald Angreifer Zugriff auf den Browser-Cache von Nutzern haben, können sie diesen auslesen. Im schlechtesten Fall enthält der Cache sensible Informationen wie Adressen, Kreditkartennummern, Sozialversicherungsnummern oder Benutzernamen.

_Objective:_

Die Betreiber von DTH müssen aktiv unterbinden, dass sensible Information im Cache des Browsers seiner Nutzer gespeichert wird.

_Definition of Done:_

* Keine Seite mit sensiblem Inhalt wird im Browser gecached. Hierzu kann die Cache-Control-Directive im HTTP-Response-Header den Browser anweisen, nicht in den Cache zu schreiben, zum Beispiel mit den Flags must-revalidate oder no-cache.

  

Es befinden sich zu keinem Testzeitpunkt sensible Daten im Browsercache.

### Schwache Passwort-Policies  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/04-Authentication_Testing/07-Testing_for_Weak_Password_Policy)

_Threat scenario:_

Das Bruteforcing von Passwörtern ist nach wie vor einer der häufigsten Angriffsvektoren. Je niedriger die Entropie eines Passworts, desto eher kann es durch gezieltes Ausprobieren erraten werden. Dabei müssen Angreifer nicht einmal sämtliche Kombinationen ausprobieren, es genügt oft das Verwenden von großen Wordlists und deren Permutation, um an das Ziel zu gelangen.

_Objective:_

Die Betreiber von DTH müssen die Verwendung unsicherer Passwörter durch Nutzer so weit wie möglich einschränken.

_Definition of Done:_

* Die Passwortrichtlinie des DTH setzt voraus, dass die Zeichenlänge des Passworts mindestens 8 Zeichen lang ist.

* Die Passwortrichtlinie des DTH setzt voraus, dass das Passwort eine Kombination aus Groß- und Kleinbuchstaben, Ziffern und Sonderzeichen sein muss. Jeder Zeichentyp muss somit mindestens einmal vorkommen. Alternativ kann eine individuellere Passwortrichtlinie gemäß BSI verwendet werden:

· 20 bis 25 Zeichen lang bei Verwendung von zwei Zeichenarten

· 8 bis 12 Zeichen lang bei Verwendung von vier Zeichenarten

· 8 Zeichen lang bei Verwendung von drei Zeichen und Multi-Faktor-Authentisierung

* Die Passwortrichtlinie des DTH setzt voraus, dass das Passwort nicht in einer öffentlich bekannten Wordlist vorkommt (z.B. rockyou.txt).

* Die Passwortrichtlinie des DTH setzt voraus, dass keine Begriffe aus dem eigenen Profil des Nutzers im verwendeten Passwort auftauchen (z.B. Vorname, Nachname, Straße, oder Stadt).

* Die Passwortrichtlinie des DTH setzt voraus, dass ein neues Passwort nicht identisch zu den letzten acht verwendeten Passwörtern sein darf.

* Es muss Nutzern die Möglichkeit gegeben werden, eine zwei-Faktor-Authentisierung einrichten zu können.

### Schwache Passwort-Resets oder -Änderungen  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/04-Authentication_Testing/09-Testing_for_Weak_Password_Change_or_Reset_Functionalities)

_Threat scenario:_

In einem fortgeschrittenen Stadium kann ein Angreifer bei Kenntnis eines Benutzernamens und Zugang zu dessen Mail-Postfach versuchen, das Abhandenkommen eines Passworts vorzutäuschen.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass das Zurücksetzen von Passwörtern nur Administratoren und den jeweiligen Besitzern der entsprechenden Accounts gestattet ist.

_Definition of Done:_

* Es ist Nutzern nicht möglich, eigene Sicherheitsfragen zu erzeugen.

* Das Zurücksetzen des Passworts ist erst möglich, nachdem die Sicherheitsfrage(n) korrekt beantwortet wurde(n).

* Die Aufforderung zum Passwort-Reset wird per Mail mit einem temporären Token versendet. Der Nutzer muss diesen Reset bestätigen, bevor das bisherige Passwort abläuft. Erst nach Klicken des Bestätigungslinks wird der eigentliche Reset eingeleitet.

* Das neue Passwort muss ebenso wie alle Passwörter der Passwortrichtlinie des DTH genügen.

* Zu keinem Zeitpunkt wird das alte Passwort im Klartext übertragen (was auch per se nicht möglich sein darf, da Passwörter als Hash gespeichert sein sollten).

### Schwächere Authentisierung in alternativen Anwendungen  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/04-Authentication_Testing/10-Testing_for_Weaker_Authentication_in_Alternative_Channel)

_Threat scenario:_

Sollte ein DTH unter  [https://example.com](https://example.com/)  mehrere Unteranwendungen bereitstellen, beispielsweise bei mobilen Ansichten über die Subdomain  [https://m.example.com](https://m.example.com/)  oder Schnittstellen über  [https://api.example.com](https://api.example.com/), so kann ein Angreifer gezielt nach Inkonsistenzen zwischen Unter- und Hauptanwendung suchen. Sollten für die Hauptanwendung alle Anforderungen an die Authentisierung erfüllt sein, muss dies noch nicht zwangsläufig für die Unteranwendung gelten. Ein Angreifer kann im Falle von Schwachstellen in den Unteranwendungen einen Authentication Bypass durchführen, ohne die Hauptanwendung angegriffen zu haben.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass für alternative Anwendungen die gleichen Anforderungen an Authentisierung gelten wie für die Hauptanwendung.

_Definition of Done:_

* Für alternative Unteranwendungen des DTH sind sämtliche Anforderungen an die Authentisierung exakt so umgesetzt, wie sie auch in der Hauptanwendung umgesetzt sind.

## Autorisierung

Autorisierung bezieht sich auf den Prozess der Überprüfung, ob ein authentisierter Benutzer berechtigt ist, auf eine bestimmte Ressource oder eine bestimmte Funktion zuzugreifen oder eine bestimmte Aktion auszuführen. Die Autorisierung ist ein wichtiger Bestandteil der Informationssicherheit, da sie sicherstellt, dass nur autorisierte Benutzer auf bestimmte Ressourcen zugreifen und bestimmte Aktionen ausführen können.

Im Gegensatz zur Authentisierung, die die Identität einer Person oder eines Systems überprüft, um sicherzustellen, dass sie tatsächlich die Person oder das System sind, die sie vorgeben zu sein, überprüft die Autorisierung, ob der authentisierte Benutzer die erforderlichen Berechtigungen hat, um auf die angeforderte Ressource oder Funktion zuzugreifen oder die angeforderte Aktion auszuführen.

Die Autorisierung wird in der Regel durch Zugriffskontrollen oder Berechtigungen implementiert, die den Benutzern oder Gruppen von Benutzern bestimmte Zugriffsrechte auf Ressourcen oder Funktionen gewähren oder verweigern.

Die Prüfpunkte aus diesem Prüfabschnitt wurden vom OWASP Web Security Testing Guide, Kapitel 4.5 („Authorization Testing“) abgeleitet. Die folgenden Unterkapitel wurden nicht berücksichtigt:

-   _Testing for Bypassing Authorization Schema (WSTG-ATHZ-02)_ [**🔗**](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/05-Authorization_Testing/02-Testing_for_Bypassing_Authorization_Schema): Zusammengefasst mit Abschnitt 3.1 (Identity Management → Rollendefinitionen)
-   _Testing for Privilege Escalation“ (WSTG-ATHZ-03)_ [**🔗**](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/05-Authorization_Testing/03-Testing_for_Privilege_Escalation): Zusammengefasst mit Abschnitt 3.1 (Identity Management → Rollendefinitionen)

### Directory Traversal File Inclusion  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/05-Authorization_Testing/01-Testing_Directory_Traversal_File_Include)

_Threat scenario:_

Angreifer können an verschiedenen Stellen in der Anwendung prüfen, ob Query- oder Bodyparameter Dateien inkludieren ohne auf Zugriffsberechtigung zu prüfen. Ein Beispiel hierfür ist der Zugriff auf die kritische Datei /etc/passwd, die Passworthashes aller Systemnutzer erhält. Ein solcher Zugriff könnte über eine File Inclusion erfolgen, beispielsweise:  [https://example.com/index.jsp?item=../../../../etc/passwd](https://example.com/index.jsp?item=../../../../etc/passwd)

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass Angreifer nicht aus dem Scope der Anwendung ausbrechen und dadurch auf den betreibenden Server zugreifen können.

_Definition of Done:_

* Es existieren keine Query- oder Body-Parameter oder Cookie, die eine Datei inkludieren und nicht ordnungsgemäß eingabevalidiert sind. Die Prüfung kann im Blackbox- oder im Graybox-Ansatz anhand des Quellcodes erfolgen. Dies ist für verschiedene Varianten zu prüfen:

· Nicht-codiert und URL-codiert

· Forward-Slash (/)- sowie Backward-Slash (\)-separierte Pfade

· Local File Inclusion (param=../<path>) sowie Remote File Inclusion (param=[http://malicious/url](http://malicious/url))

### Unsichere Objektreferenzierungen  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/05-Authorization_Testing/04-Testing_for_Insecure_Direct_Object_References)

_Threat scenario:_

Ein Angreifer kann versuchen, auf Objekte zuzugreifen, die nicht für ihn bestimmt sind, zum Beispiel über direkten Zugriffsversuch auf ein Objekt mittels ID:  [http://foo.bar/somepage?invoice=12345](http://foo.bar/somepage?invoice=12345).

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass Nutzer auf eigene Objekte zugreifen können und nicht auf Objekte anderer Nutzer.

_Definition of Done:_

* Es existiert kein Query- oder Bodyparameter, bei dem auf Ressourcen wie Datenbankeinträge, Bilder, Dokumente oder Archive außerhalb der eigenen Berechtigungen zugegriffen werden kann.

* Es existiert kein Query- oder Bodyparameter, bei dem ein Nutzer Einstellungen und Attribute anderer Nutzer ändern kann, insbesondere Zahlungsinformationen, Benutzernamen oder Passwörter.

## Sitzungsmanagement

Sicherheitsprobleme im Zusammenhang mit Session-Management können schwerwiegende Auswirkungen auf die Sicherheit einer Anwendung haben. Ein erfolgreicher Angriff auf die Session-Management-Mechanismen einer Anwendung kann es einem Angreifer ermöglichen, die Identität eines Benutzers zu stehlen oder eine Sitzung zu übernehmen, was zu unbefugtem Zugriff auf vertrauliche Daten oder sogar zur Übernahme der gesamten Anwendung führen kann. Daher ist es wichtig, dass Anwendungen umfassendem Testing unterzogen werden, um sicherzustellen, dass die Session-Management-Mechanismen ordnungsgemäß implementiert und robust genug sind, um vor Angriffen zu schützen. Dies umfasst die Überprüfung von Sitzungstoken, das Ablaufen von Sitzungen, sowie Sitzungsinformationen in Cookies oder URL-Parametern.

Die Prüfpunkte aus diesem Prüfabschnitt wurden vom OWASP Web Security Testing Guide, Kapitel 4.6 („Session Management Testing“) abgeleitet. Die folgenden Unterkapitel wurden nicht berücksichtigt:

-   _Testing for Session Management Schema (WSTG-SESS-01)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/06-Session_Management_Testing/01-Testing_for_Session_Management_Schema)_:_  Zusammengefasst mit Abschnitt 6.2 (Session-Management → Cookie-Attribute)
-   _Testing for Session Puzzling (WSTG-SESS-08)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/06-Session_Management_Testing/08-Testing_for_Session_Puzzling)_:_  Zusammengefasst mit Abschnitt 6.5 (Session-Management → Cross-Site Request Forgery)
-   _Testing for Session Hijacking (WSTG-SESS-09)_ [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/02-Configuration_and_Deployment_Management_Testing/10-Test_for_Subdomain_Takeover): Zusammengefasst mit Abschnitt 6.5 (Session-Management → Cross-Site Request Forgery)

### Cookie-Attribute  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/06-Session_Management_Testing/02-Testing_for_Cookies_Attributes)

_Threat scenario:_

Ein Angreifer kann bei unsicherer Konfiguration von Session-Cookies möglicherweise sensible Inhalte aus diesen auslesen oder gar eigene Session-Cookies berechnen und somit einen Authentication-Bypass durchführen.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass ihre Cookies sicher erzeugt werden. Das bedeutet insbesondere, dass die Cookies keine sensiblen Daten beinhalten und nicht deterministisch erzeugt werden.

_Definition of Done:_

* Sämtliche Set-Cookie-Direktiven werden mit Secure getagged.

* Sämtliche Cookie werden über HTTPS übertragen. Es kann keine Übertragung über HTTP erzwungen werden.

* Erzeugte Cookies beinhalten keine sensiblen Informationen im Kartext, wie etwa Nutzernamen oder gar Passwörter.

* Es gibt keine Möglichkeit, Session IDs vorauszusagen oder zu berechnen. Die vom DTH erzeugten Session IDs sind zufällig und zumindest 128 bit lang.

* Sämtliche Session-Cookies sollten nach Schließen des Browsers ablaufen, es sei denn, es handelt sich um Session-Cookies, die aufgrund des „Passwort merken“-Features erzeugt wurden.

### Session Fixation  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/06-Session_Management_Testing/03-Testing_for_Session_Fixation)

_Threat scenario:_

Zunächst erzeugt der Angreifer eine Session ohne Authentifizierung, beispielsweise indem er Shopping-Items einem Einkaufswagen hinzufügt. Bringt der Angreifer ein potenzielles Opfer anschließend dazu, auf Links mit diesem Cookie zu klicken, etwa via Mail, so könnte die Session des Opfers nach einem anschließenden Login „fixiert“ werden. Bei einer Session Fixation bleiben Cookies vor und nach einer Authentisierung identisch. Das bedeutet, dass der Angreifer in diesem Fall erfolgreich über den Login des Opfers authentisiert wird, da es über den Authentisierungsprozess hinweg gleich bleibt.

_Objective:_

Betreiber von DTH müssen sicherstellen, dass Session-Cookies nach einer erfolgreichen Authentisierung stets „refreshed“ werden.

_Definition of Done:_

* Sämtliche Authentisierungsprozesse lösen automatisch einen Cookie-Refresh aus.

### Offenlegung von Session-Variablen  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/06-Session_Management_Testing/04-Testing_for_Exposed_Session_Variables)

_Threat scenario:_

Werden Session-Variablen offengelegt, können Angreifer diese möglicherweise stehlen und somit einen Authentication-Bypass bewirken.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass Session-IDs geschützt und nicht an Drittsysteme übertragen werden.

_Definition of Done:_

* Das Übertragen der Session ID muss in allen Fällen auf verschlüsseltem Wege über HTTPS erfolgen.

* Beim Übertragen der Session ID muss in allen Fällen Cache-Control: no-cache gesetzt sein.

* Session IDs dürfen in keinem Fall über GET-Requests übertragen werden, um zu verhindern, dass die Session IDs in den Logdaten auftauchen.

### Cross-Site Request Forgery (CSRF)  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/06-Session_Management_Testing/05-Testing_for_Cross_Site_Request_Forgery)

_Threat scenario:_

Ein Angreifer kann über eine Mail oder eine externe Seite Inhalte an ein potenzielles Opfer schicken, die auf die Webseite des DTH verweisen, im Hintergrund aber beispielsweise böswillige Aktionen auf der Seite des DTH ausführt.

_Objective:_

Betreiber eines DTH müssen sicherstellen, dass Session-Cookies über zusätzliche Maßnahmen vor Missbrauch durch externe Seiten geschützt werden

_Definition of Done:_

* Mit allen Requests werden CSRF-Tokens gesendet.

* In keinen Fällen werden CSRF-Tokens als Teil des Session-Cookies verwendet.

### Logout  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/06-Session_Management_Testing/06-Testing_for_Logout_Functionality)

_Threat scenario:_

Bei fehlerhafter Implementierung von Logouts kann ein Angreifer eine Session fortführen, obwohl der Nutzer davon ausgeht, dass diese bereits beendet wurde.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass ein Logout die Beendigung von Sessions und das Löschen relevanter Session-Informationen erzwingt.

_Definition of Done:_

* Jeder Logout-Button beendet serverseitig unweigerlich die aktuelle Session. Es ist zu keinem Zeitpunkt möglich, nach einem Logout zu einem autorisierten Bereich des DTH vorzudringen

* Das Beenden einer Session löscht in jedem Fall auch das CSRF-Token.

### Session Timeout  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/06-Session_Management_Testing/07-Testing_Session_Timeout)

_Threat scenario:_

Wenn ein Angreifer einen CSRF-Angriff durchgeführt hat, so kann er ihn persistieren, sofern keine Gegenmaßnahmen ergriffen wurden.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass Sessions bei Inaktivität nicht beliebig lange aufrechterhalten bleiben.

_Definition of Done:_

* Es muss bei jedem Beginn einer Session gleichzeitig auch ein Session Timeout gesetzt werden.

## Eingabevalidierung

Eingabevalidierung ist ein wichtiger Schritt bei der Entwicklung von Software, der sicherstellen soll, dass alle Eingaben, die von Benutzern oder anderen Systemen in eine Anwendung eingegeben werden, gültig sind und keinen Schaden anrichten können.

Die Eingabevalidierung ist notwendig, weil es möglich ist, dass Benutzer absichtlich oder unbeabsichtigt ungültige, schädliche oder unerwartete Eingaben in eine Anwendung eingeben. Diese Eingaben können dazu führen, dass die Anwendung abstürzt, falsche Ergebnisse liefert, unerwünschte Datenzugriffe ermöglicht oder Sicherheitslücken aufweist.

Beispiele für Eingabevalidierungen sind die Überprüfung, ob E-Mail-Adressen oder Passwörter korrekt formatiert sind, ob eingegebene Zahlen innerhalb eines gültigen Bereichs liegen oder ob eingegebener Code sicher ausgeführt werden kann.

Die Prüfpunkte aus diesem Prüfabschnitt wurden vom OWASP Web Security Testing Guide, Kapitel 4.7 („Input Validation Testing“) abgeleitet. Die folgenden Unterkapitel wurden nicht berücksichtigt:

-   _Testing for Reflected Cross Site Scripting (WSTG-INPV-01)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/07-Input_Validation_Testing/01-Testing_for_Reflected_Cross_Site_Scripting)_:_  Zusammengefasst zu Abschnitt 7.1 (Eingabevalidierung → Cross-Site Scripting (XSS))
-   _Testing for Stored Cross Site Scripting (WSTG-INPV-02)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/07-Input_Validation_Testing/02-Testing_for_Stored_Cross_Site_Scripting)_:_  Zusammengefasst zu Abschnitt 7.1 (Eingabevalidierung → Cross-Site Scripting (XSS))
-   _Testing for HTTP Verb Tampering (WSTG-INPV-03)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/07-Input_Validation_Testing/03-Testing_for_HTTP_Verb_Tampering)_:_  Zusammengefasst zu Abschnitt 2.4 (Konfiguration und Deployment → HTTP-Methoden)
-   _Testing for HTTP Parameter Pollution (WSTG-INPV-04)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/07-Input_Validation_Testing/04-Testing_for_HTTP_Parameter_Pollution)_:_  Zusammengefasst zu Abschnitt 7.1 (Eingabevalidierung → Cross-Site Scripting (XSS))
-   _Testing for SQL Injection (WSTG-INPV-05)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/07-Input_Validation_Testing/05-Testing_for_SQL_Injection)_:_  Zusammengefasst zu Abschnitt 7.5 (Eingabevalidierung → Injection-Schwachstellen)
-   _Testing for LDAP Injection (WSTG-INPV-06)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/07-Input_Validation_Testing/06-Testing_for_LDAP_Injection)_:_  Zusammengefasst zu Abschnitt 7.5 (Eingabevalidierung → Injection-Schwachstellen)
-   _Testing for XML Injection (WSTG-INPV-07)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/07-Input_Validation_Testing/07-Testing_for_XML_Injection)_:_  Zusammengefasst zu Abschnitt 7.5 (Eingabevalidierung → Injection-Schwachstellen)
-   _Testing for SSI Injection (WSTG-INPV-08)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/07-Input_Validation_Testing/08-Testing_for_SSI_Injection)_:_  Zusammengefasst zu Abschnitt 7.5 (Eingabevalidierung → Injection-Schwachstellen)
-   _Testing for XPath Injection (WSTG-INPV-09)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/07-Input_Validation_Testing/09-Testing_for_XPath_Injection)_:_  Zusammengefasst zu Abschnitt 7.5 (Eingabevalidierung → Injection-Schwachstellen)
-   _Testing for IMAP SMTP Injection (WSTG-INPV-10)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/07-Input_Validation_Testing/10-Testing_for_IMAP_SMTP_Injection)_:_  Zusammengefasst zu Abschnitt 7.5 (Eingabevalidierung → Injection-Schwachstellen)
-   _Testing for Code Injection (WSTG-INPV-11)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/07-Input_Validation_Testing/11-Testing_for_Code_Injection)_:_  Zusammengefasst zu Abschnitt 7.5 (Eingabevalidierung → Injection-Schwachstellen)
-   _Testing for Command Injection (WSTG-INPV-12)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/07-Input_Validation_Testing/12-Testing_for_Command_Injection)_:_  Zusammengefasst zu Abschnitt 7.5 (Eingabevalidierung → Injection-Schwachstellen)
-   _Testing for Format String Injection (WSTG-INPV-13)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/07-Input_Validation_Testing/13-Testing_for_Format_String_Injection)_:_  Zusammengefasst zu Abschnitt 7.5 (Eingabevalidierung → Injection-Schwachstellen)
-   _Testing for Incubated Vulnerability (WSTG-INPV-14)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/07-Input_Validation_Testing/14-Testing_for_Incubated_Vulnerability)_:_  Außerhalb des Scopes für dieses Dokument
-   _Testing for HTTP Splitting Smuggling (WSTG-INPV-15)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/07-Input_Validation_Testing/15-Testing_for_HTTP_Splitting_Smuggling)_:_  Außerhalb des Scopes für dieses Dokument
-   _Testing for HTTP Incoming Requests (WSTG-INPV-16)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/07-Input_Validation_Testing/16-Testing_for_HTTP_Incoming_Requests)_:_  Außerhalb des Scopes für dieses Dokument
-   _Testing for Host Header Injection (WSTG-INPV-17)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/07-Input_Validation_Testing/17-Testing_for_Host_Header_Injection)_:_  Zusammengefasst zu Abschnitt 7.5 (Eingabevalidierung → Injection-Schwachstellen)
-   _Testing for Server-side Template Injection (WSTG-INPV-18)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/07-Input_Validation_Testing/18-Testing_for_Server-side_Template_Injection)_:_  Zusammengefasst zu Abschnitt 7.5 (Eingabevalidierung → Injection-Schwachstellen)

### Cross-Site Scripting (XSS)

_Threat scenario:_

Mittels XSS können Angreifer HTML-Tags oder JavaScript-Befehle an den DTH senden. Dadurch können Angreifer entweder unmittelbar auf Ressourcen zugreifen (Reflected XSS) oder auf Ressourcen anderer Zugreifen, indem sie ihren Angriff persistieren und dieser jedes Mal aufgerufen wird, wenn ein potenzielles Opfer die persistierte Ressource anfordert (Stored XSS). Entsprechend handelt es sich bei letzterer Variante um die kritischere Form von XSS, da sich ein Angreifer somit beispielsweise Session Cookies anderer Nutzer zusenden lassen kann.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass Nutzer weder HTML-Tags noch JavaScript-Code verwenden können, die clientseitig ausgeführt werden.

_Definition of Done:_

* Sämtliche nutzergesteuerten Variablen, die zwischen HTML-Tags im HTML Body deklariert werden, müssen durch HTML Entity und JavaScript-Codierung bereinigt werden. Safe Sinks übernehmen dies automatisch, beispielsweise .textContent in JavaScript.

* Sämtliche nutzergesteuerten Variablen in HTML-Attributen müssen entweder sichere HTML-Attribute nutzen, oder die .setAttribute-Methode nutzen, die automatisch sicheres Encoding verwendet.

* Für sämtliche GET-Query-Parameter muss URL-Encoding verwendet werden. Über JavaScript kann dies mit der window.encodeURIComponent-Methode erfolgen.

* Für URLS in src- oder href-Attributen müssen sowohl URL-Encoding als auch anschließendes HTML-Attribute-Encoding verwendet werden.

* Für die Verwendung von Variablen mit CSS muss stets der property-Kontext verwendet werden. Äquivalent kann in JavaScript style.property verwendet werden.

* Sämtliche nutzergesteuerten Variablen sind von Anführungszeichen umschlossen (" oder ').

* Sämtlicher benutzergesteuerter HTML-Code im HTML-Body wird validiert, beispielsweise mittels JSoup, AntiSamy oder HTML Sanitizer.

* Jeder serverseitig abgefangene Benutzereingabe muss HTML-codiert werden.

### Injection-Schwachstellen

_Threat scenario:_

Injection-Schwachstellen existieren in vielfältiger Form, haben jedoch allesamt gemeinsam, dass Angreifer die unsachgemäße oder fehlende Validierung von Benutzereingaben als Eintrittsvektor nutzen, um dann vom DTH Befehle ausführen zu lassen, was nicht in seiner ursprünglichen Funktionalität vorgesehen war. Diese Befehle können sich beispielsweise auf die Datenbank- aber auch die Betriebssystemebene beziehen.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass Nutzer keine Befehle einschleusen können, die vom DTH serverseitig ausgeführt werden.

_Definition of Done:_

* Um SQL-Injection-Schwachstellen vorzubeugen, werden vor ausnahmslos allen Datenbankaufrufen Prepared Statements mit parametrisierten Queries verwendet.

* Um OS Command-Injection-Schwachstellenvorzubeugen, werden in keinem Fall OS-Befehle im Code verwendet. Entsprechende Libraries sind zu bevorzugen. Für den absoluten Ausnahmefall, dass die Verwendung von OS-Befehlen zwingend erforderlich ist, muss der Befehl in Kombination mit beispielsweise escapeshellarg verwendet werden.

* Um LDAP-Injection vorzubeugen, müssen LDAP-Queries entweder LDAP-Search- oder LDAP-DN-codiert werden.

* Um XML-Injection-Schwachstellen vorzubeugen ist Document Type Definition (DTD) zu deaktivieren. Falls dies nicht möglich sein sollte, können entweder External Document Types sowie External Document Type Declaration parser-spezifisch deaktiviert werden.

* Um Server-Side Includes (SSI)-Injection-Schwachstellen vorzubeugen, wird generell kein SSI verwendet. JavaScript und AJAX bieten entsprechende Alternativen.

* Um XPath-Injection-Schwachstellen vorzubeugen, müssen ausnahmslos alle XPath-Queries parametrisiert werden (vgl. hierzu Prepared Statements bei SQL-Injections). Falls möglich, sollten alle Queries vorkompiliert werden.

* Um File-Inclusion-Schwachstellen zu vermeiden, wird auf Dateien, die im Server liegen, stets per Index zugegriffen und niemals per Dateiname. Die Abfrage des Indexes ist entsprechend zu validieren. Die Eingabe von Pfaden oder URLs muss stets unzulässig sein.

* Um Host Header-Injection-Schwachstellen vorzubeugen, ist im serverseitigen Code auf Host-Header-Zugriffe zu verzichten.

### Server-Side Request Forgery  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/07-Input_Validation_Testing/19-Testing_for_Server-Side_Request_Forgery)

_Threat scenario:_

In einigen Anwendungsfällen können Angreifer den DTH als Proxy nutzen, um Requests an ein weiteres Ziel zu senden, die nicht vom Angreifer selbst unmittelbar erreichbar sind.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass Nutzer keine Requests über den DTH erzeugen und versenden können.

_Definition of Done:_

* Es ist der „Validation flow“ im OWASP Server-Side Request Forgery Prevention Cheat Sheet umzusetzen.

## Fehlerbehandlung

Die Sicherheitsimplikationen bei der Fehlerbehandlung von Software werden oftmals vernachlässigt, dabei kann unzureichende Fehlerbehandlung dazu führen, dass Fehlermeldungen oder Protokolle vertrauliche Informationen wie z.B. Benutzernamen, Passwörter oder Systemdetails preisgeben. Diese Informationen können von Angreifern ausgenutzt werden, um Angriffe wie beispielsweise Phishing- oder SQL-Injection-Angriffe durchzuführen.

Die Prüfpunkte aus diesem Prüfabschnitt wurden vom OWASP Web Security Testing Guide, Kapitel 4.8 („Testing for Error Handling“) abgeleitet. Die folgenden Unterkapitel wurden nicht berücksichtigt:

-   _Testing for Stack Traces (WSTG-ERRH-02)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/v42/4-Web_Application_Security_Testing/08-Testing_for_Error_Handling/02-Testing_for_Stack_Traces)_:_  Zusammengefasst mit Abschnitt 8.1 (Fehlerbehandlung → Unsachgemäße Fehlerbehandlung)

### Unsachgemäße Fehlerbehandlung  🔗

_Threat scenario:_

Durch gezieltes Auslösen von Fehlermeldungen kann ein Angreifer möglicherweise wichtige Interna des DTH in Erfahrung bringen.

_Objective:_

Betreiber von DTH müssen sicherstellen, dass an den Nutzer zurückgegebene Fehlermeldungen keine Rückschlüsse auf Systeminterna zulassen.

_Definition of Done:_

* Es wurde ein globaler Error-Handler definiert, der nach außen hin generische Fehlermeldungen zurückgibt und detaillierte Fehlerbeschreibungen ausschließlich intern loggt.

## Kryptographie

Kryptographie und die Überprüfung kryptographischer Konfiguration in Systemen sind ein Thema für sich. In diesem Abschnitt soll ausschließlich die kryptographische Konfiguration von Webservern betrachtet werden. Dies bezieht sich hauptsächlich auf die sichere Konfiguration von Transport Layer Security sowie der sicheren Verwendung von Schlüsselaustausch- und Verschlüsselungsverfahren gemäß gängiger Best Practices.

Die Prüfpunkte aus diesem Prüfabschnitt wurden vom OWASP Web Security Testing Guide, Kapitel 4.9 („Testing for Weak Cryptography“) abgeleitet. Die folgenden Unterkapitel wurden nicht berücksichtigt:

-   _Testing for Padding Oracle (WSTG-CRYP-02)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/09-Testing_for_Weak_Cryptography/02-Testing_for_Padding_Oracle)_:_  Außerhalb des Scopes für dieses Dokument
-   _Testing for Testing for Sensitive Information Sent via Unencrypted Channels (WSTG-CRYP-03)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/09-Testing_for_Weak_Cryptography/03-Testing_for_Sensitive_Information_Sent_via_Unencrypted_Channels)_:_  Zusammengefasst mit Abschnitt 9.1 (Kryptographie → Schwache Transport Layer Security (TLS))

### Schwache Transport Layer Security (TLS)  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/09-Testing_for_Weak_Cryptography/01-Testing_for_Weak_Transport_Layer_Security)

_Threat scenario:_

Für den Fall, dass der DTH eine veraltete TLS-Konfiguration verwendet, können Angreifer die Vertraulichkeit des DTH gefährden. Für veraltete Krypto-Algorithmen existieren zahlreiche Exploits, welche die Verschlüsselung des Nachrichtenkanals brechen können.

_Objective:_

Der Betreiber eines DTH muss sicherstellen, dass die TLS-Konfiguration dem State of the Art entspricht.

_Definition of Done:_

_(Die nachfolgende Konfiguration entspricht den modernen Kompatibilitätsempfehlungen der Mozilla Foundation gemäß dem Stand von April 2023. Es ist eigenständig auf Aktualität der Werte zu achten.)_

* Es wird ausschließlich TLS 1.3 als Sicherheitsprotokoll verwendet

* Ausschließlich folgende Cipher-Suites sind erlaubt:

· TLS_AES_128_GCM_SHA256

· TLS_AES_256_GCM_SHA384

· TLS_CHACHA20_POLY1305_SHA256

* Unterstützte TLS-Kurven beschränken sich auf:

· X25519

· prime256v1

· secp384r1

* Das HSTS max-age ist auf 2 Jahre festgelegt

* Der Zertifikatstyp ist ECDSA (P-256)

* Die Lebenszeit des Zertifikats beträgt 90 Tage.

* Die Schlüssellänge des Zertifikats beträgt mindestens 2048 bits.

* Das Signierverfahren ist zumindest SHA-256, in keinem Fall jedoch MD5 oder SHA-1.

* Das Zertifikat wird nicht für Wildcard-Domains ausgestellt, z.B. (*.[example.org](http://example.org/))

* Die Seite wird einer Preload-List hinzugefügt, um einem möglichen SSL-Strip-Angriff entgegenzuwirken. Dies ist insbesondere relevant, wenn die Seite das erste Mal über HTTP besucht wird und der Besucher anschließend aufgrund von HSTS zum HTTPS-Kanal umgeleitet wird.

### Schwache Verschlüsselungsverfahren  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/09-Testing_for_Weak_Cryptography/04-Testing_for_Weak_Encryption)

_Threat scenario:_

Analog zur Nutzung schwacher TLS-Konfigurationen birgt auch die Nutzung schwacher Schlüsselaustausch- und Verschlüsselungsverfahren abseits von TLS die Gefahr, dass ein Angreifer die Verschlüsselung eines Nachrichtenkanals brechen kann.

_Objective:_

Der Betreiber eines DTH muss sicherstellen, dass ausschließlich State-of-the-Art Schlüsselaustausch- und Verschlüsselungsverfahren verwendet werden.

_Definition of Done:_

_(Die nachfolgende Konfiguration entspricht Empfehlungen von OWASP gemäß dem Stand von April 2023. Es ist eigenständig auf Aktualität der Werte zu achten.)_

* Für das Verschlüsselungsverfahren werden folgende Konfig:

· Im Falle von AES128 oder AES256 sollte der Initialisierungsvektor gemäß FIPS 140-2 konfiguriert sein

· Für asymmetrische Verschlüsselung sollte bevorzugt Elliptic Curve Cryptography (ECC) mit sicheren Kurven wie Curve25519 verwendet werden, alternativ RSA mit zumindest 2048 bit Schlüssellänge

· Wenn RSA zum Signieren verwendet wird, soll PSS-Padding verwendet werden

· Von schwachen Hash- und Verschlüsselungsverfahren ist gänzlich abzusehen, insbesondere MD5, RC4, DES, Blowfish, SHA1, 1024-bit RSA, 1024-bit DAS, 160-bit ECDSA, sowie 80/112-bit 2TDEA.

* Für den Schlüsselaustausch wird Diffie-Hellman mit mindestens 2048 bit Schlüssellänge verwendet.

## Clients

Obgleich serverseitiger Schwachstellen in den meisten Fällen die größere Bedrohung für die Vertraulichkeit, Integrität und Verfügbarkeit von Webanwendungen sind, so sind auch clientseitige Sicherheitsüberprüfungen notwendig, um ein ganzheitlich hohes Sicherheitsniveau zu erreichen.

So können Schwachstellen auf der Clientseite auftreten und häufig genutzt werden, um Benutzerdaten zu stehlen. Daher muss beim Web Security Testing die Prävention von XSS auf der Clientseite getestet werden.

Die Prüfpunkte aus diesem Prüfabschnitt wurden vom OWASP Web Security Testing Guide, Kapitel 4.10 („Client-side Testing“) abgeleitet. Die folgenden Unterkapitel wurden nicht berücksichtigt:

-   _Testing for DOM-Based Cross Site Scripting (WSTG-CLNT-01)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/11-Client-side_Testing/01-Testing_for_DOM-based_Cross_Site_Scripting)_:_  Zusammengefasst als Abschnitt 7.1 (Eingabevalidierung → Cross-Site Scripting (XSS))
-   _Testing for JavaScript Execution (WSTG-CLNT-02)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/11-Client-side_Testing/02-Testing_for_JavaScript_Execution)_:_  Zusammengefasst als Abschnitt 7.1 (Eingabevalidierung → Cross-Site Scripting (XSS))
-   _Testing for HTML Injection (WSTG-CLNT-03)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/11-Client-side_Testing/03-Testing_for_HTML_Injection)_:_  Zusammengefasst als Abschnitt 7.1 (Eingabevalidierung → Cross-Site Scripting (XSS))
-   _Testing for Client-side URL Redirect (WSTG-CLNT-04)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/11-Client-side_Testing/04-Testing_for_Client-side_URL_Redirect)_:_  Zusammengefasst als Abschnitt 7.1 (Eingabevalidierung → Cross-Site Scripting (XSS))
-   _Testing for CSS Injection Redirect (WSTG-CLNT-05)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/stable/4-Web_Application_Security_Testing/11-Client-side_Testing/05-Testing_for_CSS_Injection)_:_  Zusammengefasst als Abschnitt 7.1 (Eingabevalidierung → Cross-Site Scripting (XSS))
-   _Testing for Cross Site Flashing (WSTG-CLNT-08)_  [🔗](https://owasp.org/www-project-web-security-testing-guide/v42/4-Web_Application_Security_Testing/11-Client-side_Testing/08-Testing_for_Cross_Site_Flashing): Zusammengefasst als Abschnitt 7.1 (Eingabevalidierung → Cross-Site Scripting (XSS))
-   _Testing for Cross Site Script Inclusion (WSTG-CLNT-13)_ [🔗](https://owasp.org/www-project-web-security-testing-guide/v42/4-Web_Application_Security_Testing/11-Client-side_Testing/13-Testing_for_Cross_Site_Script_Inclusion): Zusammengefasst als Abschnitt 7.1 (Eingabevalidierung → Cross-Site Scripting (XSS))

### Clientseitige Manipulation von Ressourcen  [🔗](https://owasp.org/www-project-web-security-testing-guide/v42/4-Web_Application_Security_Testing/11-Client-side_Testing/06-Testing_for_Client-side_Resource_Manipulation)

_Threat scenario:_

Ein Angreifer kann einen Link an sein Opfer senden, das eine maliziöse URL in die eigentliche URL des DTH einbettet, beispielsweise mit folgendem String:  [https://dth.de/#http://evil.com/html.html](https://dth.de/#http://evil.com/html.html).

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass Nutzer keinen Ressourcenzugriff auf Remote-URLs durchführen können.

_Definition of Done:_

* Jeder Ressourcenzugriff via # wird so validiert, dass keine Remote-URL eingebettet werden kann.

### Cross-Origin Resource Sharing (CORS)  [🔗](https://owasp.org/www-project-web-security-testing-guide/v42/4-Web_Application_Security_Testing/11-Client-side_Testing/07-Testing_Cross_Origin_Resource_Sharing)

_Threat scenario:_

Mittels CORS können Angreifer unter Umständen Inhalte von fremden Webseiten in den DTH einbetten, um dadurch schädliche Aktivitäten durchzuführen.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass nicht beliebige Domänen CORS durchführen können.

_Definition of Done:_

* Cross-Origin Resource Sharing wird in keinem Fall per Wildcard (*) erlaubt.

### Clickjacking  [🔗](https://owasp.org/www-project-web-security-testing-guide/v42/4-Web_Application_Security_Testing/11-Client-side_Testing/09-Testing_for_Clickjacking)

_Threat scenario:_

Ein Angreifer kann unter Umständen UI-Funktionalitäten von Webseiten vortäuschen, die nicht vom Betreiber des DTH vorgesehen sind, um so Schaden bei Nutzern zu erzeugen. Ein Beispiel ist ein Clickjacking-Angriff, bei dem ein Angreifer einen iFrame mit dem ursprünglichen Inhalt des DTH lädt und diesen iFrame in seine eigene, böswillige Seite einbettet und dort schädliche Aktivitäten durchführt.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass ihre Seite nicht von beliebigen anderen Nutzern über iFrames eingebettet werden kann.

_Definition of Done:_

* Content-Security-Policy ist definiert und aktiv. frame-ancestors ist auf none, self oder höchstens ein streng limitiertes Set an URLs festgelegt.

### WebSockets  [🔗](https://owasp.org/www-project-web-security-testing-guide/v42/4-Web_Application_Security_Testing/11-Client-side_Testing/10-Testing_WebSockets)

_Threat scenario:_

Bei einer Full-Duplex-Verbindung über WebSockets gelten die gleichen Sicherheitsvoraussetzungen wie für HTTP-Verbindungen. Auch hier kann ein Angreifer unverschlüsselte Verbindungen als Man-in-the-Middle mitlesen und somit die Vertraulichkeit und Integrität der Anwendung brechen.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass bei der Verwendung von WebSockets gängige Sicherheitsstandards eingehalten werden.

_Definition of Done:_

* Der Origin-Header des http WebSocket-Handshakes muss validiert werden. Es dürfen in keinem Fall beliebige Domains erlaubt sein.

* Es muss eine verschlüsselte Verbindung über TLS verwendet werden. Unverschlüsselte TCP-Kommunikation ist nicht zulässig.

### Web Messaging  [🔗](https://owasp.org/www-project-web-security-testing-guide/v42/4-Web_Application_Security_Testing/11-Client-side_Testing/11-Testing_Web_Messaging)

_Threat scenario:_

Mit Einführung der Messaging API und der postMessage-Methode können Anwendungen unmittelbar miteinander kommunizieren. Bei zu lockerer Konfiguration können Angreifer die Messaging API missbrauchen und einen Kommunikationskanal über eine maliziöse Anwendung erzwingen.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass bei der Verwendung der Messaging API gängige Sicherheitsstandards eingehalten werden.

_Definition of Done:_

* Bei der Verwendung von Web Messaging muss das event.origin präzise definiert sein. Wildcards sind nicht erlaubt. HTTPS ist verpflichtend.

* Bei der Verwendung von Web Messaging dürfen unsichere Elemente wie innerHTML nicht verwendet werden.

### Browserspeicher  [🔗](https://owasp.org/www-project-web-security-testing-guide/v42/4-Web_Application_Security_Testing/11-Client-side_Testing/12-Testing_Browser_Storage)

_Threat scenario:_

Browser bieten die clientseitige Möglichkeit zum Speichern von lokalen-, Session- IndexedDB-, Web SQL- und Cookie-Daten. Im Falle einer Kompromittierung des Browsers können Angreifer diese Daten unter Umständen auslesen.

_Objective:_

Die Betreiber von DTH müssen sicherstellen, dass sensible Daten nicht clientseitig vorgehalten werden.

_Definition of Done:_

* Sämtliche sensiblen Daten in Anwendungen sind serverseitig gespeichert und nicht clientseitig.

## Schnittstellen

Da APIs einen offenen Zugangspunkt zu Anwendungsdaten und -funktionen bereitstellen, sind sie häufig ein Ziel für Angriffe von Angreifern. Einige häufige Bedrohungen für APIs zielen auf Authentifizierung und Autorisierung, Injections oder Denial of Service ab.

Um API-Sicherheitsprobleme zu vermeiden, müssen Entwickler sicherstellen, dass APIs sicher implementiert und konfiguriert werden. Dies umfasst die Verwendung von sicheren Authentifizierungs- und Autorisierungsmethoden, die Implementierung von Zugriffskontrollen, die Überwachung von API-Aufrufen und die Begrenzung von Zugriffen auf vertrauliche Daten und Funktionen.

Die Prüfpunkte aus diesem Prüfabschnitt wurden vom OWASP Web Security Testing Guide, Kapitel 4.11 („API Testing“) abgeleitet. In dem vorliegenden Dokument wurde von den Autoren der Abschnitt „OpenAPI“ ergänzt.

### GraphQL  [🔗](https://owasp.org/www-project-web-security-testing-guide/v42/4-Web_Application_Security_Testing/12-API_Testing/01-Testing_GraphQL)

_Threat scenario:_

Angreifer können, wie reguläre Nutzer auch, unmittelbar mit einer Schnittstelle interagieren. Diese kann anfällig für fehlerhafte Eingabevalidierung sein und somit der Eintrittsvektor für gängige Webschwachstellen wie SQL Injection, Cross-site Scripting oder Path Traversal sein.

_Objective:_

Der Betreiber eines DTH muss sicherstellen, dass Schnittstellen wie GraphQL sicher konfiguriert sind.

_Definition of Done:_

* Jeder Endpunkt wird eingabevalidiert. Dies kann zum Beispiel durch das Einbinden der 3rd-Party-Library graphql-constraint-directive erfolgen.

* Es ist ein Query-Timeout zum Schutz gegen Denial of Service implementiert.

* Es ist eine maximale Query Depth zum Schutz gegen Nested Queries implementiert.

* Es werden in jedem Fall generische Fehlermeldungen zurückgesendet, die keine Systeminterna preisgeben.

* Die Anzahl an Queries, die zur gleichen Zeit gesendet werden können, ist serverseitig limitiert.

### OpenAPI

_Threat scenario:_

Angreifer können, wie reguläre Nutzer auch, unmittelbar mit einer Schnittstelle interagieren. Diese kann anfällig für fehlerhafte Eingabevalidierung sein und somit der Eintrittsvektor für gängige Webschwachstellen wie SQL Injection, Cross-site Scripting oder Path Traversal sein.

_Objective:_

Der Betreiber eines DTH muss sicherstellen, dass Schnittstellen wie OpenAPI sicher konfiguriert sind.

_Definition of Done:_

* Für die Authentisierung in der OpenAPI-Schnittstelle wird oauth2 verwendet.

# Zusammenfassung und nächste Schritte

In diesem Dokument wurde die erste Fassung eines Prüfkatalogs für Datentreuhänder vorgelegt. Es beschreibt Datentreuhänder als eine Spezialform von Webanwendungen. Aus technischer Sicht ist dieser Katalog daher abgeleitet von den Best Practices des OWASP Web Security Testing Guides zur Absicherung von Webanwendungen.

Dieses Dokument ist bisweilen weiter ausbaufähig. Einige Prüfpunkte in verschiedenen Unterabschnitten überschneiden sich teilweise. Weiterhin kann auch in diesem Katalog, wie in so ziemlich allen Prüfkatalogen in der IT-Sicherheit, keine Vollständigkeit gewährleistet sein. Dieses Dokument ist daher als Version 1.0 zu verstehen und muss künftig sowohl erweitert als auch gekürzt, umsortiert und auch stellenweise möglicherweise komplett überarbeitet werden.

Insgesamt soll dieses Dokument als erster Schritt verstanden werden, die technischen Sicherheitsaspekte von Datentreuhändern greifbarer und umsetzbarer zu gestalten.

Mittelfristig soll mit der Schärfung des Themenkomplexes rund um Datentreuhänder schließlich auch dieser Prüfkatalog weiterentwickelt werden, um allen Entwicklern, die einen Datentreuhändern entwickeln und betreiben wollen, ein nützliches Werkzeug zu sein.