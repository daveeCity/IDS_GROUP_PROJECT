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
            dto.setNomeProdotto(entity.getProdotto().getNome()); // Magari rinomina il campo DTO in "nomeOggetto" se vuoi essere pulito, o usa questo
            dto.setPrezzoUnitario(entity.getProdotto().getPrezzo());
        } else if (entity.getPacco() != null) {
            // Usiamo gli stessi campi del DTO riciclandoli, oppure ne aggiungi di nuovi
            dto.setProdottoId(entity.getPacco().getId()); // Qui metti ID pacco
            dto.setNomeProdotto("[PACCO] " + entity.getPacco().getNome());
            dto.setPrezzoUnitario(entity.getPacco().getPrezzo());
        }

        dto.setTotaleElemento(dto.getPrezzoUnitario() * entity.getQuantita());
        return dto;
    }
}