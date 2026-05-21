package com.example.shiftservice.exception;

import com.example.shiftservice.entity.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

/**
 * GlobalExceptionHandler: Centralized exception handler for handling application-specific and global exceptions.
 * <p>
 * This class intercepts exceptions thrown during the execution of the application
 * and returns appropriate error responses in a consistent format.
 * </p>
 * 
 * **Annotations**:
 * - **@ControllerAdvice**: Marks this class as a global exception handler for controllers.
 * - **@Slf4j**: Provides logging capabilities to log exception details.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles EmployeeNotFoundException.
     *
     * @param ex The exception instance.
     * @return ResponseEntity containing error details with HTTP status 404 (Not Found).
     */
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ResultResponse<?>> handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
        log.warn("Employee Not Found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResultResponse.builder()
                        .success(false)
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    /**
     * Handles ShiftNotFoundException.
     *
     * @param ex The exception instance.
     * @return ResponseEntity containing error details with HTTP status 404 (Not Found).
     */
    @ExceptionHandler(ShiftNotFoundException.class)
    public ResponseEntity<ResultResponse<?>> handleShiftNotFoundException(ShiftNotFoundException ex) {
        log.warn("Shift Not Found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResultResponse.builder()
                        .success(false)
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    /**
     * Handles validation errors such as invalid request parameters or payloads.
     *
     * @param ex MethodArgumentNotValidException instance containing validation error details.
     * @return ResponseEntity containing error details with HTTP status 400 (Bad Request).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResultResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        log.warn("Validation Error: {}", ex.getBindingResult().getFieldError().getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResultResponse.builder()
                        .success(false)
                        .message("Validation Error: " + ex.getBindingResult().getFieldError().getDefaultMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    /**
     * Handles all other unhandled exceptions in the application.
     *
     * @param ex The exception instance.
     * @return ResponseEntity containing error details with HTTP status 500 (Internal Server Error).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultResponse<?>> handleGlobalException(Exception ex) {
        log.error("An unexpected error occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResultResponse.builder()
                        .success(false)
                        .message("An unexpected error occurred: " + ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}
