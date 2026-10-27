package com.example.movie.controller;

import com.example.movie.model.Booking;
import com.example.movie.model.Movie;
import com.example.movie.repository.BookingRepository;
import com.example.movie.repository.MovieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private static final Set<String> SHOWS = Set.of("Morning", "Matinee", "Evening", "Night");

    private final BookingRepository repository;
    private final MovieRepository movieRepository;

    public BookingController(BookingRepository repository, MovieRepository movieRepository) {
        this.repository = repository;
        this.movieRepository = movieRepository;
    }

    @GetMapping
    public List<Booking> getAllBookings() {
        return repository.findAll();
    }

    // Books the tickets: checks seats are available, calculates the total
    // amount (seats x ticket price) and reduces the movie's available seats.
    @PostMapping
    public Booking addBooking(@RequestBody Booking booking) {
        if (!SHOWS.contains(booking.getShowTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Show time must be Morning, Matinee, Evening or Night");
        }
        if (booking.getSeats() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seats must be greater than 0");
        }
        Movie movie = movieRepository.findById(booking.getMovieId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Movie not found"));

        if (booking.getSeats() > movie.getAvailableSeats()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Not enough seats: only " + movie.getAvailableSeats() + " available");
        }
        movie.setAvailableSeats(movie.getAvailableSeats() - booking.getSeats());
        movieRepository.save(movie);

        booking.setTotalAmount(booking.getSeats() * movie.getTicketPrice());
        return repository.save(booking);
    }
}
