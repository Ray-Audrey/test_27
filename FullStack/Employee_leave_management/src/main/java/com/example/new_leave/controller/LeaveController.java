
package com.example.new_leave.controller;

import com.example.new_leave.model.Leave;
import com.example.new_leave.repository.LeaveRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
class LeaveController {

    private final LeaveRepository repository;

    public LeaveController(LeaveRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Leave> getAllLeaves() {
        return repository.findAll();
    }

    @PostMapping
    public Leave addLeave(@RequestBody Leave leave) {
        return repository.save(leave);
    }
}

