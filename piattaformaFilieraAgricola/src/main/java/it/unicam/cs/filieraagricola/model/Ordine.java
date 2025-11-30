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
        this.stato = StatoOrdine.IN_ATTESA;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<DettaglioOrdine> getDettagliOrdine() {
        return dettagliOrdine;
    }

    public void setDettagliOrdine(List<DettaglioOrdine> dettagliOrdine) {
        this.dettagliOrdine = dettagliOrdine;
    }

    public Acquirente getAcquirente() {
        return acquirente;
    }

    public void setAcquirente(Acquirente acquirente) {
        this.acquirente = acquirente;
    }

    public StatoOrdine getStato() {
        return stato;
    }

    public void setStato(StatoOrdine stato) {
        this.stato = stato;
    }

    public double getTotale() {
        return totale;
    }

    public void setTotale(double totale) {
        this.totale = totale;
    }

    public LocalDateTime getDataOrdine() {
        return dataOrdine;
    }

    public void setDataOrdine(LocalDateTime dataOrdine) {
        this.dataOrdine = dataOrdine;
    }
}