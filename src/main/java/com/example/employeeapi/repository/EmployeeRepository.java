package com.example.employeeapi.repository;

import com.example.employeeapi.model.Employee;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findById(Long id);
    List<Employee> findByName(String name);
    List<Employee> findByPosition(String position);
    List<Employee> findBySalary(Double salary);
    List<Employee> findByEmail(String email);

}
