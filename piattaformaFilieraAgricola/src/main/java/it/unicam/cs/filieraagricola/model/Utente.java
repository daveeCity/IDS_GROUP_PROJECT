package it.unicam.cs.filieraagricola.model;

import jakarta.persistence.*;


@Entity
@Table(name = "utenti")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ruolo", discriminatorType = DiscriminatorType.STRING)
public abstract class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // Questo campo legge il valore della colonna discriminatore ma non lo scrive (gestito da Hibernate)
    @Column(name = "ruolo", insertable = false, updatable = false)
    private String ruoloString;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoAccount statoAccount;

    public StatoAccount getStatoAccount() {
        return statoAccount;
    }

    public void setStatoAccount(StatoAccount statoAccount) {
        this.statoAccount = statoAccount;
    }

    public Utente() {}

    public Utente(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password; // Verrà codificata dal service
    }

    public String getRuoloString() {
        return ruoloString;
    }

    public void setRuoloString(String ruoloString) {
        this.ruoloString = ruoloString;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}