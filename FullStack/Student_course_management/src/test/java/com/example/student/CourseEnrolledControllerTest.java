package com.example.student;

import com.example.student.controller.CourseEnrolledController;
import com.example.student.model.CourseEnrolled;
import com.example.student.repository.CourseEnrolledRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseEnrolledController.class)
class CourseEnrolledControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseEnrolledRepository repository;

    private CourseEnrolled course(String ceid, String studentId, String courseName, int credits, String status) {
        CourseEnrolled c = new CourseEnrolled();
        c.setCeid(ceid);
        c.setStudentId(studentId);
        c.setCourseName(courseName);
        c.setCredits(credits);
        c.setStatus(status);
        return c;
    }

    @Test
    void getAllCoursesEnrolled_returnsList() throws Exception {
        when(repository.findAll()).thenReturn(List.of(
                course("C1", "S101", "Data Structures", 4, "Completed"),
                course("C2", "S102", "Signals", 4, "Dropped")));

        mockMvc.perform(get("/api/courses-enrolled"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].studentId").value("S101"))
                .andExpect(jsonPath("$[1].status").value("Dropped"));
    }

    @Test
    void addCourseEnrolled_returnsSavedRecord() throws Exception {
        when(repository.save(any(CourseEnrolled.class)))
                .thenReturn(course("C3", "S101", "Databases", 3, "Ongoing"));

        String body = """
                {"ceid":"C3","studentId":"S101","courseName":"Databases","credits":3,"status":"Ongoing"}
                """;

        mockMvc.perform(post("/api/courses-enrolled")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ceid").value("C3"))
                .andExpect(jsonPath("$.credits").value(3));

        verify(repository, times(1)).save(any(CourseEnrolled.class));
    }
}
