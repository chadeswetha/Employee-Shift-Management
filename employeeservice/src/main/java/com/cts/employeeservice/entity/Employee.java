package com.cts.employeeservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import com.cts.employeeservice.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Employee entity represents the employees in the system.
 * Includes validations for data integrity.
 */
@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    /**
     * The unique identifier for an employee, generated using UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "employee_id", updatable = false, nullable = false, unique = true)
    private String id;

    /**
     * The name of the employee.
     * Must not be null or empty, and has a maximum length of 100 characters.
     */
    @Column(name = "employee_name", nullable = false, length = 100)
    @NotBlank(message = "Employee name cannot be blank")
    @Size(max = 100, message = "Employee name must not exceed 100 characters")
    private String employeeName;

    /**
     * The email address of the employee. This field is unique.
     * Must be in a valid email format.
     */
    @Column(name = "email", nullable = false, unique = true, length = 100)
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    /**
     * The password of the employee (stored as encrypted).
     * Must not be blank and should meet a minimum length requirement.
     */
    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 5, message = "Password must be at least 5 characters long")
    private String password;

    /**
     * The department where the employee works.
     * Must not be null or empty, and has a maximum length of 50 characters.
     */
    @Column(name = "department", nullable = false, length = 50)
    @NotBlank(message = "Department cannot be blank")
    @Size(max = 50, message = "Department must not exceed 50 characters")
    private String department;

    /**
     * The role of the employee, stored as an Enum.
     * Cannot be null.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    @NotNull(message = "Role cannot be null")
    private Role role;

    /**
     * The contact number of the employee.
     * Must not be blank, and must match a valid phone number pattern.
     */
    @Column(name = "contact_number", nullable = false, length = 10)
    @NotBlank(message = "Contact number cannot be blank")
    @Pattern(regexp = "^[0-9]{10}$", message = "Contact number must be exactly 10 digits")
    private String contactNumber;
    @Lob
	@Column(nullable=false, columnDefinition = "longblob")
	private byte[] image;

    /**
     * The timestamp when the record was created, automatically managed.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Generates a UUID for the employee ID before persisting.
     */
    @PrePersist
    public void generateUUID() {
        if (this.id == null || this.id.isEmpty()) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
