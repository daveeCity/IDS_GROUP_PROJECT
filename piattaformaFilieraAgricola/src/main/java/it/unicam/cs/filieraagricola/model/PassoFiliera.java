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

    @Enumerated(EnumType.STRING)
    private StatoApprovazione statoApprovazione = StatoApprovazione.IN_ATTESA;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TracciabilitaProdotto getTracciabilita() {
        return tracciabilita;
    }

    public void setTracciabilita(TracciabilitaProdotto tracciabilita) {
        this.tracciabilita = tracciabilita;
    }

    public Azienda getAzienda() {
        return azienda;
    }

    public void setAzienda(Azienda azienda) {
        this.azienda = azienda;
    }

    public int getOrdine() {
        return ordine;
    }

    public void setOrdine(int ordine) {
        this.ordine = ordine;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public LocalDateTime getDataOra() {
        return dataOra;
    }

    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public TipoPassoFiliera getTipoPasso() {
        return tipoPasso;
    }

    public void setTipoPasso(TipoPassoFiliera tipoPasso) {
        this.tipoPasso = tipoPasso;
    }
    public StatoApprovazione getStatoApprovazione() { return statoApprovazione; }
    public void setStatoApprovazione(StatoApprovazione statoApprovazione) { this.statoApprovazione = statoApprovazione; }
}