package main.java.it.unicam.cs.filieraagricola.observer;

import main.java.it.unicam.cs.filieraagricola.model.Product;
import java.time.LocalDateTime;

public class EmailNotificationObserver implements ModerationObserver {
    private String emailService;
    
    public EmailNotificationObserver(String emailService) {
        this.emailService = emailService;
    }
    
    @Override
    public void onProductApproved(Product product) {
        sendEmail(
            product.getProducer().getName(),
            "Prodotto Approvato",
            "Il tuo prodotto '" + product.getName() + "' è stato approvato e pubblicato nel catalogo."
        );
        System.out.println("📧 Email inviata per approvazione prodotto: " + product.getName());
    }
    
    @Override
    public void onProductRejected(Product product, String reason) {
        sendEmail(
            product.getProducer().getName(),
            "Prodotto Respinto",
            "Il tuo prodotto '" + product.getName() + "' è stato respinto. Motivo: " + reason
        );
        System.out.println("📧 Email inviata per rifiuto prodotto: " + product.getName() + " - " + reason);
    }
    
    @Override
    public void onProductSubmittedForReview(Product product) {
        sendEmail(
            "curatori@piattaforma.it",
            "Nuovo Prodotto in Revisione",
            "Un nuovo prodotto '" + product.getName() + "' è stato sottomesso per la revisione."
        );
        System.out.println("📧 Email inviata ai curatori per nuovo prodotto: " + product.getName());
    }
    
    private void sendEmail(String to, String subject, String body) {
        System.out.println("📨 [" + LocalDateTime.now() + "] Invio email tramite " + emailService);
        System.out.println("   To: " + to);
        System.out.println("   Subject: " + subject);
        System.out.println("   Body: " + body);
    }
}