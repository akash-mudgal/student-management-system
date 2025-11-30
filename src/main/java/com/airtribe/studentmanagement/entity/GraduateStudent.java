package com.airtribe.studentmanagement.entity;

import java.time.LocalDate;

/**
 * Class representing a Graduate Student, extends Student
 * Demonstrates multi-level inheritance
 */
public class GraduateStudent extends Student {
    
    private String thesisTitle;
    private String advisor;
    private String researchArea;
    private boolean isThesisCompleted;
    private LocalDate expectedGraduationDate;
    
    // Default constructor
    public GraduateStudent() {
        super();
        this.isThesisCompleted = false;
    }
    
    // Parameterized constructor with chaining
    public GraduateStudent(String id, String firstName, String lastName, String email, 
                          String phone, LocalDate dateOfBirth, String studentId, 
                          String program, int semester, String thesisTitle, 
                          String advisor, String researchArea) {
        super(id, firstName, lastName, email, phone, dateOfBirth, studentId, program, semester);
        this.thesisTitle = thesisTitle;
        this.advisor = advisor;
        this.researchArea = researchArea;
        this.isThesisCompleted = false;
        this.expectedGraduationDate = LocalDate.now().plusYears(2);
    }
    
    // Getters and Setters
    public String getThesisTitle() {
        return thesisTitle;
    }
    
    public void setThesisTitle(String thesisTitle) {
        this.thesisTitle = thesisTitle;
    }
    
    public String getAdvisor() {
        return advisor;
    }
    
    public void setAdvisor(String advisor) {
        this.advisor = advisor;
    }
    
    public String getResearchArea() {
        return researchArea;
    }
    
    public void setResearchArea(String researchArea) {
        this.researchArea = researchArea;
    }
    
    public boolean isThesisCompleted() {
        return isThesisCompleted;
    }
    
    public void setThesisCompleted(boolean thesisCompleted) {
        isThesisCompleted = thesisCompleted;
    }
    
    public LocalDate getExpectedGraduationDate() {
        return expectedGraduationDate;
    }
    
    public void setExpectedGraduationDate(LocalDate expectedGraduationDate) {
        this.expectedGraduationDate = expectedGraduationDate;
    }
    
    /**
     * Method overriding: Graduate students have different role
     */
    @Override
    public String getRole() {
        return "GRADUATE_STUDENT";
    }
    
    /**
     * Check if graduate student can graduate
     * @return true if all requirements are met
     */
    public boolean canGraduate() {
        return isThesisCompleted && getGpa() >= 3.0 && getSemester() >= 4;
    }
    
    /**
     * Method overriding: Graduate students need higher GPA for good standing
     */
    @Override
    public boolean isInGoodStanding() {
        return getGpa() >= 3.0;
    }
    
    /**
     * Method overloading: Display info with research details
     */
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("=== Graduate Student Details ===");
        System.out.println("Research Area: " + researchArea);
        System.out.println("Advisor: " + advisor);
        System.out.println("Thesis Title: " + (thesisTitle != null ? thesisTitle : "Not set"));
        System.out.println("Thesis Status: " + (isThesisCompleted ? "Completed" : "In Progress"));
        System.out.println("Expected Graduation: " + expectedGraduationDate);
        System.out.println("Can Graduate: " + (canGraduate() ? "Yes" : "No"));
    }
    
    /**
     * Display brief research information
     */
    public void displayResearchInfo() {
        System.out.println("=== Research Information ===");
        System.out.println("Student: " + getFullName());
        System.out.println("Research Area: " + researchArea);
        System.out.println("Thesis: " + (thesisTitle != null ? thesisTitle : "Not set"));
        System.out.println("Advisor: " + advisor);
    }
    
    @Override
    public String toString() {
        return String.format("GraduateStudent{studentId='%s', name='%s', program='%s', " +
                           "researchArea='%s', thesis='%s'}", 
                           getStudentId(), getFullName(), getProgram(), 
                           researchArea, thesisTitle != null ? thesisTitle : "Not set");
    }
}
