package it.unicam.cs.filieraagricola.DTO;

import lombok.Data;

@Data
public class ProdottoDTO {
    private Long id;
    private String nome;
    private String descrizione;
    private double prezzo;
    private int quantitaDisponibile;
    private String stato;
    private String nomeAzienda;
    private Long aziendaId;
}