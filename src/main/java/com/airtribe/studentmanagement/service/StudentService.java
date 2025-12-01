package com.airtribe.studentmanagement.service;

import com.airtribe.studentmanagement.entity.Student;
import com.airtribe.studentmanagement.entity.GraduateStudent;
import com.airtribe.studentmanagement.exception.InvalidDataException;
import com.airtribe.studentmanagement.exception.StudentNotFoundException;
import com.airtribe.studentmanagement.pattern.ConfigurationManager;
import com.airtribe.studentmanagement.util.InputValidator;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for Student operations
 * Demonstrates:
 * - Service layer pattern
 * - Java Streams for data processing (Bonus Feature C)
 * - File I/O for data persistence (Bonus Feature D)
 * - Exception handling
 */
public class StudentService {
    
    private Map<String, Student> students;
    private String dataFilePath;
    private int studentIdCounter;
    
    public StudentService() {
        this.students = new HashMap<>();
        ConfigurationManager config = ConfigurationManager.getInstance();
        this.dataFilePath = config.getDataDirectory() + "/students.dat";
        this.studentIdCounter = 1;
        
        // Ensure data directory exists
        createDataDirectoryIfNeeded();
    }
    
    /**
     * Create data directory if it doesn't exist
     */
    private void createDataDirectoryIfNeeded() {
        File dataDir = new File(ConfigurationManager.getInstance().getDataDirectory());
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }
    
    /**
     * Generate unique student ID
     */
    public String generateStudentId() {
        String studentId;
        do {
            studentId = String.format("STU%05d", studentIdCounter++);
        } while (students.containsKey(studentId));
        return studentId;
    }
    
    /**
     * Add a new student
     */
    public void addStudent(Student student) throws InvalidDataException {
        if (student == null) {
            throw new InvalidDataException("Student cannot be null");
        }
        
        // Validate student data
        InputValidator.validateNotEmpty(student.getStudentId(), "Student ID");
        InputValidator.validateNotEmpty(student.getFirstName(), "First Name");
        InputValidator.validateNotEmpty(student.getLastName(), "Last Name");
        InputValidator.validateEmail(student.getEmail());
        
        // Check for duplicate
        if (students.containsKey(student.getStudentId())) {
            throw new InvalidDataException("Student with ID already exists: " + 
                                         student.getStudentId());
        }
        
        students.put(student.getStudentId(), student);
        System.out.println("✓ Student added successfully: " + student.getFullName());
    }
    
    /**
     * Get student by ID
     */
    public Student getStudentById(String studentId) throws StudentNotFoundException {
        Student student = students.get(studentId);
        if (student == null) {
            throw new StudentNotFoundException("Student not found with ID: " + studentId, studentId);
        }
        return student;
    }
    
