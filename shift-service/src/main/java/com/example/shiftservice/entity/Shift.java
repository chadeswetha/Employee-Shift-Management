package com.example.shiftservice.entity;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.example.shiftservice.enums.ShiftType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Shift: Entity class representing the Shift data model.
 * <p>
 * This class is mapped to the database table for storing shift-related information.
 * It includes details such as start and end times, shift type, assigned employee,
 * and metadata for tracking creation timestamps and other properties.
 * </p>
 *
 * <p>
 * **Annotations**:
 * - **@Entity**: Marks this class as a JPA entity.
 * - **@Id**: Specifies the primary key of the entity.
 * - **@Builder**: Enables the builder pattern for constructing instances of this class.
 * - **@Data**: Generates common boilerplate methods like getters, setters, `equals()`, and `toString()`.
 * - **@NoArgsConstructor / @AllArgsConstructor**: Generates constructors for the entity.
 * - **@CreationTimestamp**: Automatically sets the creation timestamp for the entity.
 * - **@Enumerated**: Specifies how enums are persisted in the database.
 * - **@PrePersist**: Lifecycle event to generate a UUID for the entity before saving.
 * </p>
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shift {

    /**
     * Unique identifier for the shift.
     */
    @Id
    private String shiftId;

    /**
     * Start time of the shift in LocalDateTime format.
     */
    private LocalDateTime startTime;

    /**
     * End time of the shift in LocalDateTime format.
     */
    private LocalDateTime endTime;

    /**
     * Type of the shift (e.g., MORNING, EVENING, NIGHT).
     * Enum is stored as a string in the database.
     */
    @Enumerated(EnumType.STRING)
    private ShiftType shiftType;

    /**
     * The unique ID of the employee assigned to the shift.
     */
    private String employeeId;

    /**
     * Date of the shift in LocalDate format.
     */
    private LocalDate shiftDate;

    /**
     * ID of the user who assigned the shift.
     */
    private String assignedBy;

    /**
     * Name of the employee assigned to the shift.
     */
    private String employeeName;

    /**
     * Timestamp indicating when the shift was created.
     * Automatically managed by Hibernate.
     */
    private String department;
    @CreationTimestamp
    private LocalDateTime createdAt;

    /**
     * Lifecycle event triggered before the entity is persisted.
     * Generates a UUID for the shift ID.
     */
    @PrePersist
    public void inset() {
        String uuid = UUID.randomUUID().toString();
        shiftId = uuid;
    }

    /**
     * Uncomment if needed: Lifecycle event triggered before the entity is updated.
     * Sets the updated timestamp for the entity.
     */
    // @PreUpdate
    // public void preUpdate() {
    //     this.updatedAt = LocalDateTime.now();
    // }
    
}


