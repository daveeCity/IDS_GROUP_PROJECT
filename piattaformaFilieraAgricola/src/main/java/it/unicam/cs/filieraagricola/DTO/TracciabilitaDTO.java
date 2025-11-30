package it.unicam.cs.filieraagricola.DTO;


import lombok.Data;
import java.util.List;

@Data
public class TracciabilitaDTO {
    private Long id;
    private Long prodottoId;
    private String nomeProdotto;
    private List<PassoFilieraDTO> passiDellaFiliera;
}