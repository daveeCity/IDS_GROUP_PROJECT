package it.unicam.cs.filieraagricola.DTO;
import lombok.Data;

@Data
public class ProdottoRequestDTO {
    // L'ID non serve, è una creazione
    private String nome;
    private String descrizione;
    private double prezzo;
    private int quantitaDisponibile;
    // L'aziendaId verrà passato dal Controller in base all'utente autenticato
}