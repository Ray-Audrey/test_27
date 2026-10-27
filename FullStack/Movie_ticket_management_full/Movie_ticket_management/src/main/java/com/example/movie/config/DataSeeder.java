package com.example.movie.config;

import com.example.movie.model.Booking;
import com.example.movie.model.Movie;
import com.example.movie.repository.BookingRepository;
import com.example.movie.repository.MovieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// Adds a little sample data the first time the app runs against an empty database
@Component
public class DataSeeder implements CommandLineRunner {

    private final MovieRepository movies;
    private final BookingRepository bookings;

    public DataSeeder(MovieRepository movies, BookingRepository bookings) {
        this.movies = movies;
        this.bookings = bookings;
    }

    @Override
    public void run(String... args) {
        if (movies.count() > 0) return;

        movies.save(movie("M101", "Thunder Strike", "Action", 250, 92));
        movies.save(movie("M102", "Laugh Riot", "Comedy", 150, 100));
        movies.save(movie("M103", "Midnight Whispers", "Horror", 220, 95));
        movies.save(movie("M104", "Love in Paris", "Romance", 180, 98));
        movies.save(movie("M105", "Galaxy Quest 2", "Sci-Fi", 300, 90));

        bookings.save(booking("M101", "Arun", "Evening", 4, 250));
        bookings.save(booking("M101", "Priya", "Night", 2, 250));
        bookings.save(booking("M102", "Karthik", "Matinee", 2, 150));
        bookings.save(booking("M103", "Divya", "Night", 3, 220));
        bookings.save(booking("M104", "Suresh", "Evening", 2, 180));
        bookings.save(booking("M105", "Meena", "Night", 5, 300));
        bookings.save(booking("M105", "Ravi", "Morning", 1, 300));
    }

    private Movie movie(String id, String title, String genre, double price, int seats) {
        Movie m = new Movie();
        m.setMid(id);
        m.setTitle(title);
        m.setGenre(genre);
        m.setTicketPrice(price);
        m.setAvailableSeats(seats);
        return m;
    }

    private Booking booking(String movieId, String customer, String show, int seats, double price) {
        Booking b = new Booking();
        b.setMovieId(movieId);
        b.setCustomerName(customer);
        b.setShowTime(show);
        b.setSeats(seats);
        b.setTotalAmount(seats * price);
        return b;
    }
}
