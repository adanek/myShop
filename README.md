# Task 8:

Abgabedatum - 11.01.2016 bis 20:00 Uhr

## Doing

Bitte beachten, dass der Einsatz von Libraries/Frameworks erlaubt (und sogar erwünscht) ist.

### SocialMedia Integration

#### OAuth 2 
Der bestehende Webshop muss um die Möglichkeit erweitert werden, Authentifizierung per OAuth vorzunehmen.    
Der OAuth-Server kann dabei entweder ein SocialMedia-Dienst sein (Facebook, Google+) oder aber ein eigener OAuth2-Server

#### Share auf Facebook/Twitter/Google+ und Co.
Items im Webshop müssen über eine beliebige SocialMedia Plattform empfohlen werden können.

### Interaktive Karten

#### Benutzerprofil
Der Benutzer sollen eine Adresse erhalten (falls schon eine Liefer-/Rechnungsadresse vorhanden ist, kann diese verwendet werden).   
Die Daten des Benutzers sollen dann auf einer Profil-Seite dargestellt werden.   
Neben der Darstellung der Adresse in Textform soll sie mit Hilfe eines Markers auf einer interaktiven Karte dargestellt sein.   
Der Längen- und Breitengrad soll dabei über einen reverse geocoding dienst gefunden werden (empfohlen: OSM Nomination bzw. Google Maps Geocoding API).   
Um uneindeutigen Ergebnissen in den Griff zu bekommen, soll im Benutzerprofil direkt Längen- und Breitengrad hinterlegt werden (zusätzlich zur Textform).

#### Shops-in-der-Nähe Mashup
Je Item-Kategorie soll eine Katgorie-Site erstellt werden.   
Neben einer Verlinkung zu den Items (etwa am unteren Teil der Website oder als eigenständiger Link) soll eine Karte angezeigt werden, welche Shops in der Nähe zeigt, welche Produkte der jeweiligen Kategorie führen.   
Damit dies gelingen kann, werden zwei Dinge benötigt:   
 - Ermitteln der aktuellen Position
 - Shops ermitteln und darstellen
Das Ermitteln der aktuellen Position kann mittels HTML5 erfolgen. Falls der Benutzer die Positionsbestimmung ausgeschaltet hat bzw. die Bestätigung ablehnt, darf eine hinterlegte Location verwendet werden (bspw. Innsbruck).   
Shops, welche eine bestimmte Produktkategorie führen (und deren Lage) können über OpenStreetMap-Overpass-API gefunden werden.   
Ein Beispielaufruf für alle Bäckerein in Innsbruck könnte dabei so aussehen:   

    http://overpass-api.de/api/interpreter?data=[out:json];node(47.210812,11.30205,47.359219,11.45538)[shop=bakery];out;

# Reading:

[Facebook Share-Button Doku](https://developers.facebook.com/docs/plugins/share-button)   
[Leaflet](http://leafletjs.com/)   
[OAuth2 Server (PHP)](http://oauth2.thephpleague.com/)   
[OAuth2 Video (~12min step-by-step)](https://www.youtube.com/watch?v=io_r-0e3Qcw)   
[OpenLayers](http://openlayers.org/)   
[OSM Nomination](http://wiki.openstreetmap.org/wiki/Nominatim)   
[OSM Shop-Kategorien](http://wiki.openstreetmap.org/wiki/DE:Key:shop)

