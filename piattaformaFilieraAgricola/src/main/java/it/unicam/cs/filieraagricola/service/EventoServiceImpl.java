package it.unicam.cs.filieraagricola.service;

import it.unicam.cs.filieraagricola.DTO.EventoDTO;
import it.unicam.cs.filieraagricola.DTO.EventoRequestDTO;
import it.unicam.cs.filieraagricola.model.*;
import it.unicam.cs.filieraagricola.repository.EventoRepository;
import it.unicam.cs.filieraagricola.repository.UtenteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventoServiceImpl implements EventoService {

    @Autowired private EventoRepository eventoRepository;
    @Autowired private UtenteRepository utenteRepository;

    @Override
    public List<EventoDTO> getEventiPubblici() {
        // Mostra solo eventi PIANIFICATI o COMPLETATI
        return eventoRepository.findAll().stream()
                .filter(e -> e.getStato() != StatoEvento.CANCELLED)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EventoDTO getEventoById(Long eventoId) {
        return convertToDTO(findEventoById(eventoId));
    }

    @Override
    public List<EventoDTO> getEventiByAnimatore(Long animatoreId) {
        return eventoRepository.findByAnimatoreId(animatoreId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EventoDTO creaEvento(EventoRequestDTO request, Long animatoreId) {
        Animatore animatore = (Animatore) utenteRepository.findById(animatoreId)
                .filter(u -> u instanceof Animatore)
                .orElseThrow(() -> new EntityNotFoundException("Animatore non trovato"));

        Evento evento = new Evento();
        // (Assumendo che Evento.java sia stato refattorizzato)
        evento.setTitolo(request.getTitolo());
        evento.setDescrizione(request.getDescrizione());
        evento.setDataOraInizio(request.getDataOraInizio());
        evento.setDataOraFine(request.getDataOraFine());
        evento.setLuogo(request.getLuogo());
        evento.setTipo(TipoEvento.valueOf(request.getTipo().toUpperCase()));
        evento.setStato(StatoEvento.PLANNED);
        evento.setAnimatore(animatore);
        evento.setLatitudine(request.getLatitudine());
        evento.setLongitudine(request.getLongitudine());

        Evento salvato = eventoRepository.save(evento);
        return convertToDTO(salvato);
    }

    @Override
    public EventoDTO aggiornaEvento(Long eventoId, EventoRequestDTO request) {
        Evento evento = findEventoById(eventoId);
        // (Qui andrebbe un controllo se l'utente autenticato è l'animatore che ha creato l'evento)

        evento.setTitolo(request.getTitolo());
        evento.setDescrizione(request.getDescrizione());
        evento.setDataOraInizio(request.getDataOraInizio());
        evento.setDataOraFine(request.getDataOraFine());
        evento.setLuogo(request.getLuogo());
        evento.setTipo(TipoEvento.valueOf(request.getTipo().toUpperCase()));

        Evento aggiornato = eventoRepository.save(evento);
        return convertToDTO(aggiornato);
    }

    @Override
    public void annullaEvento(Long eventoId) {
        Evento evento = findEventoById(eventoId);
        evento.setStato(StatoEvento.CANCELLED);
        eventoRepository.save(evento);
    }

    @Override
    public void partecipaEvento(Long eventoId, Long acquirenteId) {
        Evento evento = findEventoById(eventoId);

        Acquirente acquirente = (Acquirente) utenteRepository.findById(acquirenteId)
                .filter(u -> u instanceof Acquirente)
                .orElseThrow(() -> new EntityNotFoundException("Acquirente non trovato"));

        // NOTA: Questo richiede una relazione @ManyToMany in Evento.java
        // Esempio: @ManyToMany Set<Acquirente> partecipanti;
        // evento.getPartecipanti().add(acquirente);
        // eventoRepository.save(evento);

        System.out.println("L'acquirente " + acquirente.getUsername() + " si è registrato all'evento " + evento.getTitolo());
    }

    @Override
    @Transactional
    public void invitaAzienda(Long eventoId, Long animatoreId, Long aziendaId) {
        // 1. Recupera l'Evento
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new EntityNotFoundException("Evento non trovato: " + eventoId));

        // 2. Verifica di Sicurezza: Solo l'Animatore creatore può invitare
        if (!evento.getAnimatore().getId().equals(animatoreId)) {
            throw new IllegalArgumentException("Non sei l'organizzatore di questo evento.");
        }

        // 3. Recupera l'Azienda da invitare
        Utente utenteTarget = utenteRepository.findById(aziendaId)
                .orElseThrow(() -> new EntityNotFoundException("Azienda non trovata: " + aziendaId));

        // 4. Verifica che l'utente sia effettivamente un'Azienda (Produttore o Trasformatore)
        // Nota: Distributore è un'Azienda, ma la richiesta specificava Produttori o Trasformatori.
        // Se vuoi includere tutti i tipi di Azienda, basta instanceof Azienda.
        if (!(utenteTarget instanceof Produttore) && !(utenteTarget instanceof Trasformatore)) {
            throw new IllegalArgumentException("L'utente selezionato non è un Produttore o Trasformatore.");
        }

        Azienda azienda = (Azienda) utenteTarget;

        // 5. Aggiungi l'azienda alla lista e salva
        evento.getAziendeInvitate().add(azienda);
        eventoRepository.save(evento);

        System.out.println("Invito inviato a " + azienda.getNomeAzienda() + " per l'evento " + evento.getTitolo());
    }

    private Evento findEventoById(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento non trovato con ID: " + id));
    }

    private EventoDTO convertToDTO(Evento evento) {
        EventoDTO dto = new EventoDTO();
        dto.setId(evento.getId());
        dto.setTitolo(evento.getTitolo());
        dto.setDescrizione(evento.getDescrizione());
        dto.setDataOraInizio(evento.getDataOraInizio());
        dto.setDataOraFine(evento.getDataOraFine());
        dto.setLuogo(evento.getLuogo());
        dto.setTipo(evento.getTipo().name());
        dto.setStato(evento.getStato().name());
        dto.setAnimatoreId(evento.getAnimatore().getId());
        dto.setNomeAnimatore(evento.getAnimatore().getUsername());
        return dto;
    }
}