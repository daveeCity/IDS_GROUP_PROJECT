package it.unicam.cs.filieraagricola.service;

import it.unicam.cs.filieraagricola.DTO.EventoDTO;
import it.unicam.cs.filieraagricola.DTO.ModerazioneRequestDTO;
import it.unicam.cs.filieraagricola.DTO.PassoFilieraDTO;
import it.unicam.cs.filieraagricola.DTO.ProdottoDTO;
import java.util.List;

public interface ModerazioneService {

    /**
     * Ottiene la lista di tutti i prodotti in attesa di moderazione.
     */
    List<ProdottoDTO> getProdottiInAttesa();

    /**
     * Approva un prodotto.
     * @param prodottoId L'ID del prodotto da approvare
     * @return Il ProdottoDTO aggiornato
     */
    ProdottoDTO approvaProdotto(Long prodottoId);

    /**
     * Rifiuta un prodotto.
     * @param prodottoId L'ID del prodotto da rifiutare
     * @param request Il DTO con la motivazione del rifiuto
     * @return Il ProdottoDTO aggiornato
     */
    ProdottoDTO rifiutaProdotto(Long prodottoId, ModerazioneRequestDTO request);

    List<EventoDTO> getEventiInAttesa();
    EventoDTO approvaEvento(Long id);
    EventoDTO rifiutaEvento(Long id);


    List<PassoFilieraDTO> getPassiInAttesa();
    PassoFilieraDTO approvaPasso(Long id);
    PassoFilieraDTO rifiutaPasso(Long id);
}