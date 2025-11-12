package it.unicam.cs.filieraagricola.repository;

import it.unicam.cs.filieraagricola.model.ElementoCarrello;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ElementoCarrelloRepository extends JpaRepository<ElementoCarrello, Long> {

    // Metodo per trovare un elemento specifico nel carrello
    Optional<ElementoCarrello> findByCarrelloIdAndProdottoId(Long carrelloId, Long prodottoId);
}