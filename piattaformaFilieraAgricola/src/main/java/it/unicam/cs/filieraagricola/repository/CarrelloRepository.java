package it.unicam.cs.filieraagricola.repository;
import it.unicam.cs.filieraagricola.model.Carrello;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CarrelloRepository extends JpaRepository<Carrello, Long> {

    // Metodo per trovare il carrello di uno specifico acquirente
    Optional<Carrello> findByAcquirenteId(Long acquirenteId);
}