package com.example.student.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Collection 1
@Data
@Document(collection = "Students")
public class Student {
    @Id
    private String sid;
    private String name;
    private String department;
    private double cgpa;
}
