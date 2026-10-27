package com.example.movie.controller;

import com.example.movie.model.Movie;
import com.example.movie.repository.MovieRepository;
import org.bson.Document;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieRepository repository;

    public MovieController(MovieRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Movie> getAllMovies() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Movie getMovieById(@PathVariable String id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    public Movie insertMovie(@RequestBody Movie movie) {
        // string ids (M1A2B3C) so they match movieId in Bookings
        if (movie.getMid() == null || movie.getMid().isBlank()) {
            movie.setMid("M" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        }
        return repository.save(movie);
    }

    @PutMapping("/{id}")
    public Movie update(@RequestBody Movie movie, @PathVariable String id) {
        return repository.findById(id).map(
                existing -> {
                    existing.setTitle(movie.getTitle());
                    existing.setGenre(movie.getGenre());
                    existing.setTicketPrice(movie.getTicketPrice());
                    existing.setAvailableSeats(movie.getAvailableSeats());
                    return repository.save(existing);
                }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public String deleteMovie(@PathVariable String id) {
        if (!repository.existsById(id)) {
            return "Movie not Found";
        }
        repository.deleteById(id);
        return "Movie Deleted Successfully";
    }

    // Query 1
    @GetMapping("/evening-bookings")
    public List<Document> eveningBookings() {
        return repository.findEveningBookings().getMappedResults();
    }

    // Query 2
    @GetMapping("/group-bookings")
    public List<Document> groupBookings() {
        return repository.findGroupBookings().getMappedResults();
    }

    // Query 3
    @GetMapping("/action-bookings")
    public List<Document> actionBookings() {
        return repository.findActionBookings().getMappedResults();
    }

    // Query 4
    @GetMapping("/premium-night-bookings")
    public List<Document> premiumNightBookings() {
        return repository.findPremiumNightBookings().getMappedResults();
    }

    // Query 5
    @GetMapping("/total-seats")
    public List<Document> totalSeats() {
        return repository.calculateTotalSeats().getMappedResults();
    }

    // Query 6
    @GetMapping("/at-least-two-seats")
    public List<Document> atLeastTwoSeats() {
        return repository.findAtLeastTwoSeats().getMappedResults();
    }

    // Extra 1: sorting
    @GetMapping("/sorted-by-price")
    public List<Document> sortedByPrice() {
        return repository.findSortedByPrice().getMappedResults();
    }

    // Extra 2: revenue
    @GetMapping("/revenue")
    public List<Document> revenue() {
        return repository.calculateRevenue().getMappedResults();
    }

}
