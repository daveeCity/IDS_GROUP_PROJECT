package it.unicam.cs.filieraagricola.factory;

import it.unicam.cs.filieraagricola.DTO.RegisterRequestDTO;
import it.unicam.cs.filieraagricola.model.*;
import org.springframework.stereotype.Component;

@Component // Lo rendiamo un Bean Spring
public class UtenteFactory {

    /**
     * Factory Method per creare un Utente
     * @param dto I dati di registrazione
     * @param hashedPassword La password già codificata
     * @return L'istanza concreta di Utente
     */
    public Utente creaUtente(RegisterRequestDTO dto, String hashedPassword) {

        String ruolo = dto.getRuolo().toUpperCase();

        switch (ruolo) {
            case "ACQUIRENTE":
                return new Acquirente(dto.getUsername(), dto.getEmail(), hashedPassword);

            case "PRODUTTORE":
                return new Produttore(dto.getUsername(), dto.getEmail(), hashedPassword,
                        dto.getNomeAzienda(), dto.getPartitaIva(),
                        dto.getIndirizzo(), dto.getDescrizione());

            case "TRASFORMATORE":
                return new Trasformatore(dto.getUsername(), dto.getEmail(), hashedPassword,
                        dto.getNomeAzienda(), dto.getPartitaIva(),
                        dto.getIndirizzo(), dto.getDescrizione());

            case "DISTRIBUTORE":
                return new Distributore(dto.getUsername(), dto.getEmail(), hashedPassword,
                        dto.getNomeAzienda(), dto.getPartitaIva(),
                        dto.getIndirizzo(), dto.getDescrizione());

            case "CURATORE":
                return new Curatore(dto.getUsername(), dto.getEmail(), hashedPassword);

            case "ANIMATORE":
                return new Animatore(dto.getUsername(), dto.getEmail(), hashedPassword);

            case "GESTORE":
                return new Gestore(dto.getUsername(), dto.getEmail(), hashedPassword);

            default:
                throw new IllegalArgumentException("Ruolo non valido: " + ruolo);
        }
    }
}