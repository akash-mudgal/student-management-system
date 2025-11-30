package com.airtribe.studentmanagement.pattern;

import com.airtribe.studentmanagement.entity.Student;
import com.airtribe.studentmanagement.entity.Enrollment;

/**
 * Observer pattern for grade notification system
 * Demonstrates Observer design pattern (Bonus Feature B)
 * Notifies observers (teachers, students, parents) when grades are updated
 */
public interface GradeObserver {
    /**
     * Called when a grade is updated
     */
    void onGradeUpdated(Student student, Enrollment enrollment, double oldGrade, double newGrade);
}
