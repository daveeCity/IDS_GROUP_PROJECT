package it.unicam.cs.filieraagricola.mapper;

import it.unicam.cs.filieraagricola.DTO.ElementoCarrelloDTO;
import it.unicam.cs.filieraagricola.model.ElementoCarrello;
import org.springframework.stereotype.Component;

@Component
public class ElementoCarrelloMapper {

    public ElementoCarrelloDTO toDTO(ElementoCarrello entity) {
        if (entity == null) {
            return null;
        }

        ElementoCarrelloDTO dto = new ElementoCarrelloDTO();

        // Mappiamo l'ID della riga (fondamentale per delete/update)
        dto.setId(entity.getId());

        dto.setQuantita(entity.getQuantita());

        // Controllo di sicurezza sul prodotto
        if (entity.getProdotto() != null) {
            dto.setProdottoId(entity.getProdotto().getId());
            dto.setNomeProdotto(entity.getProdotto().getNome());
            dto.setPrezzoUnitario(entity.getProdotto().getPrezzo());

            // Calcolo del totale (Prezzo * Quantità)
            double totaleCalcolato = entity.getProdotto().getPrezzo() * entity.getQuantita();
            dto.setTotaleElemento(totaleCalcolato);
        } else {
            // Fallback se il prodotto è stato eliminato dal DB
            dto.setNomeProdotto("Prodotto non disponibile");
            dto.setPrezzoUnitario(0.0);
            dto.setTotaleElemento(0.0);
        }

        return dto;
    }
}