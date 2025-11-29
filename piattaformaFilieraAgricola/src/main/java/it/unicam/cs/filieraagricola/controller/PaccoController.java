package it.unicam.cs.filieraagricola.controller;

import it.unicam.cs.filieraagricola.DTO.PaccoProdottoDTO;
import it.unicam.cs.filieraagricola.DTO.PaccoProdottoRequestDTO;
import it.unicam.cs.filieraagricola.model.Utente;
import it.unicam.cs.filieraagricola.repository.UtenteRepository;
import it.unicam.cs.filieraagricola.service.PaccoProdottoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacchi")
public class PaccoController {

    @Autowired private PaccoProdottoService paccoService;
    @Autowired private UtenteRepository utenteRepository;

    @PostMapping
    public ResponseEntity<PaccoProdottoDTO> creaPacco(@RequestBody PaccoProdottoRequestDTO request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Utente distributore = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Distributore non trovato"));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paccoService.creaPacco(request, distributore.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaccoProdottoDTO> getPacco(@PathVariable Long id) {
        return ResponseEntity.ok(paccoService.getPaccoById(id));
    }

    @GetMapping("/distributore/{distributoreId}")
    public ResponseEntity<List<PaccoProdottoDTO>> getPacchiByDistributore(@PathVariable Long distributoreId) {
        return ResponseEntity.ok(paccoService.getPacchettiByDistributore(distributoreId));
    }
}