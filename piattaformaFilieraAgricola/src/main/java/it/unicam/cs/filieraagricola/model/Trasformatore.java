package main.java.it.unicam.cs.filieraagricola.model;

import java.util.ArrayList;
import java.util.List;

public class Trasformatore extends User {
    private String licenza;
    private List<String> certificazioni;
    private List<Product> prodottiTrasformati;

    public Trasformatore(Long id, String name, String licenza) {
        super(id, name, "TRASFORMATORE");
        this.licenza = licenza;
        this.certificazioni = new ArrayList<>();
        this.prodottiTrasformati = new ArrayList<>();
    }

    public void aggiungiCertificazione(String certificazione) {
        if (!certificazioni.contains(certificazione)) {
            certificazioni.add(certificazione);
        }
    }

    public void aggiungiProdottoTrasformato(Product prodotto) {
        if (!prodottiTrasformati.contains(prodotto)) {
            prodottiTrasformati.add(prodotto);
        }
    }

    public boolean hasCertificazione(String certificazione) {
        return certificazioni.contains(certificazione);
    }

    // Getters
    public String getLicenza() { return licenza; }
    public List<String> getCertificazioni() { return new ArrayList<>(certificazioni); }
    public List<Product> getProdottiTrasformati() { return new ArrayList<>(prodottiTrasformati); }

    // Setters
    public void setLicenza(String licenza) { this.licenza = licenza; }

    @Override
    public String toString() {
        return "Trasformatore{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", licenza='" + licenza + '\'' +
                ", certificazioni=" + certificazioni.size() +
                '}';
    }
}