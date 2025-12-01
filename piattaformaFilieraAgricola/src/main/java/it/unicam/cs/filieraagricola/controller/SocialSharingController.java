package it.unicam.cs.filieraagricola.controller;

import it.unicam.cs.filieraagricola.DTO.SocialShareDTO;
import it.unicam.cs.filieraagricola.service.SocialSharingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/social")
@CrossOrigin(origins = "*") // Importante per il frontend
public class SocialSharingController {

    @Autowired
    private SocialSharingService socialSharingService;

    /**
     * GET /api/social/share?tipo=prodotto&id=1
     * Restituisce i dati pronti per essere passati ai plugin social
     */
    @GetMapping("/share")
    public ResponseEntity<SocialShareDTO> getSocialData(
            @RequestParam String tipo,
            @RequestParam Long id) {

        return ResponseEntity.ok(socialSharingService.getShareData(tipo, id));
    }
}