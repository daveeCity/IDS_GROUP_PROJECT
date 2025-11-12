package it.unicam.cs.filieraagricola.model;


import jakarta.persistence.*;

@Entity
@Table(name = "prodotti")
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String descrizione;

    private double prezzo;

    private int quantitaDisponibile;

    // Relazione: Molti prodotti appartengono a una Azienda
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "azienda_id", nullable = false)
    private Azienda azienda; // <-- AGGIORNATO da Company ad Azienda

    // Stato del prodotto per la moderazione
    @Enumerated(EnumType.STRING)
    private StatoProdotto stato; // <-- Utilizza l'enum allineato

    // Costruttori, Getter e Setter

    public Prodotto() {}

    public Prodotto(String nome, String descrizione, double prezzo, int quantitaDisponibile, Azienda azienda) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.quantitaDisponibile = quantitaDisponibile;
        this.azienda = azienda;
        this.stato = StatoProdotto.IN_ATTESA; // Stato di default alla creazione
    }

    // ... getter e setter ...
}