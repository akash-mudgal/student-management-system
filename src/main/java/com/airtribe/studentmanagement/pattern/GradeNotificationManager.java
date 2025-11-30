package com.airtribe.studentmanagement.pattern;

import com.airtribe.studentmanagement.entity.Student;
import com.airtribe.studentmanagement.entity.Enrollment;

/**
 * Manager class for the grade notification system
 * Singleton pattern combined with Observer pattern (Bonus Feature B)
 */
public class GradeNotificationManager {
    
    private static GradeNotificationManager instance;
    private GradeNotificationSubject subject;
    
    private GradeNotificationManager() {
        subject = new GradeNotificationSubject();
        // Initialize with default observers
        subject.addObserver(new EmailNotificationObserver());
        subject.addObserver(new SMSNotificationObserver());
        subject.addObserver(new AdminLogObserver());
        subject.addObserver(new ParentNotificationObserver());
    }
    
    /**
     * Get singleton instance
     */
    public static synchronized GradeNotificationManager getInstance() {
        if (instance == null) {
            instance = new GradeNotificationManager();
        }
        return instance;
    }
    
    /**
     * Update grade and notify all observers
     */
    public void updateGrade(Student student, Enrollment enrollment, double newGrade) {
        double oldGrade = enrollment.getGrade() != null ? enrollment.getGrade() : 0.0;
        
        // Update the grade
        enrollment.setGrade(newGrade);
        
        // Notify all observers
        subject.notifyObservers(student, enrollment, oldGrade, newGrade);
        
        // Update student's GPA
        student.updateGPA();
    }
    
    /**
     * Add an observer
     */
    public void addObserver(GradeObserver observer) {
        subject.addObserver(observer);
    }
    
    /**
     * Remove an observer
     */
    public void removeObserver(GradeObserver observer) {
        subject.removeObserver(observer);
    }
}

/**
 * Subject class that maintains list of observers
 */
class GradeNotificationSubject {
    private java.util.List<GradeObserver> observers = new java.util.ArrayList<>();
    
    public void addObserver(GradeObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    public void removeObserver(GradeObserver observer) {
        observers.remove(observer);
    }
    
    public void notifyObservers(Student student, Enrollment enrollment, 
                               double oldGrade, double newGrade) {
        for (GradeObserver observer : observers) {
            observer.onGradeUpdated(student, enrollment, oldGrade, newGrade);
        }
    }
}

/**
 * Concrete observer: Email notification
 */
class EmailNotificationObserver implements GradeObserver {
    
    @Override
    public void onGradeUpdated(Student student, Enrollment enrollment, 
                              double oldGrade, double newGrade) {
        System.out.println("\n[EMAIL NOTIFICATION]");
        System.out.println("To: " + student.getEmail());
        System.out.println("Subject: Grade Update for " + enrollment.getCourse().getCourseName());
        System.out.println("Dear " + student.getFullName() + ",");
        System.out.println("Your grade has been updated:");
        System.out.println("Course: " + enrollment.getCourse().getCourseCode());
        
        if (oldGrade > 0) {
            System.out.println("Previous Grade: " + String.format("%.2f", oldGrade) + "%");
        }
        System.out.println("New Grade: " + String.format("%.2f", newGrade) + "% (" + 
                          enrollment.getLetterGrade() + ")");
        System.out.println("Status: " + (enrollment.hasPassed() ? "PASSED" : "FAILED"));
        System.out.println("---");
    }
}

/**
 * Concrete observer: SMS notification
 */
class SMSNotificationObserver implements GradeObserver {
    
    @Override
    public void onGradeUpdated(Student student, Enrollment enrollment, 
                              double oldGrade, double newGrade) {
        System.out.println("\n[SMS NOTIFICATION]");
        System.out.println("To: " + student.getPhone());
        System.out.println("Grade updated for " + enrollment.getCourse().getCourseCode() + 
                          ": " + String.format("%.2f", newGrade) + "% (" + 
                          enrollment.getLetterGrade() + ")");
    }
}

/**
 * Concrete observer: Admin log observer
 */
class AdminLogObserver implements GradeObserver {
    
    @Override
    public void onGradeUpdated(Student student, Enrollment enrollment, 
                              double oldGrade, double newGrade) {
        System.out.println("\n[ADMIN LOG]");
        System.out.println("Grade Change Recorded:");
        System.out.println("Student ID: " + student.getStudentId());
        System.out.println("Student Name: " + student.getFullName());
        System.out.println("Course: " + enrollment.getCourse().getCourseCode());
        System.out.println("Old Grade: " + (oldGrade > 0 ? String.format("%.2f", oldGrade) : "N/A"));
        System.out.println("New Grade: " + String.format("%.2f", newGrade));
        System.out.println("Timestamp: " + java.time.LocalDateTime.now());
        
        // Check for significant grade changes
        if (oldGrade > 0) {
            double change = newGrade - oldGrade;
            if (Math.abs(change) > 20) {
                System.out.println("WARNING: Significant grade change detected: " + 
                                 String.format("%.2f", change) + " points");
            }
        }
    }
}

/**
 * Concrete observer: Parent notification
 */
class ParentNotificationObserver implements GradeObserver {
    
    @Override
    public void onGradeUpdated(Student student, Enrollment enrollment, 
                              double oldGrade, double newGrade) {
        // Only notify parents for undergraduate students or failing grades
        if (newGrade < 60 || student.getGpa() < 2.0) {
            System.out.println("\n[PARENT NOTIFICATION]");
            System.out.println("Dear Parent/Guardian,");
            System.out.println("This is to inform you about " + student.getFullName() + 
                             "'s grade update:");
            System.out.println("Course: " + enrollment.getCourse().getCourseName());
            System.out.println("Grade: " + String.format("%.2f", newGrade) + "% (" + 
                             enrollment.getLetterGrade() + ")");
            
            if (!enrollment.hasPassed()) {
                System.out.println("ATTENTION: Student has not passed this course.");
                System.out.println("We recommend scheduling a meeting with the academic advisor.");
            }
        }
    }
}
