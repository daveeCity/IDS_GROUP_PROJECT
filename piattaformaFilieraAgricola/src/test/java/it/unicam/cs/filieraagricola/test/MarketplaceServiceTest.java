package it.unicam.cs.filieraagricola.test;

import it.unicam.cs.filieraagricola.DTO.AggiungiAlCarrelloRequestDTO;
import it.unicam.cs.filieraagricola.DTO.CarrelloDTO;
import it.unicam.cs.filieraagricola.mapper.CarrelloMapper;
import it.unicam.cs.filieraagricola.model.*;
import it.unicam.cs.filieraagricola.repository.*;
import it.unicam.cs.filieraagricola.service.MarketplaceServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class MarketplaceServiceTest {

    @Mock private CarrelloRepository carrelloRepository;
    @Mock private ElementoCarrelloRepository elementoCarrelloRepository;
    @Mock private ProdottoRepository prodottoRepository;
    @Mock private UtenteRepository utenteRepository;
    @Mock private CarrelloMapper carrelloMapper; // Mockiamo il mapper per isolare il test

    // Mock per la sicurezza
    @Mock private SecurityContext securityContext;
    @Mock private Authentication authentication;

    @InjectMocks
    private MarketplaceServiceImpl marketplaceService;

    private Acquirente acquirenteTest;
    private Carrello carrelloTest;
    private Prodotto prodottoTest;

    @BeforeEach
    void setUp() {
        // 1. Setup dell'Utente finto
        acquirenteTest = new Acquirente();
        acquirenteTest.setId(1L);
        acquirenteTest.setUsername("marioRossi");

        // 2. Setup della Sicurezza (Simuliamo il login)
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("marioRossi");

        // 3. Istruiamo il repo utenti a restituire il nostro acquirente quando cercato
        when(utenteRepository.findByUsername("marioRossi")).thenReturn(Optional.of(acquirenteTest));

        // 4. Setup oggetti comuni
        carrelloTest = new Carrello(acquirenteTest);
        carrelloTest.setId(100L);

        prodottoTest = new Prodotto();
        prodottoTest.setId(50L);
        prodottoTest.setNome("Mela Bio");
        prodottoTest.setPrezzo(1.50);
        prodottoTest.setQuantitaDisponibile(100);
        prodottoTest.setStato(StatoProdotto.APPROVATO);
    }

    @AfterEach
    void tearDown() {
        // Pulizia del contesto di sicurezza dopo ogni test per non influenzare gli altri
        SecurityContextHolder.clearContext();
    }

    // --- TEST 1: GET CARRELLO ---
    @Test
    void testGetCarrello_Esistente() {
        // Arrange: Il carrello esiste già nel DB
        when(carrelloRepository.findByAcquirenteId(1L)).thenReturn(Optional.of(carrelloTest));
        when(carrelloMapper.toDTO(carrelloTest)).thenReturn(new CarrelloDTO()); // Ritorna un DTO vuoto per ora

        // Act
        CarrelloDTO risultato = marketplaceService.getCarrello();

        // Assert
        assertNotNull(risultato);
        verify(carrelloRepository).findByAcquirenteId(1L);
        verify(carrelloRepository, never()).save(any()); // Non deve salvare nulla se esiste già
    }

    @Test
    void testGetCarrello_Nuovo() {
        // Arrange: Il carrello NON esiste
        when(carrelloRepository.findByAcquirenteId(1L)).thenReturn(Optional.empty());
        when(carrelloRepository.save(any(Carrello.class))).thenAnswer(i -> i.getArguments()[0]); // Ritorna quello che salva
        when(carrelloMapper.toDTO(any())).thenReturn(new CarrelloDTO());
        // Act
        marketplaceService.getCarrello();

        // Assert
        // Verifica che sia stato chiamato il metodo save per creare il nuovo carrello
        verify(carrelloRepository).save(any(Carrello.class));
    }

    // --- TEST 2: AGGIUNGI AL CARRELLO ---
    @Test
    void testAggiungiAlCarrello_NuovoElemento_Successo() {
        // Arrange
        AggiungiAlCarrelloRequestDTO request = new AggiungiAlCarrelloRequestDTO();
        request.setProdottoId(50L);
        request.setQuantita(5);

        // Simuliamo carrello esistente e prodotto trovato
        when(carrelloRepository.findByAcquirenteId(1L)).thenReturn(Optional.of(carrelloTest));
        when(prodottoRepository.findById(50L)).thenReturn(Optional.of(prodottoTest));

        // Simuliamo che l'elemento NON sia già nel carrello
        when(elementoCarrelloRepository.findByCarrelloIdAndProdottoId(100L, 50L))
                .thenReturn(Optional.empty());

        when(carrelloMapper.toDTO(any())).thenReturn(new CarrelloDTO());

        // Act
        marketplaceService.aggiungiAlCarrello(request);

        // Assert
        // Verifica che salviamo il nuovo elemento con quantità 5
        verify(elementoCarrelloRepository).save(argThat(elemento ->
                elemento.getQuantita() == 5 &&
                        elemento.getProdotto().getId().equals(50L)
        ));
    }

    @Test
    void testAggiungiAlCarrello_ElementoEsistente_AggiornaQuantita() {
        // Arrange
        AggiungiAlCarrelloRequestDTO request = new AggiungiAlCarrelloRequestDTO();
        request.setProdottoId(50L);
        request.setQuantita(2); // Ne aggiungiamo altri 2

        // Simuliamo che nel carrello ci siano già 3 mele
        ElementoCarrello elementoEsistente = new ElementoCarrello(carrelloTest, prodottoTest, 3);

        when(carrelloRepository.findByAcquirenteId(1L)).thenReturn(Optional.of(carrelloTest));
        when(prodottoRepository.findById(50L)).thenReturn(Optional.of(prodottoTest));

        // Repository trova l'elemento esistente
        when(elementoCarrelloRepository.findByCarrelloIdAndProdottoId(100L, 50L))
                .thenReturn(Optional.of(elementoEsistente));

        when(carrelloMapper.toDTO(any())).thenReturn(new CarrelloDTO());

        // Act
        marketplaceService.aggiungiAlCarrello(request);

        // Assert
        // Verifica che la quantità salvata sia 3 + 2 = 5
        verify(elementoCarrelloRepository).save(argThat(elemento ->
                elemento.getQuantita() == 5
        ));
    }

    @Test
    void testAggiungiAlCarrello_ProdottoNonApprovato() {
        prodottoTest.setStato(StatoProdotto.IN_ATTESA); // Prodotto non approvato

        when(carrelloRepository.findByAcquirenteId(1L)).thenReturn(Optional.of(carrelloTest));
        when(prodottoRepository.findById(50L)).thenReturn(Optional.of(prodottoTest));

        AggiungiAlCarrelloRequestDTO request = new AggiungiAlCarrelloRequestDTO();
        request.setProdottoId(50L);
        request.setQuantita(1);

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                marketplaceService.aggiungiAlCarrello(request)
        );
        assertEquals("Prodotto non disponibile per la vendita.", ex.getMessage());
    }

    @Test
    void testAggiungiAlCarrello_StockInsufficiente() {
        prodottoTest.setQuantitaDisponibile(10); // Solo 10 disponibili

        AggiungiAlCarrelloRequestDTO request = new AggiungiAlCarrelloRequestDTO();
        request.setProdottoId(50L);
        request.setQuantita(20); // Ne voglio 20

        when(carrelloRepository.findByAcquirenteId(1L)).thenReturn(Optional.of(carrelloTest));
        when(prodottoRepository.findById(50L)).thenReturn(Optional.of(prodottoTest));

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                marketplaceService.aggiungiAlCarrello(request)
        );
        assertEquals("Quantità non disponibile a magazzino.", ex.getMessage());
    }

    // --- TEST 3: RIMUOVI DAL CARRELLO ---
    @Test
    void testRimuoviDalCarrello_Successo() {
        ElementoCarrello elementoDaRimuovere = new ElementoCarrello(carrelloTest, prodottoTest, 5);

        when(carrelloRepository.findByAcquirenteId(1L)).thenReturn(Optional.of(carrelloTest));
        when(elementoCarrelloRepository.findByCarrelloIdAndProdottoId(100L, 50L))
                .thenReturn(Optional.of(elementoDaRimuovere));
        when(carrelloMapper.toDTO(any())).thenReturn(new CarrelloDTO());

        // Act
        marketplaceService.rimuoviDalCarrello(50L);

        // Assert
        verify(elementoCarrelloRepository).delete(elementoDaRimuovere);
        // Verifica anche che sia stato rimosso dalla lista locale del carrello (opzionale ma buono)
        assertFalse(carrelloTest.getElementi().contains(elementoDaRimuovere));
    }
}