    /**
     * Get all students
     */
    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }
    
    /**
     * Update student information
     */
    public void updateStudent(Student student) throws InvalidDataException, StudentNotFoundException {
        if (student == null) {
            throw new InvalidDataException("Student cannot be null");
        }
        
        if (!students.containsKey(student.getStudentId())) {
            throw new StudentNotFoundException("Student not found: " + student.getStudentId());
        }
        
        students.put(student.getStudentId(), student);
        System.out.println("✓ Student updated successfully: " + student.getFullName());
    }
    
    /**
     * Delete student by ID
     */
    public void deleteStudent(String studentId) throws StudentNotFoundException {
        if (!students.containsKey(studentId)) {
            throw new StudentNotFoundException("Student not found: " + studentId, studentId);
        }
        
        Student removed = students.remove(studentId);
        System.out.println("✓ Student deleted: " + removed.getFullName());
    }
    
    /**
     * Search students by name (using Streams - Bonus Feature C)
     */
    public List<Student> searchByName(String name) {
        return students.values().stream()
                      .filter(s -> s.matchesName(name))
                      .collect(Collectors.toList());
    }
    
    /**
     * Search students by program (using Streams)
     */
    public List<Student> searchByProgram(String program) {
        return students.values().stream()
                      .filter(s -> s.getProgram().equalsIgnoreCase(program))
                      .collect(Collectors.toList());
    }
    
    /**
     * Get students by semester (using Streams)
     */
    public List<Student> getStudentsBySemester(int semester) {
        return students.values().stream()
                      .filter(s -> s.getSemester() == semester)
                      .sorted(Comparator.comparing(Student::getFullName))
                      .collect(Collectors.toList());
    }
    
    /**
     * Get students in good standing (using Streams and Lambda)
     */
    public List<Student> getStudentsInGoodStanding() {
        return students.values().stream()
                      .filter(Student::isInGoodStanding)
                      .sorted(Comparator.comparingDouble(Student::getGpa).reversed())
                      .collect(Collectors.toList());
    }
    
    /**
     * Get students on academic probation
     */
    public List<Student> getStudentsOnProbation() {
        return students.values().stream()
                      .filter(s -> !s.isInGoodStanding())
                      .sorted(Comparator.comparingDouble(Student::getGpa))
                      .collect(Collectors.toList());
    }
    
    /**
     * Get graduate students only (using Streams)
     */
    public List<GraduateStudent> getGraduateStudents() {
        return students.values().stream()
                      .filter(s -> s instanceof GraduateStudent)
                      .map(s -> (GraduateStudent) s)
                      .collect(Collectors.toList());
    }
    
    /**
     * Get top performers (using Streams)
     */
    public List<Student> getTopPerformers(int limit) {
        return students.values().stream()
                      .sorted(Comparator.comparingDouble(Student::getGpa).reversed())
                      .limit(limit)
                      .collect(Collectors.toList());
    }
    
    /**
     * Calculate average GPA of all students (using Streams)
     */
    public double calculateAverageGPA() {
        return students.values().stream()
                      .mapToDouble(Student::getGpa)
                      .average()
                      .orElse(0.0);
    }
    
    /**
     * Get student count by program (using Streams)
     */
    public Map<String, Long> getStudentCountByProgram() {
        return students.values().stream()
                      .collect(Collectors.groupingBy(
                          Student::getProgram,
                          Collectors.counting()
                      ));
    }
    
    /**
     * Save students to file (File I/O - Bonus Feature D)
     */
    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(dataFilePath))) {
            oos.writeObject(new ArrayList<>(students.values()));
            System.out.println("✓ Students data saved to file: " + dataFilePath);
        } catch (IOException e) {
            System.err.println("✗ Error saving students data: " + e.getMessage());
        }
    }
    
    /**
     * Load students from file (File I/O - Bonus Feature D)
     */
    @SuppressWarnings("unchecked")
    public void loadFromFile() {
        File file = new File(dataFilePath);
        if (!file.exists()) {
            System.out.println("No existing data file found. Starting fresh.");
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(dataFilePath))) {
            List<Student> loadedStudents = (List<Student>) ois.readObject();
            students.clear();
            for (Student student : loadedStudents) {
                students.put(student.getStudentId(), student);
            }
            // Update counter to avoid ID conflicts
            updateStudentIdCounter();
            System.out.println("✓ Loaded " + students.size() + " students from file");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("✗ Error loading students data: " + e.getMessage());
        }
    }
    
    /**
     * Update student ID counter based on existing IDs
     */
    private void updateStudentIdCounter() {
        int maxNumber = students.keySet().stream()
            .filter(id -> id.startsWith("STU"))
            .map(id -> id.substring(3))
            .filter(num -> num.matches("\\d+"))
            .mapToInt(Integer::parseInt)
            .max()
            .orElse(0);
        studentIdCounter = maxNumber + 1;
    }
    
    /**
     * Export students to CSV file
     */
    public void exportToCSV(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Write header
            writer.println("StudentID,FirstName,LastName,Email,Phone,Program,Semester,GPA,Type");
            
            // Write student data
            students.values().forEach(student -> {
                writer.printf("%s,%s,%s,%s,%s,%s,%d,%.2f,%s%n",
                    student.getStudentId(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getEmail(),
                    student.getPhone(),
                    student.getProgram(),
                    student.getSemester(),
                    student.getGpa(),
                    student instanceof GraduateStudent ? "Graduate" : "Undergraduate"
                );
            });
            
            System.out.println("✓ Students exported to CSV: " + filename);
        } catch (IOException e) {
            System.err.println("✗ Error exporting to CSV: " + e.getMessage());
        }
    }
    
    /**
     * Get statistics
     */
    public void displayStatistics() {
        System.out.println("\n=== Student Statistics ===");
        System.out.println("Total Students: " + students.size());
        System.out.println("Average GPA: " + String.format("%.2f", calculateAverageGPA()));
        System.out.println("Students in Good Standing: " + getStudentsInGoodStanding().size());
        System.out.println("Students on Probation: " + getStudentsOnProbation().size());
        System.out.println("Graduate Students: " + getGraduateStudents().size());
        System.out.println("\nStudents by Program:");
        getStudentCountByProgram().forEach((program, count) -> 
            System.out.println("  " + program + ": " + count)
        );
    }
    
    /**
     * Clear all students (for testing)
     */
    public void clearAll() {
        students.clear();
        System.out.println("✓ All students cleared");
    }
    
    /**
     * Get student count
     */
    public int getStudentCount() {
        return students.size();
    }
}
