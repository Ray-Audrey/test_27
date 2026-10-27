package com.example.new_leave;

import com.example.new_leave.controller.EmployeeController;
import com.example.new_leave.model.Employee;
import com.example.new_leave.repository.EmployeeRepository;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeRepository repository;

    // ---------- helpers ----------

    private Employee employee(String id, String name, String dept, double salary) {
        Employee e = new Employee();
        e.setEid(id);
        e.setName(name);
        e.setDepartment(dept);
        e.setSalary(salary);
        return e;
    }

    private AggregationResults<Document> results(Document... docs) {
        return new AggregationResults<>(List.of(docs), new Document());
    }

    // ---------- CRUD tests ----------

    // 1
    @Test
    void getAllEmployees_returnsList() throws Exception {
        when(repository.findAll()).thenReturn(List.of(
                employee("E101", "Arun", "CSE", 40000),
                employee("E102", "Priya", "IT", 50000)));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Arun"))
                .andExpect(jsonPath("$[1].department").value("IT"));
    }

    // 2
    @Test
    void getEmployeeById_found() throws Exception {
        when(repository.findById("E101"))
                .thenReturn(Optional.of(employee("E101", "Arun", "CSE", 40000)));

        mockMvc.perform(get("/api/employees/E101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eid").value("E101"))
                .andExpect(jsonPath("$.name").value("Arun"))
                .andExpect(jsonPath("$.salary").value(40000.0));
    }

    // 3
    @Test
    void getEmployeeById_notFound_returnsEmptyBody() throws Exception {
        when(repository.findById("X999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/employees/X999"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    // 4
    @Test
    void insertEmployee_returnsSavedEmployee() throws Exception {
        Employee saved = employee("E105", "Meena", "CSE", 42000);
        when(repository.save(any(Employee.class))).thenReturn(saved);

        String body = """
                {"eid":"E105","name":"Meena","department":"CSE","salary":42000}
                """;

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eid").value("E105"))
                .andExpect(jsonPath("$.name").value("Meena"));

        verify(repository, times(1)).save(any(Employee.class));
    }

    // 5
    @Test
    void updateEmployee_existing_updatesFields() throws Exception {
        Employee existing = employee("E101", "Arun", "CSE", 40000);
        when(repository.findById("E101")).thenReturn(Optional.of(existing));
        when(repository.save(any(Employee.class))).thenAnswer(inv -> inv.getArgument(0));

        String body = """
                {"name":"Arun Kumar","department":"IT","salary":55000}
                """;

        mockMvc.perform(put("/api/employees/E101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Arun Kumar"))
                .andExpect(jsonPath("$.department").value("IT"))
                .andExpect(jsonPath("$.salary").value(55000.0));
    }

    // 6
    @Test
    void deleteEmployee_existing_returnsSuccessMessage() throws Exception {
        when(repository.existsById("E101")).thenReturn(true);

        mockMvc.perform(delete("/api/employees/E101"))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee Deleted Successfully"));

        verify(repository).deleteById("E101");
    }

    // 7
    @Test
    void deleteEmployee_missing_returnsNotFoundMessage() throws Exception {
        when(repository.existsById("X999")).thenReturn(false);

        mockMvc.perform(delete("/api/employees/X999"))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee not Found"));

        verify(repository, never()).deleteById(anyString());
    }

    // ---------- Aggregation endpoint tests ----------

    // 8
    @Test
    void sickLeave_returnsEmployeesOnSickLeave() throws Exception {
        when(repository.findEmployeesOnSickLeave())
                .thenReturn(results(new Document("name", "Arun").append("leaveType", "Sick")));

        mockMvc.perform(get("/api/employees/sick-leave"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Arun"))
                .andExpect(jsonPath("$[0].leaveType").value("Sick"));
    }

    // 9
    @Test
    void longLeave_returnsEmployeesWithMoreThanThreeDays() throws Exception {
        when(repository.findEmployeesWithMoreThanThreeLeaveDays())
                .thenReturn(results(
                        new Document("name", "Priya").append("days", 4),
                        new Document("name", "Kumar").append("days", 5)));

        mockMvc.perform(get("/api/employees/long-leave"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Priya"))
                .andExpect(jsonPath("$[0].days").value(4))
                .andExpect(jsonPath("$[1].days").value(5));
    }

    // 10
    @Test
    void cseLeave_returnsCseEmployeesWithLeaveDetails() throws Exception {
        when(repository.findCSEEmployeesWithLeaves())
                .thenReturn(results(
                        new Document("name", "Arun").append("department", "CSE")
                                .append("leaveType", "Sick").append("days", 3)));

        mockMvc.perform(get("/api/employees/cse-leave"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].department").value("CSE"))
                .andExpect(jsonPath("$[0].leaveType").value("Sick"))
                .andExpect(jsonPath("$[0].days").value(3));
    }

    // 11
    @Test
    void totalLeaveDays_returnsSumPerEmployee() throws Exception {
        when(repository.calculateTotalLeaveDays())
                .thenReturn(results(
                        new Document("name", "Arun").append("totalLeaveDays", 3),
                        new Document("name", "Divya").append("totalLeaveDays", 8)));

        mockMvc.perform(get("/api/employees/total-leave-days"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].totalLeaveDays").value(3))
                .andExpect(jsonPath("$[1].name").value("Divya"))
                .andExpect(jsonPath("$[1].totalLeaveDays").value(8));
    }

    // 12
    @Test
    void sortedBySalary_returnsEmptyArrayWhenNoData() throws Exception {
        when(repository.findEmployeesSortedBySalary()).thenReturn(results());

        mockMvc.perform(get("/api/employees/sorted-by-salary"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}