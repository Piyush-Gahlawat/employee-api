package com.example.employeeapi.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.employeeapi.model.Employee;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Employee Model Tests")
class EmployeeTest {

    private Validator validator;
    private Employee employee;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setPosition("Developer");
        employee.setSalary(100000.0);
        employee.setEmail("john@example.com");
    }

    @Test
    @DisplayName("Should create valid employee")
    void testValidEmployee() {
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation for missing name")
    void testMissingName() {
        employee.setName(null);
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation for blank name")
    void testBlankName() {
        employee.setName("");
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation for missing position")
    void testMissingPosition() {
        employee.setPosition(null);
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation for missing salary")
    void testMissingSalary() {
        employee.setSalary(null);
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation for invalid email")
    void testInvalidEmail() {
        employee.setEmail("invalid-email");
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation for missing email")
    void testMissingEmail() {
        employee.setEmail(null);
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should accept valid email format")
    void testValidEmailFormat() {
        employee.setEmail("test.user@example.com");
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should test equality")
    void testEquality() {
        Employee emp1 = new Employee(1L, "John", "Developer", 100000.0, "john@example.com");
        Employee emp2 = new Employee(1L, "John", "Developer", 100000.0, "john@example.com");

        assertEquals(emp1, emp2);
    }

    @Test
    @DisplayName("Should test getter and setter")
    void testGettersAndSetters() {
        Employee emp = new Employee();
        emp.setId(5L);
        emp.setName("Test User");
        emp.setPosition("QA");
        emp.setSalary(75000.0);
        emp.setEmail("test@example.com");

        assertEquals(5L, emp.getId());
        assertEquals("Test User", emp.getName());
        assertEquals("QA", emp.getPosition());
        assertEquals(75000.0, emp.getSalary());
        assertEquals("test@example.com", emp.getEmail());
    }

}
