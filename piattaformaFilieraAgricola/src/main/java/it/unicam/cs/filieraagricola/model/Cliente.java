package main.java.it.unicam.cs.filieraagricola.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends User {
    private String email;
    private String indirizzo;
    private LocalDateTime dataRegistrazione;
    private List<Order> ordini;
    private List<Event> eventiPrenotati;

    public Cliente(Long id, String name, String email, String indirizzo) {
        super(id, name, "CLIENTE");
        this.email = email;
        this.indirizzo = indirizzo;
        this.dataRegistrazione = LocalDateTime.now();
        this.ordini = new ArrayList<>();
        this.eventiPrenotati = new ArrayList<>();
    }

    public void aggiungiOrdine(Order ordine) {
        if (!ordini.contains(ordine)) {
            ordini.add(ordine);
        }
    }

    public void prenotaEvento(Event evento) {
        if (!eventiPrenotati.contains(evento)) {
            eventiPrenotati.add(evento);
        }
    }

    public void annullaPrenotazioneEvento(Event evento) {
        eventiPrenotati.remove(evento);
    }

    public boolean haPrenotatoEvento(Event evento) {
        return eventiPrenotati.contains(evento);
    }

    // Getters
    public String getEmail() { return email; }
    public String getIndirizzo() { return indirizzo; }
    public LocalDateTime getDataRegistrazione() { return dataRegistrazione; }
    public List<Order> getOrdini() { return new ArrayList<>(ordini); }
    public List<Event> getEventiPrenotati() { return new ArrayList<>(eventiPrenotati); }

    // Setters
    public void setEmail(String email) { this.email = email; }
    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", email='" + email + '\'' +
                ", ordini=" + ordini.size() +
                ", eventi=" + eventiPrenotati.size() +
                '}';
    }
}