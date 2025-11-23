# Integritätsnachweise für Systemmetriken durch Blockchain-Verankerung

Dieses Repository gehört zu einer Bachelorarbeit zum Thema:

> **Integritätsnachweise für Systemmetriken durch Blockchain-Verankerung**

Ziel ist es, Servermetriken manipulationssicher abzulegen, indem:
- Rohdaten von einem lokalen Server abgerufen werden 
- Aufbereitete Daten in einer Datenbank gespeichert werden,
- aus diesen Daten ein (hierarchischer) Merkle-Hash-Tree berechnet wird,
- der Root-Hash dieses Trees in einer Blockchain (Waves) verankert wird,
- und ein Audit-Tool die Daten später verifizieren kann.

Das Projekt ist als **Maven-Multi-Modul-Projekt** aufgebaut:

- **`core`** – Gemeinsame Basis (Config, Datamodelle, Hashing, Mapping)
- **`app`** – Sammler-/Anker-Tool für Produktivbetrieb (Collector + Merkle + Blockchain-Anker)
- **`audit`** – Webbasierter Audit-Service inkl. Frontend, um Verifikationen durchzuführen

Die Dokumentation zu dem entwickelten Code befindet sich unter https://matthiasw96.github.io/Blockchain-Audits/

---

## Modulübersicht

### `core`

Enthält gemeinsam genutzte Funktionalität:

- `config`
    - `ConfigRecord` – Record-Struktur für Konfigurationseinträge
    - `ConfigLoader` – Lädt und parsed `config.properties`
- `datamodels`
    - `Datastructure` – Interface für unterstützte Datenstrukturen
    - `HashRecord` – Record für Hash-Daten (z. B. Root-/Leaf-Hashes)
    - `ServerRecord` – Record für Telemetriedaten auf Server-Ebene
    - `VMRecord` – Record für Telemetriedaten auf VM-Ebene
- `hashing`
    - `Hasher` – Berechnet Hashwerte und Merkle Trees
- `mapping`
    - `Converter` – Konvertiert Serverdaten in verschiedene Formate (XML/JSON/CSV, …)

Sowohl **`app`** als auch **`audit`** nutzen dieses Modul.

---

### `app` – Collector & Blockchain-Anker

Verantwortlich für:

- Einsammeln von Serverdaten vom lokalen Server (über `Collector`)
- Aufbereitung der Daten in vorgestelltes Datenmodell
- Speichern in der Datenbank (`DatabaseHandler`)
- Erzeugen eines Merkle-Hash-Trees (`Hasher` aus `core`)
- Verankerung des Merkle-Root-Hashes in der Waves-Blockchain (`AnchorService`)
- Konfiguration & Steuerung über ein einfaches UI (`UserInterface`, `ConsolePanel`)
- Zeitgesteuerter Ablauf über den `Scheduler` und `Processor`

**Wichtige Klassen:**

- `Main`  
  Einstiegspunkt, liest Konfiguration und verdrahtet die Komponenten.

- `application.Processor`  
  Orchestriert den Ablauf: Daten einsammeln → Hashing / Merkle Tree → Blockchain-Verankerung → Persistierung der Metadaten.

- `application.Scheduler`  
  Triggert den `Processor` in regelmäßigen Abständen bzw. bei Bedarf (Jobs).

- `infrastructure.collector.Collector`  
  Holt Rohdaten vom lokalen Server (z. B. über HTTP/Datei/etc.) und wandelt sie in die Datamodelle (`ServerRecord`, `VMRecord`).

- `infrastructure.database.DatabaseAPI`  
  Stellt eine API nach außen bereit (z. B. für das `audit`-Modul), um auf die Datenbank zuzugreifen.

- `infrastructure.database.DatabaseHandler`  
  Kapselt alle direkten Datenbankzugriffe (SQL, Verbindungen, CRUD).

- `infrastructure.anchor.AnchorService`  
  Verankert Root-Hashes in der Waves-Blockchain (Kommunikation mit Waves-Node).

- `infrastructure.anchor.Network`  
  Enum für die Auswahl des Blockchain-Netzwerks (z. B. TESTNET, MAINNET).

- `infrastructure.userInterface.UserInterface`  
  Einfache Benutzeroberfläche zur Konfiguration und zum Starten/Stoppen des Tools.

- `infrastructure.userInterface.ConsolePanel`  
  Ausgabe von Log-/Statusinformationen im UI.

---

### `audit` – Audit-Webservice & Frontend

Wird als getrennte JAR auf einem anderen System betrieben und greift per HTTP auf die `DatabaseAPI` des `app`-Moduls und auf die Waves-Blockchain zu.

**Backend (Java):**

- `Main`  
  Startet den Webserver / das Backend des Audit-Tools.

- `backend.api.AuditAPI`  
  REST-API, die vom Frontend aufgerufen wird, um Audit-Funktionen zu nutzen.

- `backend.datacollector.DataCollector`  
  Holt Daten über die `DatabaseAPI` aus der Datenbank.

- `backend.verifier.BlockchainHandler`  
  Kommuniziert mit der Blockchain (Waves), um Root-Hashes / Transaktionen abzurufen.

- `backend.verifier.Verifier`  
  Vergleicht neu berechnete Root-Hashes mit denen aus der Blockchain und erstellt ein Verifikationsresultat (`AuditSummary`).

- `backend.audit.AuditPdfCreator`  
  Erzeugt einen Audit-Report als PDF.

- `backend.audit.AuditSummary`  
  Record mit zusammenfassenden Auditinformationen (z. B. Zeitraum, Anzahl Datensätze, Ergebnis der Verifikation).

**Frontend (`audit/src/main/resources`):**

- `index.html` – Einstiegspunkt der Weboberfläche
- `app.js` – Logik der Single-Page-Anwendung
- `styles.css` – Layout und Styles

---
### Datenbank
- Die Datenbank wurde als PostgreSQL Datenbank aufgesetzt
- Der Ordner `db` enthält Dateien mit den für die Erstellung der Datenbank benutzten SQL-Befehle

---
### Konfigurations- & Hilfsdateien

Für die Ausführung des Tools sind die folgenden beiden Dateien im selben Verzeichnis abzulegen wie die jeweiligen `.jar`-Dateien der Anwendungen
- `config.properties`  
  Enthält Konfiguration für DB, Blockchain, Intervalle etc. (wird von `ConfigLoader` gelesen). Die Datei `config.example.properties` liefert ein Beispiel für den Aufbau 

- `map.csv`  
  Mapping-Datei, um Rohdaten vom Server auf Datamodelle/Felder zu mappen (z. B. Spaltenzuordnung).

---

## Build & Run

### Voraussetzungen
Für den Zugriff auf den in diesem Projekt verwendeten Server ist ein VPN notwendig. Der Server ist nicht öffentlich zugänglich.
Des Weiteren wird die Datei `map.csv` benötigt, welche ebenfalls nicht öffentlich verfügbar ist. Sonstige Voraussetzungen:
- Java (z. B. 17)
- Maven 3.x
- Config-Datei
- Laufende Datenbankinstanz (z. B. PostgreSQL/MySQL)
- Zugriff auf einen Waves-Node (Testnet/Mainnet)

### Projekt bauen

```bash
mvn clean package
```

### App starten
Folgenden bash-Befehl ausführen:
```bash
java -jar app/target/app-1.0-SNAPSHOT.jar
```

### Audit Webservice starten
Folgenden bash-Befehl ausführen:
```bash
java -jar audit/target/audit-1.0-SNAPSHOT.jar
```

Der Webservice ist erreichbar unter `localhost:8000/index.html`
