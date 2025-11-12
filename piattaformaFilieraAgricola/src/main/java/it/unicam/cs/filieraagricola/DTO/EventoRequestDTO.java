package it.unicam.cs.filieraagricola.DTO;


import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventoRequestDTO {
    private String titolo;
    private String descrizione;
    private LocalDateTime dataOraInizio;
    private LocalDateTime dataOraFine;
    private String luogo;
    private String tipo; // Es. FIERA, VISITA_ GUIDATA
}