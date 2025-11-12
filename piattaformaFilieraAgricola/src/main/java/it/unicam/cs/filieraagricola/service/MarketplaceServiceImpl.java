package it.unicam.cs.filieraagricola.service;
import it.unicam.cs.filieraagricola.DTO.*;
import it.unicam.cs.filieraagricola.model.*;
import it.unicam.cs.filieraagricola.repository.*;
import it.unicam.cs.filieraagricola.service.pagamento.PagamentoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarketplaceServiceImpl implements MarketplaceService {

    @Autowired private CarrelloRepository carrelloRepository;
    @Autowired private ElementoCarrelloRepository elementoCarrelloRepository;
    @Autowired private ProdottoRepository prodottoRepository;
    @Autowired private UtenteRepository utenteRepository;
    @Autowired private OrdineRepository ordineRepository;
    @Autowired private PagamentoRepository pagamentoRepository;
    @Autowired private PagamentoService pagamentoService;
    @Autowired private MarketplaceMapper marketplaceMapper; // Mapper DTO

    @Override
    @Transactional(readOnly = true)
    public CarrelloDTO getCarrello() {
        Acquirente acquirente = getAcquirenteAutenticato();
        Carrello carrello = getOrCreateCarrello(acquirente);
        return marketplaceMapper.toCarrelloDTO(carrello);
    }

    @Override
    @Transactional
    public CarrelloDTO aggiungiAlCarrello(AggiungiAlCarrelloRequestDTO request) {
        Acquirente acquirente = getAcquirenteAutenticato();
        Carrello carrello = getOrCreateCarrello(acquirente);
        Prodotto prodotto = findProdottoById(request.getProdottoId());

        if (prodotto.getStato() != StatoProdotto.APPROVATO) {
            throw new RuntimeException("Prodotto non disponibile per la vendita.");
        }
        if (prodotto.getQuantitaDisponibile() < request.getQuantita()) {
            throw new RuntimeException("Quantità non disponibile a magazzino.");
        }

        // Cerca se l'elemento è già nel carrello
        ElementoCarrello elemento = elementoCarrelloRepository
                .findByCarrelloIdAndProdottoId(carrello.getId(), prodotto.getId())
                .orElse(new ElementoCarrello(carrello, prodotto, 0));

        elemento.setQuantita(elemento.getQuantita() + request.getQuantita());
        elementoCarrelloRepository.save(elemento);

        // Aggiorna la lista nel carrello (se è un nuovo elemento)
        if (!carrello.getElementi().contains(elemento)) {
            carrello.getElementi().add(elemento);
        }

        return marketplaceMapper.toCarrelloDTO(carrello);
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

        return marketplaceMapper.toCarrelloDTO(carrello);
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
        ordine.setStato(StatoOrdine.PENDENTE);

        double totaleOrdine = 0.0;
        List<DettaglioOrdine> dettagli = new ArrayList<>();

        // 2. Convalida lo stock e crea i dettagli ordine
        for (ElementoCarrello elemento : carrello.getElementi()) {
            Prodotto prodotto = elemento.getProdotto();
            if (prodotto.getQuantitaDisponibile() < elemento.getQuantita()) {
                throw new RuntimeException("Stock non sufficiente per: " + prodotto.getNome());
            }
            // Blocca lo stock
            prodotto.setQuantitaDisponibile(prodotto.getQuantitaDisponibile() - elemento.getQuantita());
            prodottoRepository.save(prodotto);

            DettaglioOrdine dettaglio = new DettaglioOrdine(ordine, prodotto, elemento.getQuantita());
            dettagli.add(dettaglio);

            totaleOrdine += dettaglio.getPrezzoUnitario() * dettaglio.getQuantita();
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

        return marketplaceMapper.toOrdineDTO(ordineSalvato);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdineDTO> getMieiOrdini() {
        Acquirente acquirente = getAcquirenteAutenticato();
        return ordineRepository.findByAcquirenteId(acquirente.getId())
                .stream()
                .map(marketplaceMapper::toOrdineDTO)
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
        return marketplaceMapper.toOrdineDTO(ordine);
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

    // Nota: 'MarketplaceMapper' è un bean che converte Entità <-> DTO
}