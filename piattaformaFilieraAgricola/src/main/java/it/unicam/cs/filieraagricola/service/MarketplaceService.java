package it.unicam.cs.filieraagricola.service;

import it.unicam.cs.filieraagricola.DTO.*;
import java.util.List;

public interface MarketplaceService {

    /**
     * Ottiene il carrello dell'acquirente attualmente autenticato.
     */
    CarrelloDTO getCarrello();

    /**
     * Aggiunge un prodotto al carrello dell'acquirente autenticato.
     */
    CarrelloDTO aggiungiAlCarrello(AggiungiAlCarrelloRequestDTO request);

    /**
     * Rimuove un prodotto dal carrello.
     */
    CarrelloDTO rimuoviDalCarrello(Long prodottoId);

    /**
     * Esegue il checkout: converte il carrello in ordine,
     * processa il pagamento e svuota il carrello.
     */
    OrdineDTO checkout(CheckoutRequestDTO request);

    /**
     * Ottiene la lista degli ordini dell'acquirente autenticato.
     */
    List<OrdineDTO> getMieiOrdini();

    /**
     * Ottiene i dettagli di un singolo ordine.
     */
    OrdineDTO getDettaglioOrdine(Long ordineId);
}