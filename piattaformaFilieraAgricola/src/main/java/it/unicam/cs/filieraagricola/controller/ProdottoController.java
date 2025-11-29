package it.unicam.cs.filieraagricola.controller;

import it.unicam.cs.filieraagricola.DTO.ProdottoDTO;
import it.unicam.cs.filieraagricola.DTO.ProdottoRequestDTO;
import it.unicam.cs.filieraagricola.model.Utente;
import it.unicam.cs.filieraagricola.repository.UtenteRepository;
import it.unicam.cs.filieraagricola.service.ProdottoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prodotti")
public class ProdottoController {

    @Autowired private ProdottoService prodottoService;
    @Autowired private UtenteRepository utenteRepository;

    @GetMapping("/catalogo")
    public ResponseEntity<List<ProdottoDTO>> getCatalogo() {
        return ResponseEntity.ok(prodottoService.getCatalogoProdotti());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdottoDTO> getProdotto(@PathVariable Long id) {
        return ResponseEntity.ok(prodottoService.getProdottoById(id));
    }

    @GetMapping("/cerca")
    public ResponseEntity<List<ProdottoDTO>> cercaProdotti(@RequestParam String nome) {
        return ResponseEntity.ok(prodottoService.cercaProdottiPerNome(nome));
    }

    @PostMapping
    public ResponseEntity<ProdottoDTO> creaProdotto(@RequestBody ProdottoRequestDTO request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(prodottoService.creaProdotto(request, utente.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminaProdotto(@PathVariable Long id) {
        prodottoService.deleteProdotto(id);
        return ResponseEntity.noContent().build();
    }
}