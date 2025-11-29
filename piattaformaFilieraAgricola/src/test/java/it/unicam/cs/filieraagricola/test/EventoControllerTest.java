package it.unicam.cs.filieraagricola.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unicam.cs.filieraagricola.DTO.EventoDTO;
import it.unicam.cs.filieraagricola.DTO.EventoRequestDTO;
import it.unicam.cs.filieraagricola.model.Animatore;
import it.unicam.cs.filieraagricola.service.EventoService;
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
import it.unicam.cs.filieraagricola.repository.UtenteRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EventoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventoService eventoService;

    @MockBean
    private UtenteRepository utenteRepository;

    private EventoDTO eventoDTO;

    @BeforeEach
    void setUp() {
        eventoDTO = new EventoDTO();
        eventoDTO.setId(1L);
        eventoDTO.setTitolo("Fiera Test");
        eventoDTO.setStato("PLANNED");
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"}) // Simula un utente loggato
    void testGetEventiPubblici() throws Exception {
        // Arrange
        when(eventoService.getEventiPubblici()).thenReturn(List.of(eventoDTO));

        // Act & Assert
        mockMvc.perform(get("/api/eventi"))
                .andExpect(status().isOk()) // Ci aspettiamo HTTP 200
                .andExpect(jsonPath("$[0].titolo").value("Fiera Test")) // Verifichiamo il JSON
                .andExpect(jsonPath("$[0].stato").value("PLANNED"));
    }

    @Test
    @WithMockUser(username = "animatore", roles = {"ANIMATORE"}) // Simula l'animatore
    void testCreaEvento() throws Exception {
        // Arrange
        EventoRequestDTO request = new EventoRequestDTO();
        request.setTitolo("Nuovo Evento");
        request.setTipo("FIERA");

        Animatore animatoreMock = new Animatore();
        animatoreMock.setId(1L);
        animatoreMock.setUsername("animatore");

        when(utenteRepository.findByUsername("animatore")).thenReturn(Optional.of(animatoreMock));

        when(eventoService.creaEvento(any(EventoRequestDTO.class), any())).thenReturn(eventoDTO);

        // Act & Assert
        mockMvc.perform(post("/api/eventi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
}