package it.unicam.cs.filieraagricola.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unicam.cs.filieraagricola.DTO.ProdottoDTO;
import it.unicam.cs.filieraagricola.DTO.ProdottoRequestDTO;
import it.unicam.cs.filieraagricola.model.Produttore;
import it.unicam.cs.filieraagricola.repository.UtenteRepository;
import it.unicam.cs.filieraagricola.service.ProdottoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProdottoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Per convertire oggetti in JSON

    @MockBean
    private ProdottoService prodottoService;

    @MockBean
    private UtenteRepository utenteRepository;

    private ProdottoDTO prodottoDTO;
    private Produttore produttoreMock;

    @BeforeEach
    void setUp() {
        // Setup DTO di risposta
        prodottoDTO = new ProdottoDTO();
        prodottoDTO.setId(1L);
        prodottoDTO.setNome("Mela Bio");
        prodottoDTO.setPrezzo(2.50);
        prodottoDTO.setStato("APPROVATO");

        // Setup Utente (Produttore) per i test di creazione
        // Usiamo una classe concreta (Produttore) perché Azienda è abstract
        produttoreMock = new Produttore();
        produttoreMock.setId(100L);
        produttoreMock.setUsername("produttoreTest");
        produttoreMock.setEmail("prod@test.com");
    }

    // --- TEST 1: GET CATALOGO ---
    @Test
    void testGetCatalogo() throws Exception {
        // Arrange
        when(prodottoService.getCatalogoProdotti()).thenReturn(List.of(prodottoDTO));

        // Act & Assert
        mockMvc.perform(get("/api/prodotti/catalogo"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nome").value("Mela Bio"))
                .andExpect(jsonPath("$[0].prezzo").value(2.50));
    }

    // --- TEST 2: GET PRODOTTO BY ID ---
    @Test
    void testGetProdotto() throws Exception {
        // Arrange
        when(prodottoService.getProdottoById(1L)).thenReturn(prodottoDTO);

        // Act & Assert
        mockMvc.perform(get("/api/prodotti/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Mela Bio"));
    }

    // --- TEST 3: CREA PRODOTTO (Richiede Auth) ---
    @Test
    @WithMockUser(username = "produttoreTest", roles = {"PRODUTTORE"}) // Simula login
    void testCreaProdotto() throws Exception {
        // 1. Arrange Request
        ProdottoRequestDTO request = new ProdottoRequestDTO("Mela Bio", "Descrizione", 2.50, 100);

        // 2. Mock del Repository Utenti (Cruciale!)
        // Quando il controller chiede "chi è produttoreTest?", il mock restituisce il nostro oggetto
        when(utenteRepository.findByUsername("produttoreTest")).thenReturn(Optional.of(produttoreMock));

        // 3. Mock del Service
        when(prodottoService.creaProdotto(any(ProdottoRequestDTO.class), eq(100L)))
                .thenReturn(prodottoDTO);
        // 4. Act & Assert
        mockMvc.perform(post("/api/prodotti")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))) // Body della richiesta
                .andExpect(status().isCreated()) // Ci aspettiamo 201 Created
                .andExpect(jsonPath("$.nome").value("Mela Bio"));
    }

    // --- TEST 4: RICERCA ---
    @Test
    void testCercaProdotti() throws Exception {
        when(prodottoService.cercaProdottiPerNome("Mela")).thenReturn(List.of(prodottoDTO));

        mockMvc.perform(get("/api/prodotti/cerca")
                        .param("nome", "Mela"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Mela Bio"));
    }

    // --- TEST 5: ELIMINA PRODOTTO ---
    @Test
    @WithMockUser(username = "admin", roles = {"CURATORE"}) // Solo per realismo, anche se qui non controlliamo i ruoli nel controller
    void testEliminaProdotto() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/prodotti/1"))
                .andExpect(status().isNoContent()); // Ci aspettiamo 204 No Content

        // Verify che il service sia stato chiamato
        verify(prodottoService).deleteProdotto(1L);
    }
}