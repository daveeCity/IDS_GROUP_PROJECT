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

    // Costruttori, Getter e Setter

    public Utente() {}

    public Utente(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password; // Verrà codificata dal service
    }

    // ... getter e setter ...
}