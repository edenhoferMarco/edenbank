# edenbank
Projektarbeit von Marco Edenhofer für die Vorlesung Software Entwicklung an der OTH Regensburg im 
Wintersemester 19/20. 

### 1. Generelle Informationen

Die "edenbank" ist ein Zahlungsdienstleister bei dem es möglich ist über eine REST 
Schnittstelle Transaktionen zu tätigen. Jeder Neukunde bekommt ein Werbegeschenk 
durch unseren Kooperationspartner "big bazar". Während Firmenkunden über den
Eintrag im Firmenregister identifiziert werden, muss ein Privatkunde sich vor
der Anmeldung per Post-Ident bei unserem Partner "Lieferdienst" identifizieren 
lassen. Wir bieten neben Girokonten auch Tages- und Festgeldkonten, auf die es 
Zinsen gibt.

Viel Spaß bei der Verwendung unseres Services!

### 2 Usecases
#### 2.1 Kundenkonto anlegen
Um ein Privatkonto anlegen zu können, muss der Kunde per Post-Ident 
identifizierbar sein. Aus diesem Grund sind folgende Nutzer zum Anlegen bereits
bei dem Partner zur Identifizierung vermerkt:

- Herr Manuel Mehrfachvererbung
  - Geboren: 01.12.1964 in Regensburg, Deutschland
  - Wohnort: Galgenbergstraße 32, 93053 Regensburg
  - Email: mehrfachvererbung-ist-böse@gmail.de

- Frau Paula Polymorphie
  - Geboren: 24.07.1988 in Regensburg, Deutschland
  - Wohnort: Seybothstraße 2, 93053 Regensburg 
  - Email: polymorphie-nicht@gmail.de
  
Firmenkunden werden durch den Brancheneintrag identifizert 
(nicht implementiert, out of scope). Daher kann jederzeit ein neuer Firmekunde 
angelegt werden.

Durch einen weiteren Partnerservice bekommt der Neukunde ein Werbegeschenk.

##### Achtung: Das Passwort ist der Vorname des Kunden!

Diese Entscheidung wurde getroffen, da Onlinebanken dem Kunden das Passwort
häufig per Post oder E-Mail mitteilen (Quelle: S-Broker). Das Verschicken einer Email würde
hier die Angabe einer gültigen Email Adresse voraussetzen, was der Einfachheit zum
Testen halber hier nicht implementiert wurde.

#### 2.2 Bankkonto hinzufügen
Es kann jederzeit ein neues Girokonto, Tagesgeldkonto oder Festgeldkonto 
hinzugefügt werden. Nichts besonderes ist zu beachten.

#### 2.3 Bankkonto löschen
Jeder Kunde benötigt mindestens ein Konto. Ist das der Fall, kann ein bestehendes
Konto gelöscht werden, indem ein Verrechnungskonto für das übrige Geld gewählt wird.
Festgeldkonten können erst nach ablauf der Sperrfrist gelöscht werden (in diesem 
Projekt wird zur besseren Testbarkeit davon ausgegangen, dass ein 
Festgeldvertrag 10 Minuten dauert)

#### 2.4 Transaktionen
##### 2.4.1 In der Anwedung

Ein jeder Kunde kann von einem seiner bestehenden Konten auf ein beliebiges anderes
Konto (über IBAN anzugeben) bei der edenbank Echtzeitüberweisungen durchführen.

##### 2.4.2 Per REST-Schnittstelle

Über einen POST Request auf `/api/transaction/execute` kann durch Übergabe eines
DTOs mit IBAN von Sender und Empfänger, Verwendungszweck, Betrag, Kundenkontonummer
und Passwort die REST-Schnittstelle für Transaktionen verwendet werden.

##### 2.5 Kontodetails einsehen
Jeder Kunde kann Details zu seinen Konten anzeigen, inklusive aller beteiligter
Transaktionen.

### 3. Besonderheiten

#### 3.1 Zinsen
Sowohl auf Tagesgeld- als auch auf Festgeldkonten gibt es Zinsen. Diese werden 
MINÜTLICH verrechnet um das Testen der Funktion zu erleichtern. 

Das Festgeldkonto ist hier für 10 Minuten gesperrt, ehe es stillgelegt werden kann,
um das Geld zu transferieren. Es werden auch nur in diesen 10 Minuten Zinsen 
verrechnet.

Da eine minütliche Zinszahlung in Zeiten der drohenden Minuszinsen ein
ungerechter Vorteil gegenüber der Konkurrenz ist, würde sich die edenbank freuen,
wenn die Zinsberechtigten Konten nach dem Testen wieder stillgelegt
werden, um ein Erscheinen des Bundeskartellamts zu vermeiden ;)
