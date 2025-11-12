package it.unicam.cs.filieraagricola.service;

// package it.unicam.cs.filieraagricola.service;

import it.unicam.cs.filieraagricola.DTO.PassoFilieraRequestDTO;
import it.unicam.cs.filieraagricola.DTO.TracciabilitaDTO;

public interface TracciabilitaService {

    /**
     * Ottiene l'intera filiera di tracciabilità per un dato prodotto.
     * (Corrisponde all'endpoint GET /api/products/{id}/trace)
     */
    TracciabilitaDTO getTracciabilitaByProdotto(Long prodottoId);

    /**
     * Aggiunge un nuovo passo alla filiera di un prodotto.
     * (Eseguito da Produttore o Trasformatore)
     *
     * @param prodottoId L'ID del prodotto a cui aggiungere il passo
     * @param aziendaId L'ID dell'azienda (Prod/Trasf) che aggiunge il passo
     * @param request DTO con i dettagli del passo
     */
    TracciabilitaDTO aggiungiPassoFiliera(Long prodottoId, Long aziendaId, PassoFilieraRequestDTO request);
}