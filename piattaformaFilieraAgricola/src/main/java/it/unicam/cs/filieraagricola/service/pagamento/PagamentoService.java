package it.unicam.cs.filieraagricola.service.pagamento;


import it.unicam.cs.filieraagricola.model.Pagamento;
import it.unicam.cs.filieraagricola.model.TipoPagamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PagamentoService {

    private final Map<TipoPagamento, PagamentoStrategy> strategie;

    // Spring inietterà tutte le implementazioni di PagamentoStrategy
    @Autowired
    public PagamentoService(List<PagamentoStrategy> strategyList) {
        this.strategie = strategyList.stream()
                .collect(Collectors.toMap(PagamentoStrategy::getTipoPagamento, Function.identity()));
    }

    public String eseguiPagamento(Pagamento pagamento) {
        PagamentoStrategy strategia = strategie.get(pagamento.getTipoPagamento());
        if (strategia == null) {
            throw new IllegalArgumentException("Metodo di pagamento non supportato: " + pagamento.getTipoPagamento());
        }
        return strategia.processaPagamento(pagamento);
    }
}