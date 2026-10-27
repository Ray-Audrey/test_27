package com.example.scholarship.controller;

import com.example.scholarship.model.Student;
import com.example.scholarship.repository.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper mapper;
    @MockBean StudentRepository repository;

    @Test
    void getAllStudents() throws Exception {
        Student student = new Student();
        student.setName("Asha Kumar");
        when(repository.findAll()).thenReturn(List.of(student));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Asha Kumar"));
        verify(repository).findAll();
    }

    @Test
    void addStudent() throws Exception {
        Student student = new Student();
        student.setName("Asha Kumar");
        student.setEmail("asha@example.com");
        student.setDepartment("CSE");
        student.setCgpa(9.1);
        when(repository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Asha Kumar"));
        verify(repository).save(any(Student.class));
    }

    @Test
    void getStudentById() throws Exception {
        Student student = new Student();
        student.setId("s1");
        student.setName("Asha Kumar");
        when(repository.findById("s1")).thenReturn(Optional.of(student));

        mockMvc.perform(get("/api/students/s1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("s1"));
    }

    @Test
    void updateStudent() throws Exception {
        Student student = new Student();
        student.setName("Asha Updated");
        when(repository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/api/students/s1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("s1"))
                .andExpect(jsonPath("$.name").value("Asha Updated"));
        verify(repository).save(any(Student.class));
    }

    @Test
    void deleteStudent() throws Exception {
        mockMvc.perform(delete("/api/students/s1"))
                .andExpect(status().isOk());
        verify(repository).deleteById("s1");
    }
}
