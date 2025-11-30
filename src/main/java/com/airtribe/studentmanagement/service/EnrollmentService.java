package com.airtribe.studentmanagement.service;

import com.airtribe.studentmanagement.entity.Course;
import com.airtribe.studentmanagement.entity.Enrollment;
import com.airtribe.studentmanagement.entity.Student;
import com.airtribe.studentmanagement.exception.InvalidDataException;
import com.airtribe.studentmanagement.pattern.ConfigurationManager;
import com.airtribe.studentmanagement.pattern.GradeNotificationManager;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for Enrollment operations
 * Demonstrates business logic, streams, and observer pattern integration
 */
public class EnrollmentService {
    
    private Map<String, Enrollment> enrollments;
    private int enrollmentIdCounter = 1;  // Counter for generating unique IDs
    private String dataFilePath;
    private GradeNotificationManager notificationManager;
    
    public EnrollmentService() {
        this.enrollments = new HashMap<>();
        ConfigurationManager config = ConfigurationManager.getInstance();
        this.dataFilePath = config.getDataDirectory() + "/enrollments.dat";
        this.notificationManager = GradeNotificationManager.getInstance();
        
        createDataDirectoryIfNeeded();
    }
    
    private void createDataDirectoryIfNeeded() {
        File dataDir = new File(ConfigurationManager.getInstance().getDataDirectory());
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }
    
    /**
     * Enroll a student in a course
     */
    public Enrollment enrollStudent(Student student, Course course) throws InvalidDataException {
        if (student == null || course == null) {
            throw new InvalidDataException("Student and Course cannot be null");
        }
        
        // Check if already enrolled
        boolean alreadyEnrolled = enrollments.values().stream()
            .anyMatch(e -> e.getStudent().equals(student) && 
                          e.getCourse().equals(course) &&
                          e.isActive());
        
        if (alreadyEnrolled) {
            throw new InvalidDataException("Student is already enrolled in this course");
        }
        
        // Check course capacity
        long currentEnrollmentCount = getEnrollmentCountForCourse(course.getCourseId());
        if (currentEnrollmentCount >= course.getMaxCapacity()) {
            throw new InvalidDataException("Course is full. Max capacity: " + 
                                         course.getMaxCapacity());
        }
        
        // Create enrollment
        String enrollmentId = generateEnrollmentId();
        Enrollment enrollment = new Enrollment(enrollmentId, student, course);
        
        enrollments.put(enrollmentId, enrollment);
        student.addEnrollment(enrollment);
        
        System.out.println("✓ Student enrolled successfully");
        System.out.println("  Student: " + student.getFullName());
        System.out.println("  Course: " + course.getCourseName());
        
        return enrollment;
    }
    
    /**
     * Drop an enrollment
     */
    public void dropEnrollment(String enrollmentId) throws InvalidDataException {
        Enrollment enrollment = enrollments.get(enrollmentId);
        if (enrollment == null) {
            throw new InvalidDataException("Enrollment not found: " + enrollmentId);
        }
        
        enrollment.drop();
        enrollment.getStudent().removeEnrollment(enrollment);
        System.out.println("✓ Enrollment dropped successfully");
    }
    
    /**
     * Assign grade to enrollment (using Observer pattern)
     */
    public void assignGrade(String enrollmentId, double grade) throws InvalidDataException {
        Enrollment enrollment = enrollments.get(enrollmentId);
        if (enrollment == null) {
            throw new InvalidDataException("Enrollment not found: " + enrollmentId);
        }
        
        // Validate grade
        if (grade < 0 || grade > 100) {
            throw new InvalidDataException("Grade must be between 0 and 100", "grade", grade);
        }
        
        // Use notification manager to update grade (triggers observers)
        notificationManager.updateGrade(enrollment.getStudent(), enrollment, grade);
        
        System.out.println("\n✓ Grade assigned successfully");
    }
    
