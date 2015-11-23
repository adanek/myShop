#Anleitung 
  
Um das Projekt lokal zu starten verwende
gradle jettyRunWar

Um die Tests auszuführen:
gradle test

# Task 6:

Abgabedatum - 23.11.2015 bis 20:00 Uhr

## Doing

Bitte beachten, dass ab jetzt der Einsatz von Libraries/Frameworks erlaubt (und sogar erwünscht) ist.

### Testing der Web-Applikation

Die entwickelte Web-Applikation soll automatisiert getestet werden.   

#### Server-Testing (inkl. Code-Coverage)
Am Server soll (zumindest) zwischen Unit- und Integrationstests unterschieden werden.   
Dabei sollen min. folgende Komponenten geprüft werden:   
1) REST-API (sowie deren Businesslogic)
2) Authentifizierung

Ebenso ist eine Code-Coverage zu erstellen, die min. 60% erreichen soll.   
Beschreibe kurz, wie die Trennung der einzelnen Komponenten sauber gelingt und mit Hilfe welcher Techniken/Tools dies umgesetzt wurde.

#### Browser-Simulation
Der Seitenaufbau in einem (simulierten) Browser soll getestet werden.   
Falls möglich/sinnvoll, soll dabei ebenfalls eine Codeabdeckung generiert werden (vorerst gibt es hier aber keinen Wert, der erreicht werden muss).

#### Abgrenzung
Beschreibe, was im Rahmen der obigen Teilaufgaben nicht getestet wurde bzw. werden konnte.   
Was sollte noch getestet werden?   
Wie könnte dabei vorgegangen werden?

## Reading:

### Testen
[Vorlesungsfolien](http://dbis-informatik.uibk.ac.at/298-0-VOPS-Programmieren-von-Web-Information-Systems.html)

