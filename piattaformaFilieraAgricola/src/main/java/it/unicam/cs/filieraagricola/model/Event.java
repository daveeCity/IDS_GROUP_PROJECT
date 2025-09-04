package main.java.it.unicam.cs.filieraagricola.model;

import java.time.LocalDate;

public class Event {
    private Long id;
    private String name;
    private String description;
    private LocalDate date;
    private Company organizer;

    public Event(Long id, String name, String description, LocalDate date, Company organizer) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.organizer = organizer;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDate getDate() { return date; }
    public Company getOrganizer() { return organizer; }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", organizer=" + organizer.getName() +
                '}';
    }
}