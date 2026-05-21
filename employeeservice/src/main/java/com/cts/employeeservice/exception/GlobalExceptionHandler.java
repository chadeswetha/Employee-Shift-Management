package com.cts.employeeservice.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cts.employeeservice.entity.ResultResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles ResourceNotFoundException.
     *
     * @param ex The exception thrown when a resource is not found.
     * @return ResponseEntity containing the error message and HTTP status.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles general exceptions.
     *
     * @param ex The exception thrown for generic errors.
     * @return ResponseEntity containing the error message and HTTP status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultResponse<Void>> handleGeneralException(Exception ex) {
    	ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultResponse.<Void>builder()
                .success(false)
                .message("An unexpected error occurred: " + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build());
    }

    /**
     * Handles validation errors for method arguments.
     *
     * @param ex Spring's built-in MethodArgumentNotValidException.
     * @return ResponseEntity containing validation error messages and HTTP status.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResultResponse<Void>> handleValidationErrors(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return ResponseEntity.badRequest().body(ResultResponse.<Void>builder()
                .success(false)
                .message("Validation failed: " + errorMessage)
                .timestamp(LocalDateTime.now())
                .build());
    }
}
