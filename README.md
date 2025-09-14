# Supermarktsfinder

Eine einfache Webanwendung zum Finden von Supermärkten in der Nähe einer angegebenen Adresse. Die Anwendung besteht aus einem Java Spring Boot Backend und einem HTML/CSS/JavaScript Frontend.

## Backend starten
```bash
cd backend
mvn spring-boot:run
```
Der Service läuft danach auf `http://localhost:8080` und stellt die Route `/api/supermarkets` bereit.

## Frontend nutzen
Öffne `frontend/index.html` im Browser (z.B. mit einem einfachen Dateiserver). Gib Adresse, Stadt und Umkreis ein und klicke auf **Suchen**, um Supermärkte auf der Karte anzuzeigen.

Die Suche verwendet OpenStreetMap (Nominatim & Overpass API).
