package com.example.flights.repository;

import com.example.flights.model.ScheduledFlight;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScheduledFlightRepository extends MongoRepository<ScheduledFlight, String> {
}
