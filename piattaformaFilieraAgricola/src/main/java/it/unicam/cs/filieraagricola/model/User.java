package main.java.it.unicam.cs.filieraagricola.model;

public class User {
    private Long id;
    private String name;
    private String role; //Acquirente, curatore, animatore, gestore..

    public User(Long id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}