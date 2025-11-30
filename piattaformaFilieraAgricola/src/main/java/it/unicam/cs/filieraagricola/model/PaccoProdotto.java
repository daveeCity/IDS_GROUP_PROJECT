package it.unicam.cs.filieraagricola.model;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "pacchi_prodotto")
public class PaccoProdotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String descrizione;

    @Column(nullable = false)
    private double prezzo;

    @Enumerated(EnumType.STRING)
    private TipoPacco tipoPacco; // <-- Refactored

    // Relazione: Creato da un Distributore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "distributore_id", nullable = false)
    private Distributore distributore; // <-- Usa la nostra nuova entità

    // Relazione: Contiene molti Prodotti
    @ManyToMany
    @JoinTable(
            name = "pacco_prodotto_prodotti",
            joinColumns = @JoinColumn(name = "pacco_id"),
            inverseJoinColumns = @JoinColumn(name = "prodotto_id")
    )
    private List<Prodotto> prodotti; // <-- Usa la nostra nuova entità

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Prodotto> getProdotti() {
        return prodotti;
    }

    public void setProdotti(List<Prodotto> prodotti) {
        this.prodotti = prodotti;
    }

    public Distributore getDistributore() {
        return distributore;
    }

    public void setDistributore(Distributore distributore) {
        this.distributore = distributore;
    }

    public TipoPacco getTipoPacco() {
        return tipoPacco;
    }

    public void setTipoPacco(TipoPacco tipoPacco) {
        this.tipoPacco = tipoPacco;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}