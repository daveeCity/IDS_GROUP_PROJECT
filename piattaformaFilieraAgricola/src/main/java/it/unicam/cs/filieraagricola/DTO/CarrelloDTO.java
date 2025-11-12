package it.unicam.cs.filieraagricola.DTO;


import lombok.Data;

import java.util.List;

@Data
public class CarrelloDTO {
    private List<ElementoCarrelloDTO> elementi;
    private double totaleCarrello;
}