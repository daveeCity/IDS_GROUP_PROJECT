package it.unicam.cs.filieraagricola.service.pagamento;


import it.unicam.cs.filieraagricola.model.Pagamento;
import it.unicam.cs.filieraagricola.model.TipoPagamento;
import org.springframework.stereotype.Component;

@Component
public class CartaCreditoStrategy implements PagamentoStrategy {
    @Override
    public String processaPagamento(Pagamento pagamento) {
        // Logica fittizia di connessione al gateway di pagamento
        System.out.println("Processo pagamento di " + pagamento.getImporto() + "€ con Carta di Credito.");
        // Simuliamo un successo
        return "APPROVATO";
    }

    @Override
    public TipoPagamento getTipoPagamento() {
        return TipoPagamento.CARTA_DI_CREDITO;
    }
}

@Component
public class PayPalStrategy implements PagamentoStrategy {
    @Override
    public String processaPagamento(Pagamento pagamento) {
        // Logica fittizia di connessione a PayPal
        System.out.println("Processo pagamento di " + pagamento.getImporto() + "€ con PayPal.");
        return "APPROVATO";
    }

    @Override
    public TipoPagamento getTipoPagamento() {
        return TipoPagamento.PAYPAL;
    }
}