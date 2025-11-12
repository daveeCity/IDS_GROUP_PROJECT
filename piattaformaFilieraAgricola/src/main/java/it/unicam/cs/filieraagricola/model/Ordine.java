 package it.unicam.cs.filieraagricola.model;

import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ordini")
public class Ordine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataOrdine;

    @Column(nullable = false)
    private double totale;

    @Enumerated(EnumType.STRING)
    private StatoOrdine stato; // <-- AGGIORNATO (enum)

    // Relazione: Molti ordini appartengono a un Acquirente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acquirente_id", nullable = false)
    private Acquirente acquirente; // <-- AGGIORNATO da Cliente ad Acquirente

    // Relazione: Un ordine ha molti dettagli (le righe dell'ordine)
    // Questa è la classe chiave che implementeremo nel prossimo passo
    @OneToMany(mappedBy = "ordine", cascade = CascadeType.ALL)
    private List<DettaglioOrdine> dettagliOrdine;

    // Costruttori, Getter e Setter

    public Ordine() {
        this.dataOrdine = LocalDateTime.now();
        this.stato = StatoOrdine.PENDENTE;
    }

    // ... getter e setter ...
}