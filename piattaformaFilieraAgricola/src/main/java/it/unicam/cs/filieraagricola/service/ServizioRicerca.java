package main.java.it.unicam.cs.filieraagricola.service;

import main.java.it.unicam.cs.filieraagricola.model.Product;
import main.java.it.unicam.cs.filieraagricola.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ServizioRicerca {
    private final ProductRepository productRepository;
    
    public ServizioRicerca(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    public List<Product> ricercaPerNome(String nome) {
        return productRepository.findAll().stream()
                .filter(p -> p.getName().toLowerCase().contains(nome.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    public List<Product> ricercaPerCertificazione(String certificazione) {
        return productRepository.findAll().stream()
                .filter(p -> p.getCertification().toLowerCase().contains(certificazione.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    public List<Product> ricercaPerProduttore(String nomeProduttore) {
        return productRepository.findAll().stream()
                .filter(p -> p.getProducer().getName().toLowerCase().contains(nomeProduttore.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    public List<Product> ricercaPerPrezzoMassimo(double prezzoMax) {
        return productRepository.findAll().stream()
                .filter(p -> p.getPrice() <= prezzoMax)
                .collect(Collectors.toList());
    }
    
    public List<Product> ricercaPerRangePrezzo(double prezzoMin, double prezzoMax) {
        return productRepository.findAll().stream()
                .filter(p -> p.getPrice() >= prezzoMin && p.getPrice() <= prezzoMax)
                .collect(Collectors.toList());
    }
    
    public List<Product> ricercaAvanzata(String nome, String certificazione, String produttore, 
                                        Double prezzoMin, Double prezzoMax) {
        return productRepository.findAll().stream()
                .filter(p -> nome == null || p.getName().toLowerCase().contains(nome.toLowerCase()))
                .filter(p -> certificazione == null || p.getCertification().toLowerCase().contains(certificazione.toLowerCase()))
                .filter(p -> produttore == null || p.getProducer().getName().toLowerCase().contains(produttore.toLowerCase()))
                .filter(p -> prezzoMin == null || p.getPrice() >= prezzoMin)
                .filter(p -> prezzoMax == null || p.getPrice() <= prezzoMax)
                .collect(Collectors.toList());
    }
    
    public List<Product> getTuttiProdotti() {
        return productRepository.findAll();
    }
}