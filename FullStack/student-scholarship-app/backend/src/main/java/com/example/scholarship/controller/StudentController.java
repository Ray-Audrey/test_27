package com.example.scholarship.controller;

import com.example.scholarship.model.Student;
import com.example.scholarship.repository.StudentRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentRepository repository;

    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Student> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Student getById(@PathVariable String id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    public Student add(@RequestBody Student student) {
        student.setId(null);
        return repository.save(student);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable String id, @RequestBody Student student) {
        student.setId(id);
        return repository.save(student);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        repository.deleteById(id);
    }
}
