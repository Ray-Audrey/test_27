package com.example.movie;

import com.example.movie.controller.MovieController;
import com.example.movie.model.Movie;
import com.example.movie.repository.MovieRepository;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MovieRepository repository;

    private Movie movie(String id, String title, String genre, double price, int seats) {
        Movie m = new Movie();
        m.setMid(id);
        m.setTitle(title);
        m.setGenre(genre);
        m.setTicketPrice(price);
        m.setAvailableSeats(seats);
        return m;
    }

    private AggregationResults<Document> results(Document... docs) {
        return new AggregationResults<>(List.of(docs), new Document());
    }

    @Test
    void getAllMovies_returnsList() throws Exception {
        when(repository.findAll()).thenReturn(List.of(
                movie("M101", "Thunder Strike", "Action", 250, 100),
                movie("M102", "Laugh Riot", "Comedy", 150, 100)));

        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Thunder Strike"));
    }

    @Test
    void insertMovie_generatesIdWhenMissing() throws Exception {
        when(repository.save(any(Movie.class))).thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Film\",\"genre\":\"Drama\",\"ticketPrice\":200,\"availableSeats\":80}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mid").isNotEmpty())
                .andExpect(jsonPath("$.title").value("New Film"));
    }

    @Test
    void update_changesFields() throws Exception {
        when(repository.findById("M101")).thenReturn(Optional.of(movie("M101", "Thunder Strike", "Action", 250, 100)));
        when(repository.save(any(Movie.class))).thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(put("/api/movies/M101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Thunder Strike 2\",\"genre\":\"Action\",\"ticketPrice\":270,\"availableSeats\":90}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Thunder Strike 2"))
                .andExpect(jsonPath("$.availableSeats").value(90));
    }

    @Test
    void delete_existingMovie() throws Exception {
        when(repository.existsById("M101")).thenReturn(true);

        mockMvc.perform(delete("/api/movies/M101"))
                .andExpect(status().isOk())
                .andExpect(content().string("Movie Deleted Successfully"));
        verify(repository).deleteById("M101");
    }

    @Test
    void delete_missingMovie() throws Exception {
        when(repository.existsById("X")).thenReturn(false);

        mockMvc.perform(delete("/api/movies/X"))
                .andExpect(content().string("Movie not Found"));
        verify(repository, never()).deleteById(any());
    }

    @Test
    void eveningBookingsReport_returnsDocuments() throws Exception {
        when(repository.findEveningBookings()).thenReturn(
                results(new Document("title", "Thunder Strike").append("customerName", "Arun")));

        mockMvc.perform(get("/api/movies/evening-bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Thunder Strike"))
                .andExpect(jsonPath("$[0].customerName").value("Arun"));
    }

    @Test
    void revenueReport_returnsDocuments() throws Exception {
        when(repository.calculateRevenue()).thenReturn(
                results(new Document("title", "Galaxy Quest 2").append("totalRevenue", 1800.0)));

        mockMvc.perform(get("/api/movies/revenue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].totalRevenue").value(1800.0));
    }
}
