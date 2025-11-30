package it.unicam.cs.filieraagricola.DTO;

import lombok.Data;
import java.util.List;

@Data
public class PaccoProdottoRequestDTO {
    private String nome;
    private String descrizione;
    private double prezzo;
    private List<Long> prodottoIds;
}