    /**
     * Complete an enrollment
     */
    public void completeEnrollment(String enrollmentId, double finalGrade) 
            throws InvalidDataException {
        Enrollment enrollment = enrollments.get(enrollmentId);
        if (enrollment == null) {
            throw new InvalidDataException("Enrollment not found: " + enrollmentId);
        }
        
        if (finalGrade < 0 || finalGrade > 100) {
            throw new InvalidDataException("Grade must be between 0 and 100");
        }
        
        enrollment.complete(finalGrade);
        enrollment.getStudent().updateGPA();
        
        System.out.println("✓ Enrollment completed");
        System.out.println("  Final Grade: " + String.format("%.2f", finalGrade) + 
                          " (" + enrollment.getLetterGrade() + ")");
        System.out.println("  Status: " + (enrollment.hasPassed() ? "PASSED" : "FAILED"));
    }
    
    /**
     * Get enrollment by ID
     */
    public Optional<Enrollment> getEnrollmentById(String enrollmentId) {
        return Optional.ofNullable(enrollments.get(enrollmentId));
    }
    
    /**
     * Get all enrollments
     */
    public List<Enrollment> getAllEnrollments() {
        return new ArrayList<>(enrollments.values());
    }
    
    /**
     * Get enrollments for a student (using Streams)
     */
    public List<Enrollment> getEnrollmentsForStudent(String studentId) {
        return enrollments.values().stream()
                         .filter(e -> e.getStudent().getStudentId().equals(studentId))
                         .sorted(Comparator.comparing(Enrollment::getEnrollmentDate))
                         .collect(Collectors.toList());
    }
    
    /**
     * Get active enrollments for a student
     */
    public List<Enrollment> getActiveEnrollmentsForStudent(String studentId) {
        return enrollments.values().stream()
                         .filter(e -> e.getStudent().getStudentId().equals(studentId))
                         .filter(Enrollment::isActive)
                         .collect(Collectors.toList());
    }
    
    /**
     * Get enrollments for a course (using Streams)
     */
    public List<Enrollment> getEnrollmentsForCourse(String courseId) {
        return enrollments.values().stream()
                         .filter(e -> e.getCourse().getCourseId().equals(courseId))
                         .sorted(Comparator.comparing(e -> e.getStudent().getFullName()))
                         .collect(Collectors.toList());
    }
    
    /**
     * Get enrollment count for a course
     */
    public long getEnrollmentCountForCourse(String courseId) {
        return enrollments.values().stream()
                         .filter(e -> e.getCourse().getCourseId().equals(courseId))
                         .filter(Enrollment::isActive)
                         .count();
    }
    
    /**
     * Get completed enrollments
     */
    public List<Enrollment> getCompletedEnrollments() {
        return enrollments.values().stream()
                         .filter(e -> "COMPLETED".equals(e.getStatus()))
                         .collect(Collectors.toList());
    }
    
    /**
     * Get enrollments by grade range (using Streams)
     */
    public List<Enrollment> getEnrollmentsByGradeRange(double minGrade, double maxGrade) {
        return enrollments.values().stream()
                         .filter(e -> e.getGrade() != null)
                         .filter(e -> e.getGrade() >= minGrade && e.getGrade() <= maxGrade)
                         .sorted(Comparator.comparingDouble(Enrollment::getNumericGrade).reversed())
                         .collect(Collectors.toList());
    }
    
    /**
     * Calculate average grade for a course (using Streams)
     */
    public double calculateAverageGradeForCourse(String courseId) {
        return enrollments.values().stream()
                         .filter(e -> e.getCourse().getCourseId().equals(courseId))
                         .filter(e -> e.getGrade() != null)
                         .mapToDouble(Enrollment::getNumericGrade)
                         .average()
                         .orElse(0.0);
    }
    
    /**
     * Get pass rate for a course (using Streams)
     */
    public double calculatePassRateForCourse(String courseId) {
        List<Enrollment> courseEnrollments = enrollments.values().stream()
                         .filter(e -> e.getCourse().getCourseId().equals(courseId))
                         .filter(e -> e.getGrade() != null)
                         .collect(Collectors.toList());
        
        if (courseEnrollments.isEmpty()) {
            return 0.0;
        }
        
        long passedCount = courseEnrollments.stream()
                                           .filter(Enrollment::hasPassed)
                                           .count();
        
        return (passedCount * 100.0) / courseEnrollments.size();
    }
    
