package it.unicam.cs.filieraagricola.controller;

import it.unicam.cs.filieraagricola.model.*;
import it.unicam.cs.filieraagricola.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/map")
@CrossOrigin(origins = "*")
public class MapController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/locations")
    public ResponseEntity<List<MapLocation>> getAllLocations() {
        List<Company> companies = companyService.getAll();
        List<MapLocation> locations = new ArrayList<>();
        
        Random random = new Random();
        
        for (Company company : companies) {
            // Simulate coordinates in the Marche region (Italy)
            double baseLat = 43.3; // Base latitude for Marche
            double baseLng = 13.0; // Base longitude for Marche
            
            double latitude = baseLat + (random.nextDouble() - 0.5) * 1.0; // ± 0.5 degrees
            double longitude = baseLng + (random.nextDouble() - 0.5) * 1.5; // ± 0.75 degrees
            
            MapLocation location = new MapLocation(
                latitude,
                longitude,
                company.getName(),
                company.getLocation(),
                company.getRole().toLowerCase()
            );
            location.setRelatedEntityId(company.getId());
            
            locations.add(location);
        }
        
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/locations/producers")
    public ResponseEntity<List<MapLocation>> getProducerLocations() {
        List<Company> producers = companyService.findByRole("PRODUTTORE");
        return ResponseEntity.ok(convertToMapLocations(producers));
    }

    @GetMapping("/locations/transformers")
    public ResponseEntity<List<MapLocation>> getTransformerLocations() {
        List<Company> transformers = companyService.findByRole("TRASFORMATORE");
        return ResponseEntity.ok(convertToMapLocations(transformers));
    }

    @GetMapping("/locations/distributors")
    public ResponseEntity<List<MapLocation>> getDistributorLocations() {
        List<Company> distributors = companyService.findByRole("DISTRIBUTORE");
        return ResponseEntity.ok(convertToMapLocations(distributors));
    }

    @GetMapping("/locations/city/{city}")
    public ResponseEntity<List<MapLocation>> getLocationsByCity(@PathVariable String city) {
        List<Company> companies = companyService.findByLocation(city);
        return ResponseEntity.ok(convertToMapLocations(companies));
    }

    @GetMapping("/locations/company/{companyId}")
    public ResponseEntity<MapLocation> getCompanyLocation(@PathVariable Long companyId) {
        try {
            Company company = companyService.getById(companyId);
            List<MapLocation> locations = convertToMapLocations(List.of(company));
            return ResponseEntity.ok(locations.get(0));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/supply-chain-route/{productId}")
    public ResponseEntity<List<MapLocation>> getSupplyChainRoute(@PathVariable Long productId) {
        // This would integrate with TraceabilityService to get the full supply chain
        // and return the locations in order of the supply chain steps
        
        // For now, return a mock route
        List<MapLocation> route = new ArrayList<>();
        Random random = new Random();
        
        // Simulate a supply chain route with 3-5 locations
        String[] locations = {"Ancona", "Macerata", "Pesaro", "Fermo", "Ascoli Piceno"};
        String[] phases = {"Produzione", "Trasformazione", "Distribuzione", "Vendita"};
        
        for (int i = 0; i < 4; i++) {
            double lat = 43.3 + (random.nextDouble() - 0.5) * 1.0;
            double lng = 13.0 + (random.nextDouble() - 0.5) * 1.5;
            
            MapLocation location = new MapLocation(
                lat,
                lng,
                phases[i] + " - " + locations[i % locations.length],
                locations[i % locations.length],
                "supply_chain_step"
            );
            
            route.add(location);
        }
        
        return ResponseEntity.ok(route);
    }

    @GetMapping("/region-overview")
    public ResponseEntity<RegionOverview> getRegionOverview() {
        List<Company> allCompanies = companyService.getAll();
        
        RegionOverview overview = new RegionOverview();
        overview.setTotalProducers((int) allCompanies.stream().filter(c -> "PRODUTTORE".equals(c.getRole())).count());
        overview.setTotalTransformers((int) allCompanies.stream().filter(c -> "TRASFORMATORE".equals(c.getRole())).count());
        overview.setTotalDistributors((int) allCompanies.stream().filter(c -> "DISTRIBUTORE".equals(c.getRole())).count());
        overview.setCenterLatitude(43.6158);  // Center of Marche region
        overview.setCenterLongitude(13.5189);
        overview.setZoomLevel(8);
        
        return ResponseEntity.ok(overview);
    }

    private List<MapLocation> convertToMapLocations(List<Company> companies) {
        List<MapLocation> locations = new ArrayList<>();
        Random random = new Random();
        
        for (Company company : companies) {
            double baseLat = 43.3;
            double baseLng = 13.0;
            
            double latitude = baseLat + (random.nextDouble() - 0.5) * 1.0;
            double longitude = baseLng + (random.nextDouble() - 0.5) * 1.5;
            
            MapLocation location = new MapLocation(
                latitude,
                longitude,
                company.getName(),
                company.getLocation(),
                company.getRole().toLowerCase()
            );
            location.setRelatedEntityId(company.getId());
            
            locations.add(location);
        }
        
        return locations;
    }

    // Inner class for region overview
    public static class RegionOverview {
        private int totalProducers;
        private int totalTransformers;
        private int totalDistributors;
        private double centerLatitude;
        private double centerLongitude;
        private int zoomLevel;

        // Getters and Setters
        public int getTotalProducers() { return totalProducers; }
        public void setTotalProducers(int totalProducers) { this.totalProducers = totalProducers; }
        
        public int getTotalTransformers() { return totalTransformers; }
        public void setTotalTransformers(int totalTransformers) { this.totalTransformers = totalTransformers; }
        
        public int getTotalDistributors() { return totalDistributors; }
        public void setTotalDistributors(int totalDistributors) { this.totalDistributors = totalDistributors; }
        
        public double getCenterLatitude() { return centerLatitude; }
        public void setCenterLatitude(double centerLatitude) { this.centerLatitude = centerLatitude; }
        
        public double getCenterLongitude() { return centerLongitude; }
        public void setCenterLongitude(double centerLongitude) { this.centerLongitude = centerLongitude; }
        
        public int getZoomLevel() { return zoomLevel; }
        public void setZoomLevel(int zoomLevel) { this.zoomLevel = zoomLevel; }
    }
}