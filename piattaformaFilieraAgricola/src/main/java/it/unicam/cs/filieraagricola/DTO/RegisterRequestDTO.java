package it.unicam.cs.filieraagricola.DTO;
import lombok.Data;

@Data
public class RegisterRequestDTO {
    private String username;
    private String email;
    private String password;
    private String ruolo; // Es. "ACQUIRENTE", "PRODUTTORE"

    // Campi opzionali per l'azienda
    private String nomeAzienda;
    private String partitaIva;
    private String indirizzo;
    private String descrizione;
}