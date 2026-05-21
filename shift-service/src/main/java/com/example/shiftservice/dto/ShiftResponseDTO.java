package com.example.shiftservice.dto;
 
 
import com.example.shiftservice.enums.ShiftType;
 
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
 
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
 
/**
* ShiftResponseDTO: Data Transfer Object representing the response payload
* for shift details after creation, updates, or retrieval.
* <p>
* This class encapsulates the details of a shift as returned by the API, including
* shift timings, employee information, and metadata for auditing purposes.
* </p>
* 
* <p>
* **Annotations**:
* - **@Builder**: Provides the builder pattern for easy creation of DTO instances.
* - **@Data**: Generates common boilerplate methods such as getters, setters, `equals()`, `hashCode()`, and `toString()`.
* </p>
*/
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftResponseDTO {
 
    /**
     * Unique identifier for the shift.
     */
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
     */
    private ShiftType shiftType;
 
    /**
     * The unique ID of the employee assigned to the shift.
     */
    private String employeeId;
 
    /**
     * Name of the employee assigned to the shift.
     * Optional field, included only if needed.
     */
    private String employeeName;
 
    /**
     * Date of the shift in LocalDate format.
     */
    private LocalDate shiftDate;
 
    /**
     * The ID of the user who assigned the shift.
     */
//    private String assignedBy;
    private String assignedByName;
 
    /**
     * Timestamp of when the shift was created.
     */
    private LocalDateTime createdAt;
 
    /**
     * Optional: Timestamp of the last update to the shift.
     * Uncomment if needed.
     */
    // private LocalDateTime updatedAt;
    private String department;
}