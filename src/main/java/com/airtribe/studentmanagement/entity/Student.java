package com.airtribe.studentmanagement.entity;

import com.airtribe.studentmanagement.interfaces.Searchable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class representing a Student, extends Person
 * Demonstrates inheritance, polymorphism, and interface implementation
 */
public class Student extends Person implements Searchable {
    
    private String studentId;
    private String program;
    private int semester;
    private double gpa;
    private LocalDate enrollmentDate;
    private List<Enrollment> enrollments;
    private int attendancePercentage;
    
    // Default constructor
    public Student() {
        super();
        this.enrollments = new ArrayList<>();
    }
    
    // Parameterized constructor with chaining
    public Student(String id, String firstName, String lastName, String email, 
                   String phone, LocalDate dateOfBirth, String studentId, 
                   String program, int semester) {
        super(id, firstName, lastName, email, phone, dateOfBirth);
        this.studentId = studentId;
        this.program = program;
        this.semester = semester;
        this.enrollmentDate = LocalDate.now();
        this.enrollments = new ArrayList<>();
        this.gpa = 0.0;
        this.attendancePercentage = 100;
    }
    
    // Full constructor
    public Student(String id, String firstName, String lastName, String email, 
                   String phone, LocalDate dateOfBirth, String studentId, 
                   String program, int semester, double gpa, LocalDate enrollmentDate) {
        super(id, firstName, lastName, email, phone, dateOfBirth);
        this.studentId = studentId;
        this.program = program;
        this.semester = semester;
        this.gpa = gpa;
        this.enrollmentDate = enrollmentDate;
        this.enrollments = new ArrayList<>();
        this.attendancePercentage = 100;
    }
    
    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getProgram() {
        return program;
    }
    
    public void setProgram(String program) {
        this.program = program;
    }
    
    public int getSemester() {
        return semester;
    }
    
    public void setSemester(int semester) {
        this.semester = semester;
    }
    
    public double getGpa() {
        return gpa;
    }
    
    public void setGpa(double gpa) {
        this.gpa = gpa;
    }
    
    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }
    
    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
    
    public List<Enrollment> getEnrollments() {
        return new ArrayList<>(enrollments);
    }
    
    public void addEnrollment(Enrollment enrollment) {
        this.enrollments.add(enrollment);
    }
    
    public void removeEnrollment(Enrollment enrollment) {
        this.enrollments.remove(enrollment);
    }
    
    public int getAttendancePercentage() {
        return attendancePercentage;
    }
    
    public void setAttendancePercentage(int attendancePercentage) {
        this.attendancePercentage = Math.max(0, Math.min(100, attendancePercentage));
    }
    
    /**
     * Implementation of abstract method from Person
     * Demonstrates method overriding
     */
    @Override
    public String getRole() {
        return "STUDENT";
    }
    
    /**
     * Calculate average grade from all enrollments
     * @return Average grade
     */
    public double calculateAverageGrade() {
        if (enrollments.isEmpty()) {
            return 0.0;
        }
        return enrollments.stream()
                         .filter(e -> e.getGrade() != null)
                         .mapToDouble(Enrollment::getNumericGrade)
                         .average()
                         .orElse(0.0);
    }
    
    /**
     * Check if student is in good academic standing
     * @return true if GPA >= 2.0, false otherwise
     */
    public boolean isInGoodStanding() {
        return gpa >= 2.0;
    }
    
    /**
     * Update GPA based on current enrollments
     */
    public void updateGPA() {
        this.gpa = calculateAverageGrade() / 25.0; // Convert percentage to 4.0 scale
    }
    
    @Override
    public void displayInfo() {
        System.out.println("=== Student Information ===");
        System.out.println("ID: " + getId());
        System.out.println("Student ID: " + studentId);
        System.out.println("Name: " + getFullName());
        System.out.println("Email: " + getEmail());
        System.out.println("Phone: " + getPhone());
        System.out.println("Program: " + program);
        System.out.println("Semester: " + semester);
        System.out.println("GPA: " + String.format("%.2f", gpa));
        System.out.println("Attendance: " + attendancePercentage + "%");
        System.out.println("Enrollment Date: " + enrollmentDate);
        System.out.println("Number of Courses: " + enrollments.size());
        System.out.println("Academic Standing: " + (isInGoodStanding() ? "Good" : "Probation"));
    }
    
    // Searchable interface implementation
    @Override
    public boolean matchesId(String id) {
        return this.getId().equalsIgnoreCase(id) || 
               this.studentId.equalsIgnoreCase(id);
    }
    
    @Override
    public boolean matchesName(String name) {
        String fullName = getFullName().toLowerCase();
        String searchName = name.toLowerCase();
        return fullName.contains(searchName);
    }
    
    @Override
    public boolean matchesKeyword(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return matchesName(keyword) || 
               matchesId(keyword) ||
               program.toLowerCase().contains(lowerKeyword) ||
               getEmail().toLowerCase().contains(lowerKeyword);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return Objects.equals(studentId, student.studentId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), studentId);
    }
    
    @Override
    public String toString() {
        return String.format("Student{studentId='%s', name='%s', program='%s', semester=%d, gpa=%.2f}", 
                           studentId, getFullName(), program, semester, gpa);
    }
}
