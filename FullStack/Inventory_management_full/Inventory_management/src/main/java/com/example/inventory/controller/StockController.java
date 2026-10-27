package com.example.inventory.controller;

import com.example.inventory.model.Product;
import com.example.inventory.model.StockMovement;
import com.example.inventory.repository.ProductRepository;
import com.example.inventory.repository.StockMovementRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    private static final Set<String> TYPES = Set.of("Purchase", "Sale", "Return");

    private final StockMovementRepository repository;
    private final ProductRepository productRepository;

    public StockController(StockMovementRepository repository, ProductRepository productRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<StockMovement> getAllMovements() {
        return repository.findAll();
    }

    // Records the movement AND updates the product's quantity in stock.
    // Purchase / Return add stock, Sale removes it.
    @PostMapping
    public StockMovement addMovement(@RequestBody StockMovement movement) {
        if (!TYPES.contains(movement.getMovementType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Movement type must be Purchase, Sale or Return");
        }
        if (movement.getQuantity() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be greater than 0");
        }
        Product product = productRepository.findById(movement.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found"));

        if (movement.getMovementType().equals("Sale")) {
            if (movement.getQuantity() > product.getQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Insufficient stock: only " + product.getQuantity() + " available");
            }
            product.setQuantity(product.getQuantity() - movement.getQuantity());
        } else {
            product.setQuantity(product.getQuantity() + movement.getQuantity());
        }
        productRepository.save(product);
        return repository.save(movement);
    }
}
