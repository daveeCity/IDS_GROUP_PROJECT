package main.java.it.unicam.cs.filieraagricola.model;

import java.util.List;

public class Order {
    private Long id;
    private User buyer;
    private List<Product> products;
    private double total;

    public Order(Long id, User buyer, List<Product> products) {
        this.id = id;
        this.buyer = buyer;
        this.products = products;
        this.total = calculateTotal();
    }

    private double calculateTotal() {
        return products.stream().mapToDouble(Product::getPrice).sum();
    }

    public Long getId() { return id; }
    public User getBuyer() { return buyer; }
    public List<Product> getProducts() { return products; }
    public double getTotal() { return total; }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", buyer=" + buyer.getName() +
                ", total=" + total +
                '}';
    }
}