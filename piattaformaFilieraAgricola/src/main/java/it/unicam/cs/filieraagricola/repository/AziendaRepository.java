package it.unicam.cs.filieraagricola.repository;

import it.unicam.cs.filieraagricola.model.Azienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AziendaRepository extends JpaRepository<Azienda, Long> {
    
    List<Azienda> findByRole(String role);
    
    List<Azienda> findByLocation(String location);
    
    Optional<Azienda> findByNameIgnoreCase(String name);
    
    List<Azienda> findByNameContainingIgnoreCase(String name);
}