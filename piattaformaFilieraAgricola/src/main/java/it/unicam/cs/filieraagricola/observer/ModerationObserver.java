package main.java.it.unicam.cs.filieraagricola.observer;

import main.java.it.unicam.cs.filieraagricola.model.Product;

public interface ModerationObserver {
    void onProductApproved(Product product);
    void onProductRejected(Product product, String reason);
    void onProductSubmittedForReview(Product product);
}