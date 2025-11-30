package it.unicam.cs.filieraagricola.DTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PassoFilieraDTO {
    private Long id;
    private String nomeFase;
    private String descrizione;
    private LocalDateTime dataOra;
    private String luogo;
    private int ordine;
    private Long aziendaId;
    private String nomeAzienda;
}