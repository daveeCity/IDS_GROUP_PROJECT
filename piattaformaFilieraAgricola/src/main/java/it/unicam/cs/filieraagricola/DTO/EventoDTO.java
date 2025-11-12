package it.unicam.cs.filieraagricola.DTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventoDTO {
    private Long id;
    private String titolo;
    private String descrizione;
    private LocalDateTime dataOraInizio;
    private LocalDateTime dataOraFine;
    private String luogo;
    private String tipo; // Es. FIERA, VISITA_ GUIDATA
    private String stato; // Es. PIANIFICATO, ANNULLATO
    private Long animatoreId;
    private String nomeAnimatore; // (preso dallo username dell'Animatore)
}