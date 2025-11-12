package it.unicam.cs.filieraagricola.model;


public enum TipoPassoFiliera {
    SEEDING("Semina"),
    GROWING("Coltivazione"),
    HARVESTING("Raccolta"),
    PROCESSING("Lavorazione"),
    TRANSFORMATION("Trasformazione"),
    PACKAGING("Confezionamento"),
    STORAGE("Stoccaggio"),
    TRANSPORT("Trasporto"),
    DISTRIBUTION("Distribuzione"),
    RETAIL("Vendita al dettaglio");

    private final String displayName;

    TipoPassoFiliera(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}