package com.example.inventory.controller;

import com.example.inventory.model.Product;
import com.example.inventory.repository.ProductRepository;
import org.bson.Document;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable String id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    public Product insertProduct(@RequestBody Product product) {
        // string ids (P1A2B3C) so they match productId in StockMovements
        if (product.getPid() == null || product.getPid().isBlank()) {
            product.setPid("P" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        }
        return repository.save(product);
    }

    @PutMapping("/{id}")
    public Product update(@RequestBody Product product, @PathVariable String id) {
        return repository.findById(id).map(
                existing -> {
                    existing.setName(product.getName());
                    existing.setCategory(product.getCategory());
                    existing.setPrice(product.getPrice());
                    existing.setQuantity(product.getQuantity());
                    return repository.save(existing);
                }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable String id) {
        if (!repository.existsById(id)) {
            return "Product not Found";
        }
        repository.deleteById(id);
        return "Product Deleted Successfully";
    }

    // Query 1
    @GetMapping("/purchased")
    public List<Document> purchased() {
        return repository.findPurchasedProducts().getMappedResults();
    }

    // Query 2
    @GetMapping("/large-movements")
    public List<Document> largeMovements() {
        return repository.findLargeMovements().getMappedResults();
    }

    // Query 3
    @GetMapping("/electronics-movements")
    public List<Document> electronicsMovements() {
        return repository.findElectronicsMovements().getMappedResults();
    }

    // Query 4
    @GetMapping("/premium-sales")
    public List<Document> premiumSales() {
        return repository.findPremiumSales().getMappedResults();
    }

    // Query 5
    @GetMapping("/total-moved")
    public List<Document> totalMoved() {
        return repository.calculateTotalMoved().getMappedResults();
    }

    // Query 6
    @GetMapping("/bulk-movements")
    public List<Document> bulkMovements() {
        return repository.findBulkMovements().getMappedResults();
    }

    // Extra 1: sorting
    @GetMapping("/sorted-by-price")
    public List<Document> sortedByPrice() {
        return repository.findSortedByPrice().getMappedResults();
    }

    // Extra 2: net movement
    @GetMapping("/net-movement")
    public List<Document> netMovement() {
        return repository.calculateNetMovement().getMappedResults();
    }

}
