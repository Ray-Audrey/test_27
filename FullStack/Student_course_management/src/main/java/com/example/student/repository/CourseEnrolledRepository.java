package com.example.student.repository;

import com.example.student.model.CourseEnrolled;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseEnrolledRepository extends MongoRepository<CourseEnrolled, String> {
}
