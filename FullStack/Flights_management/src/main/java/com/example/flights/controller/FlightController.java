package com.example.flights.controller;

import com.example.flights.model.Flight;
import com.example.flights.repository.FlightRepository;
import org.bson.Document;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {
    private final FlightRepository repository;

    public FlightController(FlightRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Flight> getAllFlights() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Flight getFlightById(@PathVariable String id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    public Flight insertFlight(@RequestBody Flight flight) {
        return repository.save(flight);
    }

    @PutMapping("/{id}")
    public Flight update(@RequestBody Flight flight, @PathVariable String id) {
        return repository.findById(id).map(
                existing -> {
                    existing.setAirline(flight.getAirline());
                    existing.setSource(flight.getSource());
                    existing.setDestination(flight.getDestination());
                    existing.setPrice(flight.getPrice());
                    return repository.save(existing);
                }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public String deleteFlight(@PathVariable String id) {
        if (!repository.existsById(id)) {
            return "Flight not Found";
        }
        repository.deleteById(id);
        return "Flight Deleted Successfully";
    }

    // Query 1
    @GetMapping("/delayed")
    public List<Document> delayed() {
        return repository.findDelayedFlights().getMappedResults();
    }

    // Query 2
    @GetMapping("/more-than-fifty-seats")
    public List<Document> moreThanFiftySeats() {
        return repository.findFlightsWithMoreThanFiftySeats().getMappedResults();
    }

    // Query 3
    @GetMapping("/chennai-schedules")
    public List<Document> chennaiSchedules() {
        return repository.findChennaiFlightsWithSchedules().getMappedResults();
    }

    // Query 4
    @GetMapping("/premium-on-time")
    public List<Document> premiumOnTime() {
        return repository.findPremiumOnTimeFlights().getMappedResults();
    }

    // Query 5
    @GetMapping("/total-seats")
    public List<Document> totalSeats() {
        return repository.calculateTotalSeats().getMappedResults();
    }

    // Query 6
    @GetMapping("/at-least-fifty-seats")
    public List<Document> atLeastFiftySeats() {
        return repository.findFlightsWithAtLeastFiftySeats().getMappedResults();
    }

    // Extra query: sorting
    @GetMapping("/sorted-by-price")
    public List<Document> sortedByPrice() {
        return repository.findFlightsSortedByPrice().getMappedResults();
    }

}
