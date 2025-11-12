package it.unicam.cs.filieraagricola.observer;


import it.unicam.cs.filieraagricola.model.Prodotto;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class LoggingObserver implements ModerationObserver {

    @Override
    public void update(Prodotto prodotto, String action) {
        // Logica fittizia di logging
        System.out.println("[LOG] " + LocalDateTime.now() +
                " - Moderazione: Prodotto ID " + prodotto.getId() +
                " (" + prodotto.getNome() + ")" +
                " è stato " + action +
                " dal curatore.");
    }
}