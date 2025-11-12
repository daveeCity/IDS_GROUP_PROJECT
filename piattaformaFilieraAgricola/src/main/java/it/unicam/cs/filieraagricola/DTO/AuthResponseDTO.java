package it.unicam.cs.filieraagricola.DTO;
import lombok.Data;

@Data
public class AuthResponseDTO {
    private String token;
    private String username;
    private String ruolo;

    public AuthResponseDTO(String token, String username, String ruolo) {
        this.token = token;
        this.username = username;
        this.ruolo = ruolo;
    }
}