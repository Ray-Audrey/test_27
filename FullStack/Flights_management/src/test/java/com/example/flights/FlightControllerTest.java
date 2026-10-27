package com.example.flights;

import com.example.flights.controller.FlightController;
import com.example.flights.model.Flight;
import com.example.flights.repository.FlightRepository;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FlightController.class)
class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FlightRepository repository;

    private Flight flight(String id, String airline, String source, String destination, double price) {
        Flight f = new Flight();
        f.setFid(id);
        f.setAirline(airline);
        f.setSource(source);
        f.setDestination(destination);
        f.setPrice(price);
        return f;
    }

    private AggregationResults<Document> results(Document... docs) {
        return new AggregationResults<>(List.of(docs), new Document());
    }

    // ---------- CRUD tests ----------

    @Test
    void getAllFlights_returnsList() throws Exception {
        when(repository.findAll()).thenReturn(List.of(
                flight("F101", "IndiGo", "Chennai", "Delhi", 6500),
                flight("F102", "Air India", "Mumbai", "Chennai", 4800)));

        mockMvc.perform(get("/api/flights"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].airline").value("IndiGo"))
                .andExpect(jsonPath("$[1].source").value("Mumbai"));
    }

    @Test
    void getFlightById_found() throws Exception {
        when(repository.findById("F101"))
                .thenReturn(Optional.of(flight("F101", "IndiGo", "Chennai", "Delhi", 6500)));

        mockMvc.perform(get("/api/flights/F101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fid").value("F101"))
                .andExpect(jsonPath("$.destination").value("Delhi"))
                .andExpect(jsonPath("$.price").value(6500.0));
    }

    @Test
    void getFlightById_notFound_returnsEmptyBody() throws Exception {
        when(repository.findById("X999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/flights/X999"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void insertFlight_returnsSavedFlight() throws Exception {
        Flight saved = flight("F105", "SpiceJet", "Chennai", "Kolkata", 5200);
        when(repository.save(any(Flight.class))).thenReturn(saved);

        String body = """
                {"fid":"F105","airline":"SpiceJet","source":"Chennai","destination":"Kolkata","price":5200}
                """;

        mockMvc.perform(post("/api/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fid").value("F105"))
                .andExpect(jsonPath("$.airline").value("SpiceJet"));

        verify(repository, times(1)).save(any(Flight.class));
    }

    @Test
    void updateFlight_existing_updatesFields() throws Exception {
        Flight existing = flight("F101", "IndiGo", "Chennai", "Delhi", 6500);
        when(repository.findById("F101")).thenReturn(Optional.of(existing));
        when(repository.save(any(Flight.class))).thenAnswer(inv -> inv.getArgument(0));

        String body = """
                {"airline":"IndiGo","source":"Chennai","destination":"Pune","price":7000}
                """;

        mockMvc.perform(put("/api/flights/F101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.destination").value("Pune"))
                .andExpect(jsonPath("$.price").value(7000.0));
    }

    @Test
    void deleteFlight_existing_returnsSuccessMessage() throws Exception {
        when(repository.existsById("F101")).thenReturn(true);

        mockMvc.perform(delete("/api/flights/F101"))
                .andExpect(status().isOk())
                .andExpect(content().string("Flight Deleted Successfully"));

        verify(repository).deleteById("F101");
    }

    @Test
    void deleteFlight_missing_returnsNotFoundMessage() throws Exception {
        when(repository.existsById("X999")).thenReturn(false);

        mockMvc.perform(delete("/api/flights/X999"))
                .andExpect(status().isOk())
                .andExpect(content().string("Flight not Found"));

        verify(repository, never()).deleteById(anyString());
    }

    // ---------- aggregation endpoint tests ----------

    @Test
    void delayed_returnsDelayedFlights() throws Exception {
        when(repository.findDelayedFlights()).thenReturn(results(
                new Document("airline", "IndiGo").append("status", "Delayed")));

        mockMvc.perform(get("/api/flights/delayed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].airline").value("IndiGo"))
                .andExpect(jsonPath("$[0].status").value("Delayed"));
    }

    @Test
    void moreThanFiftySeats_returnsList() throws Exception {
        when(repository.findFlightsWithMoreThanFiftySeats()).thenReturn(results(
                new Document("airline", "Air India").append("seatsAvailable", 80)));

        mockMvc.perform(get("/api/flights/more-than-fifty-seats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].seatsAvailable").value(80));
    }

    @Test
    void chennaiSchedules_returnsList() throws Exception {
        when(repository.findChennaiFlightsWithSchedules()).thenReturn(results(
                new Document("airline", "IndiGo").append("source", "Chennai")
                        .append("departureDate", "2026-11-01").append("status", "On-Time")));

        mockMvc.perform(get("/api/flights/chennai-schedules"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].source").value("Chennai"))
                .andExpect(jsonPath("$[0].departureDate").value("2026-11-01"));
    }

    @Test
    void premiumOnTime_returnsList() throws Exception {
        when(repository.findPremiumOnTimeFlights()).thenReturn(results(
                new Document("airline", "IndiGo").append("price", 6500.0).append("status", "On-Time")));

        mockMvc.perform(get("/api/flights/premium-on-time"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price").value(6500.0))
                .andExpect(jsonPath("$[0].status").value("On-Time"));
    }

    @Test
    void totalSeats_returnsList() throws Exception {
        when(repository.calculateTotalSeats()).thenReturn(results(
                new Document("airline", "IndiGo").append("totalSeats", 130)));

        mockMvc.perform(get("/api/flights/total-seats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].totalSeats").value(130));
    }

    @Test
    void atLeastFiftySeats_returnsList() throws Exception {
        when(repository.findFlightsWithAtLeastFiftySeats()).thenReturn(results(
                new Document("airline", "Vistara").append("seatsAvailable", 50)));

        mockMvc.perform(get("/api/flights/at-least-fifty-seats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].seatsAvailable").value(50));
    }

    @Test
    void sortedByPrice_returnsListInGivenOrder() throws Exception {
        when(repository.findFlightsSortedByPrice()).thenReturn(results(
                new Document("airline", "IndiGo").append("price", 6500.0),
                new Document("airline", "Air India").append("price", 4800.0)));

        mockMvc.perform(get("/api/flights/sorted-by-price"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price").value(6500.0))
                .andExpect(jsonPath("$[1].price").value(4800.0));
    }
}
