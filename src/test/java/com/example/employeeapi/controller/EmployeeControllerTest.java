package com.example.employeeapi.controller;

import com.example.employeeapi.model.Employee;
import com.example.employeeapi.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Employee Controller Tests")
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Employee employee;
    private Employee employee2;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setPosition("Senior Developer");
        employee.setSalary(100000.0);
        employee.setEmail("john.doe@example.com");

        employee2 = new Employee();
        employee2.setId(2L);
        employee2.setName("Jane Smith");
        employee2.setPosition("Developer");
        employee2.setSalary(85000.0);
        employee2.setEmail("jane.smith@example.com");
    }

    @Test
    @DisplayName("GET /api/employees - Should return all employees")
    void testGetAllEmployees() throws Exception {
        List<Employee> employees = Arrays.asList(employee, employee2);
        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", equalTo("John Doe")))
                .andExpect(jsonPath("$[1].name", equalTo("Jane Smith")));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    @DisplayName("GET /api/employees - Should return empty list")
    void testGetAllEmployeesEmpty() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(List.of());

        mockMvc.perform(get("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    @DisplayName("GET /api/employees/{id} - Should return employee by id")
    void testGetEmployeeById() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(employee));

        mockMvc.perform(get("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.name", equalTo("John Doe")))
                .andExpect(jsonPath("$.position", equalTo("Senior Developer")))
                .andExpect(jsonPath("$.email", equalTo("john.doe@example.com")));

        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    @DisplayName("GET /api/employees/{id} - Should return 404 when employee not found")
    void testGetEmployeeByIdNotFound() throws Exception {
        when(employeeService.getEmployeeById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/employees/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(employeeService, times(1)).getEmployeeById(999L);
    }

    @Test
    @DisplayName("POST /api/employees - Should create employee successfully")
    void testCreateEmployee() throws Exception {
        Employee newEmployee = new Employee();
        newEmployee.setName("Alice Johnson");
        newEmployee.setPosition("QA Engineer");
        newEmployee.setSalary(80000.0);
        newEmployee.setEmail("alice.johnson@example.com");

        when(employeeService.createEmployee(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo("John Doe")))
                .andExpect(jsonPath("$.email", equalTo("john.doe@example.com")));

        verify(employeeService, times(1)).createEmployee(any(Employee.class));
    }

    @Test
    @DisplayName("POST /api/employees - Should return 400 for invalid email")
    void testCreateEmployeeInvalidEmail() throws Exception {
        Employee invalidEmployee = new Employee();
        invalidEmployee.setName("Bob Smith");
        invalidEmployee.setPosition("Developer");
        invalidEmployee.setSalary(90000.0);
        invalidEmployee.setEmail("invalid-email");

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidEmployee)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/employees - Should return 400 for missing required fields")
    void testCreateEmployeeMissingFields() throws Exception {
        Employee incompleteEmployee = new Employee();
        incompleteEmployee.setName("Charlie Brown");

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incompleteEmployee)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/employees/{id} - Should update employee successfully")
    void testUpdateEmployee() throws Exception {
        Employee updatedEmployee = new Employee();
        updatedEmployee.setName("John Doe Updated");
        updatedEmployee.setPosition("Lead Developer");
        updatedEmployee.setSalary(120000.0);
        updatedEmployee.setEmail("john.updated@example.com");

        Employee updated = new Employee();
        updated.setId(1L);
        updated.setName("John Doe Updated");
        updated.setPosition("Lead Developer");
        updated.setSalary(120000.0);
        updated.setEmail("john.updated@example.com");

        // Fixed: use matchers for both arguments (eq for ID, any for Employee)
        when(employeeService.updateEmployee(eq(1L), any(Employee.class))).thenReturn(Optional.of(updated));

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("John Doe Updated")))
                .andExpect(jsonPath("$.position", equalTo("Lead Developer")));

        verify(employeeService, times(1)).updateEmployee(anyLong(), any(Employee.class));
    }

    @Test
    @DisplayName("PUT /api/employees/{id} - Should return 404 when employee not found")
    void testUpdateEmployeeNotFound() throws Exception {
        Employee updatedEmployee = new Employee();
        updatedEmployee.setName("Jane Doe");
        updatedEmployee.setPosition("Manager");
        updatedEmployee.setSalary(110000.0);
        updatedEmployee.setEmail("jane@example.com");

        // Fixed: use matchers
        when(employeeService.updateEmployee(eq(999L), any(Employee.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/employees/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee)))
                .andExpect(status().isNotFound());

        verify(employeeService, times(1)).updateEmployee(anyLong(), any(Employee.class));
    }

    @Test
    @DisplayName("DELETE /api/employees/{id} - Should delete employee successfully")
    void testDeleteEmployee() throws Exception {
        // Improved: explicitly mock void method
        doNothing().when(employeeService).deleteEmployee(1L);

        mockMvc.perform(delete("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(employeeService, times(1)).deleteEmployee(1L);
    }

    @Test
    @DisplayName("POST /api/employees - Should return 400 for negative salary")
    void testCreateEmployeeNegativeSalary() throws Exception {
        Employee invalidEmployee = new Employee();
        invalidEmployee.setName("David Lee");
        invalidEmployee.setPosition("Developer");
        invalidEmployee.setSalary(-50000.0);
        invalidEmployee.setEmail("david.lee@example.com");

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidEmployee)))
                .andExpect(status().isBadRequest());
    }
}