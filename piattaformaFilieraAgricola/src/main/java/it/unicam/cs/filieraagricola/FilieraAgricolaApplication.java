package it.unicam.cs.filieraagricola;

import it.unicam.cs.filieraagricola.DTO.ProdottoDTO;
import it.unicam.cs.filieraagricola.DTO.ProdottoRequestDTO;
import it.unicam.cs.filieraagricola.DTO.RegisterRequestDTO;
import it.unicam.cs.filieraagricola.model.Azienda;
import it.unicam.cs.filieraagricola.repository.UtenteRepository;
import it.unicam.cs.filieraagricola.service.AutenticazioneService;
import it.unicam.cs.filieraagricola.service.ModerazioneService;
import it.unicam.cs.filieraagricola.service.ProdottoService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class FilieraAgricolaApplication{
    public static void main(String[] args) {
        SpringApplication.run(FilieraAgricolaApplication.class, args);
    }
    /*
    @Autowired
    private AutenticazioneService autenticazioneService;

    @Autowired
    private ProdottoService prodottoService;

    @Autowired
    private ModerazioneService moderazioneService;

    // Iniettiamo il Repository per recuperare gli ID
    @Autowired
    private UtenteRepository utenteRepository;



    @Override
    @Transactional // Fondamentale per operazioni multiple sul DB
    public void run(String... args) throws Exception {
        System.out.println(" === PIATTAFORMA FILIERA AGRICOLA LOCALE - SPRING BOOT === 🌾\n");

        createDemoData();
        showApplicationInfo();

        System.out.println("=== PIATTAFORMA AVVIATA CON SUCCESSO === 🌾");
        System.out.println("Server in ascolto su http://localhost:8080");
        System.out.println(" H2 Console (se attiva): http://localhost:8080/h2-console");
    }


     //Crea dati demo utilizzando i nuovi Service e DTO.

    private void createDemoData() {
        try {
            System.out.println("=== CREAZIONE DATI DEMO (REFACTORED) ===");

            // 1. Crea aziende e utenti tramite il servizio di registrazione
            autenticazioneService.registraUtente(
                    createDto("fattoriaRossi", "PRODUTTORE", "Fattoria Rossi", "11111111111"));
            autenticazioneService.registraUtente(
                    createDto("caseificioVerdi", "TRASFORMATORE", "Caseificio Verdi", "22222222222"));
            autenticazioneService.registraUtente(
                    createDto("tipicoMarche", "DISTRIBUTORE", "Tipico Marche", "33333333333"));
            autenticazioneService.registraUtente(
                    createDto("annaNeri", "ACQUIRENTE", null, null));
            autenticazioneService.registraUtente(
                    createDto("curatoreDemo", "CURATORE", null, null));

            // 2. Recupera le aziende create per ottenerne gli ID
            Azienda fattoria = (Azienda) utenteRepository.findByUsername("fattoriaRossi").get();
            Azienda caseificio = (Azienda) utenteRepository.findByUsername("caseificioVerdi").get();

            // 3. Crea Prodotti (che saranno IN_ATTESA)
            ProdottoRequestDTO melaDto = new ProdottoRequestDTO("Mela Bio", "Mela coltivata senza pesticidi", 1.50, 100);
            ProdottoRequestDTO vinoDto = new ProdottoRequestDTO("Vino Rosso DOC", "Vino DOC delle Marche", 15.00, 50);
            ProdottoRequestDTO formaggioDto = new ProdottoRequestDTO("Pecorino", "Formaggio stagionato 12 mesi", 18.50, 30);

            // Chiamiamo il servizio per creare i prodotti
            ProdottoDTO mela = prodottoService.creaProdotto(melaDto, fattoria.getId());
            ProdottoDTO vino = prodottoService.creaProdotto(vinoDto, fattoria.getId());
            ProdottoDTO formaggio = prodottoService.creaProdotto(formaggioDto, caseificio.getId());

            System.out.println("...Prodotti creati in stato IN_ATTESA.");
            // 4. Demo Moderazione: Il curatore approva i prodotti
            moderazioneService.approvaProdotto(mela.getId());
            moderazioneService.approvaProdotto(vino.getId());
            moderazioneService.approvaProdotto(formaggio.getId());

            System.out.println("...Prodotti approvati dal Curatore Demo.");

            System.out.println("Dati demo creati con successo:");
            System.out.println("   - " + utenteRepository.count() + " utenti/aziende totali");
            System.out.println("   - " + prodottoService.getCatalogoProdotti().size() + " prodotti nel catalogo (approvati)");

        } catch (Exception e) {
            System.err.println("Errore nella creazione dati demo: " + e.getMessage());
            e.printStackTrace();
            System.err.println("   L'applicazione continuerà comunque...");
        }
    }

     //Helper method per creare velocemente DTO di registrazione.

    private RegisterRequestDTO createDto(String username, String ruolo, String nomeAzienda, String pIva) {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setUsername(username);
        dto.setEmail(username + "@demo.com");
        dto.setPassword("password123"); // La password verrà codificata dal service
        dto.setRuolo(ruolo);
        if (nomeAzienda != null) {
            dto.setNomeAzienda(nomeAzienda);
            dto.setPartitaIva(pIva);
            dto.setIndirizzo("Via Demo 123");
            dto.setDescrizione("Descrizione demo per " + nomeAzienda);
        }
        return dto;
    }

     // Mostra le info sui nuovi endpoint e logiche.

    private void showApplicationInfo() {
        System.out.println("\n === INFORMAZIONI APPLICAZIONE (REFACTORED) ===");
        System.out.println("Spring Boot configurato e avviato");

        System.out.println("\n === API ENDPOINTS REFACTORED ===");
        System.out.println("Autenticazione: POST /api/auth/register, /api/auth/login");
        System.out.println("Prodotti:       GET /api/products, GET /api/search/products");
        System.out.println("(Azienda):      POST/PUT/DELETE /api/prodotti-aziendali");
        System.out.println("Marketplace:    GET/POST/DELETE /api/carrello, POST /api/checkout");
        System.out.println("Ordini:         GET /api/ordini");
        System.out.println("⚖️Moderazione:    GET/POST /api/moderazione");
        System.out.println("Eventi:         GET/POST /api/eventi");
        System.out.println("Tracciabilità:  GET/POST /api/tracciabilita");

        System.out.println("\n === DESIGN PATTERN IMPLEMENTATI ===");
        System.out.println("Factory Method (Creazione Utenti)");
        System.out.println("Strategy Pattern (Metodi di Pagamento)");
        System.out.println("Observer Pattern (Notifiche Moderazione)");
        System.out.println("DTO (Data Transfer Object) (Livello API/Service)");
        System.out.println("Repository & Service Layers (Architettura Pulita)");
    }*/
}