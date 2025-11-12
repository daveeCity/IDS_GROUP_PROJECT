package it.unicam.cs.filieraagricola.repository;



import it.unicam.cs.filieraagricola.model.PaccoProdotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaccoProdottoRepository extends JpaRepository<PaccoProdotto, Long> {

    // Metodo per trovare pacchetti di un Distributore
    List<PaccoProdotto> findByDistributoreId(Long distributoreId);
}