package it.unicam.cs.filieraagricola.DTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PassoFilieraDTO {
    private Long id;
    private String nomeFase; // Es. "Semina", "Trasformazione", "Imbottigliamento"
    private String descrizione;
    private LocalDateTime dataOra;
    private String luogo;
    private int ordine; // Per ordinarli
    private Long aziendaId; // ID dell'azienda che ha eseguito il passo
    private String nomeAzienda;
}