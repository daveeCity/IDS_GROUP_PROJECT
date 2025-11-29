package it.unicam.cs.filieraagricola.controller;

import it.unicam.cs.filieraagricola.DTO.PassoFilieraDTO;
import it.unicam.cs.filieraagricola.DTO.TracciabilitaDTO;
import it.unicam.cs.filieraagricola.model.Azienda;
import it.unicam.cs.filieraagricola.repository.AziendaRepository;
import it.unicam.cs.filieraagricola.service.TracciabilitaService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired; // Opzionale con costruttore
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mappa")
@CrossOrigin(origins = "*") // Permette al frontend (es. React/Angular) di chiamare l'API
public class MappaController {

    // RIMOSSO @Autowired da qui: i campi sono final e vengono inizializzati nel costruttore
    private final AziendaRepository aziendaRepository;
    private final TracciabilitaService tracciabilitaService;

    // --- DTO Interno per la risposta ---
    @Getter
    @Setter
    @AllArgsConstructor
    public static class PosizioneMappaDTO {
        private double latitudine;
        private double longitudine;
        private String titolo;
        private String descrizione;
        private String tipo;
        private Long relatedEntityId;
    }

    // --- Iniezione tramite costruttore (Corretto) ---
    @Autowired // Puoi lasciarlo qui, oppure toglierlo (Spring lo capisce da solo se c'è un solo costruttore)
    public MappaController(AziendaRepository aziendaRepository, TracciabilitaService tracciabilitaService) {
        this.aziendaRepository = aziendaRepository;
        this.tracciabilitaService = tracciabilitaService;
    }

    // Ritorna le posizioni simulate di tutte le aziende
    @GetMapping("/aziende")
    public ResponseEntity<List<PosizioneMappaDTO>> getPosizioniAziende() {
        List<Azienda> aziende = aziendaRepository.findAll();
        List<PosizioneMappaDTO> locations = aziende.stream()
                .map(this::convertToPosizioneMappaDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(locations);
    }

    // Ricostruisce il percorso sulla mappa in base alla tracciabilità
    @GetMapping("/percorso-filiera/{prodottoId}")
    public ResponseEntity<List<PosizioneMappaDTO>> getPercorsoFiliera(@PathVariable Long prodottoId) {
        TracciabilitaDTO traccia = tracciabilitaService.getTracciabilitaByProdotto(prodottoId);
        List<PosizioneMappaDTO> route = new ArrayList<>();

        if (traccia != null && traccia.getPassiDellaFiliera() != null) {
            for (PassoFilieraDTO passo : traccia.getPassiDellaFiliera()) {
                // Qui è importante usare findById per evitare NullPointer se l'azienda non esiste
                aziendaRepository.findById(passo.getAziendaId()).ifPresent(azienda -> {
                    PosizioneMappaDTO loc = createPosizioneMappaDTO(
                            azienda,
                            passo.getNomeFase() + " presso " + azienda.getNomeAzienda(),
                            passo.getLuogo(),
                            "STEP_FILIERA"
                    );
                    route.add(loc);
                });
            }
        }
        return ResponseEntity.ok(route);
    }

    // --- Metodi Helper ---

    private PosizioneMappaDTO convertToPosizioneMappaDTO(Azienda azienda) {
        // Uso getClass().getSimpleName() per distinguere Produttore, Trasformatore, ecc.
        String tipoAzienda = azienda.getClass().getSimpleName();
        // Se è un proxy di Hibernate (succede spesso), ripulisco il nome
        if (tipoAzienda.contains("$")) {
            tipoAzienda = "Azienda";
        }

        return createPosizioneMappaDTO(
                azienda,
                azienda.getNomeAzienda(),
                azienda.getIndirizzo(),
                tipoAzienda
        );
    }

    private PosizioneMappaDTO createPosizioneMappaDTO(Azienda azienda, String titolo, String descrizione, String tipo) {
        Random random = new Random(azienda.getId()); // Seed fisso: l'azienda avrà sempre le stesse coordinate simulate

        // Coordinate simulate (Marche circa)
        double lat = 43.3 + (random.nextDouble() - 0.5) * 1.0;
        double lon = 13.0 + (random.nextDouble() - 0.5) * 1.5;

        return new PosizioneMappaDTO(
                lat,
                lon,
                titolo,
                descrizione,
                tipo.toUpperCase(),
                azienda.getId()
        );
    }
}