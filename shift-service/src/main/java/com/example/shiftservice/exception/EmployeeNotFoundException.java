package com.example.shiftservice.exception;

/**
 * EmployeeNotFoundException: Custom exception thrown when an employee is not found in the system.
 * <p>
 * This exception is triggered whenever an operation requiring an employee record
 * is unable to locate the employee based on the provided identifier.
 * </p>
 */
public class EmployeeNotFoundException extends RuntimeException {
    
    /**
     * Constructor for EmployeeNotFoundException.
     *
     * @param message Error message describing why the exception occurred.
     */
    public EmployeeNotFoundException(String message) {
        super(message);
    }
}
