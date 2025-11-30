package it.unicam.cs.filieraagricola.model;


import jakarta.persistence.Embeddable;


@Embeddable // Indica a JPA che questa classe può essere inclusa in un'altra entità
public class PosizioneMappa {

    private double latitudine;
    private double longitudine;
    private String indirizzoTestuale;

    public double getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(double latitudine) {
        this.latitudine = latitudine;
    }

    public String getIndirizzoTestuale() {
        return indirizzoTestuale;
    }

    public void setIndirizzoTestuale(String indirizzoTestuale) {
        this.indirizzoTestuale = indirizzoTestuale;
    }

    public double getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(double longitudine) {
        this.longitudine = longitudine;
    }
}