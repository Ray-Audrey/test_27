# Library Management System

Minimal full-stack library management project using Vue 3, Pinia, Vue Router, Spring Boot, MongoDB, and HTTP session-based login.

## Data model

- `books` collection: `_id` (MongoDB id), `title`, `author`
- `borrowed_books` collection: `_id`, `bookId` (references the book's id)

`bookId` is the dependent/reference key in `borrowed_books`. The book record keeps basic title and author details so the library can display and manage books. MongoDB is document-based, so these are document fields rather than SQL table columns.

## Demo login
- Username: `admin`
- Password: `admin123`

## Requirements
- Java 17+
- Maven
- Node.js and npm
- MongoDB running locally on port 27017

## Run backend
From `backend/`:
```bash
mvn spring-boot:run
```
Backend runs on `http://localhost:8080`. Database: `library_db`.

## Run frontend
From `frontend/`:
```bash
npm install
npm run dev
```
Open the Vite URL shown in the terminal (normally `http://localhost:5173`).

## Run backend tests
From `backend/`:
```bash
mvn test
```
Includes five focused authentication/session tests.

## Features
- Session-based login/logout
- List and add books
- Borrow a book (creates a borrowed-book document referring to the book id)
- Return a borrowed book
- Pinia authentication state and Vue Router navigation guard

This is a classroom demo, not production authentication. The username/password are hard-coded and should not be used for real accounts.
