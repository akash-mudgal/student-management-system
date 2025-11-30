# Student Management System

A comprehensive Java-based Student Management System that demonstrates object-oriented programming principles, design patterns, and modern Java features. This application provides a complete solution for managing students, courses, and enrollments with a command-line interface.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technologies & Concepts](#technologies--concepts)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Design Patterns](#design-patterns)
- [Core Components](#core-components)
- [Testing](#testing)
- [License](#license)

## 🎯 Overview

The Student Management System is designed to showcase professional Java development practices while providing practical functionality for educational institutions. It manages student records, course catalogs, enrollments, and grading systems with robust error handling and data persistence.

## ✨ Features

### Core Features

1. **Student Management**
   - Add, update, view, and delete student records
   - Support for both undergraduate and graduate students
   - Track student GPA, attendance, and enrollment status
   - Search functionality for finding students by various criteria

2. **Course Management**
   - Create and manage course catalog
   - Set course prerequisites and maximum capacity
   - Track course details (credits, instructor, department)
   - View course enrollments and statistics

3. **Enrollment Management**
   - Enroll students in courses
   - Drop courses with validation
   - Track enrollment status (Active, Completed, Dropped, Withdrawn)
   - Manage attendance and grade assignments

4. **Grading System**
   - Assign and update grades for enrollments
   - Calculate letter grades (A, B, C, D, F)
   - Track student GPA automatically
   - Observer pattern for grade notifications

5. **Reporting & Analytics**
   - Generate student reports with GPA and enrollment details
   - Course enrollment statistics
   - Top-performing students lists
   - Students by program/semester filtering

6. **Data Persistence**
   - Save and load data from files
   - Serialization support for data storage
   - Automatic backup and restore functionality

### Bonus Features

- **Java Streams API** - Advanced data filtering and processing
- **Design Patterns** - Singleton, Factory, and Observer patterns
- **File I/O** - Persistent data storage using serialization
- **JUnit Testing** - Comprehensive unit and integration tests

## 🛠 Technologies & Concepts

### Core Java Concepts Demonstrated

- **Object-Oriented Programming (OOP)**
  - Encapsulation (private fields with getters/setters)
  - Inheritance (Person → Student → GraduateStudent)
  - Polymorphism (method overriding, interface implementation)
  - Abstraction (interfaces like Gradeable, Searchable)

- **Exception Handling**
  - Custom exceptions (StudentNotFoundException, InvalidDataException)
  - Try-catch-finally blocks
  - Exception propagation

- **Collections Framework**
  - HashMap for efficient student/course lookups
  - ArrayList for managing lists
  - Stream API for data processing

- **Date and Time API**
  - LocalDate for date handling
  - DateUtil for date validation and formatting

- **Input/Output**
  - File I/O for data persistence
  - Serialization/Deserialization
  - Scanner for user input

### Design Patterns

1. **Singleton Pattern** - ConfigurationManager
   - Ensures single instance of configuration
   - Thread-safe lazy initialization
   - Centralized application settings

2. **Factory Pattern** - StudentFactory
   - Creates different types of students (Undergraduate/Graduate)
   - Encapsulates object creation logic
   - Builder pattern for flexible student creation

3. **Observer Pattern** - GradeNotificationManager
   - Notifies observers when grades are updated
   - Decoupled notification system
   - Extensible for multiple observer types

### Technologies

- **Java 17** - Modern Java features
- **Maven** - Build automation and dependency management
- **JUnit 5** - Unit testing framework
- **Mockito** - Mocking framework for tests

## 📁 Project Structure

```
student-management-system/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/airtribe/studentmanagement/
│   │           ├── Main.java                      # Application entry point
│   │           ├── entity/                        # Domain entities
│   │           │   ├── Person.java               # Base class for persons
│   │           │   ├── Student.java              # Student entity
│   │           │   ├── GraduateStudent.java      # Graduate student (inheritance)
│   │           │   ├── Course.java               # Course entity
│   │           │   └── Enrollment.java           # Enrollment entity
│   │           ├── service/                       # Business logic layer
│   │           │   ├── StudentService.java       # Student operations
│   │           │   ├── CourseService.java        # Course operations
│   │           │   └── EnrollmentService.java    # Enrollment operations
│   │           ├── pattern/                       # Design pattern implementations
│   │           │   ├── ConfigurationManager.java # Singleton pattern
│   │           │   ├── StudentFactory.java       # Factory pattern
│   │           │   ├── GradeObserver.java        # Observer interface
│   │           │   └── GradeNotificationManager.java # Observer manager
│   │           ├── interfaces/                    # Contract definitions
│   │           │   ├── Gradeable.java            # Grading interface
│   │           │   └── Searchable.java           # Search interface
│   │           ├── exception/                     # Custom exceptions
│   │           │   ├── StudentNotFoundException.java
│   │           │   └── InvalidDataException.java
│   │           └── util/                          # Utility classes
│   │               ├── DateUtil.java             # Date operations
│   │               └── InputValidator.java       # Input validation
│   └── test/
│       └── java/
│           └── com/airtribe/studentmanagement/
│               ├── service/
│               │   └── StudentServiceTest.java   # Unit tests
│               └── IntegrationTest.java          # Integration tests
├── data/                                          # Data persistence directory
├── pom.xml                                        # Maven configuration
└── README.md                                      # This file
```

## 🚀 Getting Started

### Prerequisites

- **Java 17** or higher
- **Maven 3.6+** for building the project
- A terminal or command prompt

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/akash-mudgal/student-management-system.git
   cd student-management-system
   ```

2. **Build the project**
   ```bash
   mvn clean compile
   ```

3. **Run tests**
   ```bash
   mvn test
   ```

4. **Run the application**
   ```bash
   mvn exec:java -Dexec.mainClass="com.airtribe.studentmanagement.Main"
   ```
   
   Or compile and run directly:
   ```bash
   mvn package
   java -jar target/student-management-system-1.0.0.jar
   ```

## 💻 Usage

### Main Menu

When you launch the application, you'll see the main menu:

```
============================================================
      STUDENT MANAGEMENT SYSTEM
============================================================

========== MAIN MENU ==========
1. Student Management
2. Course Management
3. Enrollment Management
4. Reports
5. Save Data
6. Load Data
7. View Configuration
0. Exit
```

### Student Management

- **Add Student** - Create new undergraduate or graduate student records
- **View All Students** - List all registered students
- **Find Student** - Search by student ID
- **Update Student** - Modify student information
- **Delete Student** - Remove student records

### Course Management

- **Add Course** - Create new courses with prerequisites
- **View All Courses** - Browse course catalog
- **Find Course** - Search by course ID or code
- **Update Course** - Edit course details
- **Delete Course** - Remove courses

### Enrollment Management

- **Enroll Student** - Register student in a course
- **Drop Course** - Withdraw student from enrollment
- **Assign Grade** - Record grades for completed courses
- **View Enrollments** - See all active enrollments

### Reports

- **Student Reports** - Individual student performance
- **Course Statistics** - Enrollment and grade analysis
- **Top Performers** - Honor roll and high achievers
- **Program Statistics** - Students by program

### Data Persistence

- Data is automatically saved to the `data/` directory
- Use **Save Data** to manually persist changes
- Use **Load Data** to restore from saved files

## 🎨 Design Patterns

### 1. Singleton Pattern (ConfigurationManager)

```java
public class ConfigurationManager {
    private static ConfigurationManager instance;
    
    private ConfigurationManager() {
        // Private constructor
    }
    
    public static synchronized ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }
}
```

**Purpose**: Ensures only one configuration instance exists throughout the application lifetime.

### 2. Factory Pattern (StudentFactory)

```java
public class StudentFactory {
    public enum StudentType {
        UNDERGRADUATE,
        GRADUATE
    }
    
    public static Student createStudent(StudentType type, ...) {
        switch (type) {
            case UNDERGRADUATE:
                return createUndergraduateStudent(...);
            case GRADUATE:
                return createGraduateStudent(...);
        }
    }
}
```

**Purpose**: Encapsulates student object creation logic and supports different student types.

### 3. Observer Pattern (GradeNotificationManager)

```java
public interface GradeObserver {
    void onGradeUpdated(Student student, Enrollment enrollment, 
                        double oldGrade, double newGrade);
}
```

**Purpose**: Notifies interested parties (teachers, students, administrators) when grades change.

## 🔧 Core Components

### Entity Layer

- **Person** - Base class with common attributes (name, email, phone)
- **Student** - Extends Person, adds academic information
- **GraduateStudent** - Extends Student, adds thesis and research details
- **Course** - Represents academic courses
- **Enrollment** - Links students to courses with grades

### Service Layer

- **StudentService** - CRUD operations for students
- **CourseService** - Course management logic
- **EnrollmentService** - Handles student-course relationships

### Utilities

- **InputValidator** - Validates user input (email, phone, dates)
- **DateUtil** - Date formatting and validation helper
- **ConfigurationManager** - Application configuration

### Interfaces

- **Gradeable** - Contract for entities that can be graded
- **Searchable** - Contract for searchable entities

## 🧪 Testing

The project includes comprehensive test coverage:

### Unit Tests

Located in `src/test/java/com/airtribe/studentmanagement/service/`

- **StudentServiceTest** - Tests student CRUD operations
- Uses JUnit 5 annotations (@Test, @BeforeEach, @AfterEach)
- Includes test cases for:
  - Adding students
  - Duplicate validation
  - Retrieving students
  - Exception handling
  - Search functionality

### Integration Tests

Located in `src/test/java/com/airtribe/studentmanagement/`

- **IntegrationTest** - Tests complete workflows
- Validates interactions between services
- Tests data persistence

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=StudentServiceTest

# Run with coverage
mvn test jacoco:report
```

## 📊 Key Features in Code

### Java Streams Example

```java
// Find top students by GPA
public List<Student> getTopStudents(int limit) {
    return students.values().stream()
        .sorted((s1, s2) -> Double.compare(s2.getGpa(), s1.getGpa()))
        .limit(limit)
        .collect(Collectors.toList());
}
```

### Exception Handling Example

```java
try {
    Student student = studentService.getStudentById(id);
    System.out.println("Student found: " + student.getFullName());
} catch (StudentNotFoundException e) {
    System.err.println("Error: " + e.getMessage());
} finally {
    System.out.println("Search completed.");
}
```

### Polymorphism Example

```java
// Interface implementation
public class Enrollment implements Gradeable {
    @Override
    public double calculateGrade() {
        return grade != null ? grade : 0.0;
    }
    
    @Override
    public String getLetterGrade() {
        double grade = calculateGrade();
        if (grade >= 90) return "A";
        if (grade >= 80) return "B";
        if (grade >= 70) return "C";
        if (grade >= 60) return "D";
        return "F";
    }
}
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📝 License

This project is open source and available for educational purposes.

## 👨‍💻 Author

**Akash Mudgal**
- GitHub: [@akash-mudgal](https://github.com/akash-mudgal)

---

**Note**: This project was created as a demonstration of Java programming concepts and best practices. It showcases object-oriented design principles, design patterns, and modern Java features in a practical application.
