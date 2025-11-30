package com.airtribe.studentmanagement.entity;

import com.airtribe.studentmanagement.interfaces.Gradeable;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Class representing an Enrollment (Student enrolled in a Course)
 * Demonstrates composition and the Gradeable interface implementation
 */
public class Enrollment implements Gradeable, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String enrollmentId;
    private Student student;
    private Course course;
    private LocalDate enrollmentDate;
    private String status; // ACTIVE, COMPLETED, DROPPED, WITHDRAWN
    private Double grade; // Percentage grade (0-100)
    private int attendanceCount;
    private int totalClasses;
    private String feedback;
    
    // Default constructor
    public Enrollment() {
        this.status = "ACTIVE";
        this.enrollmentDate = LocalDate.now();
        this.attendanceCount = 0;
        this.totalClasses = 0;
    }
    
    // Parameterized constructor
    public Enrollment(String enrollmentId, Student student, Course course) {
        this.enrollmentId = enrollmentId;
        this.student = student;
        this.course = course;
        this.enrollmentDate = LocalDate.now();
        this.status = "ACTIVE";
        this.attendanceCount = 0;
        this.totalClasses = 0;
    }
    
    // Full constructor
    public Enrollment(String enrollmentId, Student student, Course course, 
                     LocalDate enrollmentDate, String status) {
        this(enrollmentId, student, course);
        this.enrollmentDate = enrollmentDate;
        this.status = status;
    }
    
    // Getters and Setters
    public String getEnrollmentId() {
        return enrollmentId;
    }
    
    public void setEnrollmentId(String enrollmentId) {
        this.enrollmentId = enrollmentId;
    }
    
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }
    
    public Course getCourse() {
        return course;
    }
    
    public void setCourse(Course course) {
        this.course = course;
    }
    
    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }
    
    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Double getGrade() {
        return grade;
    }
    
    public void setGrade(Double grade) {
        this.grade = grade;
    }
    
    public int getAttendanceCount() {
        return attendanceCount;
    }
    
    public void setAttendanceCount(int attendanceCount) {
        this.attendanceCount = attendanceCount;
    }
    
    public int getTotalClasses() {
        return totalClasses;
    }
    
    public void setTotalClasses(int totalClasses) {
        this.totalClasses = totalClasses;
    }
    
    public String getFeedback() {
        return feedback;
    }
    
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
    
    /**
     * Mark attendance for a class
     */
    public void markAttendance(boolean present) {
        totalClasses++;
        if (present) {
            attendanceCount++;
        }
    }
    
    /**
     * Get attendance percentage
     */
    public double getAttendancePercentage() {
        if (totalClasses == 0) {
            return 100.0;
        }
        return (attendanceCount * 100.0) / totalClasses;
    }
    
    /**
     * Get numeric grade (for calculations)
     */
    public double getNumericGrade() {
        return grade != null ? grade : 0.0;
    }
    
    /**
     * Complete the enrollment with a final grade
     */
    public void complete(double finalGrade) {
        this.grade = finalGrade;
        this.status = "COMPLETED";
    }
    
    /**
     * Drop the enrollment
     */
    public void drop() {
        this.status = "DROPPED";
    }
    
    /**
     * Withdraw from the enrollment
     */
    public void withdraw() {
        this.status = "WITHDRAWN";
    }
    
    // Gradeable interface implementation
    @Override
    public double calculateGrade() {
        if (grade != null) {
            return grade;
        }
        // Simple calculation based on attendance if no grade assigned
        return getAttendancePercentage() * 0.3; // 30% weightage to attendance
    }
    
    @Override
    public String getLetterGrade() {
        double gradeValue = grade != null ? grade : calculateGrade();
        
        if (gradeValue >= 90) return "A";
        if (gradeValue >= 80) return "B";
        if (gradeValue >= 70) return "C";
        if (gradeValue >= 60) return "D";
        return "F";
    }
    
    @Override
    public boolean hasPassed() {
        double gradeValue = grade != null ? grade : calculateGrade();
        return gradeValue >= 60;
    }
    
    /**
     * Check if enrollment is active
     */
    public boolean isActive() {
        return "ACTIVE".equals(status);
    }
    
    /**
     * Display enrollment information
     */
    public void displayInfo() {
        System.out.println("=== Enrollment Information ===");
        System.out.println("Enrollment ID: " + enrollmentId);
        System.out.println("Student: " + student.getFullName() + " (" + student.getStudentId() + ")");
        System.out.println("Course: " + course.getCourseName() + " (" + course.getCourseCode() + ")");
        System.out.println("Enrollment Date: " + enrollmentDate);
        System.out.println("Status: " + status);
        System.out.println("Attendance: " + String.format("%.1f%%", getAttendancePercentage()) + 
                          " (" + attendanceCount + "/" + totalClasses + ")");
        if (grade != null) {
            System.out.println("Grade: " + String.format("%.2f", grade) + " (" + getLetterGrade() + ")");
            System.out.println("Passed: " + (hasPassed() ? "Yes" : "No"));
        } else {
            System.out.println("Grade: Not assigned yet");
        }
        if (feedback != null && !feedback.isEmpty()) {
            System.out.println("Feedback: " + feedback);
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enrollment that = (Enrollment) o;
        return Objects.equals(enrollmentId, that.enrollmentId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(enrollmentId);
    }
    
    @Override
    public String toString() {
        return String.format("Enrollment{id='%s', student='%s', course='%s', grade=%s, status='%s'}", 
                           enrollmentId, 
                           student != null ? student.getStudentId() : "null",
                           course != null ? course.getCourseCode() : "null",
                           grade != null ? String.format("%.2f", grade) : "N/A",
                           status);
    }
}
