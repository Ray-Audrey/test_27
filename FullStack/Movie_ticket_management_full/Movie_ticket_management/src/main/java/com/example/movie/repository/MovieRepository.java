package com.example.movie.repository;

import com.example.movie.model.Movie;
import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovieRepository extends MongoRepository<Movie, String> {

    // QUERY 1: Bookings made for the Evening show
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'Bookings', 'localField': '_id', 'foreignField': 'movieId', 'as': 'bookingDetails' } }",
            "{ '$unwind': '$bookingDetails' }",
            "{ '$match': { 'bookingDetails.showTime': 'Evening' } }",
            "{ '$project': { '_id': 0, 'title': 1, 'customerName': '$bookingDetails.customerName', 'showTime': '$bookingDetails.showTime' } }"
    })
    AggregationResults<Document> findEveningBookings();


    // QUERY 2: Bookings with more than 3 seats
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'Bookings', 'localField': '_id', 'foreignField': 'movieId', 'as': 'bookingDetails' } }",
            "{ '$unwind': '$bookingDetails' }",
            "{ '$match': { 'bookingDetails.seats': { '$gt': 3 } } }",
            "{ '$project': { '_id': 0, 'title': 1, 'customerName': '$bookingDetails.customerName', 'seats': '$bookingDetails.seats' } }"
    })
    AggregationResults<Document> findGroupBookings();


    // QUERY 3: Action movies and their bookings
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'Bookings', 'localField': '_id', 'foreignField': 'movieId', 'as': 'bookingDetails' } }",
            "{ '$unwind': '$bookingDetails' }",
            "{ '$match': { 'genre': 'Action' } }",
            "{ '$project': { '_id': 0, 'title': 1, 'genre': 1, 'customerName': '$bookingDetails.customerName', 'seats': '$bookingDetails.seats' } }"
    })
    AggregationResults<Document> findActionBookings();


    // QUERY 4: Ticket price > 200 and Night show
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'Bookings', 'localField': '_id', 'foreignField': 'movieId', 'as': 'bookingDetails' } }",
            "{ '$unwind': '$bookingDetails' }",
            "{ '$match': { 'ticketPrice': { '$gt': 200 }, 'bookingDetails.showTime': 'Night' } }",
            "{ '$project': { '_id': 0, 'title': 1, 'ticketPrice': 1, 'showTime': '$bookingDetails.showTime' } }"
    })
    AggregationResults<Document> findPremiumNightBookings();


    // QUERY 5: Total seats booked per movie
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'Bookings', 'localField': '_id', 'foreignField': 'movieId', 'as': 'bookingDetails' } }",
            "{ '$unwind': '$bookingDetails' }",
            "{ '$group': { '_id': '$_id', 'title': { '$first': '$title' }, 'totalSeatsBooked': { '$sum': '$bookingDetails.seats' } } }",
            "{ '$project': { '_id': 0, 'title': 1, 'totalSeatsBooked': 1 } }"
    })
    AggregationResults<Document> calculateTotalSeats();


    // QUERY 6: Bookings with at least 2 seats
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'Bookings', 'localField': '_id', 'foreignField': 'movieId', 'as': 'bookingDetails' } }",
            "{ '$unwind': '$bookingDetails' }",
            "{ '$match': { 'bookingDetails.seats': { '$gte': 2 } } }",
            "{ '$project': { '_id': 0, 'title': 1, 'customerName': '$bookingDetails.customerName', 'seats': '$bookingDetails.seats' } }"
    })
    AggregationResults<Document> findAtLeastTwoSeats();


    // EXTRA 1: Movie title, ticket price and customer, sorted by price high to low
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'Bookings', 'localField': '_id', 'foreignField': 'movieId', 'as': 'bookingDetails' } }",
            "{ '$unwind': '$bookingDetails' }",
            "{ '$sort': { 'ticketPrice': -1 } }",
            "{ '$project': { '_id': 0, 'title': 1, 'ticketPrice': 1, 'customerName': '$bookingDetails.customerName' } }"
    })
    AggregationResults<Document> findSortedByPrice();


    // EXTRA 2: Total revenue per movie
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'Bookings', 'localField': '_id', 'foreignField': 'movieId', 'as': 'bookingDetails' } }",
            "{ '$unwind': '$bookingDetails' }",
            "{ '$group': { '_id': '$_id', 'title': { '$first': '$title' }, 'totalRevenue': { '$sum': '$bookingDetails.totalAmount' } } }",
            "{ '$project': { '_id': 0, 'title': 1, 'totalRevenue': 1 } }",
            "{ '$sort': { 'totalRevenue': -1 } }"
    })
    AggregationResults<Document> calculateRevenue();
}
