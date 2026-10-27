package com.example.library.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "borrowed_books")
public class BorrowedBook {
    @Id
    private String id;
    private String bookId;

    public BorrowedBook() {}
    public BorrowedBook(String id, String bookId) {
        this.id = id;
        this.bookId = bookId;
    }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }
}
