package it.unicam.cs.filieraagricola.repository;
import it.unicam.cs.filieraagricola.model.Ordine;
import it.unicam.cs.filieraagricola.model.StatoOrdine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrdineRepository extends JpaRepository<Ordine, Long> {

    // Metodo per trovare tutti gli ordini di un acquirente
    List<Ordine> findByAcquirenteId(Long acquirenteId);

    // Metodo per trovare ordini per stato
    List<Ordine> findByStato(StatoOrdine stato);
}