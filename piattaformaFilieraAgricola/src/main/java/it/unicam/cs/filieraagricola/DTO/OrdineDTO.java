package it.unicam.cs.filieraagricola.DTO;

import lombok.Data;

@Data
public class OrdineDTO {
    private Long id;
    private java.time.LocalDateTime dataOrdine;
    private String stato;
    private double totale;
    private List<DettaglioOrdineDTO> dettagli;
}

