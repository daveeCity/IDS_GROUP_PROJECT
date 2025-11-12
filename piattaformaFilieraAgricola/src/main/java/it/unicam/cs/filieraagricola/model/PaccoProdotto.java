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

    // Costruttori, Getter e Setter
    // ...
}