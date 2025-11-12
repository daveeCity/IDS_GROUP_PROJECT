package it.unicam.cs.filieraagricola.model;


import jakarta.persistence.Embeddable;

@Embeddable // Indica a JPA che questa classe può essere inclusa in un'altra entità
public class PosizioneMappa {

    private double latitudine;
    private double longitudine;
    private String indirizzoTestuale;

    // Costruttori, Getter e Setter
    // ...
}