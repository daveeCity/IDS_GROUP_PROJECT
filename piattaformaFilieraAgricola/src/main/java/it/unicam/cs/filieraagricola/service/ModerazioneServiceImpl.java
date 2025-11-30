package it.unicam.cs.filieraagricola.service;

import it.unicam.cs.filieraagricola.DTO.ModerazioneRequestDTO;
import it.unicam.cs.filieraagricola.DTO.ProdottoDTO;
import it.unicam.cs.filieraagricola.mapper.ProdottoMapper;
import it.unicam.cs.filieraagricola.model.Prodotto;
import it.unicam.cs.filieraagricola.model.StatoProdotto;
import it.unicam.cs.filieraagricola.observer.ModerationObserver;
import it.unicam.cs.filieraagricola.observer.ModerationSubject;
import it.unicam.cs.filieraagricola.repository.ProdottoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
public class ModerazioneServiceImpl implements ModerazioneService, ModerationSubject {

    @Autowired
    private ProdottoRepository prodottoRepository;

    // Mapper DTO (potremmo spostarlo in una classe 'Mapper' dedicata)
    @Autowired
    private ProdottoMapper prodottoMapper; // Assumiamo di avere un mapper

    // Lista degli observer (dal codice originale)
    private final List<ModerationObserver> observers = new ArrayList<>();

    // --- Implementazione ModerationService ---

    @Override
    public List<ProdottoDTO> getProdottiInAttesa() {
        return prodottoRepository.findByStato(StatoProdotto.IN_ATTESA)
                .stream()
                .map(prodottoMapper::toDTO) // Usa il mapper
                .collect(Collectors.toList());
    }

    @Override
    public ProdottoDTO approvaProdotto(Long prodottoId) {
        Prodotto prodotto = findProdottoById(prodottoId);

        prodotto.setStato(StatoProdotto.APPROVATO); // <-- AGGIORNATO (uso Enum)

        Prodotto salvato = prodottoRepository.save(prodotto);

        // Notifica gli observer
        notifyObservers(salvato, "approvato");

        return prodottoMapper.toDTO(salvato);
    }

    @Override
    public ProdottoDTO rifiutaProdotto(Long prodottoId, ModerazioneRequestDTO request) {
        Prodotto prodotto = findProdottoById(prodottoId);

        prodotto.setStato(StatoProdotto.RIFIUTATO); // <-- AGGIORNATO (uso Enum)

        Prodotto salvato = prodottoRepository.save(prodotto);

        // Notifica gli observer
        notifyObservers(salvato, "rifiutato con motivazione: " + request.getMotivazione());

        return prodottoMapper.toDTO(salvato);
    }

    // --- Implementazione ModerationSubject (dal codice originale) ---

    @Override
    public void addObserver(ModerationObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ModerationObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Prodotto prodotto, String action) {
        for (ModerationObserver observer : observers) {
            observer.update(prodotto, action);
        }
    }

    // --- Metodo privato di utilità ---

    private Prodotto findProdottoById(Long id) {
        return prodottoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prodotto non trovato con ID: " + id));
    }
}

/*
Nota: Ho presunto l'esistenza di un 'ProdottoMapper' per convertire Prodotto <-> ProdottoDTO,
come abbiamo fatto manualmente in ProdottoServiceImpl. È una best practice.
*/