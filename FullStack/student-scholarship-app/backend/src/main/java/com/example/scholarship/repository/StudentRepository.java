package com.example.scholarship.repository;

import com.example.scholarship.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentRepository extends MongoRepository<Student, String> {}
