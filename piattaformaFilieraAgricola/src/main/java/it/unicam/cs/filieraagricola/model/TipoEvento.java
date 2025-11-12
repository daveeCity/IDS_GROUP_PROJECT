package it.unicam.cs.filieraagricola.model;

public enum TipoEvento {
    FIERA("Fiera"),
    VISITA_AZIENDALE("Visita Aziendale"),
    TOUR_DEGUSTAZIONE("Tour di Degustazione"),
    PRESENTAZIONE_PRODOTTO("Presentazione Prodotto"),
    WORKSHOP("Workshop"),
    MERCATO_LOCALE("Mercato Locale"),
    FESTIVAL_GASTRONOMICO("Festival Gastronomico");

    private final String displayName;

    TipoEvento(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}