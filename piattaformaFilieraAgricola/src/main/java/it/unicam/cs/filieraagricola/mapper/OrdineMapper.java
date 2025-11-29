package it.unicam.cs.filieraagricola.mapper;

import it.unicam.cs.filieraagricola.DTO.DettaglioOrdineDTO;
import it.unicam.cs.filieraagricola.DTO.OrdineDTO;
import it.unicam.cs.filieraagricola.model.DettaglioOrdine;
import it.unicam.cs.filieraagricola.model.Ordine;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrdineMapper {

    // --- METODO PRINCIPALE (Pubblico) ---
    public OrdineDTO toDTO(Ordine entity) {
        if (entity == null) {
            return null;
        }

        OrdineDTO dto = new OrdineDTO();

        // Mapping dei campi semplici
        dto.setId(entity.getId());
        dto.setDataOrdine(entity.getDataOrdine());
        dto.setTotale(entity.getTotale());

        // Conversione Enum -> String
        if (entity.getStato() != null) {
            dto.setStato(entity.getStato().name());
        }

        // Mapping della lista dei dettagli (Righe dell'ordine)
        if (entity.getDettagliOrdine() != null && !entity.getDettagliOrdine().isEmpty()) {

            // Usiamo il metodo helper privato per convertire ogni riga
            List<DettaglioOrdineDTO> dettagliDTO = entity.getDettagliOrdine().stream()
                    .map(this::toDettaglioDTO)
                    .collect(Collectors.toList());

            dto.setDettagli(dettagliDTO);

        } else {
            // Se l'ordine non ha dettagli (caso raro, ma possibile in fase di init)
            dto.setDettagli(Collections.emptyList());
        }

        return dto;
    }

    // --- METODO DI SUPPORTO (Privato) ---
    private DettaglioOrdineDTO toDettaglioDTO(DettaglioOrdine entity) {
        if (entity == null) {
            return null;
        }

        DettaglioOrdineDTO dto = new DettaglioOrdineDTO();

        dto.setQuantita(entity.getQuantita());

        // IMPORTANTE: Prendiamo il prezzo SALVATO nell'ordine (storico),
        // non quello corrente del prodotto.
        dto.setPrezzoUnitario(entity.getPrezzoUnitario());

        // Recupero dati informativi dal Prodotto collegato
        if (entity.getProdotto() != null) {
            dto.setProdottoId(entity.getProdotto().getId());
            dto.setNomeProdotto(entity.getProdotto().getNome());
        } else {
            dto.setProdottoId(null);
            dto.setNomeProdotto("Prodotto eliminato o sconosciuto");
        }

        return dto;
    }
}