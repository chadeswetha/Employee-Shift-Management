package com.example.shiftservice.dto;
 
 
import com.example.shiftservice.enums.*;
 
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
 
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
 
/**
* ShiftRequestDTO: Data Transfer Object representing the request payload
* for creating or updating a shift.
* <p>
* This class encapsulates the necessary details for defining a shift, including
* start and end times, employee information, and shift type.
* </p>
* 
* <p>
* **Annotations**:
* - **@Builder**: Enables the builder pattern for creating instances of this class.
* - **@Data**: Generates boilerplate code such as getters, setters, `equals()`, `hashCode()`, and `toString()`.
* </p>
*/
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftRequestDTO {
 
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
     * Date of the shift in LocalDate format.
     */
    private LocalDate shiftDate;
 
    /**
     * The ID of the user assigning the shift.
     */
//    private String assignedBy;
    private String assignedByName; 
    private String department;
}