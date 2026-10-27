package com.example.new_leave.controller;
import com.example.new_leave.model.Employee;
import com.example.new_leave.repository.EmployeeRepository;
import org.bson.Document;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeRepository repository;
    public EmployeeController(EmployeeRepository repository){
        this.repository=repository;
    }
    @GetMapping
    public List<Employee> getAllEmployees(){
        return repository.findAll();
    }
    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable String id){
        return repository.findById(id).orElse(null);
    }
    @PostMapping
    public Employee insertEmployee(@RequestBody Employee employee){
        return repository.save(employee);
    }
    @PutMapping("/{id}")
    public Employee update(@RequestBody Employee employee,@PathVariable String id){
        return repository.findById(id).map(
                existing->{
                    existing.setName(employee.getName());
                    existing.setDepartment(employee.getDepartment());
                    existing.setSalary(employee.getSalary());
                    return repository.save(existing);
                }).orElse(null);
    }
    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable String id){
        if(!repository.existsById(id)){
            return "Employee not Found";
        }
        repository.deleteById(id);
        return "Employee Deleted Successfully";
    }

    // Query 1
    @GetMapping("/sick-leave")
    public List<Document> sickLeave(){
        return repository.findEmployeesOnSickLeave().getMappedResults();
    }

    // Query 2
    @GetMapping("/long-leave")
    public List<Document> longLeave() {
        return repository.findEmployeesWithMoreThanThreeLeaveDays().getMappedResults();
    }

    // Query 3
    @GetMapping("/cse-leave")
    public List<Document> cseLeave() {
        return repository.findCSEEmployeesWithLeaves().getMappedResults();
    }

    // Query 4
    @GetMapping("/high-salary-casual-leave")
    public List<Document> highSalaryCasualLeave() {
        return repository.findHighSalaryEmployeesWithCasualLeave().getMappedResults();
    }

    // Query 5
    @GetMapping("/total-leave-days")
    public List<Document> totalLeaveDays() {
        return repository.calculateTotalLeaveDays().getMappedResults();
    }

    // Query 6
    @GetMapping("/at-least-three-days")
    public List<Document> atLeastThreeDays() {
        return repository.findEmployeesWithAtLeastThreeLeaveDays().getMappedResults();
    }

    // Extra query: sorting
    @GetMapping("/sorted-by-salary")
    public List<Document> sortedBySalary() {
        return repository.findEmployeesSortedBySalary().getMappedResults();
    }

}