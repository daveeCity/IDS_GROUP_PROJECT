package it.unicam.cs.filieraagricola.repository;
import it.unicam.cs.filieraagricola.model.Prodotto;
import it.unicam.cs.filieraagricola.model.StatoProdotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {

    // Metodo per trovare prodotti di una specifica azienda
    List<Prodotto> findByAziendaId(Long aziendaId);

    // Metodo per trovare prodotti per stato (per la moderazione)
    List<Prodotto> findByStato(StatoProdotto stato);

    // Metodo per la ricerca (es. "search/products" come da README)
    List<Prodotto> findByNomeContainingIgnoreCase(String nome);
}