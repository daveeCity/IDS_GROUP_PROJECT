package it.unicam.cs.filieraagricola.controller;

import it.unicam.cs.filieraagricola.DTO.UtenteDTO;
import it.unicam.cs.filieraagricola.service.GestoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gestore")
public class GestoreController {

    @Autowired private GestoreService gestoreService;

    @GetMapping("/richieste-pendenti")
    public ResponseEntity<List<UtenteDTO>> getRichieste() {
        return ResponseEntity.ok(gestoreService.getUtentiDaApprovare());
    }

    @PutMapping("/approva/{id}")
    public ResponseEntity<String> approva(@PathVariable Long id) {
        gestoreService.approvaUtente(id);
        return ResponseEntity.ok("Utente approvato e attivato.");
    }

    @DeleteMapping("/rifiuta/{id}")
    public ResponseEntity<String> rifiuta(@PathVariable Long id) {
        gestoreService.rifiutaUtente(id);
        return ResponseEntity.ok("Richiesta di registrazione rifiutata.");
    }
}