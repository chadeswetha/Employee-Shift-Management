package com.example.shiftservice.enums;


/**
 * ShiftType: Enum representing the type of shifts available in the Shift Service system.
 * <p>
 * This enum is used to categorize shifts based on the time of day when they occur.
 * It ensures consistency in assigning and managing shifts within the application.
 * </p>
 * 
 * **Enum Values**:
 * - **MORNING**: Represents a morning shift.
 * - **EVENING**: Represents an evening shift.
 * - **NIGHT**: Represents a night shift.
 */
public enum ShiftType {

    /**
     * Morning shift, typically starting in the morning hours.
     */
    MORNING,

    /**
     * Evening shift, typically starting in the late afternoon or evening hours.
     */
    EVENING,

    /**
     * Night shift, typically starting in the night hours.
     */
    NIGHT;
}
