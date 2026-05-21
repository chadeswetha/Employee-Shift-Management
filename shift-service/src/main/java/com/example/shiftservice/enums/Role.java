package com.example.shiftservice.enums;

/**
 * Role: Enum representing the roles of users in the Shift Service system.
 * <p>
 * This enum is used to define the access levels and responsibilities assigned
 * to different types of users within the system. Each role corresponds to a
 * specific functionality or privilege within the application.
 * </p>
 * 
 * **Enum Values**:
 * - **EMPLOYEE**: Represents a regular employee with standard access privileges.
 * - **MANAGER**: Represents a manager who can oversee and manage shifts.
 * - **ADMIN**: Represents an administrator with full system access and control.
 */
public enum Role {
    /**
     * Represents a regular employee with basic permissions in the system.
     */
    EMPLOYEE,

    /**
     * Represents a manager with permissions to oversee employees and manage shifts.
     */
    MANAGER,

    /**
     * Represents an administrator with full access to the system.
     */
    ADMIN
}