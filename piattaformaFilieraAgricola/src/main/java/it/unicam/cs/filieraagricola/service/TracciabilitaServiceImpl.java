package it.unicam.cs.filieraagricola.service;

import it.unicam.cs.filieraagricola.DTO.PassoFilieraDTO;
import it.unicam.cs.filieraagricola.DTO.PassoFilieraRequestDTO;
import it.unicam.cs.filieraagricola.DTO.TracciabilitaDTO;
import it.unicam.cs.filieraagricola.model.*;
import it.unicam.cs.filieraagricola.repository.PassoFilieraRepository;
import it.unicam.cs.filieraagricola.repository.ProdottoRepository;
import it.unicam.cs.filieraagricola.repository.TracciabilitaProdottoRepository;
import it.unicam.cs.filieraagricola.repository.UtenteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
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
    @Transactional // Importante per gestire le relazioni
    public TracciabilitaDTO aggiungiPassoFiliera(Long prodottoId, Long aziendaId, PassoFilieraRequestDTO request) {

        // 1. Trova l'azienda (Produttore o Trasformatore)
        Azienda azienda = (Azienda) utenteRepository.findById(aziendaId)
                .filter(u -> u instanceof Produttore || u instanceof Trasformatore)
                .orElseThrow(() -> new EntityNotFoundException("Azienda non trovata o non autorizzata"));

        // 2. Trova (o crea) la scheda di tracciabilità per il prodotto
        TracciabilitaProdotto traccia = findOrCreateTracciabilita(prodottoId);

        // FIX: Inizializza la lista se è null (evita NullPointerException)
        if (traccia.getPassi() == null) {
            traccia.setPassi(new ArrayList<>());
        }

        // 3. Determina l'ordine del nuovo passo
        int ordine = traccia.getPassi().stream()
                .mapToInt(PassoFiliera::getOrdine)
                .max()
                .orElse(0) + 1;

        // 4. Crea il nuovo PassoFiliera
        PassoFiliera passo = new PassoFiliera();

        // FIX: Conversione da Stringa (DTO) a Enum (Model)
        try {
            passo.setTipoPasso(TipoPassoFiliera.valueOf(request.getNomeFase()));
        } catch (IllegalArgumentException | NullPointerException e) {
            // Se la stringa non corrisponde a nessun enum, lanciamo errore o mettiamo un default
            throw new IllegalArgumentException("Tipo fase non valido: " + request.getNomeFase());
        }

        passo.setDescrizione(request.getDescrizione());
        passo.setDataOra(request.getDataOra());
        passo.setLuogo(request.getLuogo());
        passo.setOrdine(ordine);
        passo.setAzienda(azienda);
        passo.setTracciabilita(traccia);

        passoFilieraRepository.save(passo);

        // Aggiungiamo manualmente alla lista locale per il ricalcolo immediato del DTO
        traccia.getPassi().add(passo);

        return convertToDTO(traccia);
    }

    // --- Metodi Privati di Utilità ---

    private TracciabilitaProdotto findOrCreateTracciabilita(Long prodottoId) {
        return tracciabilitaRepository.findByProdottoId(prodottoId)
                .orElseGet(() -> {
                    Prodotto prodotto = prodottoRepository.findById(prodottoId)
                            .orElseThrow(() -> new EntityNotFoundException("Prodotto non trovato"));
                    TracciabilitaProdotto nuovaTraccia = new TracciabilitaProdotto();
                    nuovaTraccia.setProdotto(prodotto);
                    // FIX: Inizializziamo subito la lista vuota!
                    nuovaTraccia.setPassi(new ArrayList<>());
                    return tracciabilitaRepository.save(nuovaTraccia);
                });
    }

    private TracciabilitaDTO convertToDTO(TracciabilitaProdotto traccia) {
        TracciabilitaDTO dto = new TracciabilitaDTO();
        dto.setId(traccia.getId());
        dto.setProdottoId(traccia.getProdotto().getId());
        dto.setNomeProdotto(traccia.getProdotto().getNome());

        // Controllo null anche qui per sicurezza
        List<PassoFiliera> passi = traccia.getPassi() != null ? traccia.getPassi() : new ArrayList<>();

        List<PassoFilieraDTO> passiDTO = passi.stream()
                .sorted(Comparator.comparingInt(PassoFiliera::getOrdine))
                .map(this::convertPassoToDTO)
                .collect(Collectors.toList());

        dto.setPassiDellaFiliera(passiDTO);
        return dto;
    }

    private PassoFilieraDTO convertPassoToDTO(PassoFiliera passo) {
        PassoFilieraDTO dto = new PassoFilieraDTO();
        dto.setId(passo.getId());
        // Controllo null per evitare crash se tipoPasso non è settato
        dto.setNomeFase(passo.getTipoPasso() != null ? passo.getTipoPasso().getDisplayName() : "Sconosciuto");
        dto.setDescrizione(passo.getDescrizione());
        dto.setDataOra(passo.getDataOra());
        dto.setLuogo(passo.getLuogo());
        dto.setOrdine(passo.getOrdine());
        dto.setAziendaId(passo.getAzienda().getId());
        dto.setNomeAzienda(passo.getAzienda().getNomeAzienda());
        return dto;
    }
}