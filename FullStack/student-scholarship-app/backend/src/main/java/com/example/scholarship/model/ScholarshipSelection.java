package com.example.scholarship.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "scholarshipSelections")
public class ScholarshipSelection {
    @Id
    private String id;
    private String studentId;
    private String scholarshipName;
    private double amount;
    private String status;

    public ScholarshipSelection() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getScholarshipName() { return scholarshipName; }
    public void setScholarshipName(String scholarshipName) { this.scholarshipName = scholarshipName; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
