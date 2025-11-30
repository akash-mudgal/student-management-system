package com.airtribe.studentmanagement.service;

import com.airtribe.studentmanagement.entity.Course;
import com.airtribe.studentmanagement.exception.InvalidDataException;
import com.airtribe.studentmanagement.pattern.ConfigurationManager;
import com.airtribe.studentmanagement.util.InputValidator;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for Course operations
 * Demonstrates service layer with Java Streams and File I/O
 */
public class CourseService {
    
    private Map<String, Course> courses;
    private String dataFilePath;
    
    public CourseService() {
        this.courses = new HashMap<>();
        ConfigurationManager config = ConfigurationManager.getInstance();
        this.dataFilePath = config.getDataDirectory() + "/courses.dat";
        
        // Ensure data directory exists
        createDataDirectoryIfNeeded();
    }
    
    private void createDataDirectoryIfNeeded() {
        File dataDir = new File(ConfigurationManager.getInstance().getDataDirectory());
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }
    
    /**
     * Add a new course
     */
    public void addCourse(Course course) throws InvalidDataException {
        if (course == null) {
            throw new InvalidDataException("Course cannot be null");
        }
        
        InputValidator.validateNotEmpty(course.getCourseId(), "Course ID");
        InputValidator.validateNotEmpty(course.getCourseName(), "Course Name");
        InputValidator.validateCourseCode(course.getCourseCode());
        InputValidator.validateCredits(course.getCredits());
        
        if (courses.containsKey(course.getCourseId())) {
            throw new InvalidDataException("Course with ID already exists: " + course.getCourseId());
        }
        
        courses.put(course.getCourseId(), course);
        System.out.println("✓ Course added successfully: " + course.getCourseName());
    }
    
    /**
     * Get course by ID
     */
    public Optional<Course> getCourseById(String courseId) {
        return Optional.ofNullable(courses.get(courseId));
    }
    
    /**
     * Get course by code
     */
    public Optional<Course> getCourseByCode(String courseCode) {
        return courses.values().stream()
                     .filter(c -> c.getCourseCode().equalsIgnoreCase(courseCode))
                     .findFirst();
    }
    
    /**
     * Get all courses
     */
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }
    
    /**
     * Update course
     */
    public void updateCourse(Course course) throws InvalidDataException {
        if (course == null) {
            throw new InvalidDataException("Course cannot be null");
        }
        
        if (!courses.containsKey(course.getCourseId())) {
            throw new InvalidDataException("Course not found: " + course.getCourseId());
        }
        
        courses.put(course.getCourseId(), course);
        System.out.println("✓ Course updated successfully: " + course.getCourseName());
    }
    
    /**
     * Delete course
     */
    public void deleteCourse(String courseId) throws InvalidDataException {
        if (!courses.containsKey(courseId)) {
            throw new InvalidDataException("Course not found: " + courseId);
        }
        
        Course removed = courses.remove(courseId);
        System.out.println("✓ Course deleted: " + removed.getCourseName());
    }
    
    /**
     * Search courses by name (using Streams)
     */
    public List<Course> searchByName(String name) {
        return courses.values().stream()
                     .filter(c -> c.matchesName(name))
                     .sorted(Comparator.comparing(Course::getCourseName))
                     .collect(Collectors.toList());
    }
    
    /**
     * Get courses by department (using Streams)
     */
    public List<Course> getCoursesByDepartment(String department) {
        return courses.values().stream()
                     .filter(c -> c.getDepartment().equalsIgnoreCase(department))
                     .sorted(Comparator.comparing(Course::getCourseCode))
                     .collect(Collectors.toList());
    }
    
    /**
     * Get courses by instructor (using Streams)
     */
    public List<Course> getCoursesByInstructor(String instructor) {
        return courses.values().stream()
                     .filter(c -> c.getInstructor().equalsIgnoreCase(instructor))
                     .collect(Collectors.toList());
    }
    
    /**
     * Get courses by credits (using Streams)
     */
    public List<Course> getCoursesByCredits(int credits) {
        return courses.values().stream()
                     .filter(c -> c.getCredits() == credits)
                     .collect(Collectors.toList());
    }
    
    /**
     * Get all departments (using Streams)
     */
    public List<String> getAllDepartments() {
        return courses.values().stream()
                     .map(Course::getDepartment)
                     .distinct()
                     .sorted()
                     .collect(Collectors.toList());
    }
    
    /**
     * Get course count by department (using Streams)
     */
    public Map<String, Long> getCourseCountByDepartment() {
        return courses.values().stream()
                     .collect(Collectors.groupingBy(
                         Course::getDepartment,
                         Collectors.counting()
                     ));
    }
    
    /**
     * Save courses to file
     */
    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(dataFilePath))) {
            oos.writeObject(new ArrayList<>(courses.values()));
            System.out.println("✓ Courses data saved to file: " + dataFilePath);
        } catch (IOException e) {
            System.err.println("✗ Error saving courses data: " + e.getMessage());
        }
    }
    
    /**
     * Load courses from file
     */
    @SuppressWarnings("unchecked")
    public void loadFromFile() {
        File file = new File(dataFilePath);
        if (!file.exists()) {
            System.out.println("No existing courses data file found. Starting fresh.");
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(dataFilePath))) {
            List<Course> loadedCourses = (List<Course>) ois.readObject();
            courses.clear();
            for (Course course : loadedCourses) {
                courses.put(course.getCourseId(), course);
            }
            System.out.println("✓ Loaded " + courses.size() + " courses from file");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("✗ Error loading courses data: " + e.getMessage());
        }
    }
    
    /**
     * Export courses to CSV
     */
    public void exportToCSV(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("CourseID,CourseCode,CourseName,Department,Instructor,Credits,MaxCapacity");
            
            courses.values().forEach(course -> {
                writer.printf("%s,%s,%s,%s,%s,%d,%d%n",
                    course.getCourseId(),
                    course.getCourseCode(),
                    course.getCourseName(),
                    course.getDepartment(),
                    course.getInstructor(),
                    course.getCredits(),
                    course.getMaxCapacity()
                );
            });
            
            System.out.println("✓ Courses exported to CSV: " + filename);
        } catch (IOException e) {
            System.err.println("✗ Error exporting to CSV: " + e.getMessage());
        }
    }
    
    /**
     * Display statistics
     */
    public void displayStatistics() {
        System.out.println("\n=== Course Statistics ===");
        System.out.println("Total Courses: " + courses.size());
        System.out.println("\nCourses by Department:");
        getCourseCountByDepartment().forEach((dept, count) -> 
            System.out.println("  " + dept + ": " + count)
        );
    }
    
    /**
     * Clear all courses
     */
    public void clearAll() {
        courses.clear();
        System.out.println("✓ All courses cleared");
    }
    
    /**
     * Get course count
     */
    public int getCourseCount() {
        return courses.size();
    }
}
