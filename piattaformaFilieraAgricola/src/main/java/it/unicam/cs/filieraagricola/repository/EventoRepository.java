package it.unicam.cs.filieraagricola.repository;
import it.unicam.cs.filieraagricola.model.Evento;
import it.unicam.cs.filieraagricola.model.TipoEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    // Metodo per trovare eventi per tipo
    List<Evento> findByTipo(TipoEvento tipo);

    // Metodo per trovare eventi creati da un Animatore
    List<Evento> findByAnimatoreId(Long animatoreId);
}