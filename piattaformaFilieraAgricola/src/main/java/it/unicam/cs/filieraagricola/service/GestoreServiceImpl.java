package it.unicam.cs.filieraagricola.service;

import it.unicam.cs.filieraagricola.DTO.UtenteDTO;
import it.unicam.cs.filieraagricola.model.StatoAccount;
import it.unicam.cs.filieraagricola.model.Utente;
import it.unicam.cs.filieraagricola.model.Azienda;
import it.unicam.cs.filieraagricola.repository.UtenteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GestoreServiceImpl implements GestoreService {

    @Autowired private UtenteRepository utenteRepository;

    @Override
    public List<UtenteDTO> getUtentiDaApprovare() {
        return utenteRepository.findByStatoAccount(StatoAccount.IN_ATTESA)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void approvaUtente(Long id) {
        Utente u = utenteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));
        u.setStatoAccount(StatoAccount.ATTIVO);
        utenteRepository.save(u);
    }

    @Override
    public void rifiutaUtente(Long id) {
        // Possiamo cancellarlo o metterlo in DISABILITATO
        utenteRepository.deleteById(id);
    }

    // Helper mapping veloce
    private UtenteDTO convertToDTO(Utente u) {
        UtenteDTO dto = new UtenteDTO();
        dto.setId(u.getId());
        dto.setUsername(u.getUsername());
        dto.setRuolo(u.getRuoloString()); // O usa getClass().getSimpleName()
        dto.setEmail(u.getEmail());

        if (u instanceof Azienda) {
            dto.setNomeAzienda(((Azienda) u).getNomeAzienda());
        }
        return dto;
    }
}