package it.unicam.cs.filieraagricola.controller;

import it.unicam.cs.filieraagricola.DTO.PassoFilieraRequestDTO;
import it.unicam.cs.filieraagricola.DTO.TracciabilitaDTO;
import it.unicam.cs.filieraagricola.model.Utente;
import it.unicam.cs.filieraagricola.model.Azienda;
import it.unicam.cs.filieraagricola.repository.UtenteRepository;
import it.unicam.cs.filieraagricola.service.TracciabilitaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tracciabilita")
public class TracciabilitaController {

    @Autowired
    private TracciabilitaService tracciabilitaService;

    @Autowired
    private UtenteRepository utenteRepository; // Necessario per risolvere l'utente loggato

    /* GET /api/tracciabilita/prodotto/{id}
     * Restituisce l'intera storia della filiera per un dato prodotto.
            * Accessibile a tutti (Utente Generico, Acquirente, ecc.)
     */
    @GetMapping("/prodotto/{prodottoId}")
    public ResponseEntity<TracciabilitaDTO> getTracciabilita(@PathVariable Long prodottoId) {
        try {
            TracciabilitaDTO tracciabilita = tracciabilitaService.getTracciabilitaByProdotto(prodottoId);
            return ResponseEntity.ok(tracciabilita);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /* POST /api/tracciabilita/prodotto/{id}/passo
     * Aggiunge un nuovo passo alla filiera (es. "Trasformazione", "Confezionamento").
            * Accessibile solo a PRODUTTORE e TRASFORMATORE (verificato via Security o logica interna).
            */
    @PostMapping("/prodotto/{prodottoId}/passo")
    public ResponseEntity<?> aggiungiPasso(
            @PathVariable Long prodottoId,
            @RequestBody PassoFilieraRequestDTO request) {

        // 1. Recupera l'utente attualmente loggato dal contesto di sicurezza
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // 2. Recupera l'entità Utente dal database
        Utente utenteLoggato = utenteRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new EntityNotFoundException("Utente loggato non trovato nel DB"));

        // 3. Verifica che l'utente sia un'Azienda (Produttore o Trasformatore)
        // Nota: questa verifica è parzialmente ridondante se usi @PreAuthorize, ma è più sicura a livello logico.
        if (!(utenteLoggato instanceof Azienda)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Solo le aziende (Produttori/Trasformatori) possono aggiungere passi alla filiera.");
        }

        try {
            // 4. Chiama il service passando l'ID dell'azienda recuperato dal token, non dal body della request!
            TracciabilitaDTO aggiornata = tracciabilitaService.aggiungiPassoFiliera(
                    prodottoId,
                    utenteLoggato.getId(),
                    request
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(aggiornata);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore generico: " + e.getMessage());
        }
    }
}