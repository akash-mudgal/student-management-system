package com.airtribe.studentmanagement.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Abstract base class representing a Person
 * Demonstrates abstraction and encapsulation in OOP
 */
public abstract class Person implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    
    // Default constructor
    public Person() {
    }
    
    // Parameterized constructor
    public Person(String id, String firstName, String lastName, String email, 
                  String phone, LocalDate dateOfBirth) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    /**
     * Calculate age based on date of birth
     * @return Age in years
     */
    public int getAge() {
        if (dateOfBirth == null) {
            return 0;
        }
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }
    
    /**
     * Abstract method to be implemented by subclasses
     * Demonstrates abstraction
     */
    public abstract String getRole();
    
    /**
     * Abstract method for displaying person information
     */
    public abstract void displayInfo();
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("Person{id='%s', name='%s', email='%s', role='%s'}", 
                           id, getFullName(), email, getRole());
    }
}
