package it.unicam.cs.filieraagricola.repository;
import it.unicam.cs.filieraagricola.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    // Metodo per trovare il pagamento associato a un ordine
    Optional<Pagamento> findByOrdineId(Long ordineId);
}