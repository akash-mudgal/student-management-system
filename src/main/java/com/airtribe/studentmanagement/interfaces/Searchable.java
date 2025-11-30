package com.airtribe.studentmanagement.interfaces;

/**
 * Interface for entities that can be searched by different criteria
 * Demonstrates interface usage in Java
 */
public interface Searchable {
    
    /**
     * Search by ID
     * @param id The ID to search for
     * @return true if matches, false otherwise
     */
    boolean matchesId(String id);
    
    /**
     * Search by name
     * @param name The name to search for
     * @return true if matches, false otherwise
     */
    boolean matchesName(String name);
    
    /**
     * Search by any keyword
     * @param keyword The keyword to search for
     * @return true if matches, false otherwise
     */
    boolean matchesKeyword(String keyword);
}
