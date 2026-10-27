package com.example.inventory.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "StockMovements")
public class StockMovement {
    @Id
    private String sid;
    private String productId;
    private String movementType;   // Purchase, Sale or Return
    private int quantity;
}
