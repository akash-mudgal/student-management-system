package com.airtribe.studentmanagement;

import com.airtribe.studentmanagement.entity.*;
import com.airtribe.studentmanagement.service.*;
import com.airtribe.studentmanagement.pattern.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Integration test for the complete Student Management System
 * Tests the interaction between multiple components
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IntegrationTest {
    
    private static StudentService studentService;
    private static CourseService courseService;
    private static EnrollmentService enrollmentService;
    
    @BeforeAll
    static void setUpAll() {
        studentService = new StudentService();
        courseService = new CourseService();
        enrollmentService = new EnrollmentService();
    }
    
    @BeforeEach
    void setUp() {
        studentService.clearAll();
        courseService.clearAll();
        enrollmentService.clearAll();
    }
    
    @Test
    @Order(1)
    @DisplayName("Integration Test: Complete student enrollment flow")
    void testCompleteEnrollmentFlow() throws Exception {
        // 1. Create a student using Factory pattern
        Student student = StudentFactory.builder(StudentFactory.StudentType.UNDERGRADUATE)
            .studentId("INT001")
            .firstName("Integration")
            .lastName("Test")
            .email("integration@test.edu")
            .phone("123-456-7890")
            .dateOfBirth(LocalDate.of(2000, 1, 1))
            .program("Computer Science")
            .semester(1)
            .build();
        
        studentService.addStudent(student);
        
        // 2. Create a course
        Course course = new Course("C001", "Introduction to Java", "CS101",
                                  3, "Computer Science", "Dr. Smith", 30);
        courseService.addCourse(course);
        
        // 3. Enroll student in course
        Enrollment enrollment = enrollmentService.enrollStudent(student, course);
        
        // 4. Verify enrollment
        assertNotNull(enrollment);
        assertEquals(student, enrollment.getStudent());
        assertEquals(course, enrollment.getCourse());
        assertEquals("ACTIVE", enrollment.getStatus());
        
        // 5. Assign grade using Observer pattern
        enrollmentService.assignGrade(enrollment.getEnrollmentId(), 85.5);
        
        // 6. Verify grade was assigned
        assertEquals(85.5, enrollment.getGrade(), 0.01);
        assertEquals("B", enrollment.getLetterGrade());
        assertTrue(enrollment.hasPassed());
    }
    
    @Test
    @Order(2)
    @DisplayName("Integration Test: Multiple students and courses")
    void testMultipleStudentsAndCourses() throws Exception {
        // Create multiple students
        for (int i = 1; i <= 5; i++) {
            Student student = StudentFactory.builder(StudentFactory.StudentType.UNDERGRADUATE)
                .studentId("STU00" + i)
                .firstName("Student" + i)
                .lastName("Test")
                .email("student" + i + "@test.edu")
                .phone("123-456-789" + i)
                .dateOfBirth(LocalDate.of(2000, 1, i))
                .program("Computer Science")
                .semester(1)
                .build();
            studentService.addStudent(student);
        }
        
        // Create multiple courses
        String[] courseCodes = {"CS101", "CS201", "MATH101"};
        for (int i = 0; i < courseCodes.length; i++) {
            Course course = new Course("C00" + (i + 1), "Course " + (i + 1),
                                      courseCodes[i], 3, "Computer Science",
                                      "Dr. Smith", 30);
            courseService.addCourse(course);
        }
        
        // Verify counts
        assertEquals(5, studentService.getStudentCount());
        assertEquals(3, courseService.getCourseCount());
        
        // Enroll students in courses
        List<Student> students = studentService.getAllStudents();
        List<Course> courses = courseService.getAllCourses();
        
        for (Student student : students) {
            for (Course course : courses) {
                enrollmentService.enrollStudent(student, course);
            }
        }
        
        // Verify enrollments
        assertEquals(15, enrollmentService.getEnrollmentCount()); // 5 students * 3 courses
    }
    
    @Test
    @Order(3)
    @DisplayName("Integration Test: Design patterns")
    void testDesignPatterns() throws Exception {
        // Test Singleton pattern
        ConfigurationManager config1 = ConfigurationManager.getInstance();
        ConfigurationManager config2 = ConfigurationManager.getInstance();
        assertSame(config1, config2); // Same instance
        
        // Test Factory pattern
        Student undergrad = StudentFactory.createStudent(
            StudentFactory.StudentType.UNDERGRADUATE,
            "FAC001", "Factory", "Undergrad", "Computer Science"
        );
        assertNotNull(undergrad);
        assertEquals("STUDENT", undergrad.getRole());
        
        Student grad = StudentFactory.createStudent(
            StudentFactory.StudentType.GRADUATE,
            "FAC002", "Factory", "Graduate", "Computer Science - MS"
        );
        assertNotNull(grad);
        assertTrue(grad instanceof GraduateStudent);
        assertEquals("GRADUATE_STUDENT", grad.getRole());
        
        // Test Observer pattern (grade notification)
        Course course = new Course("C001", "Test Course", "TEST101",
                                  3, "CS", "Dr. Test", 30);
        courseService.addCourse(course);
        studentService.addStudent(undergrad);
        
        Enrollment enrollment = enrollmentService.enrollStudent(undergrad, course);
        
        // This should trigger observer notifications
        enrollmentService.assignGrade(enrollment.getEnrollmentId(), 90.0);
        
        assertEquals(90.0, enrollment.getGrade(), 0.01);
    }
    
    @Test
    @Order(4)
    @DisplayName("Integration Test: Data persistence")
    void testDataPersistence() throws Exception {
        // Add data
        Student student = StudentFactory.builder(StudentFactory.StudentType.UNDERGRADUATE)
            .studentId("PER001")
            .firstName("Persistence")
            .lastName("Test")
            .email("persist@test.edu")
            .phone("123-456-7890")
            .dateOfBirth(LocalDate.of(2000, 1, 1))
            .program("Computer Science")
            .semester(1)
            .build();
        studentService.addStudent(student);
        
        Course course = new Course("C001", "Test Course", "TEST101",
                                  3, "CS", "Dr. Test", 30);
        courseService.addCourse(course);
        
        // Save to file
        studentService.saveToFile();
        courseService.saveToFile();
        
        // Clear in-memory data
        studentService.clearAll();
        courseService.clearAll();
        assertEquals(0, studentService.getStudentCount());
        assertEquals(0, courseService.getCourseCount());
        
        // Load from file
        studentService.loadFromFile();
        courseService.loadFromFile();
        
        // Verify data was restored
        assertEquals(1, studentService.getStudentCount());
        assertEquals(1, courseService.getCourseCount());
        
        Student loaded = studentService.getStudentById("PER001");
        assertNotNull(loaded);
        assertEquals("Persistence", loaded.getFirstName());
    }
    
    @Test
    @Order(5)
    @DisplayName("Integration Test: Java Streams operations")
    void testJavaStreams() throws Exception {
        // Create students with different GPAs
        for (int i = 1; i <= 10; i++) {
            Student student = StudentFactory.builder(StudentFactory.StudentType.UNDERGRADUATE)
                .studentId("STR00" + i)
                .firstName("Stream" + i)
                .lastName("Test")
                .email("stream" + i + "@test.edu")
                .phone("123-456-789" + i)
                .dateOfBirth(LocalDate.of(2000, 1, i))
                .program(i % 2 == 0 ? "Computer Science" : "Mathematics")
                .semester(i % 4 + 1)
                .build();
            student.setGpa(2.0 + (i * 0.2)); // GPA from 2.2 to 4.0
            studentService.addStudent(student);
        }
        
        // Test filtering and collecting
        List<Student> topPerformers = studentService.getTopPerformers(3);
        assertEquals(3, topPerformers.size());
        assertTrue(topPerformers.get(0).getGpa() >= topPerformers.get(1).getGpa());
        
        // Test grouping
        var countByProgram = studentService.getStudentCountByProgram();
        assertEquals(2, countByProgram.size());
        assertTrue(countByProgram.containsKey("Computer Science"));
        assertTrue(countByProgram.containsKey("Mathematics"));
        
        // Test filtering by semester
        List<Student> semester1Students = studentService.getStudentsBySemester(1);
        assertTrue(semester1Students.size() > 0);
        assertTrue(semester1Students.stream().allMatch(s -> s.getSemester() == 1));
    }
}
