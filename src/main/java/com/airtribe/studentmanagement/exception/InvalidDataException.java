package com.airtribe.studentmanagement.exception;

/**
 * Custom exception for invalid data scenarios
 * Demonstrates exception hierarchy and custom error handling
 */
public class InvalidDataException extends Exception {
    
    private String fieldName;
    private Object invalidValue;
    
    public InvalidDataException(String message) {
        super(message);
    }
    
    public InvalidDataException(String message, String fieldName) {
        super(message);
        this.fieldName = fieldName;
    }
    
    public InvalidDataException(String message, String fieldName, Object invalidValue) {
        super(message);
        this.fieldName = fieldName;
        this.invalidValue = invalidValue;
    }
    
    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public String getFieldName() {
        return fieldName;
    }
    
    public Object getInvalidValue() {
        return invalidValue;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("InvalidDataException: " + getMessage());
        if (fieldName != null) {
            sb.append(" [Field: ").append(fieldName).append("]");
        }
        if (invalidValue != null) {
            sb.append(" [Invalid Value: ").append(invalidValue).append("]");
        }
        return sb.toString();
    }
}
