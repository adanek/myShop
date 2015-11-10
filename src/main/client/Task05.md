# Task 5:

Abgabedatum - 09.11.2015 bis 12:00 Uhr

## Doing

Bitte beachten, dass ab jetzt der Einsatz von Libraries/Frameworks erlaubt (und sogar erwünscht) ist.

### Erstellung des Grundgerüstes

Es ist folgendes Grundgerüst mit Hilfe eines Java-Servers (Jetty, Apache Tomcat) zu erstellen.
- Kategorien (beinhalten 0 bis n Items)
- Items (besteht min. aus Titel, Beschreibung, Erstellungs- und Änderungsdatum, Author)
- Item-Kommentare (gehören zu genau einem Item, ein Item hat beliebig viele Kommentare)
- [Startseite]
- Anbindung an eine Datenbank (per JDBC, JPA, Hibernate, ...)
- An geeigneten Stellen sollen Page-Reloads vermieden werden (AJAX)

### Benutzersystem

Ein Benutzersystem mit folgenden Eckdaten soll erstellt werden:
- Am System sollen sich Benutzer anmelden und registrieren können
- Nur angemeldete Benutzer können Kommentare für Items erstellen (eigenen Kommentare können bearbeitet werden, andere nicht)
- Es gibt min die drei Rollen: Guest (read-only), Benutzer (erstellen bzw. bearbeiten von Kommentaren) und Administrator (erstellen bzw. bearbeiten von Kategorien, Items und allen Kommentaren).

### REST-API

Alle Funktionen sollen über eine REST-API aufrufbar sein. Es ist darauf zu achten, dass das Benutzersystem auch auf dieser API funktionsfähig bleibt.

## Reading:

### Java
[Jetty Konfiguration (per Annotations)](http://www.eclipse.org/jetty/documentation/current/using-annotations.html)
[REST-API (mit Jersey)](http://www.vogella.com/tutorials/REST/article.html)

### Gradle
[Gradle Documentation](https://docs.gradle.org/current/userguide/userguide.html)

