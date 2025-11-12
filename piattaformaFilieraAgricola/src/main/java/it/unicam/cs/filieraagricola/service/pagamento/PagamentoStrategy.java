package it.unicam.cs.filieraagricola.service.pagamento;

import it.unicam.cs.filieraagricola.model.Pagamento;
import it.unicam.cs.filieraagricola.model.TipoPagamento;

public interface PagamentoStrategy {
    // Processa il pagamento e ritorna l'esito (es. "APPROVATO")
    String processaPagamento(Pagamento pagamento);

    // Ritorna il tipo gestito da questa strategia
    TipoPagamento getTipoPagamento();
}