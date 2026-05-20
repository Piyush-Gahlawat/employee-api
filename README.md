# Employee Management REST API

[![Java CI with Maven](https://github.com/Piyush-Gahlawat/employee-api/actions/workflows/maven-test.yml/badge.svg)](https://github.com/Piyush-Gahlawat/employee-api/actions/workflows/maven-test.yml)

A production-ready REST API built with Spring Boot for managing employee records. Supports full CRUD operations with a layered architecture following industry best practices. Includes comprehensive unit tests, custom exception handling, and validation.

## 🚀 Features

- ✅ Complete CRUD operations for employee records
- ✅ RESTful API design with proper HTTP status codes
- ✅ Layered architecture (Controller → Service → Repository)
- ✅ Comprehensive validation with Jakarta Bean Validation
- ✅ Custom exception handling with GlobalExceptionHandler
- ✅ 46 unit tests covering all layers (Service, Controller, Repository, Model, Exception Handler)
- ✅ Mock-based and integration tests
- ✅ Lombok annotations for clean code
- ✅ H2 in-memory database for development

## 🛠️ Tech Stack

- **Java**: 21 (LTS)
- **Spring Boot**: 3.4.3
- **Spring Data JPA**: Data access layer
- **Spring Test**: Integration and unit testing
- **Spring Validation**: Jakarta Bean Validation API
- **H2 Database**: In-memory database for development
- **Lombok**: Reduce boilerplate code
- **Jackson**: JSON processing
- **Mockito**: Unit test mocking
- **Maven**: Build and dependency management
- **JUnit 5**: Testing framework

## 📦 Project Structure

```
src/
├── main/
│   ├── java/com/example/employeeapi/
│   │   ├── EmployeeApiApplication.java
│   │   ├── controller/
│   │   │   └── EmployeeController.java
│   │   ├── service/
│   │   │   └── EmployeeService.java
│   │   ├── repository/
│   │   │   └── EmployeeRepository.java
│   │   ├── model/
│   │   │   └── Employee.java
│   │   ├── exception/
│   │   │   ├── EmployeeNotFoundException.java
│   │   │   ├── InvalidEmployeeException.java
│   │   │   ├── DuplicateEmailException.java
│   │   │   └── GlobalExceptionHandler.java
│   │   └── config/
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/example/employeeapi/
        ├── controller/
        │   └── EmployeeControllerTest.java
        ├── service/
        │   └── EmployeeServiceTest.java
        ├── repository/
        │   └── EmployeeRepositoryTest.java
        ├── model/
        │   └── EmployeeTest.java
        └── exception/
            └── GlobalExceptionHandlerTest.java
```

## 📥 Installation & Setup

### Prerequisites
- Java 21 or higher
- Maven 3.6+

### Build and Run

```bash
# Clone the repository
git clone <repository-url>
cd employee-api

# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## � Docker & Containerization

### Quick Start with Docker Compose

```bash
# Build and start the container
docker-compose up --build

# Stop the container
docker-compose down

# View logs
docker-compose logs -f employee-api
```

### Build Docker Image

```bash
# Build the image
docker build -t employee-api:latest .

# Run the container
docker run -p 8080:8080 employee-api:latest
```

### Dockerfile Options

- **Dockerfile** - Multi-stage build with Alpine Linux (recommended, ~200MB)
- **Dockerfile.slim** - Alternative with explicit Docker registry

For detailed Docker documentation, see [DOCKER.md](DOCKER.md)

### Kubernetes Deployment

Deploy to Kubernetes using the provided manifest:

```bash
kubectl apply -f k8s-deployment.yaml
```

The manifest includes:
- Deployment with 3 replicas
- Service exposure
- Horizontal Pod Autoscaler (HPA)
- Resource limits and health checks
- Security best practices

See [k8s-deployment.yaml](k8s-deployment.yaml) for details.

## �🔌 API Endpoints

### Get All Employees
```
GET /api/employees
```
Returns a list of all employees.

**Response**: 200 OK
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "position": "Senior Developer",
    "salary": 100000.0,
    "email": "john.doe@example.com"
  }
]
```

### Get Employee by ID
```
GET /api/employees/{id}
```
Returns a specific employee by ID.

**Response**: 200 OK or 404 Not Found

### Create Employee
```
POST /api/employees
Content-Type: application/json

{
  "name": "Jane Smith",
  "position": "Developer",
  "salary": 85000.0,
  "email": "jane.smith@example.com"
}
```

**Response**: 201 Created
```json
{
  "id": 2,
  "name": "Jane Smith",
  "position": "Developer",
  "salary": 85000.0,
  "email": "jane.smith@example.com"
}
```

### Update Employee
```
PUT /api/employees/{id}
Content-Type: application/json

{
  "name": "Jane Smith Updated",
  "position": "Lead Developer",
  "salary": 110000.0,
  "email": "jane.updated@example.com"
}
```

**Response**: 200 OK or 404 Not Found

### Delete Employee
```
DELETE /api/employees/{id}
```

**Response**: 204 No Content or 404 Not Found

## 🗄️ Employee Model

```java
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Position is required")
    private String position;
    
    @NotNull(message = "Salary is required")
    private Double salary;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;
}
```

## ⚙️ Error Handling

The API includes comprehensive error handling with custom exceptions:

### Exception Types

- **EmployeeNotFoundException** (404): Returned when an employee is not found
- **InvalidEmployeeException** (400): Returned for invalid employee data
- **DuplicateEmailException** (409): Returned when email already exists
- **MethodArgumentNotValidException** (400): Returned for validation errors

### Error Response Format

```json
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "Employee with ID 999 not found"
}
```

### Validation Error Response

```json
{
  "name": "Name is required",
  "email": "Email should be valid",
  "salary": "Salary is required"
}
```

## 🧪 Testing

The project includes comprehensive unit and integration tests with 46 test cases:

### Run All Tests
```bash
./mvnw test
```

### Run Specific Test Class
```bash
./mvnw test -Dtest=EmployeeControllerTest
./mvnw test -Dtest=EmployeeServiceTest
./mvnw test -Dtest=EmployeeRepositoryTest
./mvnw test -Dtest=EmployeeTest
./mvnw test -Dtest=GlobalExceptionHandlerTest
```

### Test Coverage
- **EmployeeServiceTest.java**: 12 tests for service layer operations
- **EmployeeControllerTest.java**: 11 tests for REST endpoints and HTTP status codes
- **EmployeeRepositoryTest.java**: 12 tests for database operations
- **EmployeeTest.java**: 9 tests for model validation
- **GlobalExceptionHandlerTest.java**: 2 tests for exception handling

## 🏗️ Layered Architecture

```
┌─────────────────────────────────────────────┐
│         REST Controller Layer                │
│        (EmployeeController)                  │
└──────────────┬──────────────────────────────┘
               │
┌──────────────▼──────────────────────────────┐
│         Service Layer                       │
│        (EmployeeService)                    │
└──────────────┬──────────────────────────────┘
               │
┌──────────────▼──────────────────────────────┐
│        Repository Layer                     │
│       (EmployeeRepository)                  │
└──────────────┬──────────────────────────────┘
               │
┌──────────────▼──────────────────────────────┐
│         Database (H2)                       │
└─────────────────────────────────────────────┘
```

## 📋 Validation Rules

- **Name**: Required, non-blank
- **Position**: Required, non-blank
- **Salary**: Required, must be a valid number
- **Email**: Required, must be valid email format, must be unique

## 🚀 Running with Different Profiles

The application supports different Spring profiles for various environments:

```bash
# Development (default)
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Production
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

## 📚 Dependencies

See `pom.xml` for complete list. Key dependencies:
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-validation
- h2
- lombok
- spring-boot-starter-test

## 🔐 Security Considerations

- Input validation on all endpoints
- SQL injection prevention through JPA
- Proper exception handling to avoid information leakage
- RESTful design without exposing internal structure

## 📝 Logging

The application uses SLF4J with Logback for logging. Check logs for debugging and monitoring application behavior.

## 🤝 Contributing

Contributions are welcome! Please ensure:
1. All tests pass
2. New tests are added for new features
3. Code follows the existing style

## 📄 License

This project is licensed under the MIT License.

## 👤 Author

Employee API - Spring Boot REST API Project

## 📞 Support

For issues or questions, please create an issue in the repository.