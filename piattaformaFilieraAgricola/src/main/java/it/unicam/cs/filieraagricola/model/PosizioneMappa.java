package it.unicam.cs.filieraagricola.model;


import jakarta.persistence.Embeddable;


@Embeddable
public class PosizioneMappa {

    private Double latitudine;
    private Double longitudine;
    private String indirizzoTestuale;

    public Double getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(Double latitudine) {
        this.latitudine = latitudine;
    }

    public String getIndirizzoTestuale() {
        return indirizzoTestuale;
    }

    public void setIndirizzoTestuale(String indirizzoTestuale) {
        this.indirizzoTestuale = indirizzoTestuale;
    }

    public Double getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(Double longitudine) {
        this.longitudine = longitudine;
    }
}