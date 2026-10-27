package com.example.flights.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Collection 1
@Data
@Document(collection = "Flights")
public class Flight {
    @Id
    private String fid;
    private String airline;
    private String source;
    private String destination;
    private double price;
}
