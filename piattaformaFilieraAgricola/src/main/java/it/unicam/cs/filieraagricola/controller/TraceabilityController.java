package it.unicam.cs.filieraagricola.controller;

import it.unicam.cs.filieraagricola.model.*;
import it.unicam.cs.filieraagricola.service.TraceabilityService;
import it.unicam.cs.filieraagricola.service.ProductService;
import it.unicam.cs.filieraagricola.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/traceability")
@CrossOrigin(origins = "*")
public class TraceabilityController {

    @Autowired
    private TraceabilityService traceabilityService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CompanyService companyService;

    @GetMapping
    public ResponseEntity<List<TracciabilitaProdotto>> getAllTraceabilities() {
        List<TracciabilitaProdotto> traceabilities = traceabilityService.getAllTraceabilities();
        return ResponseEntity.ok(traceabilities);
    }

    @PostMapping("/product/{productId}")
    public ResponseEntity<TracciabilitaProdotto> createTraceability(@PathVariable Long productId) {
        try {
            Prodotto product = productService.getById(productId);
            TracciabilitaProdotto traceability = traceabilityService.createTraceability(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(traceability);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<TracciabilitaProdotto> getTraceabilityByProduct(@PathVariable Long productId) {
        try {
            Prodotto product = productService.getById(productId);
            TracciabilitaProdotto traceability = traceabilityService.getTraceabilityByProduct(product);
            return ResponseEntity.ok(traceability);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<TracciabilitaProdotto> getTraceabilityByCode(@PathVariable String code) {
        try {
            TracciabilitaProdotto traceability = traceabilityService.getTraceabilityByCode(code);
            return ResponseEntity.ok(traceability);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/product/{productId}/supply-chain")
    public ResponseEntity<List<SupplyChainStep>> getSupplyChainByProduct(@PathVariable Long productId) {
        try {
            Prodotto product = productService.getById(productId);
            List<SupplyChainStep> steps = traceabilityService.getSupplyChainByProduct(product);
            return ResponseEntity.ok(steps);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{traceabilityId}/steps")
    public ResponseEntity<SupplyChainStep> addSupplyChainStep(@PathVariable Long traceabilityId,
                                                             @Valid @RequestBody SupplyChainStep step) {
        try {
            SupplyChainStep createdStep = traceabilityService.addSupplyChainStep(traceabilityId, step);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdStep);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/product/{productId}/steps")
    public ResponseEntity<SupplyChainStep> addStepToProduct(@PathVariable Long productId,
                                                           @RequestParam Long companyId,
                                                           @RequestParam PassoFiliera phase,
                                                           @RequestParam String description) {
        try {
            Prodotto product = productService.getById(productId);
            Company company = companyService.getById(companyId);
            
            SupplyChainStep step = traceabilityService.addSupplyChainStep(product, company, phase, description);
            return ResponseEntity.status(HttpStatus.CREATED).body(step);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/steps/{stepId}")
    public ResponseEntity<SupplyChainStep> updateSupplyChainStep(@PathVariable Long stepId,
                                                                @Valid @RequestBody SupplyChainStep step) {
        try {
            SupplyChainStep updatedStep = traceabilityService.updateSupplyChainStep(stepId, step);
            return ResponseEntity.ok(updatedStep);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/steps/{stepId}")
    public ResponseEntity<Void> deleteSupplyChainStep(@PathVariable Long stepId) {
        try {
            traceabilityService.deleteSupplyChainStep(stepId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/company/{companyId}/steps")
    public ResponseEntity<List<SupplyChainStep>> getStepsByCompany(@PathVariable Long companyId) {
        try {
            Company company = companyService.getById(companyId);
            List<SupplyChainStep> steps = traceabilityService.getStepsByCompany(company);
            return ResponseEntity.ok(steps);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/phase/{phase}/steps")
    public ResponseEntity<List<SupplyChainStep>> getStepsByPhase(@PathVariable PassoFiliera phase) {
        List<SupplyChainStep> steps = traceabilityService.getStepsByPhase(phase);
        return ResponseEntity.ok(steps);
    }

    @GetMapping("/farm/{farmName}")
    public ResponseEntity<List<TracciabilitaProdotto>> getTraceabilitiesByFarm(@PathVariable String farmName) {
        List<TracciabilitaProdotto> traceabilities = traceabilityService.getTraceabilitiesByOriginFarm(farmName);
        return ResponseEntity.ok(traceabilities);
    }

    @GetMapping("/certification/{certification}")
    public ResponseEntity<List<TracciabilitaProdotto>> getTraceabilitiesByCertification(@PathVariable String certification) {
        List<TracciabilitaProdotto> traceabilities = traceabilityService.getTraceabilitiesByCertification(certification);
        return ResponseEntity.ok(traceabilities);
    }

    @PutMapping("/{traceabilityId}/origin")
    public ResponseEntity<TracciabilitaProdotto> updateOriginFarm(@PathVariable Long traceabilityId,
                                                                  @RequestParam String farmName,
                                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime harvestDate) {
        try {
            TracciabilitaProdotto traceability = traceabilityService.updateOriginFarm(traceabilityId, farmName, harvestDate);
            return ResponseEntity.ok(traceability);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/steps/date-range")
    public ResponseEntity<List<SupplyChainStep>> getStepsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<SupplyChainStep> steps = traceabilityService.getStepsByDateRange(startDate, endDate);
        return ResponseEntity.ok(steps);
    }

    @GetMapping("/steps/location/{location}")
    public ResponseEntity<List<SupplyChainStep>> getStepsByLocation(@PathVariable String location) {
        List<SupplyChainStep> steps = traceabilityService.getStepsByLocation(location);
        return ResponseEntity.ok(steps);
    }

    @GetMapping("/product/{productId}/completed-phases")
    public ResponseEntity<List<PassoFiliera>> getCompletedPhases(@PathVariable Long productId) {
        try {
            Prodotto product = productService.getById(productId);
            List<PassoFiliera> phases = traceabilityService.getCompletedPhases(product);
            return ResponseEntity.ok(phases);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}