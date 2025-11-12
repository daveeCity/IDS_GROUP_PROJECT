package it.unicam.cs.filieraagricola.repository;
import it.unicam.cs.filieraagricola.model.DettaglioOrdine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DettaglioOrdineRepository extends JpaRepository<DettaglioOrdine, Long> {
    // Generalmente i dettagli ordine si caricano tramite l'Ordine,
    // quindi potrebbero non servire metodi custom qui.
}