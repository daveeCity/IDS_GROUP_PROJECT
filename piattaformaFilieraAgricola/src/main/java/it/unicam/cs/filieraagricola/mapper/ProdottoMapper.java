package it.unicam.cs.filieraagricola.mapper;

import it.unicam.cs.filieraagricola.DTO.ProdottoDTO;
import it.unicam.cs.filieraagricola.model.Prodotto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ProdottoMapper {

    public ProdottoDTO toDTO(Prodotto entity) {
        if (entity == null) {
            return null;
        }

        ProdottoDTO dto = new ProdottoDTO();

        // Mapping dei campi semplici
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setDescrizione(entity.getDescrizione());
        dto.setPrezzo(entity.getPrezzo());
        dto.setQuantitaDisponibile(entity.getQuantitaDisponibile());

        // Mapping dell'Enum a String
        if (entity.getStato() != null) {
            dto.setStato(entity.getStato().name());
        }

        // Mapping della relazione Azienda (appiattimento dei dati)
        if (entity.getAzienda() != null) {
            dto.setAziendaId(entity.getAzienda().getId());
            dto.setNomeAzienda(entity.getAzienda().getNomeAzienda());
        }

        return dto;
    }

    // Opzionale: Metodo inverso (da DTO a Entity)
    // Utile quando devi creare un nuovo prodotto partendo da un JSON
    /*
    public Prodotto toEntity(ProdottoDTO dto) {
        if (dto == null) return null;

        Prodotto prodotto = new Prodotto();
        prodotto.setNome(dto.getNome());
        prodotto.setDescrizione(dto.getDescrizione());
        prodotto.setPrezzo(dto.getPrezzo());
        // Nota: Azienda e Stato vanno gestiti logicamente nel Service
        // recuperando l'azienda dal DB tramite aziendaId

        return prodotto;
    }
    */
}