package com.example.movie.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Movies")
public class Movie {
    @Id
    private String mid;
    private String title;
    private String genre;
    private double ticketPrice;
    private int availableSeats;
}
