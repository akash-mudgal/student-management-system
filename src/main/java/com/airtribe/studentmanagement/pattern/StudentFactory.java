package com.airtribe.studentmanagement.pattern;

import com.airtribe.studentmanagement.entity.GraduateStudent;
import com.airtribe.studentmanagement.entity.Student;
import com.airtribe.studentmanagement.exception.InvalidDataException;

import java.time.LocalDate;

/**
 * Factory pattern for creating different types of students
 * Demonstrates Factory design pattern (Bonus Feature B)
 * Provides a centralized way to create student objects
 */
public class StudentFactory {
    
    /**
     * Enum for student types
     */
    public enum StudentType {
        UNDERGRADUATE,
        GRADUATE
    }
    
    /**
     * Create a student based on type
     * Main factory method demonstrating Factory pattern
     */
    public static Student createStudent(StudentType type, String id, String firstName, 
                                       String lastName, String email, String phone,
                                       LocalDate dateOfBirth, String studentId, 
                                       String program, int semester) 
                                       throws InvalidDataException {
        
        // Validation
        if (type == null) {
            throw new InvalidDataException("Student type cannot be null", "type");
        }
        
        // Create appropriate student type
        switch (type) {
            case UNDERGRADUATE:
                return createUndergraduateStudent(id, firstName, lastName, email, 
                                                 phone, dateOfBirth, studentId, 
                                                 program, semester);
            
            case GRADUATE:
                return createGraduateStudent(id, firstName, lastName, email, 
                                            phone, dateOfBirth, studentId, 
                                            program, semester);
            
            default:
                throw new InvalidDataException("Unknown student type: " + type, "type");
        }
    }
    
    /**
     * Create an undergraduate student
     */
    private static Student createUndergraduateStudent(String id, String firstName, 
                                                     String lastName, String email, 
                                                     String phone, LocalDate dateOfBirth,
                                                     String studentId, String program, 
                                                     int semester) {
        Student student = new Student(id, firstName, lastName, email, phone, 
                                     dateOfBirth, studentId, program, semester);
        
        // Set undergraduate-specific defaults
        student.setEnrollmentDate(LocalDate.now());
        
        return student;
    }
    
    /**
     * Create a graduate student with default research values
     */
    private static GraduateStudent createGraduateStudent(String id, String firstName, 
                                                         String lastName, String email, 
                                                         String phone, LocalDate dateOfBirth,
                                                         String studentId, String program, 
                                                         int semester) {
        GraduateStudent student = new GraduateStudent();
        
        // Set basic information
        student.setId(id);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);
        student.setPhone(phone);
        student.setDateOfBirth(dateOfBirth);
        student.setStudentId(studentId);
        student.setProgram(program);
        student.setSemester(semester);
        student.setEnrollmentDate(LocalDate.now());
        
        // Set graduate-specific defaults
        student.setThesisCompleted(false);
        student.setExpectedGraduationDate(LocalDate.now().plusYears(2));
        
        return student;
    }
    
    /**
     * Create a graduate student with full details
     * Overloaded method for more specific creation
     */
    public static GraduateStudent createGraduateStudent(String id, String firstName, 
                                                        String lastName, String email, 
                                                        String phone, LocalDate dateOfBirth,
                                                        String studentId, String program, 
                                                        int semester, String thesisTitle,
                                                        String advisor, String researchArea) 
                                                        throws InvalidDataException {
        
        GraduateStudent student = new GraduateStudent(id, firstName, lastName, email, 
                                                      phone, dateOfBirth, studentId, 
                                                      program, semester, thesisTitle, 
                                                      advisor, researchArea);
        
        return student;
    }
    
    /**
     * Create a student with minimal information
     * Simplified factory method for quick creation
     */
    public static Student createStudent(StudentType type, String studentId, 
                                       String firstName, String lastName, 
                                       String program) throws InvalidDataException {
        
        // Generate default values
        String id = generateId();
        String email = generateEmail(firstName, lastName);
        String phone = "000-000-0000";
        LocalDate dateOfBirth = LocalDate.now().minusYears(20); // Default age 20
        int semester = 1;
        
        return createStudent(type, id, firstName, lastName, email, phone, 
                           dateOfBirth, studentId, program, semester);
    }
    
    /**
     * Create a student from a builder pattern approach
     */
    public static StudentBuilder builder(StudentType type) {
        return new StudentBuilder(type);
    }
    
    /**
     * Generate a unique ID
     */
    private static String generateId() {
        return "ID" + System.currentTimeMillis();
    }
    
    /**
     * Generate email from name
     */
    private static String generateEmail(String firstName, String lastName) {
        return (firstName + "." + lastName + "@university.edu").toLowerCase();
    }
    
    /**
     * Builder class for flexible student creation
     * Demonstrates Builder pattern within Factory
     */
    public static class StudentBuilder {
        private StudentType type;
        private String id;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private LocalDate dateOfBirth;
        private String studentId;
        private String program;
        private int semester;
        private String thesisTitle;
        private String advisor;
        private String researchArea;
        
        public StudentBuilder(StudentType type) {
            this.type = type;
            // Set defaults
            this.id = generateId();
            this.semester = 1;
            this.dateOfBirth = LocalDate.now().minusYears(20);
        }
        
        public StudentBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        public StudentBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }
        
        public StudentBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
        
        public StudentBuilder email(String email) {
            this.email = email;
            return this;
        }
        
        public StudentBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }
        
        public StudentBuilder dateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }
        
        public StudentBuilder studentId(String studentId) {
            this.studentId = studentId;
            return this;
        }
        
        public StudentBuilder program(String program) {
            this.program = program;
            return this;
        }
        
        public StudentBuilder semester(int semester) {
            this.semester = semester;
            return this;
        }
        
        public StudentBuilder thesisTitle(String thesisTitle) {
            this.thesisTitle = thesisTitle;
            return this;
        }
        
        public StudentBuilder advisor(String advisor) {
            this.advisor = advisor;
            return this;
        }
        
        public StudentBuilder researchArea(String researchArea) {
            this.researchArea = researchArea;
            return this;
        }
        
        public Student build() throws InvalidDataException {
            // Auto-generate email if not provided
            if (email == null && firstName != null && lastName != null) {
                email = generateEmail(firstName, lastName);
            }
            
            if (type == StudentType.GRADUATE && thesisTitle != null) {
                return createGraduateStudent(id, firstName, lastName, email, phone,
                                            dateOfBirth, studentId, program, semester,
                                            thesisTitle, advisor, researchArea);
            } else {
                return createStudent(type, id, firstName, lastName, email, phone,
                                   dateOfBirth, studentId, program, semester);
            }
        }
    }
}
