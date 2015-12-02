#Anleitung   
  
Um das Projekt lokal zu starten verwende
gradle jettyRunWar

Um die Tests auszuf�hren:
gradle test

# Task 7:

Abgabedatum - 14.12.2015 bis 20:00 Uhr

## Doing

Bitte beachten, dass der Einsatz von Libraries/Frameworks erlaubt (und sogar erw�nscht) ist.

### Web-Applikation-Frontend

Die entwickelte Web-Applikation wird um ein Front-End erweitert.   
Folgende Funktionalit�ten sollen damit umsetzbar sein:
 * Benutzer-Resgistrierung
 * Login/Logout
 * Ansicht der div. Warengruppen
 * Ansicht der Items in einer Warengruppe
 * Detail-Ansicht eines Items inkl. Kommentaren

F�r bestimmte Benutzer (Admins) soll au�erdem folgendes m�glich sein:
 * Benutzer l�schen
 * Benutzer sollen zu admins ernannt werden k�nnen (bzw. soll dieses Recht auch entzogen werden k�nnen).
 * Gruppen/Items/Kommentare l�schen

Die Funktionalit�ten f�r einen Admin k�nnen entweder in einem eigenen UI erfolgen, k�nnen aber auch mit der "nicht-admin-UI" integriert werden.   
Es d�rfen auch unterschiedliche Technologien pro UI verwendet werden (etwa User-UI per JSP und Admin-UI per AngularJS2 oder User-UI pre AngularJS2 und Admin-UI per JSF oder beides per AngularJS2 jedoch einmal per ES6 und einmal per TS oder...).   
Super w�re aber, wenn min. einmal AngularJS2 verwendet werden w�rde :-)

### Warenkob-Funktionalit�t
Im Front-End soll es m�glich sein, bestimmte Items in einen Warenkorb zu legen.   
In diesem soll die Anzahl von Items ver�ndert werden k�nnen.   
Ebenso sollen Items wieder aus dem Warenkorb entfernt werden k�nnen (falls mal eines unabsichtlich angeklickt wurde).   
Wird ein Warenkorb bestellt, so soll die Bezahlung �ber PayPal erfolgen.   
Dazu muss die PayPal-API verwendet werden (ACHTUNG: Bitte nur im Testing-Mode arbeiten).

### Testing
�berlege, wie die geforderte Funktionalit�t getestet werden kann.
Implementiere min. folgende Tests:
 * Erfolgreiche Benutzerregistrierung
 * Fehlerhafte Benutzerregistrierung (etwa nicht �bereinstimmende Passw�rter)
 * Benutzer l�schen
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



