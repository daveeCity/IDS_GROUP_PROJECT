package it.unicam.cs.filieraagricola.model;


import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity

@Table(name = "carrelli")
public class Carrello {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relazione: Un carrello appartiene a un solo Acquirente
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acquirente_id", nullable = false, unique = true)
    private Acquirente acquirente;

    // Relazione: Un carrello ha molti ElementiCarrello (le righe)
    @OneToMany(mappedBy = "carrello", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ElementoCarrello> elementi = new ArrayList<>();

    // Costruttori, Getter e Setter

    public Carrello() {}

    public Carrello(Acquirente acquirente) {
        this.acquirente = acquirente;
    }

    // Metodo di utilità per aggiungere un elemento
    public void aggiungiElemento(ElementoCarrello elemento) {
        elementi.add(elemento);
        elemento.setCarrello(this);
    }

    // Metodo di utilità per rimuovere un elemento
    public void rimuoviElemento(ElementoCarrello elemento) {
        elementi.remove(elemento);
        elemento.setCarrello(null);
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public Acquirente getAcquirente() {

        return acquirente;
    }

    public void setAcquirente(Acquirente acquirente) {

        this.acquirente = acquirente;
    }

    public List<ElementoCarrello> getElementi() {

        return elementi;
    }

    public void setElementi(List<ElementoCarrello> elementi) {

        this.elementi = elementi;
    }

}