package it.unicam.cs.filieraagricola.test;

import it.unicam.cs.filieraagricola.DTO.ModerazioneRequestDTO;
import it.unicam.cs.filieraagricola.DTO.ProdottoDTO;
import it.unicam.cs.filieraagricola.mapper.ProdottoMapper;
import it.unicam.cs.filieraagricola.model.Azienda;
import it.unicam.cs.filieraagricola.model.Produttore;
import it.unicam.cs.filieraagricola.model.Prodotto;
import it.unicam.cs.filieraagricola.model.StatoProdotto;
import it.unicam.cs.filieraagricola.observer.ModerationObserver;
import it.unicam.cs.filieraagricola.repository.ProdottoRepository;
import it.unicam.cs.filieraagricola.service.ModerazioneServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ModerazioneServiceTest {

    @Mock
    private ProdottoRepository prodottoRepository;

    @Mock
    private ProdottoMapper prodottoMapper;

    @Mock // Mockiamo un Observer per vedere se viene notificato
    private ModerationObserver observerMock;

    @InjectMocks
    private ModerazioneServiceImpl moderazioneService;

    // Dati di test
    private Prodotto prodottoInAttesa;
    private Azienda azienda;

    @BeforeEach
    void setUp() {
        // Setup Azienda (usiamo una classe concreta)
        azienda = new Produttore();
        azienda.setId(10L);
        azienda.setNomeAzienda("Fattoria Test");

        // Setup Prodotto
        prodottoInAttesa = new Prodotto("Mela", "Desc", 1.0, 100, azienda);
        prodottoInAttesa.setId(1L);
        prodottoInAttesa.setStato(StatoProdotto.IN_ATTESA);

        // Registriamo il mock observer al service
        moderazioneService.addObserver(observerMock);
    }

    // --- TEST 1: LISTA PRODOTTI IN ATTESA ---
    @Test
    void testGetProdottiInAttesa() {
        // Arrange
        when(prodottoRepository.findByStato(StatoProdotto.IN_ATTESA))
                .thenReturn(List.of(prodottoInAttesa));
        when(prodottoMapper.toDTO(any(Prodotto.class))).thenReturn(new ProdottoDTO());

        // Act
        List<ProdottoDTO> result = moderazioneService.getProdottiInAttesa();

        // Assert
        assertEquals(1, result.size());
        verify(prodottoRepository).findByStato(StatoProdotto.IN_ATTESA);
    }

    // --- TEST 2: APPROVAZIONE PRODOTTO ---
    @Test
    void testApprovaProdotto_Successo() {
        // Arrange
        when(prodottoRepository.findById(1L)).thenReturn(Optional.of(prodottoInAttesa));
        when(prodottoRepository.save(any(Prodotto.class))).thenAnswer(i -> i.getArguments()[0]);
        when(prodottoMapper.toDTO(any(Prodotto.class))).thenReturn(new ProdottoDTO());

        // Act
        moderazioneService.approvaProdotto(1L);

        // Assert
        // 1. Verifica cambio stato
        assertEquals(StatoProdotto.APPROVATO, prodottoInAttesa.getStato());

        // 2. Verifica salvataggio
        verify(prodottoRepository).save(prodottoInAttesa);

        // 3. VERIFICA OBSERVER PATTERN (Punto cruciale!)
        // Verifichiamo che il metodo update sia stato chiamato con il messaggio corretto
        verify(observerMock, times(1)).update(eq(prodottoInAttesa), contains("approvato"));
    }

    // --- TEST 3: RIFIUTO PRODOTTO ---
    @Test
    void testRifiutaProdotto_Successo() {
        // Arrange
        ModerazioneRequestDTO request = new ModerazioneRequestDTO();
        request.setMotivazione("Descrizione inadeguata");
        when(prodottoRepository.findById(1L)).thenReturn(Optional.of(prodottoInAttesa));
        when(prodottoRepository.save(any(Prodotto.class))).thenAnswer(i -> i.getArguments()[0]);
        when(prodottoMapper.toDTO(any(Prodotto.class))).thenReturn(new ProdottoDTO());

        // Act
        moderazioneService.rifiutaProdotto(1L, request);

        // Assert
        // 1. Verifica cambio stato
        assertEquals(StatoProdotto.RIFIUTATO, prodottoInAttesa.getStato());

        // 2. Verifica Observer con motivazione
        verify(observerMock, times(1))
                .update(eq(prodottoInAttesa), contains("rifiutato con motivazione: Descrizione inadeguata"));
    }

    // --- TEST 4: PRODOTTO NON TROVATO ---
    @Test
    void testApprovaProdotto_NonTrovato() {
        when(prodottoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                moderazioneService.approvaProdotto(99L)
        );

        // Verifica che NON siano stati chiamati gli observer in caso di errore
        verifyNoInteractions(observerMock);
    }

    // --- TEST 5: OBSERVER MANAGEMENT ---
    @Test
    void testRemoveObserver() {
        // Arrange
        when(prodottoRepository.findById(1L)).thenReturn(Optional.of(prodottoInAttesa));
        when(prodottoRepository.save(any())).thenReturn(prodottoInAttesa);

        // Rimuoviamo l'observer
        moderazioneService.removeObserver(observerMock);

        // Act
        moderazioneService.approvaProdotto(1L);

        // Assert
        // L'observer NON deve essere stato chiamato perché rimosso
        verify(observerMock, never()).update(any(), any());
    }
}