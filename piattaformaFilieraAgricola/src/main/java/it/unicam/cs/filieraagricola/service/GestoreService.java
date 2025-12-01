package it.unicam.cs.filieraagricola.service;

import it.unicam.cs.filieraagricola.DTO.UtenteDTO; // Crea un DTO semplice con id, username, ruolo, nomeAzienda
import java.util.List;

public interface GestoreService {
    List<UtenteDTO> getUtentiDaApprovare();
    void approvaUtente(Long id);
    void rifiutaUtente(Long id);
}