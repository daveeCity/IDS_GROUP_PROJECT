 package it.unicam.cs.filieraagricola.model;


 import jakarta.persistence.DiscriminatorValue;
 import jakarta.persistence.Entity;

 @Entity
@DiscriminatorValue("PRODUTTORE")
public class Produttore extends Azienda {

    public Produttore() {
        super();
    }

    public Produttore(String username, String email, String password,
                      String nomeAzienda, String partitaIva, String indirizzo, String descrizione, Double latitudine, Double longitudine) {
        super(username, email, password, nomeAzienda, partitaIva, indirizzo, descrizione, latitudine, longitudine);
    }
}