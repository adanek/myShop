#Anleitung  
  
Um das Projekt lokal zu starten verwende
gradle jettyRunWar

Um die Tests auszuführen:
gradle test

# Task 7:

Abgabedatum - 14.12.2015 bis 20:00 Uhr

## Doing

Bitte beachten, dass der Einsatz von Libraries/Frameworks erlaubt (und sogar erwünscht) ist.

### Web-Applikation-Frontend

Die entwickelte Web-Applikation wird um ein Front-End erweitert.   
Folgende Funktionalitäten sollen damit umsetzbar sein:
 * Benutzer-Resgistrierung
 * Login/Logout
 * Ansicht der div. Warengruppen
 * Ansicht der Items in einer Warengruppe
 * Detail-Ansicht eines Items inkl. Kommentaren

Für bestimmte Benutzer (Admins) soll außerdem folgendes möglich sein:
 * Benutzer löschen
 * Benutzer sollen zu admins ernannt werden können (bzw. soll dieses Recht auch entzogen werden können).
 * Gruppen/Items/Kommentare löschen

Die Funktionalitäten für einen Admin können entweder in einem eigenen UI erfolgen, können aber auch mit der "nicht-admin-UI" integriert werden.   
Es dürfen auch unterschiedliche Technologien pro UI verwendet werden (etwa User-UI per JSP und Admin-UI per AngularJS2 oder User-UI pre AngularJS2 und Admin-UI per JSF oder beides per AngularJS2 jedoch einmal per ES6 und einmal per TS oder...).   
Super wäre aber, wenn min. einmal AngularJS2 verwendet werden würde :-)

### Warenkob-Funktionalität
Im Front-End soll es möglich sein, bestimmte Items in einen Warenkorb zu legen.   
In diesem soll die Anzahl von Items verändert werden können.   
Ebenso sollen Items wieder aus dem Warenkorb entfernt werden können (falls mal eines unabsichtlich angeklickt wurde).   
Wird ein Warenkorb bestellt, so soll die Bezahlung über PayPal erfolgen.   
Dazu muss die PayPal-API verwendet werden (ACHTUNG: Bitte nur im Testing-Mode arbeiten).

### Testing
Überlege, wie die geforderte Funktionalität getestet werden kann.
Implementiere min. folgende Tests:
 * Erfolgreiche Benutzerregistrierung
 * Fehlerhafte Benutzerregistrierung (etwa nicht übereinstimmende Passwörter)
 * Benutzer löschen
 * Benutzer zu Admin ernennen
 * Admin-Recht entziehen (was passiert, wenn nur 1 Admin im System und dieses Recht diesem entzogen wird)?
 * Erfolgreiche Bestellung eines Warenkorbs
 * Fehlerhafte Bestellung eiens Warenkorbs (was kann hier schief laufen?)

## Reading:

### Testen
[NodeJS](https://nodejs.org/)
[AngularJS 2](https://angular.io/)
[AngularJS 2 Resources] (https://angular.io/docs/js/latest/resources.html)
[PayPal API](https://developer.paypal.com/docs/api/overview/)



