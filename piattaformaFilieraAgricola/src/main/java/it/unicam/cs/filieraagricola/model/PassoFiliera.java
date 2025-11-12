package it.unicam.cs.filieraagricola.model;



import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "passi_filiera")
public class PassoFiliera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- AGGIORNATO ---
    // Abbiamo sostituito 'String nomeFase' con l'enum
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPassoFiliera tipoPasso;
    // ---------------

    private String descrizione; // Es. "Semina grano duro varietà Senatore Cappelli"
    private LocalDateTime dataOra;
    private String luogo; // Es. "Campo A, Località Montefiore"

    @Column(nullable = false)
    private int ordine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "azienda_id", nullable = false)
    private Azienda azienda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tracciabilita_id", nullable = false)
    private TracciabilitaProdotto tracciabilita;

    // Costruttori, Getter e Setter
    // ...
}