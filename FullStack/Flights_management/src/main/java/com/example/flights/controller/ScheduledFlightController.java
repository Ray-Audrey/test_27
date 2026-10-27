package com.example.flights.controller;

import com.example.flights.model.ScheduledFlight;
import com.example.flights.repository.ScheduledFlightRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scheduled-flights")
public class ScheduledFlightController {

    private final ScheduledFlightRepository repository;

    public ScheduledFlightController(ScheduledFlightRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<ScheduledFlight> getAllScheduledFlights() {
        return repository.findAll();
    }

    @PostMapping
    public ScheduledFlight addScheduledFlight(@RequestBody ScheduledFlight scheduledFlight) {
        return repository.save(scheduledFlight);
    }
}