    /**
     * Get enrollment statistics for a course
     */
    public Map<String, Object> getCourseStatistics(String courseId) {
        Map<String, Object> stats = new HashMap<>();
        
        List<Enrollment> courseEnrollments = getEnrollmentsForCourse(courseId);
        List<Enrollment> gradedEnrollments = courseEnrollments.stream()
                                                             .filter(e -> e.getGrade() != null)
                                                             .collect(Collectors.toList());
        
        stats.put("totalEnrollments", courseEnrollments.size());
        stats.put("activeEnrollments", courseEnrollments.stream()
                                                       .filter(Enrollment::isActive)
                                                       .count());
        stats.put("completedEnrollments", courseEnrollments.stream()
                                                          .filter(e -> "COMPLETED".equals(e.getStatus()))
                                                          .count());
        stats.put("averageGrade", calculateAverageGradeForCourse(courseId));
        stats.put("passRate", calculatePassRateForCourse(courseId));
        
        if (!gradedEnrollments.isEmpty()) {
            stats.put("highestGrade", gradedEnrollments.stream()
                                                      .mapToDouble(Enrollment::getNumericGrade)
                                                      .max()
                                                      .orElse(0.0));
            stats.put("lowestGrade", gradedEnrollments.stream()
                                                     .mapToDouble(Enrollment::getNumericGrade)
                                                     .min()
                                                     .orElse(0.0));
        }
        
        return stats;
    }
    
    /**
     * Generate enrollment report
     */
    public void generateEnrollmentReport(String courseId) {
        List<Enrollment> courseEnrollments = getEnrollmentsForCourse(courseId);
        
        if (courseEnrollments.isEmpty()) {
            System.out.println("No enrollments found for this course");
            return;
        }
        
        Course course = courseEnrollments.get(0).getCourse();
        
        System.out.println("\n=== Enrollment Report ===");
        System.out.println("Course: " + course.getCourseName() + " (" + course.getCourseCode() + ")");
        System.out.println("Instructor: " + course.getInstructor());
        System.out.println("\nEnrollment Summary:");
        
        Map<String, Object> stats = getCourseStatistics(courseId);
        stats.forEach((key, value) -> {
            if (value instanceof Double) {
                System.out.printf("  %s: %.2f%n", key, value);
            } else {
                System.out.printf("  %s: %s%n", key, value);
            }
        });
        
        System.out.println("\nEnrolled Students:");
        courseEnrollments.forEach(e -> {
            System.out.printf("  %s - %s - Grade: %s - Status: %s%n",
                e.getStudent().getStudentId(),
                e.getStudent().getFullName(),
                e.getGrade() != null ? String.format("%.2f", e.getGrade()) : "N/A",
                e.getStatus()
            );
        });
    }
    
    /**
     * Save enrollments to file
     */
    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(dataFilePath))) {
            oos.writeObject(new ArrayList<>(enrollments.values()));
            System.out.println("✓ Enrollments data saved to file: " + dataFilePath);
        } catch (IOException e) {
            System.err.println("✗ Error saving enrollments data: " + e.getMessage());
        }
    }
    
    /**
     * Load enrollments from file
     */
    @SuppressWarnings("unchecked")
    public void loadFromFile() {
        File file = new File(dataFilePath);
        if (!file.exists()) {
            System.out.println("No existing enrollments data file found. Starting fresh.");
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(dataFilePath))) {
            List<Enrollment> loadedEnrollments = (List<Enrollment>) ois.readObject();
            enrollments.clear();
            for (Enrollment enrollment : loadedEnrollments) {
                enrollments.put(enrollment.getEnrollmentId(), enrollment);
            }
            System.out.println("✓ Loaded " + enrollments.size() + " enrollments from file");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("✗ Error loading enrollments data: " + e.getMessage());
        }
    }
    
    /**
     * Generate enrollment ID
     */
    private String generateEnrollmentId() {
        return "ENR" + String.format("%06d", enrollmentIdCounter++);
    }
    
    /**
     * Clear all enrollments
     */
    public void clearAll() {
        enrollments.clear();
        enrollmentIdCounter = 1;  // Reset counter
        System.out.println("✓ All enrollments cleared");
    }
    
    /**
     * Get enrollment count
     */
    public int getEnrollmentCount() {
        return enrollments.size();
    }
}
