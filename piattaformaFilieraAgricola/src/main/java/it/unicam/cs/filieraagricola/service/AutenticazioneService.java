package it.unicam.cs.filieraagricola.service;

import it.unicam.cs.filieraagricola.DTO.AuthResponseDTO;
import it.unicam.cs.filieraagricola.DTO.LoginRequestDTO;
import it.unicam.cs.filieraagricola.DTO.RegisterRequestDTO;

public interface AutenticazioneService {

    /**
     * Registra un nuovo utente nel sistema.
     */
    AuthResponseDTO registraUtente(RegisterRequestDTO request);

    /**
     * Esegue il login di un utente esistente.
     */
    AuthResponseDTO loginUtente(LoginRequestDTO request);
}