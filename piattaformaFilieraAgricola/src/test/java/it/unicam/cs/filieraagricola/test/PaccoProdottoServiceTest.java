package it.unicam.cs.filieraagricola.test;

import it.unicam.cs.filieraagricola.DTO.PaccoProdottoDTO;
import it.unicam.cs.filieraagricola.DTO.PaccoProdottoRequestDTO;
import it.unicam.cs.filieraagricola.DTO.ProdottoDTO;
import it.unicam.cs.filieraagricola.mapper.ProdottoMapper;
import it.unicam.cs.filieraagricola.model.Distributore;
import it.unicam.cs.filieraagricola.model.PaccoProdotto;
import it.unicam.cs.filieraagricola.model.Prodotto;
import it.unicam.cs.filieraagricola.repository.PaccoProdottoRepository;
import it.unicam.cs.filieraagricola.repository.ProdottoRepository;
import it.unicam.cs.filieraagricola.repository.UtenteRepository;
import it.unicam.cs.filieraagricola.service.PaccoProdottoServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PaccoProdottoServiceTest {

    @Mock private PaccoProdottoRepository paccoProdottoRepository;
    @Mock private ProdottoRepository prodottoRepository;
    @Mock private UtenteRepository utenteRepository;
    @Mock private ProdottoMapper prodottoMapper;

    @InjectMocks
    private PaccoProdottoServiceImpl paccoProdottoService;

    // Oggetti di test
    private Distributore distributore;
    private PaccoProdotto pacco;
    private Prodotto prodotto;
    private PaccoProdottoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        // Setup Distributore
        distributore = new Distributore();
        distributore.setId(1L);
        distributore.setNomeAzienda("Distributore Marche");

        // Setup Prodotto
        prodotto = new Prodotto();
        prodotto.setId(10L);
        prodotto.setNome("Mela Bio");

        // Setup Pacco
        pacco = new PaccoProdotto();
        pacco.setId(100L);
        pacco.setNome("Cesto Natale");
        pacco.setDescrizione("Cesto con prodotti tipici");
        pacco.setPrezzo(50.0);
        pacco.setDistributore(distributore);
        pacco.setProdotti(List.of(prodotto)); // Il pacco contiene il prodotto

        // Setup Request DTO
        requestDTO = new PaccoProdottoRequestDTO();
        requestDTO.setNome("Nuovo Pacco");
        requestDTO.setDescrizione("Descrizione pacco");
        requestDTO.setPrezzo(30.0);
        requestDTO.setProdottoIds(List.of(10L));
    }

    // --- TEST 1: GET PACCHETTI BY DISTRIBUTORE ---
    @Test
    void testGetPacchettiByDistributore_Successo() {
        // Arrange
        when(paccoProdottoRepository.findByDistributoreId(1L)).thenReturn(List.of(pacco));
        when(prodottoMapper.toDTO(any(Prodotto.class))).thenReturn(new ProdottoDTO()); // Mock del mapper

        // Act
        List<PaccoProdottoDTO> risultati = paccoProdottoService.getPacchettiByDistributore(1L);

        // Assert
        assertFalse(risultati.isEmpty());
        assertEquals(1, risultati.size());
        assertEquals("Cesto Natale", risultati.get(0).getNome());
        assertEquals("Distributore Marche", risultati.get(0).getNomeDistributore());
    }

    // --- TEST 2: CREA PACCO ---
    @Test
    void testCreaPacco_Successo() {
        // Arrange
        when(utenteRepository.findById(1L)).thenReturn(Optional.of(distributore));
        when(prodottoRepository.findAllById(anyList())).thenReturn(List.of(prodotto));
        when(paccoProdottoRepository.save(any(PaccoProdotto.class))).thenAnswer(i -> {
            PaccoProdotto p = i.getArgument(0);
            p.setId(200L); // Simuliamo ID generato
            return p;
        });
        when(prodottoMapper.toDTO(any(Prodotto.class))).thenReturn(new ProdottoDTO());

        // Act
        PaccoProdottoDTO risultato = paccoProdottoService.creaPacco(requestDTO, 1L);

        // Assert
        assertNotNull(risultato);
        assertEquals("Nuovo Pacco", risultato.getNome());
        assertEquals(30.0, risultato.getPrezzo());
        assertEquals(1, risultato.getProdottiInclusi().size());

        verify(paccoProdottoRepository).save(any(PaccoProdotto.class));
    }

    @Test
    void testCreaPacco_DistributoreNonTrovato() {
        when(utenteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                paccoProdottoService.creaPacco(requestDTO, 99L)
        );

        verify(paccoProdottoRepository, never()).save(any());
    }

    // --- TEST 3: DELETE PACCO ---
    @Test
    void testDeletePacco_Successo() {
        when(paccoProdottoRepository.existsById(100L)).thenReturn(true);

        paccoProdottoService.deletePacco(100L);

        verify(paccoProdottoRepository).deleteById(100L);
    }

    @Test
    void testDeletePacco_NonTrovato() {
        when(paccoProdottoRepository.existsById(999L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () ->
                paccoProdottoService.deletePacco(999L)
        );

        verify(paccoProdottoRepository, never()).deleteById(any());
    }

    // --- TEST 4: GET PACCO BY ID ---
    @Test
    void testGetPaccoById_Successo() {
        when(paccoProdottoRepository.findById(100L)).thenReturn(Optional.of(pacco));
        when(prodottoMapper.toDTO(any(Prodotto.class))).thenReturn(new ProdottoDTO());

        PaccoProdottoDTO dto = paccoProdottoService.getPaccoById(100L);

        assertNotNull(dto);
        assertEquals(100L, dto.getId());
    }
}