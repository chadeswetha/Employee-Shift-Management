package com.cts.employeeservice.dto;

import com.cts.employeeservice.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * EmployeeResponseDto is a Data Transfer Object used to transfer employee details
 * from the backend to the frontend. It acts as a carrier of data without exposing
 * internal entity details.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDto {

    /**
     * The ID of the employee.
     */
    private String id;

    /**
     * The name of the employee.
     */
    private String employeeName;

    /**
     * The email address of the employee.
     */
    private String email;

    /**
     * The department of the employee.
     */
    private String department;

    /**
     * The role of the employee (e.g., MANAGER, EMPLOYEE).
     */
    private Role role;
    private byte[] image;

    /**
     * The contact number of the employee.
     */
    private String contactNumber;
}
