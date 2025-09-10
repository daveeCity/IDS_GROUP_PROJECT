package main.java.it.unicam.cs.filieraagricola.observer;

import main.java.it.unicam.cs.filieraagricola.model.Product;
import java.util.ArrayList;
import java.util.List;

public class ModerationSubject {
    private List<ModerationObserver> observers = new ArrayList<>();
    
    public void addObserver(ModerationObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    public void removeObserver(ModerationObserver observer) {
        observers.remove(observer);
    }
    
    public void notifyProductApproved(Product product) {
        for (ModerationObserver observer : observers) {
            observer.onProductApproved(product);
        }
    }
    
    public void notifyProductRejected(Product product, String reason) {
        for (ModerationObserver observer : observers) {
            observer.onProductRejected(product, reason);
        }
    }
    
    public void notifyProductSubmittedForReview(Product product) {
        for (ModerationObserver observer : observers) {
            observer.onProductSubmittedForReview(product);
        }
    }
    
    public int getObserverCount() {
        return observers.size();
    }
}