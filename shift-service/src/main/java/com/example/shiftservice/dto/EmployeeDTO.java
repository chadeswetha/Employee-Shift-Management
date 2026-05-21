package com.example.shiftservice.dto;





import java.time.LocalDateTime;

import java.util.UUID;

import com.example.shiftservice.enums.Role;

//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Pattern;
//import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
/**
 * EmployeeDTO: Data Transfer Object representing an employee.
 * <p>
 * This class encapsulates employee details to be used across the application
 * for various operations such as shift assignment, retrieval, and updates.
 * </p>
 * 
 * <p>
 * **Annotations**:
 * - **@Builder**: Enables the builder pattern for creating instances of this class.
 * - **@Data**: Generates boilerplate code including getters, setters, `equals()`, `hashCode()`, and `toString()`.
 * </p>
 */
@Builder
@Data
public class EmployeeDTO {
	/**
     * Unique identifier of the employee.
     */
    private String id;
    /**
     * Full name of the employee.
     */


    private String employeeName;

    /**
     * Department to which the employee belongs.
     */
    private String department;

    /**
     * Contact number of the employee.
     */
    private String contactNumber;

    /**
     * Email address of the employee.
     */
    private String email;

    /**
     * Role of the employee (e.g., MANAGER, STAFF).
     */
    private Role role;
    /**
     * Timestamp of when the employee record was created.
     */

	private LocalDateTime createdAt;

    
}
