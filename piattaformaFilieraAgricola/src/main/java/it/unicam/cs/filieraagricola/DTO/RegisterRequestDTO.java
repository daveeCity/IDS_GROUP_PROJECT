package it.unicam.cs.filieraagricola.DTO;
import lombok.Data;

@Data
public class RegisterRequestDTO {
    private String username;
    private String email;
    private String password;
    private String ruolo;
    private String nomeAzienda;
    private String partitaIva;
    private String indirizzo;
    private String descrizione;
}