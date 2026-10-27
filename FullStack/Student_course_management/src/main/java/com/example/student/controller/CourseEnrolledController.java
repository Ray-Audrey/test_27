package com.example.student.controller;

import com.example.student.model.CourseEnrolled;
import com.example.student.repository.CourseEnrolledRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses-enrolled")
public class CourseEnrolledController {

    private final CourseEnrolledRepository repository;

    public CourseEnrolledController(CourseEnrolledRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<CourseEnrolled> getAllCoursesEnrolled() {
        return repository.findAll();
    }

    @PostMapping
    public CourseEnrolled addCourseEnrolled(@RequestBody CourseEnrolled courseEnrolled) {
        return repository.save(courseEnrolled);
    }
}
