package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.model.BorrowedBook;
import com.example.library.repository.BookRepository;
import com.example.library.repository.BorrowedBookRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LibraryController {
    private final BookRepository books;
    private final BorrowedBookRepository borrowed;

    public LibraryController(BookRepository books, BorrowedBookRepository borrowed) {
        this.books = books;
        this.borrowed = borrowed;
    }

    private boolean loggedIn(HttpSession session) {
        return session.getAttribute("username") != null;
    }

    @GetMapping("/books")
    public ResponseEntity<?> allBooks(HttpSession session) {
        if (!loggedIn(session)) return unauthorized();
        return ResponseEntity.ok(books.findAll());
    }

    @PostMapping("/books")
    public ResponseEntity<?> addBook(@RequestBody Book book, HttpSession session) {
        if (!loggedIn(session)) return unauthorized();
        if (book.getTitle() == null || book.getTitle().isBlank())
            return ResponseEntity.badRequest().body(Map.of("message", "Title is required"));
        book.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(books.save(book));
    }

    @GetMapping("/borrowed")
    public ResponseEntity<?> allBorrowed(HttpSession session) {
        if (!loggedIn(session)) return unauthorized();
        return ResponseEntity.ok(borrowed.findAll());
    }

    @PostMapping("/borrow/{bookId}")
    public ResponseEntity<?> borrow(@PathVariable String bookId, HttpSession session) {
        if (!loggedIn(session)) return unauthorized();
        if (!books.existsById(bookId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Book not found"));
        if (!borrowed.findByBookId(bookId).isEmpty())
            return ResponseEntity.badRequest().body(Map.of("message", "Book is already borrowed"));
        return ResponseEntity.status(HttpStatus.CREATED).body(borrowed.save(new BorrowedBook(null, bookId)));
    }

    @DeleteMapping("/borrowed/{borrowedId}")
    public ResponseEntity<?> returnBook(@PathVariable String borrowedId, HttpSession session) {
        if (!loggedIn(session)) return unauthorized();
        if (!borrowed.existsById(borrowedId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Borrow record not found"));
        borrowed.deleteById(borrowedId);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<?> unauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Please log in"));
    }
}
