package it.unicam.cs.filieraagricola.model;

import jakarta.persistence.*;



import java.time.LocalDateTime;
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

    // Costruttori, Getter e Setter
    // ...
}