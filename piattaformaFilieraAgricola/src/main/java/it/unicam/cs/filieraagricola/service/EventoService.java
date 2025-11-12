package it.unicam.cs.filieraagricola.service;
// package it.unicam.cs.filieraagricola.service;

import it.unicam.cs.filieraagricola.DTO.EventoDTO;
import it.unicam.cs.filieraagricola.DTO.EventoRequestDTO;
import java.util.List;

public interface EventoService {

    /**
     * Ottiene la lista di tutti gli eventi pubblici (non annullati).
     */
    List<EventoDTO> getEventiPubblici();

    /**
     * Ottiene un singolo evento tramite ID.
     */
    EventoDTO getEventoById(Long eventoId);

    /**
     * Ottiene tutti gli eventi creati da uno specifico Animatore.
     */
    List<EventoDTO> getEventiByAnimatore(Long animatoreId);

    /**
     * Crea un nuovo evento (richiede che l'utente sia un Animatore).
     */
    EventoDTO creaEvento(EventoRequestDTO request, Long animatoreId);

    /**
     * Aggiorna un evento esistente.
     */
    EventoDTO aggiornaEvento(Long eventoId, EventoRequestDTO request);

    /**
     * Annulla un evento.
     */
    void annullaEvento(Long eventoId);

    /**
     * Permette a un Acquirente di registrarsi a un evento.
     */
    void partecipaEvento(Long eventoId, Long acquirenteId);
}