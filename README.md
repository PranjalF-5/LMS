# Learning Management System (LMS)

A comprehensive Learning Management System built with Spring Boot, providing features for course management, user authentication, and educational content delivery.

[![GitHub](https://img.shields.io/badge/GitHub-Repository-blue.svg)](https://github.com/yourusername/learning_management_system)

## Table of Contents
1. [Project Overview](#1-project-overview)
2. [System Architecture](#2-system-architecture)
3. [Detailed Component Documentation](#3-detailed-component-documentation)
4. [API Documentation](#4-api-documentation)
5. [Security Implementation](#5-security-implementation)
6. [Testing Strategy](#6-testing-strategy)
7. [Deployment](#7-deployment)
8. [Future Enhancements](#8-future-enhancements)

## Project Structure

```
learning-management-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/pranjal/learning_management_system/
│   │   │       ├── aspect/                    # AOP aspects
│   │   │       │   └── LoggingAspect.java     # Logging aspect implementation
│   │   │       ├── config/                    # Configuration classes
│   │   │       │   ├── SecurityConfig.java    # Security configuration
│   │   │       │   └── WebConfig.java         # Web configuration
│   │   │       ├── controller/                # REST controllers
│   │   │       │   ├── AuthController.java    # Authentication endpoints
│   │   │       │   ├── CourseController.java  # Course management
│   │   │       │   ├── GradeController.java   # Grade management
│   │   │       │   ├── SyllabusController.java # Syllabus management
│   │   │       │   └── UserController.java    # User management
│   │   │       ├── dto/                       # Data Transfer Objects
│   │   │       │   ├── request/               # Request DTOs
│   │   │       │   └── response/              # Response DTOs
│   │   │       ├── exception/                 # Custom exceptions
│   │   │       │   ├── AuthenticationException.java
│   │   │       │   ├── ResourceNotFoundException.java
│   │   │       │   └── ValidationException.java
│   │   │       ├── model/                     # Entity classes
│   │   │       │   ├── Course.java
│   │   │       │   ├── Grade.java
│   │   │       │   ├── Syllabus.java
│   │   │       │   └── User.java
│   │   │       ├── repository/                # JPA repositories
│   │   │       │   ├── CourseRepository.java
│   │   │       │   ├── GradeRepository.java
│   │   │       │   ├── SyllabusRepository.java
│   │   │       │   └── UserRepository.java
│   │   │       ├── security/                  # Security related classes
│   │   │       │   ├── JwtUtil.java
│   │   │       │   └── UserDetailsServiceImpl.java
│   │   │       ├── service/                   # Business logic
│   │   │       │   ├── CourseService.java
│   │   │       │   ├── GradeService.java
│   │   │       │   ├── SyllabusService.java
│   │   │       │   └── UserService.java
│   │   │       └── util/                      # Utility classes
│   │   │           └── LoggingUtil.java       # Logging utility
│   │   └── resources/
│   │       ├── application.properties         # Application configuration
│   │       └── logback-spring.xml            # Logging configuration
│   └── test/                                  # Test classes
│       ├── java/
│       │   └── com/pranjal/learning_management_system/
│       │       ├── controller/                # Controller tests
│       │       ├── service/                   # Service tests
│       │       └── repository/                # Repository tests
│       └── resources/                         # Test resources
├── target/                                    # Compiled classes
├── logs/                                      # Application logs
│   ├── application.log                       # General logs
│   └── error.log                             # Error logs
├── .gitignore                                # Git ignore file
├── pom.xml                                   # Maven configuration
└── README.md                                 # Project documentation
```

### Key Directories and Files

1. **src/main/java/**
   - Contains all Java source code
   - Organized by package structure
   - Follows standard Spring Boot architecture

2. **src/main/resources/**
   - Configuration files
   - Property files
   - Logging configuration

3. **src/test/**
   - Unit tests
   - Integration tests
   - Test resources

4. **logs/**
   - Application logs
   - Error logs
   - Archived logs

5. **Configuration Files**
   - `pom.xml`: Maven dependencies and build configuration
   - `application.properties`: Application settings
   - `logback-spring.xml`: Logging configuration

## 1. Project Overview
The Learning Management System (LMS) is a Spring Boot application that provides a platform for managing educational courses, student enrollments, grades, and course materials.

## 2. System Architecture

### 2.1 Core Components
- **Controllers**: Handle HTTP requests and responses
- **Services**: Implement business logic
- **Repositories**: Handle data persistence
- **Models**: Define data entities
- **DTOs**: Data Transfer Objects for API communication
- **Config**: Application configuration

### 2.2 Technology Stack
- **Backend**: Spring Boot
- **Database**: SQL
- **Security**: Spring Security
- **Testing**: JUnit, Mockito

## 3. Detailed Component Documentation

### 3.1 Models

#### User
```java
public class User {
    private Long userId;
    private String username;
    private String email;
    private Role role; // INSTRUCTOR, STUDENT, ADMIN
    // ... other fields and methods
}
```

#### Course
```java
public class Course {
    private Long courseId;
    private String name;
    private String description;
    private CourseStatus status; // PENDING, APPROVED, REJECTED
    private User instructor;
    // ... other fields and methods
}
```

#### Grade
```java
public class Grade {
    private Long gradeId;
    private User student;
    private Course course;
    private Integer score;
    // ... other fields and methods
}
```

#### Syllabus
```java
public class Syllabus {
    private Long syllabusId;
    private Course course;
    private String content;
    // ... other fields and methods
}
```

### 3.2 Controllers

#### InstructorController
- **Endpoint**: `/api/instructor`
- **Responsibilities**:
  - Course creation and management
  - Grade assignment
  - Syllabus updates
- **Key Methods**:
  - `createCourse`: Create new courses
  - `assignGrade`: Assign grades to students
  - `updateSyllabus`: Update course syllabus

#### StudentController
- **Endpoint**: `/api/student`
- **Responsibilities**:
  - Course enrollment
  - Grade viewing
  - Syllabus access
- **Key Methods**:
  - `enrollCourse`: Enroll in courses
  - `viewGrades`: View assigned grades
  - `viewSyllabus`: Access course syllabus

#### AdminController
- **Endpoint**: `/api/admin`
- **Responsibilities**:
  - Course approval
  - User management
- **Key Methods**:
  - `updateCourseStatus`: Approve/reject courses
  - `deleteUser`: Manage user accounts

### 3.3 Services

#### CourseService
- **Responsibilities**:
  - Course creation and management
  - Status updates
  - Course retrieval
- **Key Methods**:
  - `createCourse`: Create new courses
  - `updateCourseStatus`: Update course status
  - `getCoursesByStatus`: Retrieve courses by status
  - `getCourseById`: Get course by ID

#### GradeService
- **Responsibilities**:
  - Grade management
  - Score validation
- **Key Methods**:
  - `assignGrade`: Assign grades to students
  - `validateScore`: Validate grade scores

#### SyllabusService
- **Responsibilities**:
  - Syllabus management
  - Content updates
- **Key Methods**:
  - `updateSyllabus`: Update syllabus content
  - `getSyllabus`: Retrieve syllabus

### 3.4 Repositories

#### CourseRepository
```java
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByStatus(CourseStatus status);
    List<Course> findByInstructor(User instructor);
}
```

#### GradeRepository
```java
public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudent(User student);
    List<Grade> findByCourse(Course course);
}
```

#### SyllabusRepository
```java
public interface SyllabusRepository extends JpaRepository<Syllabus, Long> {
    Optional<Syllabus> findByCourse(Course course);
}
```

## 4. API Documentation

### 4.1 Instructor Endpoints

#### Create Course
```
POST /api/instructor/courses
Request Body:
{
    "courseName": "string",
    "description": "string"
}
Response:
{
    "courseId": "long",
    "name": "string",
    "description": "string",
    "status": "string",
    "instructorId": "long"
}
```

#### Assign Grade
```
POST /api/instructor/grade
Request Body:
{
    "studentId": "long",
    "courseId": "long",
    "score": "integer"
}
Response:
{
    "gradeId": "long",
    "score": "integer",
    "studentId": "long",
    "studentName": "string",
    "courseId": "long",
    "courseName": "string"
}
```

### 4.2 Student Endpoints

#### Enroll in Course
```
POST /api/student/enroll
Request Body:
{
    "courseId": "long"
}
Response:
{
    "enrollmentId": "long",
    "courseId": "long",
    "courseName": "string",
    "status": "string"
}
```

#### View Grades
```
GET /api/student/grades
Response:
[
    {
        "gradeId": "long",
        "score": "integer",
        "courseId": "long",
        "courseName": "string"
    }
]
```

## 5. Security Implementation

### 5.1 Authentication
- JWT-based authentication
- Role-based access control
- Custom user details implementation

### 5.2 Authorization
- Role-based endpoints protection
- Pre-authorization checks
- Method-level security

## 6. Testing Strategy

### 6.1 Unit Tests
- Service layer testing
- Controller testing
- Repository testing

### 6.2 Integration Tests
- API endpoint testing
- Security testing
- Database integration testing

## 7. Deployment

### 7.1 Requirements
- Java 17+
- Maven
- Database server
- Application server

### 7.2 Configuration
- Application properties
- Security configuration
- Database configuration

## 8. Future Enhancements
1. Course material upload/download
2. Discussion forums
3. Assignment submission
4. Progress tracking
5. Analytics dashboard

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven
- Your preferred IDE (IntelliJ IDEA, Eclipse, etc.)

### Installation
1. Clone the repository
```bash
git clone https://github.com/yourusername/learning-management-system.git
```

2. Navigate to the project directory
```bash
cd learning-management-system
```

3. Build the project
```bash
mvn clean install
```

4. Run the application
```bash
mvn spring-boot:run
```

### Configuration
Update the `application.properties` file with your database and security configurations.

## Contributing
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact
Your Name - your.email@example.com

Project Link: [https://github.com/yourusername/learning-management-system](https://github.com/yourusername/learning-management-system) 