package it.unicam.cs.filieraagricola.controller;

import it.unicam.cs.filieraagricola.DTO.EventoDTO;
import it.unicam.cs.filieraagricola.DTO.EventoRequestDTO;
import it.unicam.cs.filieraagricola.model.Utente;
import it.unicam.cs.filieraagricola.repository.UtenteRepository;
import it.unicam.cs.filieraagricola.service.EventoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventi")
public class EventoController {

    @Autowired private EventoService eventoService;
    @Autowired private UtenteRepository utenteRepository;

    @GetMapping
    public ResponseEntity<List<EventoDTO>> getEventiPubblici() {
        return ResponseEntity.ok(eventoService.getEventiPubblici());
    }

    @PostMapping
    public ResponseEntity<EventoDTO> creaEvento(@RequestBody EventoRequestDTO request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Utente animatore = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Animatore non trovato"));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eventoService.creaEvento(request, animatore.getId()));
    }

    @PostMapping("/{id}/partecipa")
    public ResponseEntity<Void> partecipaEvento(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Utente acquirente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));

        eventoService.partecipaEvento(id, acquirente.getId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/annulla")
    public ResponseEntity<Void> annullaEvento(@PathVariable Long id) {
        eventoService.annullaEvento(id);
        return ResponseEntity.ok().build();
    }
}