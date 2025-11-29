package it.unicam.cs.filieraagricola.test;

import it.unicam.cs.filieraagricola.DTO.ProdottoDTO;
import it.unicam.cs.filieraagricola.DTO.ProdottoRequestDTO;
import it.unicam.cs.filieraagricola.model.Azienda;
import it.unicam.cs.filieraagricola.model.Prodotto;
import it.unicam.cs.filieraagricola.model.Produttore;
import it.unicam.cs.filieraagricola.model.StatoProdotto;
import it.unicam.cs.filieraagricola.repository.ProdottoRepository;
import it.unicam.cs.filieraagricola.repository.UtenteRepository;
import it.unicam.cs.filieraagricola.service.ProdottoServiceImpl;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ProdottoServiceTest {

    @Mock
    private ProdottoRepository prodottoRepository;

    @Mock
    private UtenteRepository utenteRepository;

    @InjectMocks
    private ProdottoServiceImpl prodottoService;

    // Oggetti di supporto
    private Azienda aziendaTest;
    private Prodotto prodottoApprovato;
    private Prodotto prodottoInAttesa;
    private ProdottoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        // Setup Azienda
        Produttore produttore = new Produttore();
        produttore.setId(1L);
        produttore.setUsername("aziendaAgricola");
        produttore.setNomeAzienda("Fattoria Felice");

        aziendaTest = produttore;

        // Setup Prodotto APPROVATO (per il catalogo)
        prodottoApprovato = new Prodotto("Mela Bio", "Buona", 1.50, 100, aziendaTest);
        prodottoApprovato.setId(10L);
        prodottoApprovato.setStato(StatoProdotto.APPROVATO);

        // Setup Prodotto IN_ATTESA (appena creato)
        prodottoInAttesa = new Prodotto("Vino Rosso", "DOC", 12.00, 50, aziendaTest);
        prodottoInAttesa.setId(11L);
        prodottoInAttesa.setStato(StatoProdotto.IN_ATTESA);

        // Setup DTO per le richieste
        requestDTO = new ProdottoRequestDTO("Mela Golden", "Nuova descrizione", 2.00, 200);
    }

    // --- TEST 1: CATALOGO PRODOTTI ---
    @Test
    void testGetCatalogoProdotti_MostraSoloApprovati() {
        // Arrange: Il repository restituisce una lista mista, ma il metodo findByStato filtra già
        when(prodottoRepository.findByStato(StatoProdotto.APPROVATO))
                .thenReturn(List.of(prodottoApprovato));

        // Act
        List<ProdottoDTO> risultati = prodottoService.getCatalogoProdotti();

        // Assert
        assertEquals(1, risultati.size());
        assertEquals("Mela Bio", risultati.get(0).getNome());
        assertEquals("APPROVATO", risultati.get(0).getStato());
        verify(prodottoRepository).findByStato(StatoProdotto.APPROVATO);
    }

    // --- TEST 2: CREAZIONE PRODOTTO ---
    @Test
    void testCreaProdotto_Successo_StatoInAttesa() {
        // Arrange
        when(utenteRepository.findById(1L)).thenReturn(Optional.of(aziendaTest));
        // Simuliamo il salvataggio restituendo l'oggetto passato
        when(prodottoRepository.save(any(Prodotto.class))).thenAnswer(i -> {
            Prodotto p = i.getArgument(0);
            p.setId(99L); // ID generato
            return p;
        });

        // Act
        ProdottoDTO risultato = prodottoService.creaProdotto(requestDTO, 1L);

        // Assert
        assertNotNull(risultato);
        assertEquals("Mela Golden", risultato.getNome());
        assertEquals("Fattoria Felice", risultato.getNomeAzienda());

        // Verifica cruciale: Un prodotto appena creato DEVE essere IN_ATTESA
        assertEquals(StatoProdotto.IN_ATTESA.name(), risultato.getStato());

        verify(prodottoRepository).save(any(Prodotto.class));
    }

    @Test
    void testCreaProdotto_AziendaNonTrovata() {
        when(utenteRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                prodottoService.creaProdotto(requestDTO, 999L)
        );

        verify(prodottoRepository, never()).save(any());
    }

    // --- TEST 3: AGGIORNAMENTO PRODOTTO ---
    @Test
    void testUpdateProdotto_ResettaStatoInAttesa() {
        // Scenario: Modifico un prodotto che era APPROVATO. Deve tornare IN_ATTESA.

        // Arrange
        when(prodottoRepository.findById(10L)).thenReturn(Optional.of(prodottoApprovato));
        when(prodottoRepository.save(any(Prodotto.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        ProdottoDTO risultato = prodottoService.updateProdotto(10L, requestDTO);

        // Assert
        assertEquals("Mela Golden", risultato.getNome()); // Nome cambiato

        // Verifica cruciale: La modifica deve invalidare l'approvazione precedente
        assertEquals(StatoProdotto.IN_ATTESA.name(), risultato.getStato());
    }

    // --- TEST 4: RICERCA ---
    @Test
    void testCercaProdottiPerNome_FiltraNonApprovati() {
        // Arrange: Il repository trova 2 prodotti col nome simile, ma uno è IN_ATTESA
        when(prodottoRepository.findByNomeContainingIgnoreCase("Mela"))
                .thenReturn(List.of(prodottoApprovato, prodottoInAttesa));

        // Act
        List<ProdottoDTO> risultati = prodottoService.cercaProdottiPerNome("Mela");

        // Assert
        assertEquals(1, risultati.size()); // Deve scartare quello IN_ATTESA
        assertEquals(10L, risultati.get(0).getId()); // Solo quello approvato
    }

    // --- TEST 5: ELIMINAZIONE ---
    @Test
    void testDeleteProdotto_Successo() {
        when(prodottoRepository.findById(10L)).thenReturn(Optional.of(prodottoApprovato));

        prodottoService.deleteProdotto(10L);

        verify(prodottoRepository).delete(prodottoApprovato);
    }
}