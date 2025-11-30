package it.unicam.cs.filieraagricola.model;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("DISTRIBUTORE")
public class Distributore extends Azienda {

    public Distributore() {
        super();
    }

    public Distributore(String username, String email, String password,
                        String nomeAzienda, String partitaIva, String indirizzo, String descrizione) {
        super(username, email, password, nomeAzienda, partitaIva, indirizzo, descrizione);
    }
}