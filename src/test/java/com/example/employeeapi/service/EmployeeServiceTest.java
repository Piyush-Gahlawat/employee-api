package com.example.employeeapi.service;

import com.example.employeeapi.model.Employee;
import com.example.employeeapi.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Employee Service Tests")
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private Employee updatedEmployee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setPosition("Senior Developer");
        employee.setSalary(100000.0);
        employee.setEmail("john.doe@example.com");

        updatedEmployee = new Employee();
        updatedEmployee.setId(1L);
        updatedEmployee.setName("John Smith");
        updatedEmployee.setPosition("Lead Developer");
        updatedEmployee.setSalary(120000.0);
        updatedEmployee.setEmail("john.smith@example.com");
    }

    @Test
    @DisplayName("Should return all employees")
    void testGetAllEmployees() {
        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setName("Jane Doe");
        employee2.setPosition("Developer");
        employee2.setSalary(90000.0);
        employee2.setEmail("jane.doe@example.com");

        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee, employee2));

        List<Employee> result = employeeService.getAllEmployees();

        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Doe", result.get(1).getName());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no employees exist")
    void testGetAllEmployeesEmpty() {
        when(employeeRepository.findAll()).thenReturn(List.of());

        List<Employee> result = employeeService.getAllEmployees();

        assertTrue(result.isEmpty());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get employee by id successfully")
    void testGetEmployeeById() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.getEmployeeById(1L);

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return empty optional when employee not found")
    void testGetEmployeeByIdNotFound() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Employee> result = employeeService.getEmployeeById(999L);

        assertFalse(result.isPresent());
        verify(employeeRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should create employee successfully")
    void testCreateEmployee() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee result = employeeService.createEmployee(employee);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    @DisplayName("Should update employee successfully")
    void testUpdateEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        Optional<Employee> result = employeeService.updateEmployee(1L, updatedEmployee);

        assertTrue(result.isPresent());
        assertEquals("John Smith", result.get().getName());
        assertEquals("Lead Developer", result.get().getPosition());
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should return empty optional when updating non-existent employee")
    void testUpdateEmployeeNotFound() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Employee> result = employeeService.updateEmployee(999L, updatedEmployee);

        assertFalse(result.isPresent());
        verify(employeeRepository, times(1)).findById(999L);
        verify(employeeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete employee successfully")
    void testDeleteEmployee() {
        employeeService.deleteEmployee(1L);

        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should handle null employee gracefully")
    void testCreateNullEmployee() {
        assertThrows(NullPointerException.class, () -> {
            employeeService.createEmployee(null);
        });
    }

}
