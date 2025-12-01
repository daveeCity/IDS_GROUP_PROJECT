package it.unicam.cs.filieraagricola.DTO;

import lombok.Data;

@Data
public class UtenteDTO {
    private Long id;
    private String username;
    private String email;
    private String ruolo;
    private String nomeAzienda;   // Solo per Produttori/Trasformatori/Distributori
    private String statoAccount;  // "IN_ATTESA", "ATTIVO", ecc.

    // Costruttore vuoto
    public UtenteDTO() {}

    // Costruttore completo (opzionale, utile se non usi i setter uno per uno)
    public UtenteDTO(Long id, String username, String email, String ruolo, String nomeAzienda, String statoAccount) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.ruolo = ruolo;
        this.nomeAzienda = nomeAzienda;
        this.statoAccount = statoAccount;
    }
}