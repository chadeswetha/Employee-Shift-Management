package com.example.shiftservice.exception;

/**
 * ShiftNotFoundException: Custom exception thrown when a shift is not found in the system.
 * <p>
 * This exception is triggered whenever an operation requiring a shift record
 * is unable to locate the shift based on the provided identifier.
 * </p>
 */
public class ShiftNotFoundException extends RuntimeException {

    /**
     * Constructor for ShiftNotFoundException.
     *
     * @param message Error message describing why the exception occurred.
     */
    public ShiftNotFoundException(String message) {
        super(message);
    }
}
