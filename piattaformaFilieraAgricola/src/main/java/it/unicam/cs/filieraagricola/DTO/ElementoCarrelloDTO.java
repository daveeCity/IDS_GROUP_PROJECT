package it.unicam.cs.filieraagricola.DTO;

import lombok.Data;

@Data
public class ElementoCarrelloDTO {
    private Long id;
    private Long prodottoId;
    private String nomeProdotto;
    private int quantita;
    private double prezzoUnitario;
    private double totaleElemento;
}
