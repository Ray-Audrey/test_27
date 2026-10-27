# Student Scholarship Management System

A minimal full-stack application with:
- **Backend:** Java 17, Spring Boot 3.5.6, Spring Web, Spring Data MongoDB
- **Database:** MongoDB (`student_scholarship_db`)
- **Frontend:** Vue 3 + Vite, Vue Router, Pinia, Axios
- **Session management:** simple server-side HTTP session login/logout
- **Backend tests:** 5 MockMvc tests for Student CRUD endpoints

## Demo login
- Username: `admin`
- Password: `password123`

This is a classroom/demo login only. Replace it with real authentication before deployment.

## 1. Start MongoDB
Make sure MongoDB is running locally on `mongodb://localhost:27017`.

## 2. Start the backend
Open a terminal in `backend/`:

```powershell
.\mvnw.cmd spring-boot:run
```

If this project does not include a Maven Wrapper in your environment, use:

```powershell
mvn spring-boot:run
```

Backend URL: `http://localhost:8080`

Run the 5 tests:

```powershell
.\mvnw.cmd clean test
```

## 3. Start the frontend
Open another terminal in `frontend/`:

```powershell
npm install
npm run dev
```

Open the Vite URL shown in the terminal, usually `http://localhost:5173`.

## Collections
### `students`
```json
{
  "name": "Asha Kumar",
  "email": "asha@example.com",
  "department": "CSE",
  "cgpa": 9.1
}
```

### `scholarshipSelections`
```json
{
  "studentId": "<MongoDB student _id>",
  "scholarshipName": "Merit Scholarship",
  "amount": 25000,
  "status": "Selected"
}
```

`studentId` links a scholarship selection to a student. One student can have multiple scholarship selection records.

## API routes
- `POST /api/auth/login` — body: `{"username":"admin","password":"password123"}`
- `GET /api/auth/me`
- `POST /api/auth/logout`
- `GET /api/students`
- `GET /api/students/{id}`
- `POST /api/students`
- `PUT /api/students/{id}`
- `DELETE /api/students/{id}`
- `GET /api/selections`
- `GET /api/selections/student/{studentId}`
- `POST /api/selections`
- `PUT /api/selections/{id}`
- `DELETE /api/selections/{id}`

The frontend sends requests with cookies enabled so the browser session is maintained.
