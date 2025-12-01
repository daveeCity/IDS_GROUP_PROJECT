package it.unicam.cs.filieraagricola.service;
import it.unicam.cs.filieraagricola.DTO.*;
import it.unicam.cs.filieraagricola.mapper.CarrelloMapper;
import it.unicam.cs.filieraagricola.mapper.OrdineMapper;
import it.unicam.cs.filieraagricola.model.*;
import it.unicam.cs.filieraagricola.repository.*;
import it.unicam.cs.filieraagricola.service.pagamento.PagamentoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
public class MarketplaceServiceImpl implements MarketplaceService {

    @Autowired private CarrelloRepository carrelloRepository;
    @Autowired private ElementoCarrelloRepository elementoCarrelloRepository;
    @Autowired private ProdottoRepository prodottoRepository;
    @Autowired private UtenteRepository utenteRepository;
    @Autowired private OrdineRepository ordineRepository;
    @Autowired private PagamentoRepository pagamentoRepository;
    @Autowired private PagamentoService pagamentoService;
    @Autowired private CarrelloMapper carrelloMapper;
    @Autowired private OrdineMapper ordineMapper;
    @Autowired private PaccoProdottoRepository paccoRepository;

    @Override
    @Transactional(readOnly = true)
    public CarrelloDTO getCarrello() {
        Acquirente acquirente = getAcquirenteAutenticato();
        Carrello carrello = getOrCreateCarrello(acquirente);
        return carrelloMapper.toDTO(carrello);
    }

    @Override
    @Transactional
    public CarrelloDTO aggiungiAlCarrello(AggiungiAlCarrelloRequestDTO request) {
        Carrello carrello = getOrCreateCarrello(getAcquirenteAutenticato()); // Tuo metodo esistente

        ElementoCarrello elemento = new ElementoCarrello();
        elemento.setCarrello(carrello);
        elemento.setQuantita(request.getQuantita());

        if ("PACCO".equalsIgnoreCase(request.getTipo())) {
            // GESTIONE PACCO
            PaccoProdotto pacco = paccoRepository.findById(request.getIdOggetto())
                    .orElseThrow(() -> new EntityNotFoundException("Pacco non trovato"));

            // Verifica esistenza nel carrello (opzionale: potresti sommare le quantità)
            // ...

            elemento.setPacco(pacco);
            elemento.setProdotto(null); // Importante

        } else {
            // GESTIONE PRODOTTO STANDARD
            Prodotto prodotto = prodottoRepository.findById(request.getIdOggetto())
                    .orElseThrow(() -> new EntityNotFoundException("Prodotto non trovato"));

            // Verifica disponibilità, stato approvato, ecc...
            if (prodotto.getQuantitaDisponibile() < request.getQuantita()) {
                throw new IllegalArgumentException("Quantità non disponibile");
            }

            elemento.setProdotto(prodotto);
            elemento.setPacco(null); // Importante
        }

        carrello.getElementi().add(elemento); // O logica di merge se esiste già
        elementoCarrelloRepository.save(elemento);

        return carrelloMapper.toDTO(carrello);
    }

    @Override
    @Transactional
    public CarrelloDTO rimuoviDalCarrello(Long prodottoId) {
        Acquirente acquirente = getAcquirenteAutenticato();
        Carrello carrello = getOrCreateCarrello(acquirente);

        ElementoCarrello elemento = elementoCarrelloRepository
                .findByCarrelloIdAndProdottoId(carrello.getId(), prodottoId)
                .orElseThrow(() -> new EntityNotFoundException("Prodotto non nel carrello"));

        elementoCarrelloRepository.delete(elemento);
        carrello.getElementi().remove(elemento);

        return carrelloMapper.toDTO(carrello);
    }


