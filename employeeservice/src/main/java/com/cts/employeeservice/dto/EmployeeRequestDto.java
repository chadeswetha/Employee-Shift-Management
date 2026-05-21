package com.cts.employeeservice.dto;

import org.springframework.web.multipart.MultipartFile;

import com.cts.employeeservice.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * EmployeeRequestDto is a Data Transfer Object used to capture the input
 * details required for creating or updating an employee. It acts as a
 * carrier of data between layers without exposing internal entity details.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequestDto {
	
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
     * The password of the employee.
     */
    private String password;

    /**
     * The department of the employee.
     */
    private String department;

    /**
     * The role of the employee (e.g., MANAGER, EMPLOYEE).
     */
    private Role role;
    
    private MultipartFile image;

    /**
     * The contact number of the employee.
     */
    private String contactNumber;
}
