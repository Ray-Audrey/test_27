package com.example.new_leave.repository;

import com.example.new_leave.model.Employee;
import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface EmployeeRepository extends MongoRepository<Employee,String> {
    // QUERY 1: Employees who took Sick leave
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'Leaves', 'localField': '_id', 'foreignField': 'employeeId', 'as': 'leaveDetails' } }",
            "{ '$unwind': '$leaveDetails' }",
            "{ '$match': { 'leaveDetails.leaveType': 'Sick' } }",
            "{ '$project': { '_id': 0, 'name': 1, 'leaveType': '$leaveDetails.leaveType' } }"
    })
    AggregationResults<Document> findEmployeesOnSickLeave();


    // QUERY 2: Employees who took more than 3 days in a leave record
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'Leaves', 'localField': '_id', 'foreignField': 'employeeId', 'as': 'leaveDetails' } }",
            "{ '$unwind': '$leaveDetails' }",
            "{ '$match': { 'leaveDetails.days': { '$gt': 3 } } }",
            "{ '$project': { '_id': 0, 'name': 1, 'days': '$leaveDetails.days' } }"
    })
    AggregationResults<Document> findEmployeesWithMoreThanThreeLeaveDays();


    // QUERY 3: CSE employees and their leave details
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'Leaves', 'localField': '_id', 'foreignField': 'employeeId', 'as': 'leaveDetails' } }",
            "{ '$unwind': '$leaveDetails' }",
            "{ '$match': { 'department': 'CSE' } }",
            "{ '$project': { '_id': 0, 'name': 1, 'department': 1, 'leaveType': '$leaveDetails.leaveType', 'days': '$leaveDetails.days' } }"
    })
    AggregationResults<Document> findCSEEmployeesWithLeaves();


    // QUERY 4: Salary > 40000 and Casual leave
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'Leaves', 'localField': '_id', 'foreignField': 'employeeId', 'as': 'leaveDetails' } }",
            "{ '$unwind': '$leaveDetails' }",
            "{ '$match': { 'salary': { '$gt': 40000 }, 'leaveDetails.leaveType': 'Casual' } }",
            "{ '$project': { '_id': 0, 'name': 1, 'salary': 1, 'leaveType': '$leaveDetails.leaveType' } }"
    })
    AggregationResults<Document> findHighSalaryEmployeesWithCasualLeave();


    // QUERY 5: Calculate total leave days per employee
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'Leaves', 'localField': '_id', 'foreignField': 'employeeId', 'as': 'leaveDetails' } }",
            "{ '$unwind': '$leaveDetails' }",
            "{ '$group': { '_id': '$_id', 'name': { '$first': '$name' }, 'totalLeaveDays': { '$sum': '$leaveDetails.days' } } }",
            "{ '$project': { '_id': 0, 'name': 1, 'totalLeaveDays': 1 } }"
    })
    AggregationResults<Document> calculateTotalLeaveDays();


    // QUERY 6: Employees who took at least 3 days in a leave record
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'Leaves', 'localField': '_id', 'foreignField': 'employeeId', 'as': 'leaveDetails' } }",
            "{ '$unwind': '$leaveDetails' }",
            "{ '$match': { 'leaveDetails.days': { '$gte': 3 } } }",
            "{ '$project': { '_id': 0, 'name': 1, 'days': '$leaveDetails.days' } }"
    })
    AggregationResults<Document> findEmployeesWithAtLeastThreeLeaveDays();


    // EXTRA: Employee name, salary and leave type,
    // sorted by salary from highest to lowest
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'Leaves', 'localField': '_id', 'foreignField': 'employeeId', 'as': 'leaveDetails' } }",
            "{ '$unwind': '$leaveDetails' }",
            "{ '$sort': { 'salary': -1 } }",
            "{ '$project': { '_id': 0, 'name': 1, 'salary': 1, 'leaveType': '$leaveDetails.leaveType' } }"
    })
    AggregationResults<Document> findEmployeesSortedBySalary();
}
