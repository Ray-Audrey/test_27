package com.example.inventory.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Products")
public class Product {
    @Id
    private String pid;
    private String name;
    private String category;
    private double price;
    private int quantity;
}
