package main.java.it.unicam.cs.filieraagricola.repository;

import com.filieraagricola.model.Product;
import java.util.*;

public class ProductRepository {

    private final Map<Long, Product> database = new HashMap<>();

    public List<Product> findAll() {
        return new ArrayList<>(database.values());
    }

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(database.get(id));
    }

    public Product save(Product product) {
        database.put(product.getId(), product);
        return product;
    }

    public void delete(Long id) {
        database.remove(id);
    }
}
