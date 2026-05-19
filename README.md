# Employee Management REST API

A production-ready REST API built with Spring Boot for managing employee records. Supports full CRUD operations with a layered architecture following industry best practices.

## 🚀 Features

- Create, Read, Update, Delete employee records
- RESTful API design with proper HTTP status codes
- Layered architecture (Controller → Service → Repository)
- Validation and error handling
- Unit tests with JUnit

## 🛠️ Tech Stack

- Java 21
- Spring Boot 2.7.x / 3.x
- Spring Data JPA
- H2 Database (development) / MySQL (production)
- Maven
- Postman for API testing

## 📦 Installation & Setup

```bash
# Clone the repository
git clone https://github.com/Piyush-Gahlawat/employee-api.git

# Navigate to project directory
cd employee-api

# Run the application
./mvnw spring-boot:run