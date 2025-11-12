package it.unicam.cs.filieraagricola.service;


import it.unicam.cs.filieraagricola.DTO.PaccoProdottoDTO;
import it.unicam.cs.filieraagricola.DTO.PaccoProdottoRequestDTO;
import java.util.List;

public interface PaccoProdottoService {

    /**
     * Ottiene tutti i pacchetti creati da uno specifico distributore.
     */
    List<PaccoProdottoDTO> getPacchettiByDistributore(Long distributoreId);

    /**
     * Ottiene un pacchetto specifico tramite ID.
     */
    PaccoProdottoDTO getPaccoById(Long paccoId);

    /**
     * Crea un nuovo pacchetto.
     */
    PaccoProdottoDTO creaPacco(PaccoProdottoRequestDTO request, Long distributoreId);

    /**
     * Elimina un pacchetto.
     */
    void deletePacco(Long paccoId);
}