package it.unicam.cs.filieraagricola.observer;



import it.unicam.cs.filieraagricola.model.Azienda;
import it.unicam.cs.filieraagricola.model.Prodotto;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationObserver implements ModerationObserver {

    @Override
    public void update(Prodotto prodotto, String action) {
        Azienda azienda = prodotto.getAzienda();
        String emailAzienda = azienda.getEmail();
        String nomeProdotto = prodotto.getNome();

        String subject = "Aggiornamento Moderazione Prodotto: " + nomeProdotto;
        String message = "Gentile " + azienda.getNomeAzienda() + ",\n\n" +
                "Il tuo prodotto '" + nomeProdotto + "' (ID: " + prodotto.getId() + ") " +
                "è stato " + action + ".\n\n" +
                "Saluti,\nIl Team della Piattaforma";

        // Logica fittizia per l'invio email
        System.out.println("--- INVIO EMAIL ---");
        System.out.println("A: " + emailAzienda);
        System.out.println("Oggetto: " + subject);
        System.out.println("Messaggio: " + message);
        System.out.println("-------------------");
    }
}