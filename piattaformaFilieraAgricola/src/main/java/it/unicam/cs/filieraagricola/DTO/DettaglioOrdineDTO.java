package it.unicam.cs.filieraagricola.DTO;

import lombok.Data;

@Data
public class DettaglioOrdineDTO {
    private Long prodottoId;
    private String nomeProdotto;
    private int quantita;
    private double prezzoUnitario;
}
