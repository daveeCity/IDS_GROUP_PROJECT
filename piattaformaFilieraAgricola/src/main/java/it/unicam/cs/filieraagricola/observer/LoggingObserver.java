package main.java.it.unicam.cs.filieraagricola.observer;

import main.java.it.unicam.cs.filieraagricola.model.Product;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LoggingObserver implements ModerationObserver {
    private List<String> logs = new ArrayList<>();
    
    @Override
    public void onProductApproved(Product product) {
        String logEntry = "[" + LocalDateTime.now() + "] APPROVATO: Prodotto '" + 
                         product.getName() + "' (ID: " + product.getId() + ") approvato";
        logs.add(logEntry);
        System.out.println("📝 " + logEntry);
    }
    
    @Override
    public void onProductRejected(Product product, String reason) {
        String logEntry = "[" + LocalDateTime.now() + "] RESPINTO: Prodotto '" + 
                         product.getName() + "' (ID: " + product.getId() + ") respinto - Motivo: " + reason;
        logs.add(logEntry);
        System.out.println("📝 " + logEntry);
    }
    
    @Override
    public void onProductSubmittedForReview(Product product) {
        String logEntry = "[" + LocalDateTime.now() + "] IN_REVISIONE: Prodotto '" + 
                         product.getName() + "' (ID: " + product.getId() + ") sottomesso per revisione";
        logs.add(logEntry);
        System.out.println("📝 " + logEntry);
    }
    
    public List<String> getLogs() {
        return new ArrayList<>(logs);
    }
    
    public void clearLogs() {
        logs.clear();
    }
    
    public int getLogCount() {
        return logs.size();
    }
}