package com.example.student.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Collection 2
@Document(collection = "CoursesEnrolled")
@Data
public class CourseEnrolled {
    @Id
    private String ceid;
    private String studentId;       // refers to Student.sid
    private String courseName;
    private int credits;
    private String status;          // Ongoing / Completed / Dropped
}
