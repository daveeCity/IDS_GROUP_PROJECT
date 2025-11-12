package it.unicam.cs.filieraagricola.model;



import jakarta.persistence.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "pagamenti")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relazione: Un pagamento è associato a un Ordine
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordine_id", nullable = false, unique = true)
    private Ordine ordine;

    @Column(nullable = false)
    private double importo;

    @Column(nullable = false)
    private LocalDateTime dataPagamento;

    @Enumerated(EnumType.STRING)
    private TipoPagamento tipoPagamento; // Es. CARTA_CREDITO, PAYPAL

    @Column(nullable = false)
    private String esito; // Es. "APPROVATO", "RIFIUTATO"

    // Costruttori, Getter e Setter

    public Pagamento() {
        this.dataPagamento = LocalDateTime.now();
    }

    public Pagamento(Ordine ordine, double importo, TipoPagamento tipoPagamento, String esito) {
        this();
        this.ordine = ordine;
        this.importo = importo;
        this.tipoPagamento = tipoPagamento;
        this.esito = esito;
    }

    // ... getter e setter ...
}