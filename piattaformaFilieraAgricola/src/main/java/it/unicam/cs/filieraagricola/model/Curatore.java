package it.unicam.cs.filieraagricola.model;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CURATORE")
public class Curatore extends Utente {

    // Campi specifici (es. per la moderazione)

    public Curatore() {
        super();
    }

    public Curatore(String username, String email, String password) {
        super(username, email, password);
    }
}