package main.java.it.unicam.cs.filieraagricola.model;

import java.time.LocalDateTime;

public class UtenteOspite extends User {
    private String sessionId;
    private LocalDateTime ultimoAccesso;
    private boolean pueVisualizzareCatalogo;

    public UtenteOspite(String sessionId) {
        super(null, "Ospite", "OSPITE");
        this.sessionId = sessionId;
        this.ultimoAccesso = LocalDateTime.now();
        this.pueVisualizzareCatalogo = true;
    }

    public void aggiornaUltimoAccesso() {
        this.ultimoAccesso = LocalDateTime.now();
    }

    public boolean isSessioneValida() {
        return LocalDateTime.now().minusHours(2).isBefore(ultimoAccesso);
    }

    public void visualizzaCatalogo() {
        if (!pueVisualizzareCatalogo) {
            throw new IllegalStateException("Utente ospite non autorizzato a visualizzare il catalogo");
        }
        aggiornaUltimoAccesso();
    }

    // Getters
    public String getSessionId() { return sessionId; }
    public LocalDateTime getUltimoAccesso() { return ultimoAccesso; }
    public boolean isPueVisualizzareCatalogo() { return pueVisualizzareCatalogo; }

    // Setters
    public void setPueVisualizzareCatalogo(boolean pueVisualizzareCatalogo) { 
        this.pueVisualizzareCatalogo = pueVisualizzareCatalogo; 
    }

    @Override
    public String toString() {
        return "UtenteOspite{" +
                "sessionId='" + sessionId + '\'' +
                ", ultimoAccesso=" + ultimoAccesso +
                ", sessioneValida=" + isSessioneValida() +
                '}';
    }
}