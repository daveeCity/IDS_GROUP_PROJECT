package it.unicam.cs.filieraagricola.factory;

import it.unicam.cs.filieraagricola.DTO.RegisterRequestDTO;
import it.unicam.cs.filieraagricola.model.*;
import org.springframework.stereotype.Component;

@Component
public class UtenteFactory {

    public Utente creaUtente(RegisterRequestDTO dto, String hashedPassword) {

        String ruolo = dto.getRuolo().toUpperCase();

        switch (ruolo) {
            case "ACQUIRENTE":
                return new Acquirente(dto.getUsername(), dto.getEmail(), hashedPassword);

            case "PRODUTTORE":
                // 1. Creiamo l'oggetto usando il costruttore ESISTENTE (senza lat/long)
                Produttore produttore = new Produttore(
                        dto.getUsername(),
                        dto.getEmail(),
                        hashedPassword,
                        dto.getNomeAzienda(),
                        dto.getPartitaIva(),
                        dto.getIndirizzo(),
                        dto.getDescrizione(),
                        dto.getLongitudine(),
                        dto.getLatitudine()
                );

                // 2. Impostiamo le coordinate separatamente (se presenti nel DTO)
                if (dto.getLatitudine() != null) produttore.setLatitudine(dto.getLatitudine());
                if (dto.getLongitudine() != null) produttore.setLongitudine(dto.getLongitudine());

                return produttore;

            case "TRASFORMATORE":
                Trasformatore trasformatore = new Trasformatore(
                        dto.getUsername(),
                        dto.getEmail(),
                        hashedPassword,
                        dto.getNomeAzienda(),
                        dto.getPartitaIva(),
                        dto.getIndirizzo(),
                        dto.getDescrizione(),
                        dto.getLongitudine(),
                        dto.getLatitudine()
                );

                // Anche i trasformatori hanno una sede
                if (dto.getLatitudine() != null) trasformatore.setLatitudine(dto.getLatitudine());
                if (dto.getLongitudine() != null) trasformatore.setLongitudine(dto.getLongitudine());

                return trasformatore;

            case "DISTRIBUTORE":
                Distributore distributore = new Distributore(
                        dto.getUsername(),
                        dto.getEmail(),
                        hashedPassword,
                        dto.getNomeAzienda(),
                        dto.getPartitaIva(),
                        dto.getIndirizzo(),
                        dto.getDescrizione(),
                        dto.getLongitudine(),
                        dto.getLatitudine()
                );

                // Anche i distributori hanno una sede
                if (dto.getLatitudine() != null) distributore.setLatitudine(dto.getLatitudine());
                if (dto.getLongitudine() != null) distributore.setLongitudine(dto.getLongitudine());

                return distributore;

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