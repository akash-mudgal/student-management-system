package com.airtribe.studentmanagement.util;

import com.airtribe.studentmanagement.exception.InvalidDataException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * Utility class for input validation
 * Demonstrates exception handling and validation patterns
 */
public class InputValidator {
    
    // Email pattern for validation
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    // Phone pattern for validation (supports various formats)
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^[+]?[(]?[0-9]{1,4}[)]?[-\\s.]?[(]?[0-9]{1,4}[)]?[-\\s.]?[0-9]{1,9}$");
    
    // Student ID pattern (alphanumeric, 6-10 characters)
    private static final Pattern STUDENT_ID_PATTERN = 
        Pattern.compile("^[A-Z0-9]{6,10}$");
    
    // Course code pattern (e.g., CS101, MATH201)
    private static final Pattern COURSE_CODE_PATTERN = 
        Pattern.compile("^[A-Z]{2,4}[0-9]{3,4}$");
    
    /**
     * Validate if a string is not null or empty
     */
    public static void validateNotEmpty(String value, String fieldName) throws InvalidDataException {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidDataException(
                fieldName + " cannot be null or empty", 
                fieldName, 
                value
            );
        }
    }
    
    /**
     * Validate email format
     */
    public static void validateEmail(String email) throws InvalidDataException {
        validateNotEmpty(email, "Email");
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidDataException(
                "Invalid email format", 
                "email", 
                email
            );
        }
    }
    
    /**
     * Validate phone number format
     */
    public static void validatePhone(String phone) throws InvalidDataException {
        validateNotEmpty(phone, "Phone");
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new InvalidDataException(
                "Invalid phone number format", 
                "phone", 
                phone
            );
        }
    }
    
    /**
     * Validate student ID format
     */
    public static void validateStudentId(String studentId) throws InvalidDataException {
        validateNotEmpty(studentId, "Student ID");
        if (!STUDENT_ID_PATTERN.matcher(studentId.toUpperCase()).matches()) {
            throw new InvalidDataException(
                "Student ID must be 6-10 alphanumeric characters", 
                "studentId", 
                studentId
            );
        }
    }
    
    /**
     * Validate course code format
     */
    public static void validateCourseCode(String courseCode) throws InvalidDataException {
        validateNotEmpty(courseCode, "Course Code");
        if (!COURSE_CODE_PATTERN.matcher(courseCode.toUpperCase()).matches()) {
            throw new InvalidDataException(
                "Course code must be 2-4 letters followed by 3-4 digits (e.g., CS101)", 
                "courseCode", 
                courseCode
            );
        }
    }
    
    /**
     * Validate GPA (0.0 - 4.0)
     */
    public static void validateGPA(double gpa) throws InvalidDataException {
        if (gpa < 0.0 || gpa > 4.0) {
            throw new InvalidDataException(
                "GPA must be between 0.0 and 4.0", 
                "gpa", 
                gpa
            );
        }
    }
    
    /**
     * Validate grade percentage (0-100)
     */
    public static void validateGrade(double grade) throws InvalidDataException {
        if (grade < 0.0 || grade > 100.0) {
            throw new InvalidDataException(
                "Grade must be between 0 and 100", 
                "grade", 
                grade
            );
        }
    }
    
    /**
     * Validate credits (1-6 typically)
     */
    public static void validateCredits(int credits) throws InvalidDataException {
        if (credits < 1 || credits > 10) {
            throw new InvalidDataException(
                "Credits must be between 1 and 10", 
                "credits", 
                credits
            );
        }
    }
    
    /**
     * Validate semester (1-12 typically)
     */
    public static void validateSemester(int semester) throws InvalidDataException {
        if (semester < 1 || semester > 12) {
            throw new InvalidDataException(
                "Semester must be between 1 and 12", 
                "semester", 
                semester
            );
        }
    }
    
    /**
     * Validate date is not in the future
     */
    public static void validateDateNotFuture(LocalDate date, String fieldName) throws InvalidDataException {
        if (date != null && date.isAfter(LocalDate.now())) {
            throw new InvalidDataException(
                fieldName + " cannot be in the future", 
                fieldName, 
                date
            );
        }
    }
    
    /**
     * Validate age is within reasonable range for students (15-100)
     */
    public static void validateAge(LocalDate dateOfBirth) throws InvalidDataException {
        if (dateOfBirth == null) {
            throw new InvalidDataException("Date of birth cannot be null", "dateOfBirth", null);
        }
        
        int age = LocalDate.now().getYear() - dateOfBirth.getYear();
        if (age < 15 || age > 100) {
            throw new InvalidDataException(
                "Age must be between 15 and 100", 
                "age", 
                age
            );
        }
    }
    
    /**
     * Parse and validate date string
     */
    public static LocalDate parseDate(String dateString, String fieldName) throws InvalidDataException {
        try {
            validateNotEmpty(dateString, fieldName);
            return LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            throw new InvalidDataException(
                "Invalid date format for " + fieldName + ". Expected format: YYYY-MM-DD", 
                fieldName, 
                dateString
            );
        }
    }
    
    /**
     * Parse and validate integer
     */
    public static int parseInt(String value, String fieldName) throws InvalidDataException {
        try {
            validateNotEmpty(value, fieldName);
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new InvalidDataException(
                "Invalid integer value for " + fieldName, 
                fieldName, 
                value
            );
        }
    }
    
    /**
     * Parse and validate double
     */
    public static double parseDouble(String value, String fieldName) throws InvalidDataException {
        try {
            validateNotEmpty(value, fieldName);
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            throw new InvalidDataException(
                "Invalid decimal value for " + fieldName, 
                fieldName, 
                value
            );
        }
    }
    
    /**
     * Validate positive integer
     */
    public static void validatePositiveInteger(int value, String fieldName) throws InvalidDataException {
        if (value <= 0) {
            throw new InvalidDataException(
                fieldName + " must be a positive integer", 
                fieldName, 
                value
            );
        }
    }
    
    /**
     * Validate percentage (0-100)
     */
    public static void validatePercentage(int percentage, String fieldName) throws InvalidDataException {
        if (percentage < 0 || percentage > 100) {
            throw new InvalidDataException(
                fieldName + " must be between 0 and 100", 
                fieldName, 
                percentage
            );
        }
    }
}
