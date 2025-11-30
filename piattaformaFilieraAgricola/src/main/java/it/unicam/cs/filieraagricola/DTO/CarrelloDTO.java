package it.unicam.cs.filieraagricola.DTO;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
public class CarrelloDTO {
    private List<ElementoCarrelloDTO> elementi;
    private double totaleCarrello;
}