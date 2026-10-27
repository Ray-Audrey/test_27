package com.example.new_leave.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Leaves")
@Data
public class Leave {
    @Id
    private String lid;
    private String employeeId;
    private String leaveType;
    private int days;
}

