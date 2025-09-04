package main.java.it.unicam.cs.filieraagricola.model;

public class Company {
    private Long id;
    private String name;
    private String role; //Produttore, trasformatore, distributore
    private String location;

    public Company(Long id, String name, String role, String location) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.location = location;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public String getLocation() { return location; }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
