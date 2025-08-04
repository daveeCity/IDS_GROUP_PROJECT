# IDS_GROUP_PROJECT
--------------------------------------------------------------------------------
 Piattaforma di Digitalizzazione e Valorizzazione della Filiera Agricola Locale
       
Una piattaforma innovativa per la gestione, valorizzazione e tracciabilità dei prodotti agricoli locali, sviluppata seguendo il processo Unified Process (UP) e i principi GRASP.

## Indice

•	 Panoramica
•	 Caratteristiche Principali
•	Attori del Sistema
•	 Architettura
•	Tecnologie Utilizzate
•	Struttura del Progetto
•	Diagrammi UML

## Panoramica

La Piattaforma di Digitalizzazione e Valorizzazione della Filiera Agricola Locale è un sistema completo che permette la gestione, valorizzazione e tracciabilità dei prodotti agricoli di un territorio comunale.
Obiettivi Principali
•	Tracciabilità Completa: Visualizzazione dell'intero ciclo produttivo dei prodotti
•	Mappatura Territoriale: Geolocalizzazione di tutti i punti della filiera
•	Marketplace Integrato: Vendita diretta da produttori a consumatori
•	Gestione Eventi: Organizzazione di fiere e visite guidate
•	 Sistema di Moderazione: Controllo qualità dei contenuti pubblicati

## Caratteristiche Principali

Utenti
•	Catalogo Prodotti: Ricerca avanzata con filtri geografici e di categoria
•	Tracciabilità: Visualizzazione completa della filiera produttiva
•	Mappa Interattiva: Localizzazione di produttori, trasformatori e distributori
•	Marketplace: Acquisto diretto di prodotti locali
•	Eventi: Partecipazione a fiere e visite aziendali
Aziende
•	Gestione Profilo: Creazione e gestione del profilo aziendale
•	Caricamento Prodotti: Upload di informazioni e certificazioni
•	Vendita Online: Integrazione con sistema di pagamento
•	Gestione Eventi: Organizzazione e promozione eventi
Sistema di Controllo
•	Moderazione Contenuti: Workflow di approvazione per curatori
•	Sistema di Notifiche: Comunicazioni multicanale
•	Analytics: Dashboard per analisi dati e performance

## Attori del Sistema

Attore	Descrizione	Funzionalità Principali
Produttore	Aziende agricole	Caricamento prodotti, certificazioni, vendita
Trasformatore	Industrie di trasformazione	Gestione processi, tracciabilità, vendita
Distributore	Negozi di tipicità	Vendita prodotti, creazione pacchetti
Curatore	Moderatore contenuti	Approvazione, controllo qualità
Animatore	Organizzatore eventi	Gestione fiere, visite guidate
Acquirente	Consumatori finali	Ricerca, acquisto, partecipazione eventi
Utente Generico	Visitatori	Consultazione informazioni
Gestore	Amministratore	Gestione piattaforma, autorizzazioni

## Architettura

Pattern Architetturali
•	Layered Architecture: Separazione in livelli logici
•	Repository Pattern: Astrazione accesso dati
•	Service Layer: Logica di business centralizzata
•	MVC Pattern: Separazione presentazione e logica
Principi GRASP Implementati
•	Information Expert: Assegnazione responsabilità basata su informazioni
•	Creator: Gestione creazione oggetti
•	Low Coupling: Basso accoppiamento tra componenti
•	High Cohesion: Alta coesione funzionale
•	Polymorphism: Gestione variazioni comportamentali

## Tecnologie Utilizzate

Backend
•	Java: Linguaggio principale
•	Spring Boot: Framework applicativo
•	Spring Security: Autenticazione e autorizzazione JWT
•	Spring Data JPA: ORM e gestione database
•	PostgreSQL: Database relazionale principale
DevOps & Tools
•	Maven: Build automation
•	Docker: Containerizzazione
•	JUnit 5: Testing framework

## Struttura del Progetto

filiera-agricola-platform/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   
//TODO

## Endpoints Principali

Autenticazione
POST /api/auth/login          # Login utente
POST /api/auth/register       # Registrazione
POST /api/auth/refresh        # Refresh token
Prodotti
GET    /api/products          # Lista prodotti
POST   /api/products          # Crea prodotto
GET    /api/products/{id}     # Dettagli prodotto
PUT    /api/products/{id}     # Aggiorna prodotto
DELETE /api/products/{id}     # Elimina prodotto
GET    /api/products/{id}/trace # Tracciabilità
Ricerca
GET /api/search/products      # Ricerca prodotti
GET /api/search/companies     # Ricerca aziende
GET /api/search/geo          # Ricerca geografica
Eventi
GET    /api/events           # Lista eventi
POST   /api/events           # Crea evento
GET    /api/events/{id}      # Dettagli evento
POST   /api/events/{id}/join # Partecipa evento

## Diagrammi UML

Diagrammi Disponibili
•	Casi d'Uso: Interazioni attori-sistema
•	Classi di Analisi: Modello concettuale BCE
•	Classi di Progetto: Architettura implementativa
•	Sequenze: Flussi di interazione temporali
•	Attività: Processi di business


## Collaboratori

- [daveeCity](https://github.com/daveeCity)
- [Yaso-01](https://github.com/Yaso-01)
- [GitAlboBis](https://github.com/GitAlboBis)
