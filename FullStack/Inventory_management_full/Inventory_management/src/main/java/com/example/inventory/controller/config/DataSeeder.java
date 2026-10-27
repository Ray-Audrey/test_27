package com.example.inventory.config;

import com.example.inventory.model.Product;
import com.example.inventory.model.StockMovement;
import com.example.inventory.repository.ProductRepository;
import com.example.inventory.repository.StockMovementRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// Adds a little sample data the first time the app runs against an empty database
@Component
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository products;
    private final StockMovementRepository movements;

    public DataSeeder(ProductRepository products, StockMovementRepository movements) {
        this.products = products;
        this.movements = movements;
    }

    @Override
    public void run(String... args) {
        if (products.count() > 0) return;

        products.save(product("P101", "Laptop", "Electronics", 55000, 20));
        products.save(product("P102", "Mouse", "Electronics", 500, 150));
        products.save(product("P103", "Office Chair", "Furniture", 4500, 35));
        products.save(product("P104", "Notebook", "Stationery", 60, 300));
        products.save(product("P105", "Smartphone", "Electronics", 25000, 40));

        movements.save(move("P101", "Purchase", 25));
        movements.save(move("P101", "Sale", 5));
        movements.save(move("P102", "Purchase", 200));
        movements.save(move("P102", "Sale", 50));
        movements.save(move("P103", "Purchase", 40));
        movements.save(move("P103", "Sale", 5));
        movements.save(move("P104", "Purchase", 400));
        movements.save(move("P104", "Sale", 100));
        movements.save(move("P105", "Purchase", 50));
        movements.save(move("P105", "Sale", 8));
        movements.save(move("P105", "Return", 2));
    }

    private Product product(String id, String name, String category, double price, int qty) {
        Product p = new Product();
        p.setPid(id);
        p.setName(name);
        p.setCategory(category);
        p.setPrice(price);
        p.setQuantity(qty);
        return p;
    }

    private StockMovement move(String productId, String type, int qty) {
        StockMovement m = new StockMovement();
        m.setProductId(productId);
        m.setMovementType(type);
        m.setQuantity(qty);
        return m;
    }
}
