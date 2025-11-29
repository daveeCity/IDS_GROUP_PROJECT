package it.unicam.cs.filieraagricola.test;

import it.unicam.cs.filieraagricola.model.Pagamento;
import it.unicam.cs.filieraagricola.model.TipoPagamento;
import it.unicam.cs.filieraagricola.service.pagamento.CartaCreditoStrategy;
import it.unicam.cs.filieraagricola.service.pagamento.PagamentoService;
import it.unicam.cs.filieraagricola.service.pagamento.PagamentoStrategy;
import it.unicam.cs.filieraagricola.service.pagamento.PayPalStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PagamentoServiceTest {

    private PagamentoService pagamentoService;

    // Mockiamo le singole strategie
    @Mock private CartaCreditoStrategy cartaCreditoStrategy;
    @Mock private PayPalStrategy payPalStrategy;

    @BeforeEach
    void setUp() {
        // 1. Configura i Mock per dire "chi sono"
        // Questo è CRUCIALE perché il costruttore del Service chiama getTipoPagamento()
        // per decidere quale chiave usare nella Mappa.
        lenient().when(cartaCreditoStrategy.getTipoPagamento()).thenReturn(TipoPagamento.CARTA_DI_CREDITO);
        lenient().when(payPalStrategy.getTipoPagamento()).thenReturn(TipoPagamento.PAYPAL);

        // Configuriamo le risposte di successo per i mock
        lenient().when(cartaCreditoStrategy.processaPagamento(any())).thenReturn("APPROVATO");
        lenient().when(payPalStrategy.processaPagamento(any())).thenReturn("APPROVATO");

        // 2. Crea la lista di strategie che Spring inietterebbe automaticamente
        List<PagamentoStrategy> strategies = List.of(cartaCreditoStrategy, payPalStrategy);

        // 3. Istanzia manualmente il Service (Constructor Injection)
        pagamentoService = new PagamentoService(strategies);
    }

    // --- TEST 1: STRATEGIA CARTA DI CREDITO ---
    @Test
    void testEseguiPagamento_CartaCredito() {
        // Arrange
        Pagamento pagamento = new Pagamento();
        pagamento.setImporto(100.0);
        pagamento.setTipoPagamento(TipoPagamento.CARTA_DI_CREDITO);

        // Act
        String esito = pagamentoService.eseguiPagamento(pagamento);

        // Assert
        assertEquals("APPROVATO", esito);
        // Verifica che sia stata chiamata ESATTAMENTE la strategia della Carta di Credito
        verify(cartaCreditoStrategy).processaPagamento(pagamento);
        // Verifica che NON sia stata chiamata quella di PayPal
        verify(payPalStrategy, never()).processaPagamento(any());
    }

    // --- TEST 2: STRATEGIA PAYPAL ---
    @Test
    void testEseguiPagamento_PayPal() {
        // Arrange
        Pagamento pagamento = new Pagamento();
        pagamento.setImporto(50.0);
        pagamento.setTipoPagamento(TipoPagamento.PAYPAL);

        // Act
        String esito = pagamentoService.eseguiPagamento(pagamento);

        // Assert
        assertEquals("APPROVATO", esito);
        // Verifica routing corretto verso PayPal
        verify(payPalStrategy).processaPagamento(pagamento);
        verify(cartaCreditoStrategy, never()).processaPagamento(any());
    }

    // --- TEST 3: TIPO NON SUPPORTATO (Bonifico) ---
    @Test
    void testEseguiPagamento_Bonifico_NonSupportato() {
        // Scenario: Abbiamo l'enum BONIFICO, ma non abbiamo passato una BonificoStrategy al costruttore
        Pagamento pagamento = new Pagamento();
        pagamento.setImporto(1000.0);
        pagamento.setTipoPagamento(TipoPagamento.BONIFICO_BANCARIO);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pagamentoService.eseguiPagamento(pagamento);
        });

        assertTrue(exception.getMessage().contains("non supportato"));
    }
}
