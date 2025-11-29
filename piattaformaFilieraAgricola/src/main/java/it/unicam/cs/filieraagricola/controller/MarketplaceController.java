package it.unicam.cs.filieraagricola.controller;

import it.unicam.cs.filieraagricola.DTO.*;
import it.unicam.cs.filieraagricola.service.MarketplaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marketplace")
public class MarketplaceController {

    @Autowired
    private MarketplaceService marketplaceService;

    @GetMapping("/carrello")
    public ResponseEntity<CarrelloDTO> getCarrello() {
        return ResponseEntity.ok(marketplaceService.getCarrello());
    }

    @PostMapping("/carrello/aggiungi")
    public ResponseEntity<CarrelloDTO> aggiungiAlCarrello(@RequestBody AggiungiAlCarrelloRequestDTO request) {
        return ResponseEntity.ok(marketplaceService.aggiungiAlCarrello(request));
    }

    @DeleteMapping("/carrello/rimuovi/{prodottoId}")
    public ResponseEntity<CarrelloDTO> rimuoviDalCarrello(@PathVariable Long prodottoId) {
        return ResponseEntity.ok(marketplaceService.rimuoviDalCarrello(prodottoId));
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrdineDTO> checkout(@RequestBody CheckoutRequestDTO request) {
        return ResponseEntity.ok(marketplaceService.checkout(request));
    }

    @GetMapping("/ordini")
    public ResponseEntity<List<OrdineDTO>> getMieiOrdini() {
        return ResponseEntity.ok(marketplaceService.getMieiOrdini());
    }
}