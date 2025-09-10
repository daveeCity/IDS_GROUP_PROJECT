package main.java.it.unicam.cs.filieraagricola.model;

import java.util.ArrayList;
import java.util.List;

public class Catalogo {
    private Long id;
    private String nome;
    private String descrizione;
    private List<Product> prodotti;
    private StatoProdotto stato;

    public Catalogo(Long id, String nome, String descrizione) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.prodotti = new ArrayList<>();
        this.stato = StatoProdotto.BOZZA;
    }

    public void aggiungiProdotto(Product prodotto) {
        if (!prodotti.contains(prodotto)) {
            prodotti.add(prodotto);
        }
    }

    public void rimuoviProdotto(Product prodotto) {
        prodotti.remove(prodotto);
    }

    public void pubblica() {
        this.stato = StatoProdotto.PUBBLICATO;
    }

    // Getters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getDescrizione() { return descrizione; }
    public List<Product> getProdotti() { return new ArrayList<>(prodotti); }
    public StatoProdotto getStato() { return stato; }

    // Setters
    public void setNome(String nome) { this.nome = nome; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public void setStato(StatoProdotto stato) { this.stato = stato; }

    @Override
    public String toString() {
        return "Catalogo{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", prodotti=" + prodotti.size() +
                ", stato=" + stato +
                '}';
    }
}