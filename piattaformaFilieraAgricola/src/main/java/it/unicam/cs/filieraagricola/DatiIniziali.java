package it.unicam.cs.filieraagricola;

import it.unicam.cs.filieraagricola.DTO.*;
import it.unicam.cs.filieraagricola.model.Azienda;
import it.unicam.cs.filieraagricola.model.Distributore;
import it.unicam.cs.filieraagricola.model.StatoAccount;
import it.unicam.cs.filieraagricola.model.Utente;
import it.unicam.cs.filieraagricola.repository.UtenteRepository;
import it.unicam.cs.filieraagricola.service.*;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Profile("!test")
public class DatiIniziali implements CommandLineRunner {

    @Autowired private AutenticazioneService autenticazioneService;
    @Autowired private ProdottoService prodottoService;
    @Autowired private ModerazioneService moderazioneService;
    @Autowired private EventoService eventoService;
    @Autowired private PaccoProdottoService paccoProdottoService;
    @Autowired private TracciabilitaService tracciabilitaService;
    @Autowired private UtenteRepository utenteRepository;

    public static void main(String[] args) {
        SpringApplication.run(FilieraAgricolaApplication.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println(" === PIATTAFORMA FILIERA AGRICOLA LOCALE - INIT DATA === 🌾\n");

        createDemoData();

    }

    private void createDemoData() {
        try {
            System.out.println("... Creazione Utenti ...");
            // 1. UTENTI
            autenticazioneService.registraUtente(createDto("fattoriaRossi", "PRODUTTORE", "Fattoria Rossi", "11111111111", "Via Rossi 123", 100.0, 200.0));
            autenticazioneService.registraUtente(createDto("caseificioVerdi", "TRASFORMATORE", "Caseificio Verdi", "22222222222", "Via Verdi 456", 300.0, 400.0));
            autenticazioneService.registraUtente(createDto("tipicoMarche", "DISTRIBUTORE", "Tipico Marche", "33333333333", "Via Marche 789", 500.0, 600.0));
            autenticazioneService.registraUtente(createDto("annaNeri", "ACQUIRENTE", null, null, null, null, null));
            autenticazioneService.registraUtente(createDto("curatoreDemo", "CURATORE", null, null, null, null, null));
            autenticazioneService.registraUtente(createDto("animatoreDemo", "ANIMATORE", null, null, null, null, null));
            autenticazioneService.registraUtente(createDto("lucaNeri", "GESTORE", null, null, null, null, null));
            System.out.println("... Attivazione Account Demo ...");
            attivaUtenteDemo("fattoriaRossi");
            attivaUtenteDemo("caseificioVerdi");
            attivaUtenteDemo("tipicoMarche");
            attivaUtenteDemo("animatoreDemo");
            attivaUtenteDemo("curatoreDemo");
            attivaUtenteDemo("lucaNeri");

            // Recupero ID utili
            Azienda fattoria = (Azienda) utenteRepository.findByUsername("fattoriaRossi").get();
            Azienda caseificio = (Azienda) utenteRepository.findByUsername("caseificioVerdi").get();
            Distributore distributore = (Distributore) utenteRepository.findByUsername("tipicoMarche").get();
            Utente animatore = utenteRepository.findByUsername("animatoreDemo").get();

            System.out.println("... Creazione Prodotti ...");
            // 2. PRODOTTI (Alcuni approvati, alcuni no)

            // Prodotti Approvati (per testare Carrello e Ordini)
            ProdottoDTO mela = prodottoService.creaProdotto(new ProdottoRequestDTO("Mela Bio", "Mela croccante", 1.50, 100), fattoria.getId());
            ProdottoDTO vino = prodottoService.creaProdotto(new ProdottoRequestDTO("Vino Rosso DOC", "Vino marchigiano", 15.00, 50), fattoria.getId());
            ProdottoDTO formaggio = prodottoService.creaProdotto(new ProdottoRequestDTO("Pecorino", "Stagionato", 18.50, 30), caseificio.getId());

            moderazioneService.approvaProdotto(mela.getId());
            moderazioneService.approvaProdotto(vino.getId());
            moderazioneService.approvaProdotto(formaggio.getId());
            // PRODOTTO PER IL TUO TEST DI MODERAZIONE (Non approvato!)
            ProdottoDTO miele = prodottoService.creaProdotto(new ProdottoRequestDTO("Miele Grezzo", "Miele non filtrato, in attesa di controllo", 8.00, 20), fattoria.getId());
            System.out.println("   -> Creato 'Miele Grezzo' (ID: " + miele.getId() + ") in stato IN_ATTESA per testare la moderazione.");

            System.out.println("... Creazione Pacchi ...");
            // 3. PACCHI (Per testare PaccoProdottoController)
            PaccoProdottoRequestDTO paccoDto = new PaccoProdottoRequestDTO();
            paccoDto.setNome("Cesto Aperitivo");
            paccoDto.setDescrizione("Vino e Formaggio");
            paccoDto.setPrezzo(30.00);
            paccoDto.setProdottoIds(List.of(vino.getId(), formaggio.getId()));

            paccoProdottoService.creaPacco(paccoDto, distributore.getId());

            System.out.println("... Creazione Eventi ...");
            // 4. EVENTI (Per testare EventoController)
            EventoRequestDTO eventoDto = new EventoRequestDTO();
            eventoDto.setTitolo("Festa del Raccolto");
            eventoDto.setDescrizione("Musica e cibo km0");
            eventoDto.setLuogo("Piazza Cavour, Ancona");
            eventoDto.setTipo("FESTIVAL_GASTRONOMICO");
            eventoDto.setDataOraInizio(LocalDateTime.now().plusDays(5));
            eventoDto.setDataOraFine(LocalDateTime.now().plusDays(5).plusHours(4));
            eventoDto.setLongitudine(100.0);
            eventoDto.setLatitudine(100.0);
            EventoDTO eventooo;
            eventooo = eventoService.creaEvento(eventoDto, animatore.getId());
            System.out.println("   -> Creato evento '" + eventooo.getId());
            System.out.println("... Creazione Tracciabilità ...");

            // 5. TRACCIABILITÀ (Per testare TracciabilitaController)
            PassoFilieraRequestDTO passo1 = new PassoFilieraRequestDTO();
            passo1.setNomeFase("SEEDING");
            passo1.setDescrizione("Semina del campo sud");
            passo1.setLuogo("Fattoria Rossi - Campo A");
            passo1.setDataOra(LocalDateTime.now().minusMonths(3));

            // Aggiungiamo un passo alla Mela
            tracciabilitaService.aggiungiPassoFiliera(mela.getId(), fattoria.getId(), passo1);


            System.out.println("DATI INIZIALI PRONTI PER L'ESAME");

        } catch (Exception e) {
            System.err.println("Errore init dati: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private RegisterRequestDTO createDto(String username, String ruolo, String nomeAzienda, String pIva, String indirizzo, Double latitudine, Double longitudine) {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setUsername(username);
        dto.setEmail(username + "@demo.com");
        dto.setPassword("password123");
        dto.setRuolo(ruolo);
        if (nomeAzienda != null) {
            dto.setNomeAzienda(nomeAzienda);
            dto.setPartitaIva(pIva);
            dto.setIndirizzo("Via Demo 123");
            dto.setDescrizione("Descrizione demo");
            dto.setLatitudine(latitudine);
            dto.setLongitudine(longitudine);
        }
        return dto;
    }

    private void attivaUtenteDemo(String username) {
        utenteRepository.findByUsername(username).ifPresent(u -> {
            u.setStatoAccount(StatoAccount.ATTIVO);
            utenteRepository.save(u);
        });
    }

}