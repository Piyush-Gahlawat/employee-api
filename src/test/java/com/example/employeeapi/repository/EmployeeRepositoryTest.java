package com.example.employeeapi.repository;

import com.example.employeeapi.model.Employee;
import com.example.employeeapi.repository.EmployeeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Employee Repository Tests")
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
        
        employee = new Employee();
        employee.setName("John Doe");
        employee.setPosition("Senior Developer");
        employee.setSalary(100000.0);
        employee.setEmail("john.doe@example.com");
    }

    @Test
    @DisplayName("Should save employee successfully")
    void testSaveEmployee() {
        Employee savedEmployee = employeeRepository.save(employee);

        assertNotNull(savedEmployee.getId());
        assertEquals("John Doe", savedEmployee.getName());
        assertEquals("john.doe@example.com", savedEmployee.getEmail());
    }

    @Test
    @DisplayName("Should find employee by id")
    void testFindById() {
        Employee savedEmployee = employeeRepository.save(employee);

        Optional<Employee> found = employeeRepository.findById(savedEmployee.getId());

        assertTrue(found.isPresent());
        assertEquals("John Doe", found.get().getName());
    }

    @Test
    @DisplayName("Should return empty optional for non-existent id")
    void testFindByIdNotFound() {
        Optional<Employee> found = employeeRepository.findById(999L);

        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Should find employees by name")
    void testFindByName() {
        employeeRepository.save(employee);

        List<Employee> employees = employeeRepository.findByName("John Doe");

        assertEquals(1, employees.size());
        assertEquals("John Doe", employees.get(0).getName());
    }

    @Test
    @DisplayName("Should return empty list when name not found")
    void testFindByNameNotFound() {
        List<Employee> employees = employeeRepository.findByName("Non Existent");

        assertTrue(employees.isEmpty());
    }

    @Test
    @DisplayName("Should find employees by position")
    void testFindByPosition() {
        employeeRepository.save(employee);

        List<Employee> employees = employeeRepository.findByPosition("Senior Developer");

        assertEquals(1, employees.size());
        assertEquals("Senior Developer", employees.get(0).getPosition());
    }

    @Test
    @DisplayName("Should find employees by salary")
    void testFindBySalary() {
        employeeRepository.save(employee);

        List<Employee> employees = employeeRepository.findBySalary(100000.0);

        assertEquals(1, employees.size());
        assertEquals(100000.0, employees.get(0).getSalary());
    }

    @Test
    @DisplayName("Should find employees by email")
    void testFindByEmail() {
        employeeRepository.save(employee);

        List<Employee> employees = employeeRepository.findByEmail("john.doe@example.com");

        assertEquals(1, employees.size());
        assertEquals("john.doe@example.com", employees.get(0).getEmail());
    }

    @Test
    @DisplayName("Should update employee successfully")
    void testUpdateEmployee() {
        Employee savedEmployee = employeeRepository.save(employee);
        savedEmployee.setName("Jane Doe");
        savedEmployee.setPosition("Lead Developer");

        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        assertEquals("Jane Doe", updatedEmployee.getName());
        assertEquals("Lead Developer", updatedEmployee.getPosition());
    }

    @Test
    @DisplayName("Should delete employee successfully")
    void testDeleteEmployee() {
        Employee savedEmployee = employeeRepository.save(employee);
        Long id = savedEmployee.getId();

        employeeRepository.deleteById(id);

        Optional<Employee> found = employeeRepository.findById(id);
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Should find all employees")
    void testFindAll() {
        Employee employee2 = new Employee();
        employee2.setName("Jane Smith");
        employee2.setPosition("Developer");
        employee2.setSalary(85000.0);
        employee2.setEmail("jane.smith@example.com");

        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        List<Employee> all = employeeRepository.findAll();

        assertEquals(2, all.size());
    }

    @Test
    @DisplayName("Should handle multiple employees with different attributes")
    void testMultipleEmployees() {
        Employee emp1 = new Employee();
        emp1.setName("Alice");
        emp1.setPosition("Developer");
        emp1.setSalary(90000.0);
        emp1.setEmail("alice@example.com");

        Employee emp2 = new Employee();
        emp2.setName("Bob");
        emp2.setPosition("Manager");
        emp2.setSalary(110000.0);
        emp2.setEmail("bob@example.com");

        employeeRepository.save(emp1);
        employeeRepository.save(emp2);

        List<Employee> developers = employeeRepository.findByPosition("Developer");
        List<Employee> managers = employeeRepository.findByPosition("Manager");

        assertEquals(1, developers.size());
        assertEquals(1, managers.size());
    }

}
