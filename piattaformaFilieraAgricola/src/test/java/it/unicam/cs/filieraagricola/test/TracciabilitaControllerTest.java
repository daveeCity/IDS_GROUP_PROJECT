package it.unicam.cs.filieraagricola.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unicam.cs.filieraagricola.DTO.PassoFilieraDTO;
import it.unicam.cs.filieraagricola.DTO.PassoFilieraRequestDTO;
import it.unicam.cs.filieraagricola.DTO.TracciabilitaDTO;
import it.unicam.cs.filieraagricola.model.Acquirente;
import it.unicam.cs.filieraagricola.model.Produttore;
import it.unicam.cs.filieraagricola.repository.UtenteRepository;
import it.unicam.cs.filieraagricola.service.TracciabilitaService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TracciabilitaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TracciabilitaService tracciabilitaService;

    @MockBean
    private UtenteRepository utenteRepository;

    // Oggetti di test
    private TracciabilitaDTO tracciabilitaDTO;
    private PassoFilieraDTO passoDTO;
    private Produttore produttoreMock;
    private Acquirente acquirenteMock;

    @BeforeEach
    void setUp() {
        // 1. Setup Utente Azienda (Produttore)
        produttoreMock = new Produttore();
        produttoreMock.setId(100L);
        produttoreMock.setUsername("produttoreTest");
        produttoreMock.setNomeAzienda("Fattoria Mock");

        // 2. Setup Utente Non-Azienda (Acquirente) per testare il 403 Forbidden
        acquirenteMock = new Acquirente();
        acquirenteMock.setId(200L);
        acquirenteMock.setUsername("clienteCurioso");

        // 3. Setup DTO Risposta Passo
        passoDTO = new PassoFilieraDTO();
        passoDTO.setId(5L);
        passoDTO.setNomeFase("Semina");
        passoDTO.setLuogo("Campo A");
        passoDTO.setNomeAzienda("Fattoria Mock");
        passoDTO.setOrdine(1);

        // 4. Setup DTO Risposta Tracciabilità
        tracciabilitaDTO = new TracciabilitaDTO();
        tracciabilitaDTO.setId(1L);
        tracciabilitaDTO.setProdottoId(10L);
        tracciabilitaDTO.setNomeProdotto("Grano Antico");
        tracciabilitaDTO.setPassiDellaFiliera(List.of(passoDTO));
    }

    // --- TEST 1: GET TRACCIABILITÀ (Pubblico) ---
    // URL: /api/tracciabilita/prodotto/{prodottoId}
    @Test
    @WithMockUser(username = "utenteQualsiasi")
    void testGetTracciabilita() throws Exception {
        // Arrange
        when(tracciabilitaService.getTracciabilitaByProdotto(10L)).thenReturn(tracciabilitaDTO);

        // Act & Assert
        mockMvc.perform(get("/api/tracciabilita/prodotto/10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nomeProdotto").value("Grano Antico"))
                .andExpect(jsonPath("$.passiDellaFiliera[0].nomeFase").value("Semina"));
    }
    // --- TEST 2: AGGIUNGI PASSO (Successo - Azienda) ---
    // URL: /api/tracciabilita/prodotto/{prodottoId}/passo
    @Test
    @WithMockUser(username = "produttoreTest", roles = {"PRODUTTORE"})
    void testAggiungiPassoFiliera_Successo() throws Exception {
        // 1. Arrange Request
        PassoFilieraRequestDTO request = new PassoFilieraRequestDTO();
        request.setNomeFase("SEEDING");
        request.setDescrizione("Semina inizio stagione");
        request.setLuogo("Macerata");
        request.setDataOra(LocalDateTime.now());

        // 2. Mock Repository: Restituisce un oggetto di tipo Produttore (che è un'Azienda)
        when(utenteRepository.findByUsername("produttoreTest")).thenReturn(Optional.of(produttoreMock));

        // 3. Mock Service
        when(tracciabilitaService.aggiungiPassoFiliera(eq(10L), eq(100L), any(PassoFilieraRequestDTO.class)))
                .thenReturn(tracciabilitaDTO);

        // 4. Act & Assert
        mockMvc.perform(post("/api/tracciabilita/prodotto/10/passo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()) // Il tuo controller ritorna HttpStatus.CREATED
                .andExpect(jsonPath("$.passiDellaFiliera[0].nomeAzienda").value("Fattoria Mock"));
    }

    // --- TEST 3: AGGIUNGI PASSO (Fallimento - Utente non autorizzato) ---
    // Verifichiamo la logica: if (!(utenteLoggato instanceof Azienda))
    @Test
    @WithMockUser(username = "clienteCurioso", roles = {"ACQUIRENTE"})
    void testAggiungiPassoFiliera_Forbidden_NonAzienda() throws Exception {
        // 1. Arrange Request
        PassoFilieraRequestDTO request = new PassoFilieraRequestDTO();
        request.setNomeFase("SEEDING");

        // 2. Mock Repository: Restituisce un Acquirente (che NON è un'Azienda)
        when(utenteRepository.findByUsername("clienteCurioso")).thenReturn(Optional.of(acquirenteMock));

        // 3. Act & Assert
        mockMvc.perform(post("/api/tracciabilita/prodotto/10/passo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden()); // Ci aspettiamo 403 Forbidden
    }
}