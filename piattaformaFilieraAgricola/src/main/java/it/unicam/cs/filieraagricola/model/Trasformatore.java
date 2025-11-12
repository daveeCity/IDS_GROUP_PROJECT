package it.unicam.cs.filieraagricola.model;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("TRASFORMATORE")
public class Trasformatore extends Azienda {

    public Trasformatore() {
        super();
    }

    public Trasformatore(String username, String email, String password,
                         String nomeAzienda, String partitaIva, String indirizzo, String descrizione) {
        super(username, email, password, nomeAzienda, partitaIva, indirizzo, descrizione);
    }
}