package it.unicam.cs.filieraagricola.DTO;

import lombok.Data;

@Data
public class AggiungiAlCarrelloRequestDTO {
    private Long prodottoId;
    private int quantita;
}
