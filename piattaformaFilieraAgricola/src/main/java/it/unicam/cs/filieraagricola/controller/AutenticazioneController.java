package it.unicam.cs.filieraagricola.controller;

import it.unicam.cs.filieraagricola.DTO.AuthResponseDTO;
import it.unicam.cs.filieraagricola.DTO.LoginRequestDTO;
import it.unicam.cs.filieraagricola.DTO.RegisterRequestDTO;
import it.unicam.cs.filieraagricola.service.AutenticazioneService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AutenticazioneController {

    @Autowired
    private AutenticazioneService authService;

    @PostMapping("/registra")
    public ResponseEntity<AuthResponseDTO> registraUtente(@Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(authService.registraUtente(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.loginUtente(request));
    }
}