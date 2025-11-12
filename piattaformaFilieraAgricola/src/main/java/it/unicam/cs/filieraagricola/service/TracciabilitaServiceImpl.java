package it.unicam.cs.filieraagricola.service;

// package it.unicam.cs.filieraagricola.service;

import it.unicam.cs.filieraagricola.DTO.PassoFilieraDTO;
import it.unicam.cs.filieraagricola.DTO.PassoFilieraRequestDTO;
import it.unicam.cs.filieraagricola.DTO.TracciabilitaDTO;
import it.unicam.cs.filieraagricola.model.*; // Importa modelli
import it.unicam.cs.filieraagricola.repository.PassoFilieraRepository;
import it.unicam.cs.filieraagricola.repository.ProdottoRepository;
import it.unicam.cs.filieraagricola.repository.TracciabilitaProdottoRepository;
import it.unicam.cs.filieraagricola.repository.UtenteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TracciabilitaServiceImpl implements TracciabilitaService {

    @Autowired private TracciabilitaProdottoRepository tracciabilitaRepository;
    @Autowired private PassoFilieraRepository passoFilieraRepository;
    @Autowired private ProdottoRepository prodottoRepository;
    @Autowired private UtenteRepository utenteRepository;

    @Override
    public TracciabilitaDTO getTracciabilitaByProdotto(Long prodottoId) {
        TracciabilitaProdotto traccia = findOrCreateTracciabilita(prodottoId);
        return convertToDTO(traccia);
    }

    @Override
    public TracciabilitaDTO aggiungiPassoFiliera(Long prodottoId, Long aziendaId, PassoFilieraRequestDTO request) {

        // 1. Trova l'azienda (Produttore o Trasformatore)
        Azienda azienda = (Azienda) utenteRepository.findById(aziendaId)
                .filter(u -> u instanceof Produttore || u instanceof Trasformatore)
                .orElseThrow(() -> new EntityNotFoundException("Azienda non trovata o non autorizzata"));

        // 2. Trova (o crea) la scheda di tracciabilità per il prodotto
        TracciabilitaProdotto traccia = findOrCreateTracciabilita(prodottoId);

        // 3. Determina l'ordine del nuovo passo
        int ordine = traccia.getPassi().stream()
                .mapToInt(PassoFiliera::getOrdine)
                .max()
                .orElse(0) + 1;

        // 4. Crea il nuovo PassoFiliera
        PassoFiliera passo = new PassoFiliera();
        passo.setNomeFase(request.getNomeFase());
        passo.setDescrizione(request.getDescrizione());
        passo.setDataOra(request.getDataOra());
        passo.setLuogo(request.getLuogo());
        passo.setOrdine(ordine);
        passo.setAzienda(azienda);
        passo.setTracciabilita(traccia);

        passoFilieraRepository.save(passo);

        // Ricarica la tracciabilità per avere la lista aggiornata
        TracciabilitaProdotto tracciaAggiornata = tracciabilitaRepository.findById(traccia.getId()).get();
        return convertToDTO(tracciaAggiornata);
    }

    // --- Metodi Privati di Utilità ---

    private TracciabilitaProdotto findOrCreateTracciabilita(Long prodottoId) {
        // Cerca se esiste già una tracciabilità per questo prodotto
        return tracciabilitaRepository.findByProdottoId(prodottoId)
                .orElseGet(() -> {
                    // Se non esiste, la crea
                    Prodotto prodotto = prodottoRepository.findById(prodottoId)
                            .orElseThrow(() -> new EntityNotFoundException("Prodotto non trovato"));

                    TracciabilitaProdotto nuovaTraccia = new TracciabilitaProdotto();
                    nuovaTraccia.setProdotto(prodotto);
                    return tracciabilitaRepository.save(nuovaTraccia);
                });
    }

    private TracciabilitaDTO convertToDTO(TracciabilitaProdotto traccia) {
        TracciabilitaDTO dto = new TracciabilitaDTO();
        dto.setId(traccia.getId());
        dto.setProdottoId(traccia.getProdotto().getId());
        dto.setNomeProdotto(traccia.getProdotto().getNome());

        // Ordina i passi
        List<PassoFilieraDTO> passiDTO = traccia.getPassi().stream()
                .sorted(Comparator.comparingInt(PassoFiliera::getOrdine))
                .map(this::convertPassoToDTO)
                .collect(Collectors.toList());

        dto.setPassiDellaFiliera(passiDTO);
        return dto;
    }

    private PassoFilieraDTO convertPassoToDTO(PassoFiliera passo) {
        PassoFilieraDTO dto = new PassoFilieraDTO();
        dto.setId(passo.getId());
        dto.setNomeFase(passo.getNomeFase());
        dto.setDescrizione(passo.getDescrizione());
        dto.setDataOra(passo.getDataOra());
        dto.setLuogo(passo.getLuogo());
        dto.setOrdine(passo.getOrdine());
        dto.setAziendaId(passo.getAzienda().getId());
        dto.setNomeAzienda(passo.getAzienda().getNomeAzienda());
        return dto;
    }
}