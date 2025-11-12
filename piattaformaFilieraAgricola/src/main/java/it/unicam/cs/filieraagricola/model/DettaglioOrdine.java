package it.unicam.cs.filieraagricola.model;


import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

@Entity
@Table(name = "dettagli_ordine")
public class DettaglioOrdine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relazione: Molti dettagli appartengono a un Ordine
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordine_id", nullable = false)
    private Ordine ordine;

    // Relazione: Molti dettagli puntano a un Prodotto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prodotto_id", nullable = false)
    private Prodotto prodotto;

    @Column(nullable = false)
    private int quantita;

    // Memorizza il prezzo al momento dell'acquisto,
    // per evitare che future modifiche al prezzo del prodotto
    // alterino gli ordini storici.
    @Column(nullable = false)
    private double prezzoUnitario;

    // Costruttori, Getter e Setter

    public DettaglioOrdine() {}

    public DettaglioOrdine(Ordine ordine, Prodotto prodotto, int quantita) {
        this.ordine = ordine;
        this.prodotto = prodotto;
        this.quantita = quantita;
        this.prezzoUnitario = prodotto.getPrezzo(); // Fissa il prezzo
    }

    // ... getter e setter ...
}