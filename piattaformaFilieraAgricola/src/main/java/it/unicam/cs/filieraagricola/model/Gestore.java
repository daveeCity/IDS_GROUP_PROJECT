package it.unicam.cs.filieraagricola.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("GESTORE")
public class Gestore extends Utente {

    public Gestore() {
        super();
    }

    public Gestore(String username, String email, String password) {
        super(username, email, password);
    }
}