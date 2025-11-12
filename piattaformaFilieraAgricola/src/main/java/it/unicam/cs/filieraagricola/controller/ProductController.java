package it.unicam.cs.filieraagricola.controller;

import it.unicam.cs.filieraagricola.model.Prodotto;
import it.unicam.cs.filieraagricola.model.StatoProdotto;
import it.unicam.cs.filieraagricola.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Prodotto>> getAllProducts() {
        List<Prodotto> products = productService.getAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prodotto> getProductById(@PathVariable Long id) {
        try {
            Prodotto product = productService.getById(id);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Prodotto> createProduct(@Valid @RequestBody Prodotto product) {
        Prodotto createdProduct = productService.create(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prodotto> updateProduct(@PathVariable Long id,
                                                  @Valid @RequestBody Prodotto product) {
        try {
            Prodotto updatedProduct = productService.update(id, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Prodotto>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String certification,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) StatoProdotto status) {
        
        List<Prodotto> products;
        
        if (name != null) {
            products = productService.findByName(name);
        } else if (certification != null) {
            products = productService.findByCertification(certification);
        } else if (maxPrice != null) {
            products = productService.findByMaxPrice(maxPrice);
        } else if (status != null) {
            products = productService.findByStatus(status);
        } else {
            products = productService.getAll();
        }
        
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Prodotto> approveProduct(@PathVariable Long id) {
        try {
            Prodotto approvedProduct = productService.approveProduct(id);
            return ResponseEntity.ok(approvedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Prodotto> rejectProduct(@PathVariable Long id) {
        try {
            Prodotto rejectedProduct = productService.rejectProduct(id);
            return ResponseEntity.ok(rejectedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}