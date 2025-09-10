package main.java.it.unicam.cs.filieraagricola.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Curatore extends User {
    private String settoreSpecializzazione;
    private List<Long> prodottiApprovati;
    private List<Long> prodottiRespinti;

    public Curatore(Long id, String name, String settoreSpecializzazione) {
        super(id, name, "CURATORE");
        this.settoreSpecializzazione = settoreSpecializzazione;
        this.prodottiApprovati = new ArrayList<>();
        this.prodottiRespinti = new ArrayList<>();
    }

    public void approvaProdotto(Long prodottoId) {
        if (!prodottiApprovati.contains(prodottoId)) {
            prodottiApprovati.add(prodottoId);
            prodottiRespinti.remove(prodottoId);
        }
    }

    public void respingiProdotto(Long prodottoId, String motivo) {
        if (!prodottiRespinti.contains(prodottoId)) {
            prodottiRespinti.add(prodottoId);
            prodottiApprovati.remove(prodottoId);
        }
    }

    public boolean haApprovato(Long prodottoId) {
        return prodottiApprovati.contains(prodottoId);
    }

    public boolean haRespinto(Long prodottoId) {
        return prodottiRespinti.contains(prodottoId);
    }

    // Getters
    public String getSettoreSpecializzazione() { return settoreSpecializzazione; }
    public List<Long> getProdottiApprovati() { return new ArrayList<>(prodottiApprovati); }
    public List<Long> getProdottiRespinti() { return new ArrayList<>(prodottiRespinti); }

    // Setters
    public void setSettoreSpecializzazione(String settoreSpecializzazione) { 
        this.settoreSpecializzazione = settoreSpecializzazione; 
    }

    @Override
    public String toString() {
        return "Curatore{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", settore='" + settoreSpecializzazione + '\'' +
                ", approvati=" + prodottiApprovati.size() +
                ", respinti=" + prodottiRespinti.size() +
                '}';
    }
}