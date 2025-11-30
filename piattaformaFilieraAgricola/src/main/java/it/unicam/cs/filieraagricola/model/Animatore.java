package it.unicam.cs.filieraagricola.model;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
@DiscriminatorValue("ANIMATORE")
public class Animatore extends Utente {

    // Relazione: Un animatore gestisce molti eventi
    @OneToMany(mappedBy = "animatore")
    private List<Evento> eventiCreati;

    public Animatore() {
        super();
    }

    public Animatore(String username, String email, String password) {
        super(username, email, password);
    }

    public List<Evento> getEventiCreati() {

        return eventiCreati;
    }

    public void setEventiCreati(List<Evento> eventiCreati) {

        this.eventiCreati = eventiCreati;
    }
}