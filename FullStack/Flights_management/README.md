# Flights (Spring Boot + MongoDB)

Same layout as the Employee Leave Management project.

| Employee project | This project |
|---|---|
| `Employees` collection (Employee) | `Flights` collection (Flight) |
| `Leaves` collection (Leave) | `ScheduledFlights` collection (ScheduledFlight) |
| `Leaves.employeeId` -> `Employees._id` | `ScheduledFlights.flightId` -> `Flights._id` |

## Run
1. Start MongoDB on `localhost:27017` (database `Work`).
2. Load sample data (optional):
   ```
   mongoimport --db Work --collection Flights          --jsonArray --file sample-data/flights.json
   mongoimport --db Work --collection ScheduledFlights --jsonArray --file sample-data/scheduled_flights.json
   ```
3. `./mvnw spring-boot:run` (Java 17+)

## Endpoints
Flights (`/api/flights`): GET, GET /{id}, POST, PUT /{id}, DELETE /{id}

Scheduled flights (`/api/scheduled-flights`): GET, POST

Aggregation queries (`$lookup` between the two collections):

| Endpoint | Query |
|---|---|
| `/api/flights/delayed` | Flights whose schedule status is Delayed |
| `/api/flights/more-than-fifty-seats` | Schedules with seatsAvailable > 50 |
| `/api/flights/chennai-schedules` | Flights from Chennai with schedule details |
| `/api/flights/premium-on-time` | Price > 5000 and status On-Time |
| `/api/flights/total-seats` | Total seats available per flight ($group / $sum) |
| `/api/flights/at-least-fifty-seats` | Schedules with seatsAvailable >= 50 |
| `/api/flights/sorted-by-price` | Airline, price, status sorted by price desc |
