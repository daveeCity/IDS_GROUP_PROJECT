package it.unicam.cs.filieraagricola.model;

import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "eventi")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titolo;
    private String descrizione;
    private LocalDateTime dataOraInizio;
    private LocalDateTime dataOraFine;
    private String luogo;
    private double latitudine;
    private double longitudine;

    @Enumerated(EnumType.STRING)
    private TipoEvento tipo; // <-- Refactored

    @Enumerated(EnumType.STRING)
    private StatoEvento stato; // <-- Refactored

    // Relazione: Creato da un Animatore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animatore_id", nullable = false)
    private Animatore animatore; // <-- Usa la nostra nuova entità

    // Relazione: Molti Acquirenti partecipano a molti Eventi
    @ManyToMany
    @JoinTable(
            name = "evento_partecipanti",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "acquirente_id")
    )
    private Set<Acquirente> partecipanti;

    @ManyToMany
    @JoinTable(
            name = "evento_aziende",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "azienda_id")
    )
    private Set<Azienda> aziendeInvitate = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public LocalDateTime getDataOraInizio() {
        return dataOraInizio;
    }

    public void setDataOraInizio(LocalDateTime dataOraInizio) {
        this.dataOraInizio = dataOraInizio;
    }

    public LocalDateTime getDataOraFine() {
        return dataOraFine;
    }

    public void setDataOraFine(LocalDateTime dataOraFine) {
        this.dataOraFine = dataOraFine;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public TipoEvento getTipo() {
        return tipo;
    }

    public void setTipo(TipoEvento tipo) {
        this.tipo = tipo;
    }

    public StatoEvento getStato() {
        return stato;
    }

    public void setStato(StatoEvento stato) {
        this.stato = stato;
    }

    public Animatore getAnimatore() {
        return animatore;
    }

    public void setAnimatore(Animatore animatore) {
        this.animatore = animatore;
    }

    public Set<Acquirente> getPartecipanti() {
        return partecipanti;
    }

    public void setPartecipanti(Set<Acquirente> partecipanti) {
        this.partecipanti = partecipanti;
    }

    public Set<Azienda> getAziendeInvitate() {
        return aziendeInvitate;
    }

    public void setAziendeInvitate(Set<Azienda> aziendeInvitate) {
        this.aziendeInvitate = aziendeInvitate;
    }

    public double getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(double latitudine) {
        this.latitudine = latitudine;
    }

    public double getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(double longitudine) {
        this.longitudine = longitudine;
    }
}