package com.example.student;

import com.example.student.controller.StudentController;
import com.example.student.model.Student;
import com.example.student.repository.StudentRepository;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentRepository repository;

    private Student student(String id, String name, String department, double cgpa) {
        Student s = new Student();
        s.setSid(id);
        s.setName(name);
        s.setDepartment(department);
        s.setCgpa(cgpa);
        return s;
    }

    private AggregationResults<Document> results(Document... docs) {
        return new AggregationResults<>(List.of(docs), new Document());
    }

    // ---------- CRUD tests ----------

    @Test
    void getAllStudents_returnsList() throws Exception {
        when(repository.findAll()).thenReturn(List.of(
                student("S101", "Arun", "CSE", 8.6),
                student("S102", "Divya", "ECE", 7.8)));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Arun"))
                .andExpect(jsonPath("$[1].department").value("ECE"));
    }

    @Test
    void getStudentById_found() throws Exception {
        when(repository.findById("S101"))
                .thenReturn(Optional.of(student("S101", "Arun", "CSE", 8.6)));

        mockMvc.perform(get("/api/students/S101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sid").value("S101"))
                .andExpect(jsonPath("$.department").value("CSE"))
                .andExpect(jsonPath("$.cgpa").value(8.6));
    }

    @Test
    void getStudentById_notFound_returnsEmptyBody() throws Exception {
        when(repository.findById("X999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/students/X999"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void insertStudent_returnsSavedStudent() throws Exception {
        Student saved = student("S105", "Surya", "CSE", 6.9);
        when(repository.save(any(Student.class))).thenReturn(saved);

        String body = """
                {"sid":"S105","name":"Surya","department":"CSE","cgpa":6.9}
                """;

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sid").value("S105"))
                .andExpect(jsonPath("$.name").value("Surya"));

        verify(repository, times(1)).save(any(Student.class));
    }

    @Test
    void updateStudent_existing_updatesFields() throws Exception {
        Student existing = student("S101", "Arun", "CSE", 8.6);
        when(repository.findById("S101")).thenReturn(Optional.of(existing));
        when(repository.save(any(Student.class))).thenAnswer(inv -> inv.getArgument(0));

        String body = """
                {"name":"Arun","department":"ECE","cgpa":9.0}
                """;

        mockMvc.perform(put("/api/students/S101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.department").value("ECE"))
                .andExpect(jsonPath("$.cgpa").value(9.0));
    }

    @Test
    void deleteStudent_existing_returnsSuccessMessage() throws Exception {
        when(repository.existsById("S101")).thenReturn(true);

        mockMvc.perform(delete("/api/students/S101"))
                .andExpect(status().isOk())
                .andExpect(content().string("Student Deleted Successfully"));

        verify(repository).deleteById("S101");
    }

    @Test
    void deleteStudent_missing_returnsNotFoundMessage() throws Exception {
        when(repository.existsById("X999")).thenReturn(false);

        mockMvc.perform(delete("/api/students/X999"))
                .andExpect(status().isOk())
                .andExpect(content().string("Student not Found"));

        verify(repository, never()).deleteById(anyString());
    }

    // ---------- aggregation endpoint tests ----------

    @Test
    void dropped_returnsList() throws Exception {
        when(repository.findStudentsWithDroppedCourse()).thenReturn(results(
                new Document("name", "Divya").append("status", "Dropped")));

        mockMvc.perform(get("/api/students/dropped"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Divya"))
                .andExpect(jsonPath("$[0].status").value("Dropped"));
    }

    @Test
    void moreThanThreeCredits_returnsList() throws Exception {
        when(repository.findStudentsWithMoreThanThreeCredits()).thenReturn(results(
                new Document("name", "Arun").append("credits", 4)));

        mockMvc.perform(get("/api/students/more-than-three-credits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].credits").value(4));
    }

    @Test
    void cseCourses_returnsList() throws Exception {
        when(repository.findCSEStudentsWithCourses()).thenReturn(results(
                new Document("name", "Arun").append("department", "CSE")
                        .append("courseName", "Databases").append("status", "Ongoing")));

        mockMvc.perform(get("/api/students/cse-courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].department").value("CSE"))
                .andExpect(jsonPath("$[0].courseName").value("Databases"));
    }

    @Test
    void topperCompleted_returnsList() throws Exception {
        when(repository.findTopperCompletedStudents()).thenReturn(results(
                new Document("name", "Karthik").append("cgpa", 9.1).append("status", "Completed")));

        mockMvc.perform(get("/api/students/topper-completed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cgpa").value(9.1))
                .andExpect(jsonPath("$[0].status").value("Completed"));
    }

    @Test
    void totalCredits_returnsList() throws Exception {
        when(repository.calculateTotalCredits()).thenReturn(results(
                new Document("name", "Arun").append("totalCredits", 7)));

        mockMvc.perform(get("/api/students/total-credits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].totalCredits").value(7));
    }

    @Test
    void atLeastThreeCredits_returnsList() throws Exception {
        when(repository.findStudentsWithAtLeastThreeCredits()).thenReturn(results(
                new Document("name", "Surya").append("credits", 3)));

        mockMvc.perform(get("/api/students/at-least-three-credits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].credits").value(3));
    }

    @Test
    void sortedByCgpa_returnsListInGivenOrder() throws Exception {
        when(repository.findStudentsSortedByCgpa()).thenReturn(results(
                new Document("name", "Karthik").append("cgpa", 9.1),
                new Document("name", "Arun").append("cgpa", 8.6)));

        mockMvc.perform(get("/api/students/sorted-by-cgpa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cgpa").value(9.1))
                .andExpect(jsonPath("$[1].cgpa").value(8.6));
    }
}
