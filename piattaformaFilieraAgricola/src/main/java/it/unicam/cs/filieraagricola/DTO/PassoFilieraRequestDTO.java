package it.unicam.cs.filieraagricola.DTO;


import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PassoFilieraRequestDTO {
    private String nomeFase;
    private String descrizione;
    private LocalDateTime dataOra;
    private String luogo;
}