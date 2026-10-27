package com.example.new_leave.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection="Employees")
public class Employee {
    @Id
    private String eid;
    private String name;
    private String department;
    private double salary;
}
