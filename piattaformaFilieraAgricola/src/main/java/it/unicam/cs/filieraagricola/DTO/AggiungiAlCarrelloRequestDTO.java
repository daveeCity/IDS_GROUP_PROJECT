package it.unicam.cs.filieraagricola.DTO;

import lombok.Data;

@Data
public class AggiungiAlCarrelloRequestDTO {
    private Long idOggetto;
    private int quantita;
    private String tipo;
}
