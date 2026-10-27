package com.example.movie;

import com.example.movie.controller.BookingController;
import com.example.movie.model.Booking;
import com.example.movie.model.Movie;
import com.example.movie.repository.BookingRepository;
import com.example.movie.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookingRepository repository;

    @MockitoBean
    private MovieRepository movieRepository;

    private Movie movie(int seats) {
        Movie m = new Movie();
        m.setMid("M101");
        m.setTitle("Thunder Strike");
        m.setTicketPrice(250);
        m.setAvailableSeats(seats);
        return m;
    }

    @Test
    void booking_reducesSeats_andCalculatesTotal() throws Exception {
        Movie m = movie(100);
        when(movieRepository.findById("M101")).thenReturn(Optional.of(m));
        when(repository.save(any(Booking.class))).thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(post("/api/bookings").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"movieId\":\"M101\",\"customerName\":\"Arun\",\"showTime\":\"Evening\",\"seats\":4}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmount").value(1000.0));
        assertEquals(96, m.getAvailableSeats());
    }

    @Test
    void booking_moreThanAvailable_isRejected() throws Exception {
        when(movieRepository.findById("M101")).thenReturn(Optional.of(movie(2)));

        mockMvc.perform(post("/api/bookings").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"movieId\":\"M101\",\"customerName\":\"Arun\",\"showTime\":\"Evening\",\"seats\":5}"))
                .andExpect(status().isBadRequest());
        verify(repository, never()).save(any());
    }

    @Test
    void booking_unknownMovie_isRejected() throws Exception {
        when(movieRepository.findById("NOPE")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/bookings").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"movieId\":\"NOPE\",\"customerName\":\"Arun\",\"showTime\":\"Night\",\"seats\":1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void booking_invalidShowTime_isRejected() throws Exception {
        mockMvc.perform(post("/api/bookings").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"movieId\":\"M101\",\"customerName\":\"Arun\",\"showTime\":\"Midnight\",\"seats\":1}"))
                .andExpect(status().isBadRequest());
    }
}
