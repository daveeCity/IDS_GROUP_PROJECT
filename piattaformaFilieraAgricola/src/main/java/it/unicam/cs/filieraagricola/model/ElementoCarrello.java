package it.unicam.cs.filieraagricola.model;


import jakarta.persistence.*;


@Entity
@Table(name = "elementi_carrello")
public class ElementoCarrello {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relazione: Molti elementi appartengono a un Carrello
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrello_id", nullable = false)
    private Carrello carrello;

    // Relazione: Molti elementi puntano a un Prodotto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prodotto_id", nullable = false)
    private Prodotto prodotto;

    @Column(nullable = false)
    private int quantita;

    // Costruttori, Getter e Setter

    public ElementoCarrello() {}

    public ElementoCarrello(Carrello carrello, Prodotto prodotto, int quantita) {
        this.carrello = carrello;
        this.prodotto = prodotto;
        this.quantita = quantita;
    }

    // ... getter e setter ...
}