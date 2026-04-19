package com.example.employeeapi.service;
import com.example.employeeapi.model.Employee;
import com.example.employeeapi.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService { 
    private final EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Optional<Employee> updateEmployee(Long id, Employee updatedEmployee) {
        return employeeRepository.findById(id).map(employee -> {
            employee.setName(updatedEmployee.getName());
            employee.setPosition(updatedEmployee.getPosition());
            employee.setSalary(updatedEmployee.getSalary());
            employee.setEmail(updatedEmployee.getEmail());
            return employeeRepository.save(employee);
        });
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

}
