package it.unicam.cs.filieraagricola.repository;
import it.unicam.cs.filieraagricola.model.PassoFiliera;
import it.unicam.cs.filieraagricola.model.StatoApprovazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PassoFilieraRepository extends JpaRepository<PassoFiliera, Long> {

    // Metodo per trovare tutti i passi di una specifica tracciabilità
    List<PassoFiliera> findByTracciabilitaIdOrderByOrdine(Long tracciabilitaId);
    List<PassoFiliera> findByTracciabilitaIdAndStatoApprovazioneOrderByOrdine(Long tracciabilitaId, StatoApprovazione stato);
    List<PassoFiliera> findByStatoApprovazione(StatoApprovazione stato);
}