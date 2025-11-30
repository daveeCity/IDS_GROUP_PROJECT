package it.unicam.cs.filieraagricola.DTO;

import lombok.Data;
import java.util.List;

@Data
public class PaccoProdottoDTO {
    private Long id;
    private String nome;
    private String descrizione;
    private double prezzo;
    private Long distributoreId;
    private String nomeDistributore;
    private List<ProdottoDTO> prodottiInclusi;
}