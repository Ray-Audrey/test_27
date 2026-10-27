package com.example.student.repository;

import com.example.student.model.Student;
import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface StudentRepository extends MongoRepository<Student, String> {

    // QUERY 1: Students who have dropped a course
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'CoursesEnrolled', 'localField': '_id', 'foreignField': 'studentId', 'as': 'courseDetails' } }",
            "{ '$unwind': '$courseDetails' }",
            "{ '$match': { 'courseDetails.status': 'Dropped' } }",
            "{ '$project': { '_id': 0, 'name': 1, 'department': 1, 'courseName': '$courseDetails.courseName', 'status': '$courseDetails.status' } }"
    })
    AggregationResults<Document> findStudentsWithDroppedCourse();


    // QUERY 2: Courses with more than 3 credits
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'CoursesEnrolled', 'localField': '_id', 'foreignField': 'studentId', 'as': 'courseDetails' } }",
            "{ '$unwind': '$courseDetails' }",
            "{ '$match': { 'courseDetails.credits': { '$gt': 3 } } }",
            "{ '$project': { '_id': 0, 'name': 1, 'courseName': '$courseDetails.courseName', 'credits': '$courseDetails.credits' } }"
    })
    AggregationResults<Document> findStudentsWithMoreThanThreeCredits();


    // QUERY 3: CSE students and their course details
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'CoursesEnrolled', 'localField': '_id', 'foreignField': 'studentId', 'as': 'courseDetails' } }",
            "{ '$unwind': '$courseDetails' }",
            "{ '$match': { 'department': 'CSE' } }",
            "{ '$project': { '_id': 0, 'name': 1, 'department': 1, 'courseName': '$courseDetails.courseName', 'status': '$courseDetails.status' } }"
    })
    AggregationResults<Document> findCSEStudentsWithCourses();


    // QUERY 4: CGPA > 8 and course Completed
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'CoursesEnrolled', 'localField': '_id', 'foreignField': 'studentId', 'as': 'courseDetails' } }",
            "{ '$unwind': '$courseDetails' }",
            "{ '$match': { 'cgpa': { '$gt': 8 }, 'courseDetails.status': 'Completed' } }",
            "{ '$project': { '_id': 0, 'name': 1, 'cgpa': 1, 'courseName': '$courseDetails.courseName', 'status': '$courseDetails.status' } }"
    })
    AggregationResults<Document> findTopperCompletedStudents();


    // QUERY 5: Calculate total credits per student
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'CoursesEnrolled', 'localField': '_id', 'foreignField': 'studentId', 'as': 'courseDetails' } }",
            "{ '$unwind': '$courseDetails' }",
            "{ '$group': { '_id': '$_id', 'name': { '$first': '$name' }, 'totalCredits': { '$sum': '$courseDetails.credits' } } }",
            "{ '$project': { '_id': 0, 'name': 1, 'totalCredits': 1 } }"
    })
    AggregationResults<Document> calculateTotalCredits();


    // QUERY 6: Courses with at least 3 credits
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'CoursesEnrolled', 'localField': '_id', 'foreignField': 'studentId', 'as': 'courseDetails' } }",
            "{ '$unwind': '$courseDetails' }",
            "{ '$match': { 'courseDetails.credits': { '$gte': 3 } } }",
            "{ '$project': { '_id': 0, 'name': 1, 'courseName': '$courseDetails.courseName', 'credits': '$courseDetails.credits' } }"
    })
    AggregationResults<Document> findStudentsWithAtLeastThreeCredits();


    // EXTRA: Name, CGPA and course, sorted by CGPA from highest to lowest
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'CoursesEnrolled', 'localField': '_id', 'foreignField': 'studentId', 'as': 'courseDetails' } }",
            "{ '$unwind': '$courseDetails' }",
            "{ '$sort': { 'cgpa': -1 } }",
            "{ '$project': { '_id': 0, 'name': 1, 'cgpa': 1, 'courseName': '$courseDetails.courseName' } }"
    })
    AggregationResults<Document> findStudentsSortedByCgpa();
}
