package it.unicam.cs.filieraagricola.DTO;

// Usiamo Lombok per ridurre il codice boilerplate (getter/setter)
import lombok.Data;

@Data
public class ProdottoDTO {
    private Long id;
    private String nome;
    private String descrizione;
    private double prezzo;
    private int quantitaDisponibile;
    private String stato; // (IN_ATTESA, APPROVATO, RIFIUTATO)
    private String nomeAzienda; // Dato denormalizzato dall'entità Azienda
    private Long aziendaId;
}