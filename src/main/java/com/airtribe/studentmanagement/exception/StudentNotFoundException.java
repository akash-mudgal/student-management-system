package com.airtribe.studentmanagement.exception;

/**
 * Custom exception for student not found scenarios
 * Demonstrates custom exception handling in Java
 */
public class StudentNotFoundException extends Exception {
    
    private String studentId;
    
    public StudentNotFoundException(String message) {
        super(message);
    }
    
    public StudentNotFoundException(String message, String studentId) {
        super(message);
        this.studentId = studentId;
    }
    
    public StudentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    @Override
    public String toString() {
        return "StudentNotFoundException: " + getMessage() + 
               (studentId != null ? " [Student ID: " + studentId + "]" : "");
    }
}
