package main.java.it.unicam.cs.filieraagricola.model;

public enum StatoProdotto {
    BOZZA("In bozza"),
    IN_REVISIONE("In revisione"),
    PUBBLICATO("Pubblicato"),
    RESPINTO("Respinto"),
    ARCHIVIATO("Archiviato");

    private final String descrizione;

    StatoProdotto(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public String toString() {
        return descrizione;
    }
}