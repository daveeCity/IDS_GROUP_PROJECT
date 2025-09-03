package main.java.it.unicam.cs.filieraagricola.model;

public class Product {
    private Long id;
    private String name;
    private String description;
    private String certification;
    private double price;
    private Company producer;

    public Product(Long id, String name, String description, String certification, double price, Company producer) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.certification = certification;
        this.price = price;
        this.producer = producer;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCertification() { return certification; }
    public double getPrice() { return price; }
    public Company getProducer() { return producer; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setCertification(String certification) { this.certification = certification; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", certification='" + certification + '\'' +
                ", price=" + price +
                ", producer=" + producer.getName() +
                '}';
    }
}
