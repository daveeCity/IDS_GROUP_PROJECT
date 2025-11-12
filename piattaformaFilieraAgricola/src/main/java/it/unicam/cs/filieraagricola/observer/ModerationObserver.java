package it.unicam.cs.filieraagricola.observer;



import it.unicam.cs.filieraagricola.model.Prodotto;

public interface ModerationObserver {
    /**
     * Metodo chiamato dal Subject quando lo stato di un prodotto cambia.
     * @param prodotto L'entità prodotto aggiornata
     * @param action L'azione eseguita (es. "approvato", "rifiutato")
     */
    void update(Prodotto prodotto, String action);
}