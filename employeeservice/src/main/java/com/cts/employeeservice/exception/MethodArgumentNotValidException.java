package com.cts.employeeservice.exception;

/**
 * Custom exception to handle method argument validation failures.
 */
public class MethodArgumentNotValidException extends RuntimeException {

    /**
     * Constructor to create an exception with a message.
     *
     * @param message The detail message for this exception.
     */
    public MethodArgumentNotValidException(String message) {
        super(message);
    }
}
