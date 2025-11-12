package it.unicam.cs.filieraagricola.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Piattaforma Filiera Agricola è operativa");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("application", "Piattaforma Filiera Agricola Locale");
        info.put("version", "3.0.0");
        info.put("framework", "Spring Boot 3.2.0");
        info.put("java_version", System.getProperty("java.version"));
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("companies", "GET/POST /api/companies");
        endpoints.put("products", "GET/POST /api/products"); 
        endpoints.put("orders", "GET/POST /api/orders");
        endpoints.put("events", "GET/POST /api/events");
        endpoints.put("packages", "GET/POST /api/packages");
        endpoints.put("traceability", "GET/POST /api/traceability");
        endpoints.put("map", "GET /api/map/locations");
        
        info.put("available_endpoints", endpoints);
        return ResponseEntity.ok(info);
    }
}