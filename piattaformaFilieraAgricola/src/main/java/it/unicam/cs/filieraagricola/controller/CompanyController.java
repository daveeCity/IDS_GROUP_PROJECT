package it.unicam.cs.filieraagricola.controller;

import it.unicam.cs.filieraagricola.model.Company;
import it.unicam.cs.filieraagricola.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin(origins = "*")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        List<Company> companies = companyService.getAll();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        try {
            Company company = companyService.getById(id);
            return ResponseEntity.ok(company);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company) {
        Company createdCompany = companyService.create(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCompany);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable Long id, 
                                                @Valid @RequestBody Company company) {
        try {
            Company updatedCompany = companyService.update(id, company);
            return ResponseEntity.ok(updatedCompany);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        try {
            companyService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Company>> searchCompanies(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String location) {
        
        List<Company> companies;
        
        if (name != null) {
            companies = companyService.searchByName(name);
        } else if (role != null) {
            companies = companyService.findByRole(role);
        } else if (location != null) {
            companies = companyService.findByLocation(location);
        } else {
            companies = companyService.getAll();
        }
        
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/producers")
    public ResponseEntity<List<Company>> getProducers() {
        List<Company> producers = companyService.findByRole("PRODUTTORE");
        return ResponseEntity.ok(producers);
    }

    @GetMapping("/transformers")
    public ResponseEntity<List<Company>> getTransformers() {
        List<Company> transformers = companyService.findByRole("TRASFORMATORE");
        return ResponseEntity.ok(transformers);
    }

    @GetMapping("/distributors")
    public ResponseEntity<List<Company>> getDistributors() {
        List<Company> distributors = companyService.findByRole("DISTRIBUTORE");
        return ResponseEntity.ok(distributors);
    }
}