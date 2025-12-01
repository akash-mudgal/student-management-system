package com.airtribe.studentmanagement;

import com.airtribe.studentmanagement.entity.*;
import com.airtribe.studentmanagement.exception.*;
import com.airtribe.studentmanagement.service.*;
import com.airtribe.studentmanagement.pattern.*;
import com.airtribe.studentmanagement.util.*;

import java.time.LocalDate;
import java.util.*;

/**
 * Main class for Student Management System
 * Demonstrates:
 * - Complete application flow
 * - Exception handling with try-catch-finally
 * - Resource management
 * - User interaction through CLI
 */
public class Main {
    
    private static final Scanner scanner = new Scanner(System.in);
    private static StudentService studentService;
    private static CourseService courseService;
    private static EnrollmentService enrollmentService;
    private static ConfigurationManager config;
    
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("      STUDENT MANAGEMENT SYSTEM");
        System.out.println("=".repeat(60));
        
        // Initialize services and configuration
        initialize();
        
        // Load existing data
        loadData();
        
        // Add sample data if needed
        if (studentService.getStudentCount() == 0) {
            System.out.println("\nNo existing data found. Would you like to load sample data? (y/n)");
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("y") || response.equals("yes")) {
                loadSampleData();
            }
        }
        
        // Main application loop
        try {
            boolean running = true;
            while (running) {
                displayMainMenu();
                String choice = scanner.nextLine().trim();
                
                try {
                    switch (choice) {
                        case "1":
                            studentManagementMenu();
                            break;
                        case "2":
                            courseManagementMenu();
                            break;
                        case "3":
                            enrollmentManagementMenu();
                            break;
                        case "4":
                            reportsMenu();
                            break;
                        case "5":
                            saveData();
                            break;
                        case "6":
                            loadData();
                            break;
                        case "7":
                            config.displayConfiguration();
                            break;
                        case "0":
                            running = false;
                            shutdown();
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                    System.out.println("Press Enter to continue...");
                    scanner.nextLine();
                }
            }
        } finally {
            // Ensure resources are cleaned up
            scanner.close();
            System.out.println("\nThank you for using Student Management System!");
        }
    }
    
    /**
     * Initialize services and configuration
     */
    private static void initialize() {
        config = ConfigurationManager.getInstance();
        studentService = new StudentService();
        courseService = new CourseService();
        enrollmentService = new EnrollmentService();
        System.out.println("✓ System initialized successfully");
    }
    
    /**
     * Display main menu
     */
    private static void displayMainMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("MAIN MENU");
        System.out.println("=".repeat(60));
        System.out.println("1. Student Management");
        System.out.println("2. Course Management");
        System.out.println("3. Enrollment Management");
        System.out.println("4. Reports & Statistics");
        System.out.println("5. Save Data");
        System.out.println("6. Load Data");
        System.out.println("7. View Configuration");
        System.out.println("0. Exit");
        System.out.println("=".repeat(60));
        System.out.print("Enter your choice: ");
    }
    
    /**
     * Student Management Menu
     */
    private static void studentManagementMenu() {
        System.out.println("\n--- Student Management ---");
        System.out.println("1. Add Student");
        System.out.println("2. View All Students");
        System.out.println("3. Search Student");
        System.out.println("4. Update Student");
        System.out.println("5. Delete Student");
        System.out.println("6. View Student Details");
        System.out.println("0. Back");
        System.out.print("Choice: ");
        
        String choice = scanner.nextLine().trim();
        
        try {
            switch (choice) {
                case "1":
                    addStudent();
                    break;
                case "2":
                    viewAllStudents();
                    break;
                case "3":
                    searchStudent();
                    break;
                case "4":
                    updateStudent();
                    break;
                case "5":
                    deleteStudent();
                    break;
                case "6":
                    viewStudentDetails();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Add a new student
     */
    private static void addStudent() throws InvalidDataException {
        System.out.println("\n--- Add New Student ---");
        
        System.out.print("Student Type (1=Undergraduate, 2=Graduate): ");
        String typeChoice = scanner.nextLine().trim();
        
        StudentFactory.StudentType type = typeChoice.equals("2") ? 
            StudentFactory.StudentType.GRADUATE : StudentFactory.StudentType.UNDERGRADUATE;
        
        // Auto-generate student ID
        String studentId = studentService.generateStudentId();
        System.out.println("Generated Student ID: " + studentId);
        
        System.out.print("First Name: ");
        String firstName = scanner.nextLine().trim();
        
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine().trim();
        
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        
        System.out.print("Phone: ");
        String phone = scanner.nextLine().trim();
        
        System.out.print("Date of Birth (YYYY-MM-DD): ");
        LocalDate dob = InputValidator.parseDate(scanner.nextLine().trim(), "Date of Birth");
        
        System.out.print("Program: ");
        String program = scanner.nextLine().trim();
        
        System.out.print("Semester: ");
        int semester = InputValidator.parseInt(scanner.nextLine().trim(), "Semester");
        
        // Use Factory pattern to create student
        Student student = StudentFactory.builder(type)
            .studentId(studentId)
            .firstName(firstName)
            .lastName(lastName)
            .email(email)
            .phone(phone)
            .dateOfBirth(dob)
            .program(program)
            .semester(semester)
            .build();
        
        // Add thesis information for graduate students
        if (type == StudentFactory.StudentType.GRADUATE && student instanceof GraduateStudent) {
            GraduateStudent gradStudent = (GraduateStudent) student;
            
            System.out.print("Research Area: ");
            String researchArea = scanner.nextLine().trim();
            gradStudent.setResearchArea(researchArea);
            
            System.out.print("Advisor: ");
            String advisor = scanner.nextLine().trim();
            gradStudent.setAdvisor(advisor);
        }
        
        studentService.addStudent(student);
    }
    
    /**
     * View all students
     */
    private static void viewAllStudents() {
        List<Student> students = studentService.getAllStudents();
        
        if (students.isEmpty()) {
            System.out.println("\nNo students found.");
            return;
        }
        
        System.out.println("\n--- All Students ---");
        System.out.println(String.format("%-12s %-20s %-25s %-10s %-6s", 
                                        "Student ID", "Name", "Program", "Semester", "GPA"));
        System.out.println("-".repeat(80));
        
        students.forEach(s -> 
            System.out.println(String.format("%-12s %-20s %-25s %-10d %.2f",
                s.getStudentId(),
                s.getFullName(),
                s.getProgram(),
                s.getSemester(),
                s.getGpa()
            ))
        );
        
        System.out.println("\nTotal Students: " + students.size());
    }
    
    /**
     * Search student
     */
    private static void searchStudent() {
        System.out.print("\nEnter search term (name or ID): ");
        String searchTerm = scanner.nextLine().trim();
        
        List<Student> results = studentService.searchByName(searchTerm);
        
        if (results.isEmpty()) {
            System.out.println("No students found matching: " + searchTerm);
        } else {
            System.out.println("\n--- Search Results ---");
            results.forEach(s -> System.out.println(s.toString()));
        }
    }
    
    /**
     * Update student
     */
    private static void updateStudent() throws InvalidDataException, StudentNotFoundException {
        System.out.print("\nEnter Student ID to update: ");
        String studentId = scanner.nextLine().trim();
        
        Student student = studentService.getStudentById(studentId);
        student.displayInfo();
        
        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Email");
        System.out.println("2. Phone");
        System.out.println("3. Program");
        System.out.println("4. Semester");
        System.out.print("Choice: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                System.out.print("New Email: ");
                student.setEmail(scanner.nextLine().trim());
                break;
            case "2":
                System.out.print("New Phone: ");
                student.setPhone(scanner.nextLine().trim());
                break;
            case "3":
                System.out.print("New Program: ");
                student.setProgram(scanner.nextLine().trim());
                break;
            case "4":
                System.out.print("New Semester: ");
                student.setSemester(InputValidator.parseInt(scanner.nextLine().trim(), "Semester"));
                break;
            default:
                System.out.println("Invalid choice");
                return;
        }
        
        studentService.updateStudent(student);
    }
    
    /**
     * Delete student
     */
    private static void deleteStudent() throws StudentNotFoundException {
        System.out.print("\nEnter Student ID to delete: ");
        String studentId = scanner.nextLine().trim();
        
        Student student = studentService.getStudentById(studentId);
        System.out.println("Are you sure you want to delete: " + student.getFullName() + "? (y/n)");
        
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            studentService.deleteStudent(studentId);
        } else {
            System.out.println("Deletion cancelled");
        }
    }
    
    /**
     * View student details
     */
    private static void viewStudentDetails() throws StudentNotFoundException {
        System.out.print("\nEnter Student ID: ");
        String studentId = scanner.nextLine().trim();
        
        Student student = studentService.getStudentById(studentId);
        student.displayInfo();
        
        // Show enrollments
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsForStudent(studentId);
        if (!enrollments.isEmpty()) {
            System.out.println("\n--- Enrolled Courses ---");
            enrollments.forEach(e -> {
                System.out.printf("%s - %s - Grade: %s - Status: %s%n",
                    e.getCourse().getCourseCode(),
                    e.getCourse().getCourseName(),
                    e.getGrade() != null ? String.format("%.2f", e.getGrade()) : "N/A",
                    e.getStatus()
                );
            });
        }
    }
    
    /**
     * Course Management Menu
     */
    private static void courseManagementMenu() {
        System.out.println("\n--- Course Management ---");
        System.out.println("1. Add Course");
        System.out.println("2. View All Courses");
        System.out.println("3. Search Course");
        System.out.println("4. View Course Details");
        System.out.println("0. Back");
        System.out.print("Choice: ");
        
        String choice = scanner.nextLine().trim();
        
        try {
            switch (choice) {
                case "1":
                    addCourse();
                    break;
                case "2":
                    viewAllCourses();
                    break;
                case "3":
                    searchCourse();
                    break;
                case "4":
                    viewCourseDetails();
                    break;
                case "0":
                    return;
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Add a new course
     */
    private static void addCourse() throws InvalidDataException {
        System.out.println("\n--- Add New Course ---");
        
        // Auto-generate course ID
        String courseId = courseService.generateCourseId();
        System.out.println("Generated Course ID: " + courseId);
        
        System.out.print("Course Code (e.g., CS101): ");
        String courseCode = scanner.nextLine().trim().toUpperCase();
        
        System.out.print("Course Name: ");
        String courseName = scanner.nextLine().trim();
        
        System.out.print("Department: ");
        String department = scanner.nextLine().trim();
        
        System.out.print("Instructor: ");
        String instructor = scanner.nextLine().trim();
        
        System.out.print("Credits: ");
        int credits = InputValidator.parseInt(scanner.nextLine().trim(), "Credits");
        
        System.out.print("Max Capacity: ");
        int maxCapacity = InputValidator.parseInt(scanner.nextLine().trim(), "Max Capacity");
        
        Course course = new Course(courseId, courseName, courseCode, credits, 
                                   department, instructor, maxCapacity);
        
        courseService.addCourse(course);
    }
    
    /**
     * View all courses
     */
    private static void viewAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        
        if (courses.isEmpty()) {
            System.out.println("\nNo courses found.");
            return;
        }
        
        System.out.println("\n--- All Courses ---");
        System.out.println(String.format("%-10s %-30s %-15s %-8s %-20s",
                                        "Code", "Name", "Department", "Credits", "Instructor"));
        System.out.println("-".repeat(90));
        
        courses.forEach(c ->
            System.out.println(String.format("%-10s %-30s %-15s %-8d %-20s",
                c.getCourseCode(),
                c.getCourseName(),
                c.getDepartment(),
                c.getCredits(),
                c.getInstructor()
            ))
        );
        
        System.out.println("\nTotal Courses: " + courses.size());
    }
    
    /**
     * Search course
     */
    private static void searchCourse() {
        System.out.print("\nEnter search term: ");
        String searchTerm = scanner.nextLine().trim();
        
        List<Course> results = courseService.searchByName(searchTerm);
        
        if (results.isEmpty()) {
            System.out.println("No courses found matching: " + searchTerm);
        } else {
            System.out.println("\n--- Search Results ---");
            results.forEach(c -> System.out.println(c.toString()));
        }
    }
    
    /**
     * View course details
     */
    private static void viewCourseDetails() {
        System.out.print("\nEnter Course ID or Code: ");
        String searchTerm = scanner.nextLine().trim();
        
        Optional<Course> courseOpt = courseService.getCourseById(searchTerm);
        if (!courseOpt.isPresent()) {
            courseOpt = courseService.getCourseByCode(searchTerm);
        }
        
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            course.displayInfo();
            
            // Show enrollment statistics
            enrollmentService.generateEnrollmentReport(course.getCourseId());
        } else {
            System.out.println("Course not found");
        }
    }
    
    /**
     * Enrollment Management Menu
     */
    private static void enrollmentManagementMenu() {
        System.out.println("\n--- Enrollment Management ---");
        System.out.println("1. Enroll Student in Course");
        System.out.println("2. Assign Grade");
        System.out.println("3. Drop Enrollment");
        System.out.println("4. View All Enrollments");
        System.out.println("0. Back");
        System.out.print("Choice: ");
        
        String choice = scanner.nextLine().trim();
        
        try {
            switch (choice) {
                case "1":
                    enrollStudent();
                    break;
                case "2":
                    assignGrade();
                    break;
                case "3":
                    dropEnrollment();
                    break;
                case "4":
                    viewAllEnrollments();
                    break;
                case "0":
                    return;
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Enroll student in course
     */
    private static void enrollStudent() throws Exception {
        System.out.print("\nEnter Student ID: ");
        String studentId = scanner.nextLine().trim();
        
        System.out.print("Enter Course ID or Code: ");
        String courseSearch = scanner.nextLine().trim();
        
        Student student = studentService.getStudentById(studentId);
        
        Optional<Course> courseOpt = courseService.getCourseById(courseSearch);
        if (!courseOpt.isPresent()) {
            courseOpt = courseService.getCourseByCode(courseSearch);
        }
        
        if (!courseOpt.isPresent()) {
            System.out.println("Course not found");
            return;
        }
        
        enrollmentService.enrollStudent(student, courseOpt.get());
    }
    
    /**
     * Assign grade (demonstrates Observer pattern)
     */
    private static void assignGrade() throws InvalidDataException {
        System.out.print("\nEnter Enrollment ID: ");
        String enrollmentId = scanner.nextLine().trim();
        
        System.out.print("Enter Grade (0-100): ");
        double grade = InputValidator.parseDouble(scanner.nextLine().trim(), "Grade");
        
        enrollmentService.assignGrade(enrollmentId, grade);
    }
    
    /**
     * Drop enrollment
     */
    private static void dropEnrollment() throws InvalidDataException {
        System.out.print("\nEnter Enrollment ID: ");
        String enrollmentId = scanner.nextLine().trim();
        
        enrollmentService.dropEnrollment(enrollmentId);
    }
    
    /**
     * View all enrollments
     */
    private static void viewAllEnrollments() {
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
        
        if (enrollments.isEmpty()) {
            System.out.println("\nNo enrollments found.");
            return;
        }
        
        System.out.println("\n--- All Enrollments ---");
        enrollments.forEach(e -> System.out.println(e.toString()));
        System.out.println("\nTotal Enrollments: " + enrollments.size());
    }
    
    /**
     * Reports Menu
     */
    private static void reportsMenu() {
        System.out.println("\n--- Reports & Statistics ---");
        System.out.println("1. Student Statistics");
        System.out.println("2. Course Statistics");
        System.out.println("3. Top Performers");
        System.out.println("4. Students on Probation");
        System.out.println("5. Export Students to CSV");
        System.out.println("6. Export Courses to CSV");
        System.out.println("0. Back");
        System.out.print("Choice: ");
        
        String choice = scanner.nextLine().trim();
        
        try {
            switch (choice) {
                case "1":
                    studentService.displayStatistics();
                    break;
                case "2":
                    courseService.displayStatistics();
                    break;
                case "3":
                    showTopPerformers();
                    break;
                case "4":
                    showStudentsOnProbation();
                    break;
                case "5":
                    studentService.exportToCSV("students_export.csv");
                    break;
                case "6":
                    courseService.exportToCSV("courses_export.csv");
                    break;
                case "0":
                    return;
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Show top performers
     */
    private static void showTopPerformers() {
        List<Student> topStudents = studentService.getTopPerformers(10);
        
        System.out.println("\n--- Top 10 Performers ---");
        int rank = 1;
        for (Student s : topStudents) {
            System.out.printf("%d. %s - GPA: %.2f - %s%n",
                rank++, s.getFullName(), s.getGpa(), s.getProgram());
        }
    }
    
    /**
     * Show students on probation
     */
    private static void showStudentsOnProbation() {
        List<Student> probationStudents = studentService.getStudentsOnProbation();
        
        System.out.println("\n--- Students on Academic Probation ---");
        if (probationStudents.isEmpty()) {
            System.out.println("No students on probation.");
        } else {
            probationStudents.forEach(s ->
                System.out.printf("%s - GPA: %.2f%n",
                    s.getFullName(), s.getGpa())
            );
        }
    }
    
    /**
     * Save all data to files
     */
    private static void saveData() {
        System.out.println("\n--- Saving Data ---");
        studentService.saveToFile();
        courseService.saveToFile();
        enrollmentService.saveToFile();
        System.out.println("✓ All data saved successfully");
    }
    
    /**
     * Load all data from files
     */
    private static void loadData() {
        System.out.println("\n--- Loading Data ---");
        studentService.loadFromFile();
        courseService.loadFromFile();
        enrollmentService.loadFromFile();
    }
    
    /**
     * Shutdown procedure
     */
    private static void shutdown() {
        System.out.println("\n--- Shutting Down ---");
        System.out.print("Save data before exit? (y/n): ");
        String response = scanner.nextLine().trim().toLowerCase();
        
        if (response.equals("y") || response.equals("yes")) {
            saveData();
        }
        
        System.out.println("✓ Shutdown complete");
    }
    
    /**
     * Load sample data for demonstration
     */
    private static void loadSampleData() {
        System.out.println("\n--- Loading Sample Data ---");
        
        try {
            // Add sample courses
            Course cs101 = new Course("C001", "Introduction to Programming", "CS101", 
                                     3, "Computer Science", "Dr. Smith", 30);
            Course cs201 = new Course("C002", "Data Structures", "CS201", 
                                     4, "Computer Science", "Dr. Johnson", 25);
            Course math101 = new Course("C003", "Calculus I", "MATH101", 
                                       4, "Mathematics", "Dr. Williams", 40);
            
            courseService.addCourse(cs101);
            courseService.addCourse(cs201);
            courseService.addCourse(math101);
            
            // Add sample students using Factory pattern
            Student student1 = StudentFactory.builder(StudentFactory.StudentType.UNDERGRADUATE)
                .studentId("STU001")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@university.edu")
                .phone("123-456-7890")
                .dateOfBirth(LocalDate.of(2002, 5, 15))
                .program("Computer Science")
                .semester(3)
                .build();
            student1.setGpa(3.5);
            student1.setAttendancePercentage(95);
            
            Student student2 = StudentFactory.builder(StudentFactory.StudentType.UNDERGRADUATE)
                .studentId("STU002")
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@university.edu")
                .phone("123-456-7891")
                .dateOfBirth(LocalDate.of(2003, 8, 22))
                .program("Computer Science")
                .semester(2)
                .build();
            student2.setGpa(3.8);
            student2.setAttendancePercentage(98);
            
            GraduateStudent gradStudent = (GraduateStudent) StudentFactory.builder(StudentFactory.StudentType.GRADUATE)
                .studentId("GRAD001")
                .firstName("Alice")
                .lastName("Johnson")
                .email("alice.johnson@university.edu")
                .phone("123-456-7892")
                .dateOfBirth(LocalDate.of(1998, 3, 10))
                .program("Computer Science - MS")
                .semester(4)
                .thesisTitle("Machine Learning in Healthcare")
                .advisor("Dr. Smith")
                .researchArea("Artificial Intelligence")
                .build();
            gradStudent.setGpa(3.9);
            gradStudent.setAttendancePercentage(100);
            
            studentService.addStudent(student1);
            studentService.addStudent(student2);
            studentService.addStudent(gradStudent);
            
            // Add sample enrollments
            Enrollment e1 = enrollmentService.enrollStudent(student1, cs101);
            e1.setGrade(88.5);
            
            Enrollment e2 = enrollmentService.enrollStudent(student2, cs101);
            e2.setGrade(92.0);
            
            Enrollment e3 = enrollmentService.enrollStudent(gradStudent, cs201);
            e3.setGrade(95.5);
            
            System.out.println("✓ Sample data loaded successfully");
            System.out.println("  - 3 courses added");
            System.out.println("  - 3 students added (2 undergraduate, 1 graduate)");
            System.out.println("  - 3 enrollments added");
            
        } catch (Exception e) {
            System.err.println("Error loading sample data: " + e.getMessage());
        }
    }
}
