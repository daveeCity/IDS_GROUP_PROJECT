package main.java.it.unicam.cs.filieraagricola.service;

import main.java.it.unicam.cs.filieraagricola.model.Product;
import main.java.it.unicam.cs.filieraagricola.repository.ProductRepository;

import java.util.List;

public class ProductService {
    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> getAll() {
        return repo.findAll();
    }

    public Product getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Prodotto non trovato"));
    }

    public Product create(Product product) {
        return repo.save(product);
    }

    public Product update(Long id, Product product) {
        Product existing = getById(id);
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setCertification(product.getCertification());
        return repo.save(existing);
    }

    public void delete(Long id) {
        repo.delete(id);
    }
}
