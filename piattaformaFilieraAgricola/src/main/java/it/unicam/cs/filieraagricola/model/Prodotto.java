package it.unicam.cs.filieraagricola.model;


import jakarta.persistence.*;


@Entity
@Table(name = "prodotti")
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String descrizione;

    private double prezzo;

    private int quantitaDisponibile;

    // Relazione: Molti prodotti appartengono a una Azienda
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "azienda_id", nullable = false)
    private Azienda azienda; // <-- AGGIORNATO da Company ad Azienda

    // Stato del prodotto per la moderazione
    @Enumerated(EnumType.STRING)
    private StatoProdotto stato; // <-- Utilizza l'enum allineato

    // Costruttori, Getter e Setter

    public Prodotto() {}

    public Prodotto(String nome, String descrizione, double prezzo, int quantitaDisponibile, Azienda azienda) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.quantitaDisponibile = quantitaDisponibile;
        this.azienda = azienda;
        this.stato = StatoProdotto.IN_ATTESA; // Stato di default alla creazione
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatoProdotto getStato() {
        return stato;
    }

    public void setStato(StatoProdotto stato) {
        this.stato = stato;
    }

    public Azienda getAzienda() {
        return azienda;
    }

    public void setAzienda(Azienda azienda) {
        this.azienda = azienda;
    }

    public int getQuantitaDisponibile() {
        return quantitaDisponibile;
    }

    public void setQuantitaDisponibile(int quantitaDisponibile) {
        this.quantitaDisponibile = quantitaDisponibile;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}