package com.example.student.controller;

import com.example.student.model.Student;
import com.example.student.repository.StudentRepository;
import org.bson.Document;
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
    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable String id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    public Student insertStudent(@RequestBody Student student) {
        return repository.save(student);
    }

    @PutMapping("/{id}")
    public Student update(@RequestBody Student student, @PathVariable String id) {
        return repository.findById(id).map(
                existing -> {
                    existing.setName(student.getName());
                    existing.setDepartment(student.getDepartment());
                    existing.setCgpa(student.getCgpa());
                    return repository.save(existing);
                }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable String id) {
        if (!repository.existsById(id)) {
            return "Student not Found";
        }
        repository.deleteById(id);
        return "Student Deleted Successfully";
    }

    // Query 1
    @GetMapping("/dropped")
    public List<Document> dropped() {
        return repository.findStudentsWithDroppedCourse().getMappedResults();
    }

    // Query 2
    @GetMapping("/more-than-three-credits")
    public List<Document> moreThanThreeCredits() {
        return repository.findStudentsWithMoreThanThreeCredits().getMappedResults();
    }

    // Query 3
    @GetMapping("/cse-courses")
    public List<Document> cseCourses() {
        return repository.findCSEStudentsWithCourses().getMappedResults();
    }

    // Query 4
    @GetMapping("/topper-completed")
    public List<Document> topperCompleted() {
        return repository.findTopperCompletedStudents().getMappedResults();
    }

    // Query 5
    @GetMapping("/total-credits")
    public List<Document> totalCredits() {
        return repository.calculateTotalCredits().getMappedResults();
    }

    // Query 6
    @GetMapping("/at-least-three-credits")
    public List<Document> atLeastThreeCredits() {
        return repository.findStudentsWithAtLeastThreeCredits().getMappedResults();
    }

    // Extra query: sorting
    @GetMapping("/sorted-by-cgpa")
    public List<Document> sortedByCgpa() {
        return repository.findStudentsSortedByCgpa().getMappedResults();
    }

}
