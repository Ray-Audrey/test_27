
package com.example.new_leave.repository;

import com.example.new_leave.model.Leave;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LeaveRepository extends MongoRepository<Leave, String> {
}
