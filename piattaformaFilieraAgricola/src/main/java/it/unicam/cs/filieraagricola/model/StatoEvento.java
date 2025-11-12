package it.unicam.cs.filieraagricola.model;

public enum StatoEvento {
    PLANNED("Pianificato"),
    OPEN_REGISTRATION("Registrazioni Aperte"),
    REGISTRATION_CLOSED("Registrazioni Chiuse"),
    IN_PROGRESS("In Corso"),
    COMPLETED("Completato"),
    CANCELLED("Annullato"),
    POSTPONED("Rinviato");

    private final String displayName;

    StatoEvento(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}