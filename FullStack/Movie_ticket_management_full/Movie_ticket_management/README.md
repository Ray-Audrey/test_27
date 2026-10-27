# Movie Ticket Management - Spring Boot backend

Requirements: Java 17+, MongoDB running on `localhost:27017`.

```
./mvnw spring-boot:run        (Windows: mvnw.cmd spring-boot:run)
```

Runs on http://localhost:8080. Database `MovieTickets` (collections `Movies`, `Bookings`).
Sample data is added automatically on first run.

## API
| Method | Path | What |
|---|---|---|
| GET/POST | /api/movies | list / add movie |
| GET/PUT/DELETE | /api/movies/{id} | one movie |
| GET/POST | /api/bookings | list / book tickets (checks seats, calculates total, reduces available seats) |
| GET | /api/movies/evening-bookings | report 1 |
| GET | /api/movies/group-bookings | report 2 (more than 3 seats) |
| GET | /api/movies/action-bookings | report 3 |
| GET | /api/movies/premium-night-bookings | report 4 |
| GET | /api/movies/total-seats | report 5 |
| GET | /api/movies/at-least-two-seats | report 6 |
| GET | /api/movies/sorted-by-price | extra: sorting |
| GET | /api/movies/revenue | extra: revenue per movie |
