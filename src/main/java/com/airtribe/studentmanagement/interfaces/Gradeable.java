package com.airtribe.studentmanagement.interfaces;

/**
 * Interface for entities that can be graded
 * Demonstrates polymorphism through interfaces
 */
public interface Gradeable {
    
    /**
     * Calculate the grade based on implementation-specific logic
     * @return The calculated grade as a percentage
     */
    double calculateGrade();
    
    /**
     * Get the letter grade (A, B, C, D, F)
     * @return The letter grade
     */
    String getLetterGrade();
    
    /**
     * Check if the entity has passed
     * @return true if passed, false otherwise
     */
    boolean hasPassed();
}
