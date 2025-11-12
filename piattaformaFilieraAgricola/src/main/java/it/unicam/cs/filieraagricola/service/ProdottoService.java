package it.unicam.cs.filieraagricola.service;
import it.unicam.cs.filieraagricola.DTO.ProdottoDTO;
import it.unicam.cs.filieraagricola.DTO.ProdottoRequestDTO;
import java.util.List;

public interface ProdottoService {

    /**
     * Mostra il catalogo pubblico (solo prodotti approvati).
     */
    List<ProdottoDTO> getCatalogoProdotti();

    /**
     * Trova un singolo prodotto tramite ID.
     */
    ProdottoDTO getProdottoById(Long id);

    /**
     * Ricerca prodotti per nome (sostituisce ServizioRicerca).
     */
    List<ProdottoDTO> cercaProdottiPerNome(String nome);

    /**
     * Trova tutti i prodotti di una specifica azienda (per la dashboard dell'azienda).
     */
    List<ProdottoDTO> getProdottiByAziendaId(Long aziendaId);

    /**
     * Crea un nuovo prodotto (caricamento prodotto).
     * @param request DTO con i dati del prodotto
     * @param aziendaId ID dell'azienda che sta creando il prodotto
     */
    ProdottoDTO creaProdotto(ProdottoRequestDTO request, Long aziendaId);

    /**
     * Aggiorna un prodotto esistente.
     */
    ProdottoDTO updateProdotto(Long prodottoId, ProdottoRequestDTO request);

    /**
     * Elimina un prodotto.
     */
    void deleteProdotto(Long prodottoId);

    // Nota: I metodi per la moderazione (approva, rifiuta)
    // andranno nel ServizioModerazioni.
}