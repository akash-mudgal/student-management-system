package com.airtribe.studentmanagement.service;

import com.airtribe.studentmanagement.entity.Student;
import com.airtribe.studentmanagement.entity.GraduateStudent;
import com.airtribe.studentmanagement.exception.InvalidDataException;
import com.airtribe.studentmanagement.exception.StudentNotFoundException;
import com.airtribe.studentmanagement.pattern.StudentFactory;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Test class for StudentService
 * Demonstrates JUnit 5 testing
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StudentServiceTest {
    
    private StudentService studentService;
    
    @BeforeEach
    void setUp() {
        studentService = new StudentService();
        studentService.clearAll();
    }
    
    @AfterEach
    void tearDown() {
        studentService.clearAll();
    }
    
    @Test
    @Order(1)
    @DisplayName("Test adding a new student")
    void testAddStudent() throws Exception {
        // Arrange
        Student student = StudentFactory.builder(StudentFactory.StudentType.UNDERGRADUATE)
            .studentId("TEST001")
            .firstName("Test")
            .lastName("Student")
            .email("test@university.edu")
            .phone("123-456-7890")
            .dateOfBirth(LocalDate.of(2000, 1, 1))
            .program("Computer Science")
            .semester(1)
            .build();
        
        // Act
        studentService.addStudent(student);
        
        // Assert
        assertEquals(1, studentService.getStudentCount());
        Student retrieved = studentService.getStudentById("TEST001");
        assertNotNull(retrieved);
        assertEquals("Test", retrieved.getFirstName());
    }
    
    @Test
    @Order(2)
    @DisplayName("Test adding duplicate student throws exception")
    void testAddDuplicateStudent() throws Exception {
        // Arrange
        Student student1 = createTestStudent("TEST001", "John", "Doe");
        studentService.addStudent(student1);
        
        Student student2 = createTestStudent("TEST001", "Jane", "Smith");
        
        // Act & Assert
        assertThrows(InvalidDataException.class, () -> {
            studentService.addStudent(student2);
        });
    }
    
    @Test
    @Order(3)
    @DisplayName("Test getting student by ID")
    void testGetStudentById() throws Exception {
        // Arrange
        Student student = createTestStudent("TEST001", "John", "Doe");
        studentService.addStudent(student);
        
        // Act
        Student retrieved = studentService.getStudentById("TEST001");
        
        // Assert
        assertNotNull(retrieved);
        assertEquals("John", retrieved.getFirstName());
        assertEquals("Doe", retrieved.getLastName());
    }
    
    @Test
    @Order(4)
    @DisplayName("Test getting non-existent student throws exception")
    void testGetNonExistentStudent() {
        // Act & Assert
        assertThrows(StudentNotFoundException.class, () -> {
            studentService.getStudentById("NONEXISTENT");
        });
    }
    
    @Test
    @Order(5)
    @DisplayName("Test updating student")
    void testUpdateStudent() throws Exception {
        // Arrange
        Student student = createTestStudent("TEST001", "John", "Doe");
        studentService.addStudent(student);
        
        // Act
        student.setEmail("newemail@university.edu");
        studentService.updateStudent(student);
        
        // Assert
        Student updated = studentService.getStudentById("TEST001");
        assertEquals("newemail@university.edu", updated.getEmail());
    }
    
    @Test
    @Order(6)
    @DisplayName("Test deleting student")
    void testDeleteStudent() throws Exception {
        // Arrange
        Student student = createTestStudent("TEST001", "John", "Doe");
        studentService.addStudent(student);
        
        // Act
        studentService.deleteStudent("TEST001");
        
        // Assert
        assertEquals(0, studentService.getStudentCount());
        assertThrows(StudentNotFoundException.class, () -> {
            studentService.getStudentById("TEST001");
        });
    }
    
    @Test
    @Order(7)
    @DisplayName("Test searching students by name")
    void testSearchByName() throws Exception {
        // Arrange
        studentService.addStudent(createTestStudent("TEST001", "John", "Doe"));
        studentService.addStudent(createTestStudent("TEST002", "Jane", "Doe"));
        studentService.addStudent(createTestStudent("TEST003", "Bob", "Smith"));
        
        // Act
        List<Student> results = studentService.searchByName("Doe");
        
        // Assert
        assertEquals(2, results.size());
    }
    
    @Test
    @Order(8)
    @DisplayName("Test getting students by program")
    void testSearchByProgram() throws Exception {
        // Arrange
        Student s1 = createTestStudent("TEST001", "John", "Doe");
        s1.setProgram("Computer Science");
        studentService.addStudent(s1);
        
        Student s2 = createTestStudent("TEST002", "Jane", "Smith");
        s2.setProgram("Mathematics");
        studentService.addStudent(s2);
        
        // Act
        List<Student> csStudents = studentService.searchByProgram("Computer Science");
        
        // Assert
        assertEquals(1, csStudents.size());
        assertEquals("Computer Science", csStudents.get(0).getProgram());
    }
    
    @Test
    @Order(9)
    @DisplayName("Test getting students in good standing")
    void testGetStudentsInGoodStanding() throws Exception {
        // Arrange
        Student s1 = createTestStudent("TEST001", "John", "Doe");
        s1.setGpa(3.5);
        studentService.addStudent(s1);
        
        Student s2 = createTestStudent("TEST002", "Jane", "Smith");
        s2.setGpa(1.5);
        studentService.addStudent(s2);
        
        // Act
        List<Student> goodStanding = studentService.getStudentsInGoodStanding();
        
        // Assert
        assertEquals(1, goodStanding.size());
        assertTrue(goodStanding.get(0).getGpa() >= 2.0);
    }
    
    @Test
    @Order(10)
    @DisplayName("Test calculating average GPA")
    void testCalculateAverageGPA() throws Exception {
        // Arrange
        Student s1 = createTestStudent("TEST001", "John", "Doe");
        s1.setGpa(3.0);
        studentService.addStudent(s1);
        
        Student s2 = createTestStudent("TEST002", "Jane", "Smith");
        s2.setGpa(4.0);
        studentService.addStudent(s2);
        
        // Act
        double avgGPA = studentService.calculateAverageGPA();
        
        // Assert
        assertEquals(3.5, avgGPA, 0.01);
    }
    
    @Test
    @Order(11)
    @DisplayName("Test graduate student creation")
    void testGraduateStudent() throws Exception {
        // Arrange
        GraduateStudent gradStudent = (GraduateStudent) StudentFactory.builder(
            StudentFactory.StudentType.GRADUATE)
            .studentId("GRAD001")
            .firstName("Alice")
            .lastName("Johnson")
            .email("alice@university.edu")
            .phone("123-456-7890")
            .dateOfBirth(LocalDate.of(1998, 5, 15))
            .program("Computer Science - MS")
            .semester(4)
            .thesisTitle("AI in Healthcare")
            .advisor("Dr. Smith")
            .researchArea("Artificial Intelligence")
            .build();
        
        // Act
        studentService.addStudent(gradStudent);
        Student retrieved = studentService.getStudentById("GRAD001");
        
        // Assert
        assertTrue(retrieved instanceof GraduateStudent);
        GraduateStudent retrievedGrad = (GraduateStudent) retrieved;
        assertEquals("AI in Healthcare", retrievedGrad.getThesisTitle());
        assertEquals("Dr. Smith", retrievedGrad.getAdvisor());
    }
    
    // Helper method
    private Student createTestStudent(String id, String firstName, String lastName) throws Exception {
        return StudentFactory.builder(StudentFactory.StudentType.UNDERGRADUATE)
            .studentId(id)
            .firstName(firstName)
            .lastName(lastName)
            .email(firstName.toLowerCase() + "@university.edu")
            .phone("123-456-7890")
            .dateOfBirth(LocalDate.of(2000, 1, 1))
            .program("Computer Science")
            .semester(1)
            .build();
    }
}