    @Override
    @Transactional
    public OrdineDTO checkout(CheckoutRequestDTO request) {
        Acquirente acquirente = getAcquirenteAutenticato();
        Carrello carrello = getOrCreateCarrello(acquirente);

        if (carrello.getElementi().isEmpty()) {
            throw new RuntimeException("Il carrello è vuoto.");
        }

        // 1. Crea l'Ordine
        Ordine ordine = new Ordine();
        ordine.setAcquirente(acquirente);
        ordine.setStato(StatoOrdine.IN_ATTESA);

        double totaleOrdine = 0.0;
        List<DettaglioOrdine> dettagli = new ArrayList<>();
        // 2. Convalida lo stock e crea i dettagli ordine
        for (ElementoCarrello item : carrello.getElementi()) {
            // Prendi il prezzo corrente usando il metodo helper creato nel Passo 2
            double prezzo = item.getPrezzoUnitarioCorrente();

            DettaglioOrdine dettaglio = new DettaglioOrdine(ordine, item.getQuantita(), prezzo);

            if (item.getProdotto() != null) {
                dettaglio.setProdotto(item.getProdotto());
                // Scala quantità prodotto
                item.getProdotto().setQuantitaDisponibile(item.getProdotto().getQuantitaDisponibile() - item.getQuantita());
            } else if (item.getPacco() != null) {
                dettaglio.setPacco(item.getPacco());
                // N.B. Per i pacchi, dovresti scalare la quantità dei prodotti contenuti?
                // Per semplicità d'esame, assumiamo che il pacco sia un'entità virtuale o infinita,
                // oppure dovresti fare un ciclo sui prodotti dentro il pacco e scalarli.
            }

            dettagli.add(dettaglio);
        }

        ordine.setDettagliOrdine(dettagli);
        ordine.setTotale(totaleOrdine);
        Ordine ordineSalvato = ordineRepository.save(ordine);

        // 3. Processa il Pagamento
        TipoPagamento tipoPagamento = TipoPagamento.valueOf(request.getTipoPagamento().toUpperCase());
        Pagamento pagamento = new Pagamento(ordineSalvato, totaleOrdine, tipoPagamento, "IN_CORSO");
        String esito = pagamentoService.eseguiPagamento(pagamento);
        pagamento.setEsito(esito);
        pagamentoRepository.save(pagamento);

        // 4. Finalizza l'Ordine e Svuota il Carrello
        if ("APPROVATO".equals(esito)) {
            ordineSalvato.setStato(StatoOrdine.CONFERMATO);
            ordineRepository.save(ordineSalvato);

            // Svuota carrello
            elementoCarrelloRepository.deleteAll(carrello.getElementi());
            carrello.getElementi().clear();
            carrelloRepository.save(carrello);
        } else {
            // Rollback (lo @Transactional aiuta, ma qui dovremmo rimettere lo stock a posto)
            throw new RuntimeException("Pagamento fallito: " + esito);
        }

        return ordineMapper.toDTO(ordineSalvato);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdineDTO> getMieiOrdini() {
        Acquirente acquirente = getAcquirenteAutenticato();
        return ordineRepository.findByAcquirenteId(acquirente.getId())
                .stream()
                .map(ordineMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrdineDTO getDettaglioOrdine(Long ordineId) {
        Acquirente acquirente = getAcquirenteAutenticato();
        Ordine ordine = ordineRepository.findById(ordineId)
                .orElseThrow(() -> new EntityNotFoundException("Ordine non trovato"));

        if (!ordine.getAcquirente().getId().equals(acquirente.getId())) {
            throw new RuntimeException("Accesso non autorizzato all'ordine.");
        }
        return ordineMapper.toDTO(ordine);
    }


    // --- Metodi Privati di Utilità ---

    private Acquirente getAcquirenteAutenticato() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));
        if (!(utente instanceof Acquirente)) {
            throw new RuntimeException("Utente non è un acquirente");
        }
        return (Acquirente) utente;
    }

    private Carrello getOrCreateCarrello(Acquirente acquirente) {
        return carrelloRepository.findByAcquirenteId(acquirente.getId())
                .orElseGet(() -> {
                    Carrello nuovoCarrello = new Carrello(acquirente);
                    return carrelloRepository.save(nuovoCarrello);
                });
    }

    private Prodotto findProdottoById(Long id) {
        return prodottoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prodotto non trovato con ID: " + id));
    }

}