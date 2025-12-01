package it.unicam.cs.filieraagricola.DTO;
import lombok.Data;

@Data
public class AuthResponseDTO {
    private Long id;
    private String token;
    private String username;
    private String ruolo;

    public AuthResponseDTO(Long id, String token, String username, String ruolo) {
        this.id = id;
        this.token = token;
        this.username = username;
        this.ruolo = ruolo;
    }
}