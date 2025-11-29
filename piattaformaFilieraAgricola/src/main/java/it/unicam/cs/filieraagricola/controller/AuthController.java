package it.unicam.cs.filieraagricola.controller;

import it.unicam.cs.filieraagricola.DTO.AuthResponseDTO;
import it.unicam.cs.filieraagricola.DTO.LoginRequestDTO;
import it.unicam.cs.filieraagricola.DTO.RegisterRequestDTO;
import it.unicam.cs.filieraagricola.service.AutenticazioneService;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    @Lazy
    private AutenticazioneService autenticazioneService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        AuthResponseDTO response = autenticazioneService.registraUtente(registerRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        AuthResponseDTO response = autenticazioneService.loginUtente(loginRequest);
        return ResponseEntity.ok(response);
    }
}