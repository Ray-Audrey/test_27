package com.example.scholarship.controller;

import com.example.scholarship.model.ScholarshipSelection;
import com.example.scholarship.repository.ScholarshipSelectionRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/selections")
public class ScholarshipSelectionController {
    private final ScholarshipSelectionRepository repository;

    public ScholarshipSelectionController(ScholarshipSelectionRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<ScholarshipSelection> getAll() {
        return repository.findAll();
    }

    @GetMapping("/student/{studentId}")
    public List<ScholarshipSelection> getByStudent(@PathVariable String studentId) {
        return repository.findByStudentId(studentId);
    }

    @PostMapping
    public ScholarshipSelection add(@RequestBody ScholarshipSelection selection) {
        selection.setId(null);
        return repository.save(selection);
    }

    @PutMapping("/{id}")
    public ScholarshipSelection update(@PathVariable String id, @RequestBody ScholarshipSelection selection) {
        selection.setId(id);
        return repository.save(selection);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        repository.deleteById(id);
    }
}
