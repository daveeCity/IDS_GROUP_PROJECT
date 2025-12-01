package it.unicam.cs.filieraagricola.service;

import it.unicam.cs.filieraagricola.DTO.SocialShareDTO;

public interface SocialSharingService {

    /**
     * Genera i metadati per la condivisione social.
     * @param tipo Il tipo di entità ("prodotto", "evento", "azienda")
     * @param id L'ID dell'entità
     * @return DTO con link e testi formattati
     */
    SocialShareDTO getShareData(String tipo, Long id);
}