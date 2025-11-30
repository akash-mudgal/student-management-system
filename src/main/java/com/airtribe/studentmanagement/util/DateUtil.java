package com.airtribe.studentmanagement.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for date operations
 * Demonstrates Java 8 Date/Time API usage
 */
public class DateUtil {
    
    // Common date formatters
    public static final DateTimeFormatter DEFAULT_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public static final DateTimeFormatter DISPLAY_FORMATTER = 
        DateTimeFormatter.ofPattern("MMM dd, yyyy");
    
    public static final DateTimeFormatter LONG_FORMATTER = 
        DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
    
    /**
     * Format date with default formatter
     */
    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DEFAULT_FORMATTER) : "N/A";
    }
    
    /**
     * Format date for display
     */
    public static String formatDateForDisplay(LocalDate date) {
        return date != null ? date.format(DISPLAY_FORMATTER) : "N/A";
    }
    
    /**
     * Format date with long format
     */
    public static String formatDateLong(LocalDate date) {
        return date != null ? date.format(LONG_FORMATTER) : "N/A";
    }
    
    /**
     * Parse date from string with default formatter
     */
    public static LocalDate parseDate(String dateString) throws DateTimeParseException {
        return LocalDate.parse(dateString, DEFAULT_FORMATTER);
    }
    
    /**
     * Calculate age from date of birth
     */
    public static int calculateAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return 0;
        }
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
    
    /**
     * Calculate days between two dates
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
    
    /**
     * Calculate months between two dates
     */
    public static long monthsBetween(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.MONTHS.between(startDate, endDate);
    }
    
    /**
     * Calculate years between two dates
     */
    public static long yearsBetween(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.YEARS.between(startDate, endDate);
    }
    
    /**
     * Check if date is in the past
     */
    public static boolean isPast(LocalDate date) {
        return date != null && date.isBefore(LocalDate.now());
    }
    
    /**
     * Check if date is in the future
     */
    public static boolean isFuture(LocalDate date) {
        return date != null && date.isAfter(LocalDate.now());
    }
    
    /**
     * Check if date is today
     */
    public static boolean isToday(LocalDate date) {
        return date != null && date.equals(LocalDate.now());
    }
    
    /**
     * Get current academic year (e.g., "2024-2025")
     */
    public static String getCurrentAcademicYear() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        // Academic year typically starts in August/September
        if (now.getMonthValue() >= 8) {
            return year + "-" + (year + 1);
        } else {
            return (year - 1) + "-" + year;
        }
    }
    
    /**
     * Get current semester based on current date
     */
    public static String getCurrentSemester() {
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        
        if (month >= 1 && month <= 5) {
            return "Spring";
        } else if (month >= 6 && month <= 7) {
            return "Summer";
        } else {
            return "Fall";
        }
    }
    
    /**
     * Add days to a date
     */
    public static LocalDate addDays(LocalDate date, int days) {
        return date != null ? date.plusDays(days) : null;
    }
    
    /**
     * Add months to a date
     */
    public static LocalDate addMonths(LocalDate date, int months) {
        return date != null ? date.plusMonths(months) : null;
    }
    
    /**
     * Add years to a date
     */
    public static LocalDate addYears(LocalDate date, int years) {
        return date != null ? date.plusYears(years) : null;
    }
    
    /**
     * Get start of current academic year
     */
    public static LocalDate getAcademicYearStart() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        // Academic year starts September 1st
        LocalDate academicStart = LocalDate.of(year, 9, 1);
        
        if (now.isBefore(academicStart)) {
            academicStart = academicStart.minusYears(1);
        }
        
        return academicStart;
    }
    
    /**
     * Get end of current academic year
     */
    public static LocalDate getAcademicYearEnd() {
        return getAcademicYearStart().plusYears(1).minusDays(1);
    }
    
    /**
     * Format duration in human-readable format
     */
    public static String formatDuration(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return "N/A";
        }
        
        Period period = Period.between(startDate, endDate);
        StringBuilder sb = new StringBuilder();
        
        if (period.getYears() > 0) {
            sb.append(period.getYears()).append(" year");
            if (period.getYears() > 1) sb.append("s");
        }
        
        if (period.getMonths() > 0) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(period.getMonths()).append(" month");
            if (period.getMonths() > 1) sb.append("s");
        }
        
        if (period.getDays() > 0) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(period.getDays()).append(" day");
            if (period.getDays() > 1) sb.append("s");
        }
        
        return sb.length() > 0 ? sb.toString() : "Same day";
    }
    
    /**
     * Check if a date is within a range
     */
    public static boolean isDateInRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        if (date == null || startDate == null || endDate == null) {
            return false;
        }
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
}
