package it.unicam.cs.filieraagricola.controller;

import it.unicam.cs.filieraagricola.DTO.EventoDTO;
import it.unicam.cs.filieraagricola.DTO.ModerazioneRequestDTO;
import it.unicam.cs.filieraagricola.DTO.PassoFilieraDTO;
import it.unicam.cs.filieraagricola.DTO.ProdottoDTO;
import it.unicam.cs.filieraagricola.service.ModerazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/moderazione")

public class ModerazioneController {

    @Autowired
    private ModerazioneService moderazioneService;

    @GetMapping("/in-attesa")
    public ResponseEntity<List<ProdottoDTO>> getProdottiDaApprovare() {
        return ResponseEntity.ok(moderazioneService.getProdottiInAttesa());
    }

    @PutMapping("/approva/{prodottoId}")
    public ResponseEntity<ProdottoDTO> approvaProdotto(@PathVariable Long prodottoId) {
        return ResponseEntity.ok(moderazioneService.approvaProdotto(prodottoId));
    }

    @PutMapping("/rifiuta/{prodottoId}")
    public ResponseEntity<ProdottoDTO> rifiutaProdotto(@PathVariable Long prodottoId,
                                                       @RequestBody ModerazioneRequestDTO request) {
        return ResponseEntity.ok(moderazioneService.rifiutaProdotto(prodottoId, request));
    }

    @GetMapping("/eventi/in-attesa")
    public ResponseEntity<List<EventoDTO>> getEventiDaApprovare() {
        return ResponseEntity.ok(moderazioneService.getEventiInAttesa());
    }

    @PutMapping("/eventi/approva/{id}")
    public ResponseEntity<EventoDTO> approvaEvento(@PathVariable Long id) {
        return ResponseEntity.ok(moderazioneService.approvaEvento(id));
    }

    @GetMapping("/passi/in-attesa")
    public ResponseEntity<List<PassoFilieraDTO>> getPassiDaApprovare() {
        return ResponseEntity.ok(moderazioneService.getPassiInAttesa());
    }

    @PutMapping("/passi/approva/{id}")
    public ResponseEntity<PassoFilieraDTO> approvaPasso(@PathVariable Long id) {
        return ResponseEntity.ok(moderazioneService.approvaPasso(id));
    }
}