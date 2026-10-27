package com.example.flights.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Collection 2
@Document(collection = "ScheduledFlights")
@Data
public class ScheduledFlight {
    @Id
    private String sid;
    private String flightId;        // refers to Flight.fid
    private String departureDate;
    private String status;          // On-Time / Delayed / Cancelled
    private int seatsAvailable;
}
