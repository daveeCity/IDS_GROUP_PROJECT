package it.unicam.cs.filieraagricola.repository;
import it.unicam.cs.filieraagricola.model.TracciabilitaProdotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TracciabilitaProdottoRepository extends JpaRepository<TracciabilitaProdotto, Long> {

    // Metodo per trovare la tracciabilità associata a un prodotto
    Optional<TracciabilitaProdotto> findByProdottoId(Long prodottoId);
}