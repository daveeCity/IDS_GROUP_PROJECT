package it.unicam.cs.filieraagricola.test;

import it.unicam.cs.filieraagricola.DTO.EventoDTO;
import it.unicam.cs.filieraagricola.DTO.EventoRequestDTO;
import it.unicam.cs.filieraagricola.model.*;
import it.unicam.cs.filieraagricola.repository.EventoRepository;
import it.unicam.cs.filieraagricola.repository.UtenteRepository;
import it.unicam.cs.filieraagricola.service.EventoServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Abilita Mockito
@ActiveProfiles("test") // Disabilita il caricamento dei dati di produzione
class  EventoServiceTest {

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private UtenteRepository utenteRepository;

    @InjectMocks
    private EventoServiceImpl eventoService;

    // Oggetti di supporto per i test
    private Animatore animatoreTest;
    private Evento eventoTest;
    private EventoRequestDTO eventoRequestDTO;

    @BeforeEach
    void setUp() {
        // Setup Animatore
        animatoreTest = new Animatore();
        animatoreTest.setId(1L);
        animatoreTest.setUsername("animatoreDemo");

        // Setup Evento base
        eventoTest = new Evento();
        eventoTest.setId(100L);
        eventoTest.setTitolo("Fiera del Vino");
        eventoTest.setDescrizione("Degustazione vini locali");
        eventoTest.setDataOraInizio(LocalDateTime.now().plusDays(1));
        eventoTest.setDataOraFine(LocalDateTime.now().plusDays(1).plusHours(2));
        eventoTest.setLuogo("Cantina Sociale");
        eventoTest.setTipo(TipoEvento.FIERA);
        eventoTest.setStato(StatoEvento.PLANNED);
        eventoTest.setAnimatore(animatoreTest);

        // Setup DTO Request
        eventoRequestDTO = new EventoRequestDTO();
        eventoRequestDTO.setTitolo("Nuova Fiera");
        eventoRequestDTO.setDescrizione("Descrizione nuova");
        eventoRequestDTO.setDataOraInizio(LocalDateTime.now().plusDays(2));
        eventoRequestDTO.setDataOraFine(LocalDateTime.now().plusDays(2).plusHours(3));
        eventoRequestDTO.setLuogo("Piazza Centrale");
        eventoRequestDTO.setTipo("FIERA");
    }

    // --- TEST 1: GET EVENTI PUBBLICI ---
    @Test
    void testGetEventiPubblici_Successo() {
        // Arrange (Prepariamo i dati finti)
        when(eventoRepository.findAll()).thenReturn(List.of(eventoTest));

        // Act (Eseguiamo il metodo da testare)
        List<EventoDTO> risultati = eventoService.getEventiPubblici();

        // Assert (Verifichiamo il risultato)
        assertFalse(risultati.isEmpty());
        assertEquals(1, risultati.size());
        assertEquals("Fiera del Vino", risultati.get(0).getTitolo());

        // Verifica che il filtro sugli eventi CANCELLATI funzioni
        verify(eventoRepository, times(1)).findAll();
    }

    @Test
    void testGetEventiPubblici_FiltraCancellati() {
        Evento eventoAnnullato = new Evento();
        eventoAnnullato.setStato(StatoEvento.CANCELLED);
        eventoAnnullato.setAnimatore(animatoreTest); // Necessario per evitare NullPointer nel mapping

        when(eventoRepository.findAll()).thenReturn(List.of(eventoTest, eventoAnnullato));

        List<EventoDTO> risultati = eventoService.getEventiPubblici();

        assertEquals(1, risultati.size()); // Dovrebbe essercene solo uno (quello attivo)
        assertEquals(StatoEvento.PLANNED.name(), risultati.get(0).getStato());
    }

    // --- TEST 2: CREAZIONE EVENTO ---
    @Test
    void testCreaEvento_Successo() {
        // Arrange
        when(utenteRepository.findById(1L)).thenReturn(Optional.of(animatoreTest));
        // Quando salvo, restituisci l'evento che ti passo
        when(eventoRepository.save(any(Evento.class))).thenAnswer(invocation -> {
            Evento e = invocation.getArgument(0);
            e.setId(123L); // Simuliamo l'ID generato dal DB
            return e;
        });

        // Act
        EventoDTO risultato = eventoService.creaEvento(eventoRequestDTO, 1L);

        // Assert
        assertNotNull(risultato);
        assertEquals("Nuova Fiera", risultato.getTitolo());
        assertEquals("PLANNED", risultato.getStato());
        verify(eventoRepository).save(any(Evento.class));
    }

    @Test
    void testCreaEvento_AnimatoreNonTrovato() {
        when(utenteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            eventoService.creaEvento(eventoRequestDTO, 99L);
        });

        verify(eventoRepository, never()).save(any());
    }

    // --- TEST 3: ANNULLA EVENTO ---
    @Test
    void testAnnullaEvento_Successo() {
        when(eventoRepository.findById(100L)).thenReturn(Optional.of(eventoTest));

        eventoService.annullaEvento(100L);

        assertEquals(StatoEvento.CANCELLED, eventoTest.getStato());
        verify(eventoRepository).save(eventoTest);
    }

    // --- TEST 4: PARTECIPAZIONE EVENTO ---
    @Test
    void testPartecipaEvento_Successo() {
        Acquirente acquirente = new Acquirente();
        acquirente.setId(50L);
        acquirente.setUsername("marioRossi");

        // Mockiamo repository
        when(eventoRepository.findById(100L)).thenReturn(Optional.of(eventoTest));
        when(utenteRepository.findById(50L)).thenReturn(Optional.of(acquirente));

        // Act
        eventoService.partecipaEvento(100L, 50L);

        // Assert
        // Nota: Nel tuo codice originale il metodo stampa solo a video e non salva nel DB
        // per la relazione ManyToMany. Se aggiornerai il codice per salvare,
        // qui dovrai aggiungere: verify(eventoRepository).save(eventoTest);

        // Verifica base: il metodo deve essere eseguito senza eccezioni
        verify(utenteRepository).findById(50L);
    }
}