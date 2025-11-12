package it.unicam.cs.filieraagricola.controller;

import it.unicam.cs.filieraagricola.model.PaccoProdotto;
import it.unicam.cs.filieraagricola.service.ProductPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/packages")
@CrossOrigin(origins = "*")
public class ProductPackageController {

    @Autowired
    private ProductPackageService packageService;

    @GetMapping
    public ResponseEntity<List<PaccoProdotto>> getAllPackages() {
        List<PaccoProdotto> packages = packageService.getAll();
        return ResponseEntity.ok(packages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaccoProdotto> getPackageById(@PathVariable Long id) {
        try {
            PaccoProdotto paccoProdotto = packageService.getById(id);
            return ResponseEntity.ok(paccoProdotto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<PaccoProdotto> createPackage(@Valid @RequestBody PaccoProdotto paccoProdotto) {
        PaccoProdotto createdPackage = packageService.create(paccoProdotto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPackage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaccoProdotto> updatePackage(@PathVariable Long id,
                                                       @Valid @RequestBody PaccoProdotto paccoProdotto) {
        try {
            PaccoProdotto updatedPackage = packageService.update(id, paccoProdotto);
            return ResponseEntity.ok(updatedPackage);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable Long id) {
        try {
            packageService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<PaccoProdotto>> searchPackages(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String theme,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) BigDecimal minDiscount) {
        
        List<PaccoProdotto> packages;
        
        if (name != null) {
            packages = packageService.searchByName(name);
        } else if (theme != null) {
            packages = packageService.searchByTheme(theme);
        } else if (maxPrice != null) {
            packages = packageService.getPackagesByMaxPrice(maxPrice);
        } else if (minDiscount != null) {
            packages = packageService.getPackagesByMinDiscount(minDiscount);
        } else {
            packages = packageService.getAll();
        }
        
        return ResponseEntity.ok(packages);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<PaccoProdotto>> getPackagesContainingProduct(@PathVariable Long productId) {
        List<PaccoProdotto> packages = packageService.getPackagesContainingProduct(productId);
        return ResponseEntity.ok(packages);
    }

    @GetMapping("/best-deals")
    public ResponseEntity<List<PaccoProdotto>> getBestDeals(
            @RequestParam(defaultValue = "10.0") BigDecimal minDiscount) {
        List<PaccoProdotto> bestDeals = packageService.getPackagesByMinDiscount(minDiscount);
        return ResponseEntity.ok(bestDeals);
    }

    @GetMapping("/{id}/savings")
    public ResponseEntity<BigDecimal> getPackageSavings(@PathVariable Long id) {
        try {
            PaccoProdotto paccoProdotto = packageService.getById(id);
            BigDecimal savings = paccoProdotto.calculateSavings();
            return ResponseEntity.ok(savings);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/original-price")
    public ResponseEntity<BigDecimal> getPackageOriginalPrice(@PathVariable Long id) {
        try {
            PaccoProdotto paccoProdotto = packageService.getById(id);
            BigDecimal originalPrice = paccoProdotto.calculateOriginalPrice();
            return ResponseEntity.ok(originalPrice);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}