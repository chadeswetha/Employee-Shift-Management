package com.cts.leavemanagementmodule.exception;
/**
 * LeaveRequestNotFoundException: Custom exception for handling cases where a leave request is not found.
 *
 * Author: K.Ankitha Reddy
 */

public class LeaveRequestNotFoundException extends RuntimeException {
    public LeaveRequestNotFoundException(String message) {
        super(message);
    }
}