package it.unicam.cs.filieraagricola.observer;

import it.unicam.cs.filieraagricola.model.Prodotto; //

public interface ModerationSubject {
    void addObserver(ModerationObserver observer);
    void removeObserver(ModerationObserver observer);
    void notifyObservers(Prodotto prodotto, String action); //
}