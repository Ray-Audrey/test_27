package com.example.flights;

import com.example.flights.controller.ScheduledFlightController;
import com.example.flights.model.ScheduledFlight;
import com.example.flights.repository.ScheduledFlightRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScheduledFlightController.class)
class ScheduledFlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ScheduledFlightRepository repository;

    private ScheduledFlight scheduled(String sid, String flightId, String date, String status, int seats) {
        ScheduledFlight s = new ScheduledFlight();
        s.setSid(sid);
        s.setFlightId(flightId);
        s.setDepartureDate(date);
        s.setStatus(status);
        s.setSeatsAvailable(seats);
        return s;
    }

    @Test
    void getAllScheduledFlights_returnsList() throws Exception {
        when(repository.findAll()).thenReturn(List.of(
                scheduled("S1", "F101", "2026-11-01", "On-Time", 60),
                scheduled("S2", "F102", "2026-11-02", "Delayed", 40)));

        mockMvc.perform(get("/api/scheduled-flights"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].flightId").value("F101"))
                .andExpect(jsonPath("$[1].status").value("Delayed"));
    }

    @Test
    void addScheduledFlight_returnsSavedRecord() throws Exception {
        when(repository.save(any(ScheduledFlight.class)))
                .thenReturn(scheduled("S3", "F101", "2026-11-05", "On-Time", 75));

        String body = """
                {"sid":"S3","flightId":"F101","departureDate":"2026-11-05","status":"On-Time","seatsAvailable":75}
                """;

        mockMvc.perform(post("/api/scheduled-flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sid").value("S3"))
                .andExpect(jsonPath("$.seatsAvailable").value(75));

        verify(repository, times(1)).save(any(ScheduledFlight.class));
    }
}
