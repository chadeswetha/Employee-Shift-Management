
package com.cts.leavemanagementmodule.exception;

/**
 * EmployeeIdNotFoundException: Custom exception for handling cases where an employee ID is not found.
 *
 * Author: K.Ankitha Reddy
 */
public class EmployeeIdNotFoundException extends RuntimeException {
    /**
     * Constructor for EmployeeIdNotFoundException.
     *
     * @param message The exception message.
     */
    public EmployeeIdNotFoundException(String message) {
        super(message);
    }
}
