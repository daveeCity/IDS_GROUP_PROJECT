package it.unicam.cs.filieraagricola.model;


import jakarta.persistence.*;

@Entity
@Table(name = "dettagli_ordine")
public class DettaglioOrdine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relazione: molti dettagli appartengono a un Ordine
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordine_id", nullable = false)
    private Ordine ordine;

    // Relazione: molti dettagli puntano a un Prodotto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prodotto_id")
    private Prodotto prodotto;

    @Column(nullable = false)
    private int quantita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pacco_id")
    private PaccoProdotto pacco;

    // Memorizza il prezzo al momento dell'acquisto,
    // per evitare che future modifiche al prezzo del prodotto
    // alterino gli ordini storici.
    @Column(nullable = false)
    private double prezzoUnitario;


    public DettaglioOrdine() {}

    public DettaglioOrdine(Ordine ordine, int quantita, double prezzoEffettivo) {
        this.ordine = ordine;
        this.quantita = quantita;
        this.prezzoUnitario = prezzoEffettivo;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public Ordine getOrdine() {

        return ordine;
    }

    public void setOrdine(Ordine ordine) {

        this.ordine = ordine;
    }

    public Prodotto getProdotto() {

        return prodotto;
    }

    public void setProdotto(Prodotto prodotto) {

        this.prodotto = prodotto;
    }

    public int getQuantita() {

        return quantita;
    }

    public void setQuantita(int quantita) {

        this.quantita = quantita;
    }

    public double getPrezzoUnitario() {

        return prezzoUnitario;
    }

    public void setPrezzoUnitario(double prezzoUnitario) {

        this.prezzoUnitario = prezzoUnitario;
    }

    public PaccoProdotto getPacco() {

        return pacco;
    }

    public void setPacco(PaccoProdotto pacco) {

        this.pacco = pacco;
    }
}