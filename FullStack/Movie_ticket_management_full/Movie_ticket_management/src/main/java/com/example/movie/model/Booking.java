package com.example.movie.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Bookings")
public class Booking {
    @Id
    private String bid;
    private String movieId;
    private String customerName;
    private String showTime;     // Morning, Matinee, Evening or Night
    private int seats;
    private double totalAmount;  // calculated by the server: seats x ticketPrice
}
