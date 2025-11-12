package it.unicam.cs.filieraagricola.repository;
// package it.unicam.cs.filieraagricola.repository;

import it.unicam.cs.filieraagricola.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {

    // Metodo per trovare un utente dal suo username (per il login)
    Optional<Utente> findByUsername(String username);

    // Metodo per trovare un utente dalla sua email (per la registrazione)
    Optional<Utente> findByEmail(String email);

    /**
     * Trova tutti gli utenti di un tipo specifico (ruolo) usando il TIPO JPQL.
     * Questo sfrutta l'ereditarietà JPA e il DiscriminatorValue.
     * * Esempio di utilizzo nel service:
     * List<Curatore> curatori = utenteRepository.findByRuolo(Curatore.class);
     * List<Produttore> produttori = utenteRepository.findByRuolo(Produttore.class);
     *
     * @param tipoLaClasse la classe .class dell'entità da cercare (es. Produttore.class)
     * @return Una lista di utenti che corrispondono a quel tipo.
     */
    @Query("SELECT u FROM Utente u WHERE TYPE(u) = :tipo")
    <T extends Utente> List<T> findByRuolo(@Param("tipo") Class<T> tipo);
}