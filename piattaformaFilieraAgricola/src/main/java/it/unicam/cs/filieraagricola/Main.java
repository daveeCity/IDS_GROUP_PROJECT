package main.java.it.unicam.cs.filieraagricola;

import main.java.it.unicam.cs.filieraagricola.model.Company;
import main.java.it.unicam.cs.filieraagricola.model.Product;
import main.java.it.unicam.cs.filieraagricola.repository.ProductRepository;
import main.java.it.unicam.cs.filieraagricola.service.ProductService;

public class Main {
    public static void main(String[] args) {
        ProductRepository repo = new ProductRepository();
        ProductService service = new ProductService(repo);

        Company farm = new Company(1L, "Fattoria Rossi", "Produttore", "Ancona");

        Product apple = new Product(1L, "Mela Bio", "Mela coltivata senza pesticidi", "Certificazione Bio", 1.50, farm);
        Product wine = new Product(2L, "Vino Rosso", "Vino DOC locale", "DOC", 10.00, farm);

        service.create(apple);
        service.create(wine);

        System.out.println("📦 Prodotti disponibili:");
        service.getAll().forEach(System.out::println);

        System.out.println("\n🔍 Dettagli prodotto ID=1:");
        System.out.println(service.getById(1L));
    }
}
