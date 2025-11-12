package it.unicam.cs.filieraagricola.model;



import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "tracciabilita_prodotto")
public class TracciabilitaProdotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relazione: Scheda di tracciabilità per un Prodotto
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prodotto_id", nullable = false, unique = true)
    private Prodotto prodotto; // <-- Usa la nostra entità Prodotto

    // Relazione: Ha molti PassiFiliera
    @OneToMany(mappedBy = "tracciabilita", cascade = CascadeType.ALL)
    private List<PassoFiliera> passi;

    // Costruttori, Getter e Setter
    // ...
}