package it.unicam.cs.filieraagricola.service;

import it.unicam.cs.filieraagricola.DTO.*;
import it.unicam.cs.filieraagricola.mapper.ProdottoMapper;
import it.unicam.cs.filieraagricola.model.*;
import it.unicam.cs.filieraagricola.observer.ModerationObserver;
import it.unicam.cs.filieraagricola.observer.ModerationSubject;
import it.unicam.cs.filieraagricola.repository.EventoRepository;
import it.unicam.cs.filieraagricola.repository.PassoFilieraRepository;
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

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private PassoFilieraRepository passoFilieraRepository;

    @Autowired
    private ProdottoMapper prodottoMapper;

    // Lista degli observer per le notifiche
    private final List<ModerationObserver> observers = new ArrayList<>();


    @Override
    public List<ProdottoDTO> getProdottiInAttesa() {
        return prodottoRepository.findByStato(StatoApprovazione.IN_ATTESA)
                .stream()
                .map(prodottoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProdottoDTO approvaProdotto(Long prodottoId) {
        Prodotto prodotto = findProdottoById(prodottoId);
        prodotto.setStato(StatoApprovazione.APPROVATO);
        Prodotto salvato = prodottoRepository.save(prodotto);

        // Notifica (solo per i prodotti come da interfaccia Observer attuale)
        notifyObservers(salvato, "approvato");

        return prodottoMapper.toDTO(salvato);
    }

    @Override
    public ProdottoDTO rifiutaProdotto(Long prodottoId, ModerazioneRequestDTO request) {
        Prodotto prodotto = findProdottoById(prodottoId);
        prodotto.setStato(StatoApprovazione.RIFIUTATO);
        Prodotto salvato = prodottoRepository.save(prodotto);

        notifyObservers(salvato, "rifiutato con motivazione: " + request.getMotivazione());

        return prodottoMapper.toDTO(salvato);
    }

    // =================================================================================
    //  SEZIONE EVENTI (Usa StatoApprovazione)
    // =================================================================================

    @Override
    public List<EventoDTO> getEventiInAttesa() {
        return eventoRepository.findByStatoApprovazione(StatoApprovazione.IN_ATTESA)
                .stream()
                .map(this::convertEventoToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EventoDTO approvaEvento(Long eventoId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new EntityNotFoundException("Evento non trovato: " + eventoId));

        evento.setStatoApprovazione(StatoApprovazione.APPROVATO);
        // Quando approvato, diventa pubblico (PLANNED è lo stato operativo dell'evento)
        if(evento.getStato() == null) {
            evento.setStato(StatoEvento.PLANNED);
        }

        Evento salvato = eventoRepository.save(evento);
        return convertEventoToDTO(salvato);
    }

    @Override
    public EventoDTO rifiutaEvento(Long eventoId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new EntityNotFoundException("Evento non trovato: " + eventoId));

        evento.setStatoApprovazione(StatoApprovazione.RIFIUTATO);
        // Opzionale: se rifiutato, potresti settare lo stato operativo a CANCELLED
        // evento.setStato(StatoEvento.CANCELLED);

        Evento salvato = eventoRepository.save(evento);
        return convertEventoToDTO(salvato);
    }

    // =================================================================================
    //  SEZIONE PASSI FILIERA (Usa StatoApprovazione)
    // =================================================================================

    @Override
    public List<PassoFilieraDTO> getPassiInAttesa() {
        return passoFilieraRepository.findByStatoApprovazione(StatoApprovazione.IN_ATTESA)
                .stream()
                .map(this::convertPassoToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PassoFilieraDTO approvaPasso(Long passoId) {
        PassoFiliera passo = passoFilieraRepository.findById(passoId)
                .orElseThrow(() -> new EntityNotFoundException("Passo filiera non trovato: " + passoId));

        passo.setStatoApprovazione(StatoApprovazione.APPROVATO);
        PassoFiliera salvato = passoFilieraRepository.save(passo);
        return convertPassoToDTO(salvato);
    }

    @Override
    public PassoFilieraDTO rifiutaPasso(Long passoId) {
        PassoFiliera passo = passoFilieraRepository.findById(passoId)
                .orElseThrow(() -> new EntityNotFoundException("Passo filiera non trovato: " + passoId));

        passo.setStatoApprovazione(StatoApprovazione.RIFIUTATO);
        PassoFiliera salvato = passoFilieraRepository.save(passo);
        return convertPassoToDTO(salvato);
    }


    // =================================================================================
    //  IMPLEMENTAZIONE OBSERVER (Prodotti)
    // =================================================================================

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

    // =================================================================================
    //  METODI PRIVATI DI UTILITA' & MAPPERS MANUALI
    // =================================================================================

    private Prodotto findProdottoById(Long id) {
        return prodottoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prodotto non trovato con ID: " + id));
    }

    // Mapper Manuale per Evento (per evitare dipendenze circolari o bean mancanti)
    private EventoDTO convertEventoToDTO(Evento evento) {
        EventoDTO dto = new EventoDTO();
        dto.setId(evento.getId());
        dto.setTitolo(evento.getTitolo());
        dto.setDescrizione(evento.getDescrizione());
        dto.setDataOraInizio(evento.getDataOraInizio());
        dto.setDataOraFine(evento.getDataOraFine());
        dto.setLuogo(evento.getLuogo());

        if (evento.getTipo() != null) dto.setTipo(evento.getTipo().getDisplayName());
        if (evento.getStato() != null) dto.setStato(evento.getStato().getDisplayName());

        if (evento.getAnimatore() != null) {
            dto.setAnimatoreId(evento.getAnimatore().getId());
            dto.setNomeAnimatore(evento.getAnimatore().getUsername()); // O getNomeAzienda se applicabile
        }
        return dto;
    }

    // Mapper Manuale per PassoFiliera
    private PassoFilieraDTO convertPassoToDTO(PassoFiliera passo) {
        PassoFilieraDTO dto = new PassoFilieraDTO();
        dto.setId(passo.getId());
        dto.setNomeFase(passo.getTipoPasso() != null ? passo.getTipoPasso().getDisplayName() : "Sconosciuto");
        dto.setDescrizione(passo.getDescrizione());
        dto.setDataOra(passo.getDataOra());
        dto.setLuogo(passo.getLuogo());
        dto.setOrdine(passo.getOrdine());

        if (passo.getAzienda() != null) {
            dto.setAziendaId(passo.getAzienda().getId());
            dto.setNomeAzienda(passo.getAzienda().getNomeAzienda());
        }
        return dto;
    }
}