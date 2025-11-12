package it.unicam.cs.filieraagricola.controller;

import it.unicam.cs.filieraagricola.model.*;
import it.unicam.cs.filieraagricola.service.EventService;
import it.unicam.cs.filieraagricola.service.UserService;
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
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @GetMapping
    public ResponseEntity<List<Evento>> getAllEvents() {
        List<Evento> events = eventService.getAll();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evento> getEventById(@PathVariable Long id) {
        try {
            Evento event = eventService.getById(id);
            return ResponseEntity.ok(event);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Evento> createEvent(@Valid @RequestBody Evento event) {
        Evento createdEvent = eventService.create(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evento> updateEvent(@PathVariable Long id,
                                              @Valid @RequestBody Evento event) {
        try {
            Evento updatedEvent = eventService.update(id, event);
            return ResponseEntity.ok(updatedEvent);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        try {
            eventService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Evento>> getUpcomingEvents() {
        List<Evento> events = eventService.getUpcomingEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Evento>> searchEvents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) TipoEvento tipoEvento,
            @RequestParam(required = false) StatoEvento status) {
        
        List<Evento> events;
        
        if (name != null) {
            events = eventService.searchByName(name);
        } else if (location != null) {
            events = eventService.searchByLocation(location);
        } else if (tipoEvento != null) {
            events = eventService.getEventsByType(tipoEvento);
        } else if (status != null) {
            events = eventService.getEventsByStatus(status);
        } else {
            events = eventService.getAll();
        }
        
        return ResponseEntity.ok(events);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Evento>> getEventsInDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Evento> events = eventService.getEventsInDateRange(startDate, endDate);
        return ResponseEntity.ok(events);
    }

    @PutMapping("/{id}/open-registration")
    public ResponseEntity<Evento> openRegistration(@PathVariable Long id) {
        try {
            Evento event = eventService.openRegistration(id);
            return ResponseEntity.ok(event);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/close-registration")
    public ResponseEntity<Evento> closeRegistration(@PathVariable Long id) {
        try {
            Evento event = eventService.closeRegistration(id);
            return ResponseEntity.ok(event);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<Evento> startEvent(@PathVariable Long id) {
        try {
            Evento event = eventService.startEvent(id);
            return ResponseEntity.ok(event);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Evento> completeEvent(@PathVariable Long id) {
        try {
            Evento event = eventService.completeEvent(id);
            return ResponseEntity.ok(event);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Evento> cancelEvent(@PathVariable Long id) {
        try {
            Evento event = eventService.cancelEvent(id);
            return ResponseEntity.ok(event);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/register/{userId}")
    public ResponseEntity<Evento> registerUserForEvent(@PathVariable Long id,
                                                       @PathVariable Long userId) {
        try {
            User user = userService.getById(userId);
            Evento event = eventService.registerUserForEvent(id, user);
            return ResponseEntity.ok(event);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}/unregister/{userId}")
    public ResponseEntity<Evento> unregisterUserFromEvent(@PathVariable Long id,
                                                          @PathVariable Long userId) {
        try {
            User user = userService.getById(userId);
            Evento event = eventService.unregisterUserFromEvent(id, user);
            return ResponseEntity.ok(event);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/add-company/{companyId}")
    public ResponseEntity<Evento> addCompanyToEvent(@PathVariable Long id,
                                                    @PathVariable Long companyId) {
        try {
            Company company = companyService.getById(companyId);
            Evento event = eventService.addCompanyToEvent(id, company);
            return ResponseEntity.ok(event);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/remove-company/{companyId}")
    public ResponseEntity<Evento> removeCompanyFromEvent(@PathVariable Long id,
                                                         @PathVariable Long companyId) {
        try {
            Company company = companyService.getById(companyId);
            Evento event = eventService.removeCompanyFromEvent(id, company);
            return ResponseEntity.ok(event);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Evento>> getEventsByParticipant(@PathVariable Long userId) {
        List<Evento> events = eventService.getEventsByParticipant(userId);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Evento>> getEventsByCompany(@PathVariable Long companyId) {
        List<Evento> events = eventService.getEventsByCompany(companyId);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Evento>> getEventsWithAvailableSpots() {
        List<Evento> events = eventService.getEventsWithAvailableSpots();
        return ResponseEntity.ok(events);
    }
}