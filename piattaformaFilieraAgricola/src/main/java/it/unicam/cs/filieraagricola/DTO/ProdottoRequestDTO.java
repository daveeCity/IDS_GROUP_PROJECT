package it.unicam.cs.filieraagricola.DTO;
import lombok.Data;

@Data
public class ProdottoRequestDTO {
    private String nome;
    private String descrizione;
    private double prezzo;
    private int quantitaDisponibile;

    public ProdottoRequestDTO(String nome, String descrizione, double prezzo, int quantitaDisponibile) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.quantitaDisponibile = quantitaDisponibile;
    }
}