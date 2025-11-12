package it.unicam.cs.filieraagricola.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public abstract class Azienda extends Utente {

    @Column(name = "nome_azienda")
    private String nomeAzienda;

    @Column(name = "partita_iva", unique = true)
    private String partitaIva;

    private String indirizzo;

    private String descrizione;

    // Relazione: Un'azienda ha molti prodotti
    // "azienda" è il nome del campo nella classe Prodotto
    @OneToMany(mappedBy = "azienda")
    private List<Prodotto> prodotti;

    // Costruttori, Getter e Setter

    public Azienda() {
        super();
    }

    public Azienda(String username, String email, String password,
                   String nomeAzienda, String partitaIva, String indirizzo, String descrizione) {
        super(username, email, password);
        this.nomeAzienda = nomeAzienda;
        this.partitaIva = partitaIva;
        this.indirizzo = indirizzo;
        this.descrizione = descrizione;
    }

    // ... getter e setter ...
}