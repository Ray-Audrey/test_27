package com.example.library.repository;

import com.example.library.model.BorrowedBook;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface BorrowedBookRepository extends MongoRepository<BorrowedBook, String> {
    List<BorrowedBook> findByBookId(String bookId);
}
