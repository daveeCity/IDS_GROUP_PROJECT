package main.java.it.unicam.cs.filieraagricola;

import main.java.it.unicam.cs.filieraagricola.factory.UserFactory;
import main.java.it.unicam.cs.filieraagricola.model.*;
import main.java.it.unicam.cs.filieraagricola.observer.EmailNotificationObserver;
import main.java.it.unicam.cs.filieraagricola.observer.LoggingObserver;
import main.java.it.unicam.cs.filieraagricola.repository.ProductRepository;
import main.java.it.unicam.cs.filieraagricola.service.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("🌾 === PIATTAFORMA FILIERA AGRICOLA LOCALE === 🌾\n");
        
        // Inizializzazione servizi
        ProductRepository repo = new ProductRepository();
        ProductService productService = new ProductService(repo);
        ServizioAutenticazione auth = new ServizioAutenticazione();
        ServizioRicerca ricerca = new ServizioRicerca(repo);
        ServizioModerazioni moderazioni = new ServizioModerazioni();
        
        // Configurazione Observer Pattern per notifiche
        EmailNotificationObserver emailNotifier = new EmailNotificationObserver("SMTP Gmail");
        LoggingObserver logger = new LoggingObserver();
        moderazioni.addObserver(emailNotifier);
        moderazioni.addObserver(logger);
        
        System.out.println("📧 Sistema di notifiche configurato con " + moderazioni.getObserverCount() + " observer\n");

        // Dimostrazione Factory Pattern - Registrazione utenti
        System.out.println("👥 === REGISTRAZIONE UTENTI (Factory Pattern) ===");
        try {
            User produttore = auth.registra("mario.rossi", "password123", "PRODUTTORE");
            User trasformatore = auth.registra("lucia.bianchi", "secret456", "TRASFORMATORE", "LIC001");
            User curatore = auth.registra("francesco.verdi", "admin789", "CURATORE", "Agricoltura Biologica");
            User cliente = auth.registra("anna.neri", "cliente123", "CLIENTE", "anna@email.com", "Via Roma 1");
            
            System.out.println("Utenti registrati: " + auth.getNumeroUtentiRegistrati());
        } catch (Exception e) {
            System.err.println("Errore registrazione: " + e.getMessage());
        }

        // Test autenticazione
        System.out.println("\n🔐 === TEST AUTENTICAZIONE ===");
        auth.login("mario.rossi", "password123");
        
        // Creazione prodotti
        System.out.println("\n📦 === CREAZIONE PRODOTTI ===");
        Company farm = new Company(1L, "Fattoria Rossi", "Produttore", "Ancona");
        Product apple = new Product(1L, "Mela Bio", "Mela coltivata senza pesticidi", "Certificazione Bio", 1.50, farm);
        Product wine = new Product(2L, "Vino Rosso", "Vino DOC locale", "DOC", 10.00, farm);
        Product cheese = new Product(3L, "Pecorino Marchigiano", "Formaggio stagionato 12 mesi", "DOP", 15.50, farm);
        
        productService.create(apple);
        productService.create(wine);
        productService.create(cheese);

        // Dimostrazione Observer Pattern - Sistema di moderazione
        System.out.println("\n⚖️ === SISTEMA DI MODERAZIONE (Observer Pattern) ===");
        auth.login("francesco.verdi", "admin789");
        
        if (auth.getUtenteCorrente().isPresent() && auth.getUtenteCorrente().get() instanceof Curatore) {
            Curatore curatore = (Curatore) auth.getUtenteCorrente().get();
            
            // Sottometti prodotti per revisione
            moderazioni.sottomettiPerRevisione(apple);
            moderazioni.sottomettiPerRevisione(wine);
            moderazioni.sottomettiPerRevisione(cheese);
            
            System.out.println("\nProdotti in revisione: " + moderazioni.getProdottiInRevisione().size());
            
            // Approva alcuni prodotti
            moderazioni.approvaProdotto(apple, curatore);
            moderazioni.approvaProdotto(wine, curatore);
            
            // Respingi un prodotto
            moderazioni.respingiProdotto(cheese, curatore, "Certificazione DOP non valida");
            
        }

        // Test sistema di ricerca
        System.out.println("\n🔍 === SISTEMA DI RICERCA ===");
        auth.login("anna.neri", "cliente123");
        
        System.out.println("Tutti i prodotti:");
        ricerca.getTuttiProdotti().forEach(System.out::println);
        
        System.out.println("\nRicerca per 'Mela':");
        ricerca.ricercaPerNome("Mela").forEach(System.out::println);
        
        System.out.println("\nRicerca per certificazione 'Bio':");
        ricerca.ricercaPerCertificazione("Bio").forEach(System.out::println);
        
        System.out.println("\nRicerca prodotti sotto €12:");
        ricerca.ricercaPerPrezzoMassimo(12.0).forEach(System.out::println);

        // Catalogo
        System.out.println("\n📋 === GESTIONE CATALOGO ===");
        Catalogo catalogoPrimavera = new Catalogo(1L, "Catalogo Primavera", "Prodotti di stagione primaverile");
        catalogoPrimavera.aggiungiProdotto(apple);
        catalogoPrimavera.aggiungiProdotto(wine);
        catalogoPrimavera.pubblica();
        
        System.out.println("Catalogo creato: " + catalogoPrimavera);

        // Log finale
        System.out.println("\n📝 === LOG DELLE OPERAZIONI ===");
        System.out.println("Operazioni di moderazione registrate: " + logger.getLogCount());
        logger.getLogs().forEach(System.out::println);
        
        auth.logout();
        System.out.println("\n🌾 === FINE DEMO === 🌾");
    }
}
