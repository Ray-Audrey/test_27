package com.example.scholarship.repository;

import com.example.scholarship.model.ScholarshipSelection;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ScholarshipSelectionRepository extends MongoRepository<ScholarshipSelection, String> {
    List<ScholarshipSelection> findByStudentId(String studentId);
}
