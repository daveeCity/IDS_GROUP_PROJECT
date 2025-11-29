package it.unicam.cs.filieraagricola.mapper;

import it.unicam.cs.filieraagricola.DTO.CarrelloDTO;
import it.unicam.cs.filieraagricola.DTO.ElementoCarrelloDTO;
import it.unicam.cs.filieraagricola.model.Carrello;
import it.unicam.cs.filieraagricola.model.ElementoCarrello;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CarrelloMapper {

    // --- METODO PRINCIPALE (Pubblico) ---
    public CarrelloDTO toDTO(Carrello entity) {
        if (entity == null) {
            return null;
        }

        CarrelloDTO dto = new CarrelloDTO();

        // Controlliamo se ci sono elementi nel carrello
        if (entity.getElementi() != null && !entity.getElementi().isEmpty()) {

            // 1. Convertiamo la lista di entità in lista di DTO
            // Usiamo il metodo privato 'toElementoDTO' definito qui sotto
            List<ElementoCarrelloDTO> elementiDTO = entity.getElementi().stream()
                    .map(this::toElementoDTO)
                    .collect(Collectors.toList());

            dto.setElementi(elementiDTO);

            // 2. Calcoliamo il totale complessivo sommando i totali delle singole righe
            double totaleComplessivo = elementiDTO.stream()
                    .mapToDouble(ElementoCarrelloDTO::getTotaleElemento)
                    .sum();

            dto.setTotaleCarrello(totaleComplessivo);

        } else {
            // Se il carrello è vuoto
            dto.setElementi(Collections.emptyList());
            dto.setTotaleCarrello(0.0);
        }

        return dto;
    }

    // --- METODO DI SUPPORTO (Privato) ---
    // Serve per convertire la singola riga del carrello
    private ElementoCarrelloDTO toElementoDTO(ElementoCarrello entity) {
        if (entity == null) {
            return null;
        }

        ElementoCarrelloDTO dto = new ElementoCarrelloDTO();

        // Dati strutturali
        dto.setId(entity.getId());
        dto.setQuantita(entity.getQuantita());

        // Dati del prodotto e calcolo prezzi
        if (entity.getProdotto() != null) {
            dto.setProdottoId(entity.getProdotto().getId());
            dto.setNomeProdotto(entity.getProdotto().getNome());
            dto.setPrezzoUnitario(entity.getProdotto().getPrezzo());

            // Calcolo: Prezzo Unitario * Quantità
            double totaleRiga = entity.getProdotto().getPrezzo() * entity.getQuantita();
            dto.setTotaleElemento(totaleRiga);
        } else {
            // Fallback se il prodotto non esiste più
            dto.setNomeProdotto("Prodotto non disponibile");
            dto.setPrezzoUnitario(0.0);
            dto.setTotaleElemento(0.0);
        }

        return dto;
    }
}