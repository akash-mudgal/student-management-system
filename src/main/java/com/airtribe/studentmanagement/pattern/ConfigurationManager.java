package com.airtribe.studentmanagement.pattern;

import java.io.*;
import java.util.Properties;

/**
 * Singleton pattern for configuration management
 * Demonstrates Singleton design pattern (Bonus Feature B)
 * Ensures only one instance of configuration exists throughout the application
 */
public class ConfigurationManager {
    
    // Private static instance (lazy initialization)
    private static ConfigurationManager instance;
    
    // Configuration properties
    private Properties properties;
    private String dataDirectory;
    private int maxStudentsPerCourse;
    private double passingGrade;
    private String applicationName;
    
    // Private constructor to prevent instantiation
    private ConfigurationManager() {
        properties = new Properties();
        loadDefaultConfiguration();
    }
    
    /**
     * Get singleton instance (thread-safe using synchronized)
     * Demonstrates lazy initialization with thread safety
     */
    public static synchronized ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }
    
    /**
     * Load default configuration
     */
    private void loadDefaultConfiguration() {
        dataDirectory = "data";
        maxStudentsPerCourse = 30;
        passingGrade = 60.0;
        applicationName = "Student Management System";
        
        properties.setProperty("data.directory", dataDirectory);
        properties.setProperty("course.max.students", String.valueOf(maxStudentsPerCourse));
        properties.setProperty("grade.passing", String.valueOf(passingGrade));
        properties.setProperty("app.name", applicationName);
    }
    
    /**
     * Load configuration from file
     */
    public void loadConfiguration(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                properties.load(fis);
                updateFieldsFromProperties();
                System.out.println("Configuration loaded from: " + filename);
            } catch (IOException e) {
                System.err.println("Error loading configuration: " + e.getMessage());
                loadDefaultConfiguration();
            }
        } else {
            System.out.println("Configuration file not found. Using defaults.");
        }
    }
    
    /**
     * Save configuration to file
     */
    public void saveConfiguration(String filename) {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            properties.store(fos, "Student Management System Configuration");
            System.out.println("Configuration saved to: " + filename);
        } catch (IOException e) {
            System.err.println("Error saving configuration: " + e.getMessage());
        }
    }
    
    /**
     * Update fields from properties
     */
    private void updateFieldsFromProperties() {
        dataDirectory = properties.getProperty("data.directory", "data");
        maxStudentsPerCourse = Integer.parseInt(
            properties.getProperty("course.max.students", "30"));
        passingGrade = Double.parseDouble(
            properties.getProperty("grade.passing", "60.0"));
        applicationName = properties.getProperty("app.name", "Student Management System");
    }
    
    // Getters
    public String getDataDirectory() {
        return dataDirectory;
    }
    
    public int getMaxStudentsPerCourse() {
        return maxStudentsPerCourse;
    }
    
    public double getPassingGrade() {
        return passingGrade;
    }
    
    public String getApplicationName() {
        return applicationName;
    }
    
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    // Setters
    public void setDataDirectory(String dataDirectory) {
        this.dataDirectory = dataDirectory;
        properties.setProperty("data.directory", dataDirectory);
    }
    
    public void setMaxStudentsPerCourse(int maxStudentsPerCourse) {
        this.maxStudentsPerCourse = maxStudentsPerCourse;
        properties.setProperty("course.max.students", String.valueOf(maxStudentsPerCourse));
    }
    
    public void setPassingGrade(double passingGrade) {
        this.passingGrade = passingGrade;
        properties.setProperty("grade.passing", String.valueOf(passingGrade));
    }
    
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
        properties.setProperty("app.name", applicationName);
    }
    
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
    
    /**
     * Reset to default configuration
     */
    public void resetToDefaults() {
        properties.clear();
        loadDefaultConfiguration();
        System.out.println("Configuration reset to defaults");
    }
    
    /**
     * Display current configuration
     */
    public void displayConfiguration() {
        System.out.println("=== Current Configuration ===");
        System.out.println("Application Name: " + applicationName);
        System.out.println("Data Directory: " + dataDirectory);
        System.out.println("Max Students Per Course: " + maxStudentsPerCourse);
        System.out.println("Passing Grade: " + passingGrade);
        System.out.println("============================");
    }
    
    // Prevent cloning
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cannot clone singleton instance");
    }
}
