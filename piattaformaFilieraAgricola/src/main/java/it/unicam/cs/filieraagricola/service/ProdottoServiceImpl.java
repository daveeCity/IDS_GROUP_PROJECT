package it.unicam.cs.filieraagricola.service;

// package it.unicam.cs.filieraagricola.service;

import it.unicam.cs.filieraagricola.DTO.ProdottoDTO;
import it.unicam.cs.filieraagricola.DTO.ProdottoRequestDTO;
import it.unicam.cs.filieraagricola.model.Azienda;
import it.unicam.cs.filieraagricola.model.Prodotto;
import it.unicam.cs.filieraagricola.model.StatoProdotto;
import it.unicam.cs.filieraagricola.repository.ProdottoRepository;
import it.unicam.cs.filieraagricola.repository.UtenteRepository; // Usiamo questo per trovare l'azienda
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdottoServiceImpl implements ProdottoService {

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Autowired
    private UtenteRepository utenteRepository; // Per trovare l'Azienda

    // --- METODI PUBBLICI ---

    @Override
    public List<ProdottoDTO> getCatalogoProdotti() {
        return prodottoRepository.findByStato(StatoProdotto.APPROVATO)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProdottoDTO getProdottoById(Long id) {
        Prodotto prodotto = findProdottoById(id);
        return convertToDTO(prodotto);
    }

    @Override
    public List<ProdottoDTO> cercaProdottiPerNome(String nome) {
        return prodottoRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .filter(p -> p.getStato() == StatoProdotto.APPROVATO) // Mostra solo approvati
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProdottoDTO> getProdottiByAziendaId(Long aziendaId) {
        return prodottoRepository.findByAziendaId(aziendaId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProdottoDTO creaProdotto(ProdottoRequestDTO request, Long aziendaId) {
        // 1. Trova l'Azienda
        Azienda azienda = (Azienda) utenteRepository.findById(aziendaId)
                .orElseThrow(() -> new EntityNotFoundException("Azienda non trovata con ID: " + aziendaId));

        // 2. Crea l'entità
        Prodotto prodotto = new Prodotto(
                request.getNome(),
                request.getDescrizione(),
                request.getPrezzo(),
                request.getQuantitaDisponibile(),
                azienda
        );
        // Lo stato (IN_ATTESA) è già impostato dal costruttore di Prodotto

        // 3. Salva
        Prodotto salvato = prodottoRepository.save(prodotto);

        // 4. Ritorna il DTO
        return convertToDTO(salvato);
    }

    @Override
    public ProdottoDTO updateProdotto(Long prodottoId, ProdottoRequestDTO request) {
        Prodotto prodotto = findProdottoById(prodottoId);

        prodotto.setNome(request.getNome());
        prodotto.setDescrizione(request.getDescrizione());
        prodotto.setPrezzo(request.getPrezzo());
        prodotto.setQuantitaDisponibile(request.getQuantitaDisponibile());
        prodotto.setStato(StatoProdotto.IN_ATTESA); // Ogni modifica richiede ri-approvazione

        Prodotto aggiornato = prodottoRepository.save(prodotto);
        return convertToDTO(aggiornato);
    }

    @Override
    public void deleteProdotto(Long prodottoId) {
        Prodotto prodotto = findProdottoById(prodottoId);
        prodottoRepository.delete(prodotto);
    }

    // --- METODI PRIVATI DI UTILITÀ ---

    private Prodotto findProdottoById(Long id) {
        return prodottoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prodotto non trovato con ID: " + id));
    }

    // Mapper manuale da Entità a DTO
    private ProdottoDTO convertToDTO(Prodotto prodotto) {
        ProdottoDTO dto = new ProdottoDTO();
        dto.setId(prodotto.getId());
        dto.setNome(prodotto.getNome());
        dto.setDescrizione(prodotto.getDescrizione());
        dto.setPrezzo(prodotto.getPrezzo());
        dto.setQuantitaDisponibile(prodotto.getQuantitaDisponibile());
        dto.setStato(prodotto.getStato().name());
        dto.setAziendaId(prodotto.getAzienda().getId());
        dto.setNomeAzienda(prodotto.getAzienda().getNomeAzienda());
        return dto;
    }
}