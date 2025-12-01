package it.unicam.cs.filieraagricola.service;

import it.unicam.cs.filieraagricola.DTO.SocialShareDTO;
import it.unicam.cs.filieraagricola.model.Azienda;
import it.unicam.cs.filieraagricola.model.Evento;
import it.unicam.cs.filieraagricola.model.Prodotto;
import it.unicam.cs.filieraagricola.model.StatoApprovazione;
import it.unicam.cs.filieraagricola.repository.AziendaRepository;
import it.unicam.cs.filieraagricola.repository.EventoRepository;
import it.unicam.cs.filieraagricola.repository.ProdottoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SocialSharingServiceImpl implements SocialSharingService {

    @Autowired private ProdottoRepository prodottoRepository;
    @Autowired private EventoRepository eventoRepository;
    @Autowired private AziendaRepository aziendaRepository;

    // In un'app reale, questo verrebbe da application.properties
    private final String BASE_URL_FRONTEND = "http://localhost:4200";

    @Override
    public SocialShareDTO getShareData(String tipo, Long id) {

        switch (tipo.toLowerCase()) {
            case "prodotto":
                return generaPerProdotto(id);

            case "evento":
                return generaPerEvento(id);

            case "azienda":
                return generaPerAzienda(id);

            default:
                throw new IllegalArgumentException("Tipo contenuto non supportato per la condivisione: " + tipo);
        }
    }

    private SocialShareDTO generaPerProdotto(Long id) {
        Prodotto p = prodottoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prodotto non trovato"));

        // Controllo se è pubblico (opzionale, ma consigliato)
        if (p.getStato() != StatoApprovazione.APPROVATO) {
            throw new IllegalArgumentException("Non puoi condividere un prodotto non approvato!");
        }

        String link = BASE_URL_FRONTEND + "/prodotti/" + id;
        String messaggio = "🍏 Guarda " + p.getNome() + " di " + p.getAzienda().getNomeAzienda() + " su Filiera Agricola! #km0 #biologico";

        return new SocialShareDTO(
                p.getNome(),
                p.getDescrizione(),
                link,
                messaggio
        );
    }

    private SocialShareDTO generaPerEvento(Long id) {
        Evento e = eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento non trovato"));

        if (e.getStatoApprovazione() != StatoApprovazione.APPROVATO) {
            throw new IllegalArgumentException("Evento non ancora pubblico.");
        }

        String link = BASE_URL_FRONTEND + "/eventi/" + id;
        String messaggio = "🎉 Partecipa all'evento " + e.getTitolo() + " a " + e.getLuogo() + "! Scopri di più su Filiera Agricola.";

        return new SocialShareDTO(
                e.getTitolo(),
                e.getDescrizione(),
                link,
                messaggio
        );
    }

    private SocialShareDTO generaPerAzienda(Long id) {
        Azienda a = aziendaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Azienda non trovata"));

        String link = BASE_URL_FRONTEND + "/aziende/" + id;
        String messaggio = "🚜 Visita la vetrina di " + a.getNomeAzienda() + ". Prodotti locali e genuini!";

        return new SocialShareDTO(
                a.getNomeAzienda(),
                a.getDescrizione(),
                link,
                messaggio
        );
    }
}