# Student Course Management (Spring Boot + MongoDB)

Same layout as the Flights / Employee Leave Management projects.

| Flights project | This project |
|---|---|
| `Flights` collection (Flight) | `Students` collection (Student) |
| `ScheduledFlights` collection (ScheduledFlight) | `CoursesEnrolled` collection (CourseEnrolled) |
| `ScheduledFlights.flightId` -> `Flights._id` | `CoursesEnrolled.studentId` -> `Students._id` |

## Run
1. Start MongoDB on `localhost:27017` (database `Work`).
2. Load sample data (optional):
   ```
   mongoimport --db Work --collection Students        --jsonArray --file sample-data/students.json
   mongoimport --db Work --collection CoursesEnrolled --jsonArray --file sample-data/courses_enrolled.json
   ```
3. `./mvnw spring-boot:run` (Java 17+)

## Endpoints
Students (`/api/students`): GET, GET /{id}, POST, PUT /{id}, DELETE /{id}

Courses enrolled (`/api/courses-enrolled`): GET, POST

Aggregation queries (`$lookup` between the two collections):

| Endpoint | Query |
|---|---|
| `/api/students/dropped` | Students who dropped a course |
| `/api/students/more-than-three-credits` | Courses with credits > 3 |
| `/api/students/cse-courses` | CSE students with course details |
| `/api/students/topper-completed` | CGPA > 8 and course Completed |
| `/api/students/total-credits` | Total credits per student ($group / $sum) |
| `/api/students/at-least-three-credits` | Courses with credits >= 3 |
| `/api/students/sorted-by-cgpa` | Name, CGPA, course sorted by CGPA desc |
