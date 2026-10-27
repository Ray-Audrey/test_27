package com.example.flights.repository;

import com.example.flights.model.Flight;
import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface FlightRepository extends MongoRepository<Flight, String> {

    // QUERY 1: Flights that are Delayed
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'ScheduledFlights', 'localField': '_id', 'foreignField': 'flightId', 'as': 'scheduleDetails' } }",
            "{ '$unwind': '$scheduleDetails' }",
            "{ '$match': { 'scheduleDetails.status': 'Delayed' } }",
            "{ '$project': { '_id': 0, 'airline': 1, 'source': 1, 'destination': 1, 'status': '$scheduleDetails.status' } }"
    })
    AggregationResults<Document> findDelayedFlights();


    // QUERY 2: Scheduled flights with more than 50 seats available
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'ScheduledFlights', 'localField': '_id', 'foreignField': 'flightId', 'as': 'scheduleDetails' } }",
            "{ '$unwind': '$scheduleDetails' }",
            "{ '$match': { 'scheduleDetails.seatsAvailable': { '$gt': 50 } } }",
            "{ '$project': { '_id': 0, 'airline': 1, 'seatsAvailable': '$scheduleDetails.seatsAvailable' } }"
    })
    AggregationResults<Document> findFlightsWithMoreThanFiftySeats();


    // QUERY 3: Flights departing from Chennai and their schedule details
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'ScheduledFlights', 'localField': '_id', 'foreignField': 'flightId', 'as': 'scheduleDetails' } }",
            "{ '$unwind': '$scheduleDetails' }",
            "{ '$match': { 'source': 'Chennai' } }",
            "{ '$project': { '_id': 0, 'airline': 1, 'source': 1, 'destination': 1, 'departureDate': '$scheduleDetails.departureDate', 'status': '$scheduleDetails.status' } }"
    })
    AggregationResults<Document> findChennaiFlightsWithSchedules();


    // QUERY 4: Price > 5000 and On-Time
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'ScheduledFlights', 'localField': '_id', 'foreignField': 'flightId', 'as': 'scheduleDetails' } }",
            "{ '$unwind': '$scheduleDetails' }",
            "{ '$match': { 'price': { '$gt': 5000 }, 'scheduleDetails.status': 'On-Time' } }",
            "{ '$project': { '_id': 0, 'airline': 1, 'price': 1, 'status': '$scheduleDetails.status' } }"
    })
    AggregationResults<Document> findPremiumOnTimeFlights();


    // QUERY 5: Calculate total available seats per flight
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'ScheduledFlights', 'localField': '_id', 'foreignField': 'flightId', 'as': 'scheduleDetails' } }",
            "{ '$unwind': '$scheduleDetails' }",
            "{ '$group': { '_id': '$_id', 'airline': { '$first': '$airline' }, 'totalSeats': { '$sum': '$scheduleDetails.seatsAvailable' } } }",
            "{ '$project': { '_id': 0, 'airline': 1, 'totalSeats': 1 } }"
    })
    AggregationResults<Document> calculateTotalSeats();


    // QUERY 6: Scheduled flights with at least 50 seats available
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'ScheduledFlights', 'localField': '_id', 'foreignField': 'flightId', 'as': 'scheduleDetails' } }",
            "{ '$unwind': '$scheduleDetails' }",
            "{ '$match': { 'scheduleDetails.seatsAvailable': { '$gte': 50 } } }",
            "{ '$project': { '_id': 0, 'airline': 1, 'seatsAvailable': '$scheduleDetails.seatsAvailable' } }"
    })
    AggregationResults<Document> findFlightsWithAtLeastFiftySeats();


    // EXTRA: Airline, price and status, sorted by price from highest to lowest
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'ScheduledFlights', 'localField': '_id', 'foreignField': 'flightId', 'as': 'scheduleDetails' } }",
            "{ '$unwind': '$scheduleDetails' }",
            "{ '$sort': { 'price': -1 } }",
            "{ '$project': { '_id': 0, 'airline': 1, 'price': 1, 'status': '$scheduleDetails.status' } }"
    })
    AggregationResults<Document> findFlightsSortedByPrice();
}
