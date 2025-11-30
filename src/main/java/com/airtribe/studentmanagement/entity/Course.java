package com.airtribe.studentmanagement.entity;

import com.airtribe.studentmanagement.interfaces.Searchable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class representing a Course
 * Demonstrates encapsulation and composition
 */
public class Course implements Searchable, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String courseId;
    private String courseName;
    private String courseCode;
    private int credits;
    private String department;
    private String instructor;
    private int maxCapacity;
    private String description;
    private List<String> prerequisites;
    
    // Default constructor
    public Course() {
        this.prerequisites = new ArrayList<>();
    }
    
    // Parameterized constructor
    public Course(String courseId, String courseName, String courseCode, 
                 int credits, String department, String instructor, int maxCapacity) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.credits = credits;
        this.department = department;
        this.instructor = instructor;
        this.maxCapacity = maxCapacity;
        this.prerequisites = new ArrayList<>();
    }
    
    // Full constructor
    public Course(String courseId, String courseName, String courseCode, 
                 int credits, String department, String instructor, 
                 int maxCapacity, String description) {
        this(courseId, courseName, courseCode, credits, department, instructor, maxCapacity);
        this.description = description;
    }
    
    // Getters and Setters
    public String getCourseId() {
        return courseId;
    }
    
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public String getCourseCode() {
        return courseCode;
    }
    
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    
    public int getCredits() {
        return credits;
    }
    
    public void setCredits(int credits) {
        this.credits = credits;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getInstructor() {
        return instructor;
    }
    
    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
    
    public int getMaxCapacity() {
        return maxCapacity;
    }
    
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<String> getPrerequisites() {
        return new ArrayList<>(prerequisites);
    }
    
    public void addPrerequisite(String courseCode) {
        if (!prerequisites.contains(courseCode)) {
            prerequisites.add(courseCode);
        }
    }
    
    public void removePrerequisite(String courseCode) {
        prerequisites.remove(courseCode);
    }
    
    public boolean hasPrerequisite(String courseCode) {
        return prerequisites.contains(courseCode);
    }
    
    /**
     * Display course information
     */
    public void displayInfo() {
        System.out.println("=== Course Information ===");
        System.out.println("Course ID: " + courseId);
        System.out.println("Course Code: " + courseCode);
        System.out.println("Course Name: " + courseName);
        System.out.println("Department: " + department);
        System.out.println("Instructor: " + instructor);
        System.out.println("Credits: " + credits);
        System.out.println("Max Capacity: " + maxCapacity);
        if (description != null && !description.isEmpty()) {
            System.out.println("Description: " + description);
        }
        if (!prerequisites.isEmpty()) {
            System.out.println("Prerequisites: " + String.join(", ", prerequisites));
        }
    }
    
    // Searchable interface implementation
    @Override
    public boolean matchesId(String id) {
        return this.courseId.equalsIgnoreCase(id) || 
               this.courseCode.equalsIgnoreCase(id);
    }
    
    @Override
    public boolean matchesName(String name) {
        return this.courseName.toLowerCase().contains(name.toLowerCase());
    }
    
    @Override
    public boolean matchesKeyword(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return matchesName(keyword) || 
               matchesId(keyword) ||
               department.toLowerCase().contains(lowerKeyword) ||
               instructor.toLowerCase().contains(lowerKeyword) ||
               (description != null && description.toLowerCase().contains(lowerKeyword));
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(courseId, course.courseId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }
    
    @Override
    public String toString() {
        return String.format("Course{courseCode='%s', name='%s', credits=%d, instructor='%s'}", 
                           courseCode, courseName, credits, instructor);
    }
}
