package main.java.it.unicam.cs.filieraagricola.service;

import main.java.it.unicam.cs.filieraagricola.model.*;
import main.java.it.unicam.cs.filieraagricola.observer.ModerationSubject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServizioModerazioni extends ModerationSubject {
    private Map<Long, StatoProdotto> statiProdotti = new HashMap<>();
    private Map<Long, String> motiviRifiuto = new HashMap<>();
    private List<Product> prodottiInRevisione = new ArrayList<>();
    
    public void sottomettiPerRevisione(Product prodotto) {
        statiProdotti.put(prodotto.getId(), StatoProdotto.IN_REVISIONE);
        
        if (!prodottiInRevisione.contains(prodotto)) {
            prodottiInRevisione.add(prodotto);
        }
        
        notifyProductSubmittedForReview(prodotto);
        System.out.println("📋 Prodotto sottomesso per revisione: " + prodotto.getName());
    }
    
    public void approvaProdotto(Product prodotto, Curatore curatore) {
        if (!statiProdotti.containsKey(prodotto.getId())) {
            throw new IllegalStateException("Prodotto non in revisione");
        }
        
        statiProdotti.put(prodotto.getId(), StatoProdotto.PUBBLICATO);
        prodottiInRevisione.remove(prodotto);
        curatore.approvaProdotto(prodotto.getId());
        
        notifyProductApproved(prodotto);
        System.out.println("✅ Prodotto approvato da " + curatore.getName() + ": " + prodotto.getName());
    }
    
    public void respingiProdotto(Product prodotto, Curatore curatore, String motivo) {
        if (!statiProdotti.containsKey(prodotto.getId())) {
            throw new IllegalStateException("Prodotto non in revisione");
        }
        
        statiProdotti.put(prodotto.getId(), StatoProdotto.RESPINTO);
        motiviRifiuto.put(prodotto.getId(), motivo);
        prodottiInRevisione.remove(prodotto);
        curatore.respingiProdotto(prodotto.getId(), motivo);
        
        notifyProductRejected(prodotto, motivo);
        System.out.println("❌ Prodotto respinto da " + curatore.getName() + ": " + prodotto.getName() + " - " + motivo);
    }
    
    public StatoProdotto getStatoProdotto(Long prodottoId) {
        return statiProdotti.getOrDefault(prodottoId, StatoProdotto.BOZZA);
    }
    
    public String getMotivoRifiuto(Long prodottoId) {
        return motiviRifiuto.get(prodottoId);
    }
    
    public List<Product> getProdottiInRevisione() {
        return new ArrayList<>(prodottiInRevisione);
    }
    
    public List<Product> getProdottiApprovati() {
        List<Product> approvati = new ArrayList<>();
        for (Product prodotto : prodottiInRevisione) {
            if (getStatoProdotto(prodotto.getId()) == StatoProdotto.PUBBLICATO) {
                approvati.add(prodotto);
            }
        }
        return approvati;
    }
    
    public List<Product> getProdottiRespinti() {
        List<Product> respinti = new ArrayList<>();
        for (Map.Entry<Long, StatoProdotto> entry : statiProdotti.entrySet()) {
            if (entry.getValue() == StatoProdotto.RESPINTO) {
                // Trova il prodotto corrispondente (semplificazione)
                for (Product prodotto : prodottiInRevisione) {
                    if (prodotto.getId().equals(entry.getKey())) {
                        respinti.add(prodotto);
                        break;
                    }
                }
            }
        }
        return respinti;
    }
    
    public boolean isProdottoApprovato(Long prodottoId) {
        return getStatoProdotto(prodottoId) == StatoProdotto.PUBBLICATO;
    }
    
    public boolean isProdottoRespinto(Long prodottoId) {
        return getStatoProdotto(prodottoId) == StatoProdotto.RESPINTO;
    }
}