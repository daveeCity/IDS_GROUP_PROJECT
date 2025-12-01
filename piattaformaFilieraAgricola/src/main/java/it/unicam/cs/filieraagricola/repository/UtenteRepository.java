package it.unicam.cs.filieraagricola.repository;
// package it.unicam.cs.filieraagricola.repository;

import it.unicam.cs.filieraagricola.model.StatoAccount;
import it.unicam.cs.filieraagricola.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {

    // Metodo per trovare un utente dal suo username (per il login)
    Optional<Utente> findByUsername(String username);

    // Metodo per trovare un utente dalla sua email (per la registrazione)
    Optional<Utente> findByEmail(String email);

    @Query("SELECT u FROM Utente u WHERE TYPE(u) = :tipo")
    <T extends Utente> List<T> findByRuolo(@Param("tipo") Class<T> tipo);

    List<Utente> findByStatoAccount(StatoAccount statoAccount);
}