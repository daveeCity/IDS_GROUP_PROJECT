package it.unicam.cs.filieraagricola.controller;

import it.unicam.cs.filieraagricola.DTO.ModerazioneRequestDTO;
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
}