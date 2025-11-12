package it.unicam.cs.filieraagricola.model;



import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
@DiscriminatorValue("ACQUIRENTE")
public class Acquirente extends Utente {

    // Relazione con gli ordini (che refattorizzeremo tra poco)
    @OneToMany(mappedBy = "acquirente")
    private List<Ordine> ordini;

    public Acquirente() {
        super();
    }

    public Acquirente(String username, String email, String password) {
        super(username, email, password);
    }

    // ... getter e setter ...
}