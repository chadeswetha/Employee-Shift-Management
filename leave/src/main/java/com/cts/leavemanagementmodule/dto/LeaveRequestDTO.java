package com.cts.leavemanagementmodule.dto;

import jakarta.validation.constraints.FutureOrPresent;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

import com.cts.leavemanagementmodule.enums.LeaveStatus;
/**
 * LeaveRequestDTO: Data Transfer Object for leave requests.
 *
 * Author: K.Ankitha Reddy
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequestDTO {

    private String leaveId;

    //@NotNull(message = "Employee ID cannot be null")
    private String employeeId;

    @Min(value = 1, message = "Manager ID must be greater than 0")
    private String managerId;
    
    @NotNull(message = "Employeename ID cannot be null")
    private String employeeName;

    @NotNull(message = "Start date cannot be null")
    @FutureOrPresent(message = "Start date must be in the present or future")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    @FutureOrPresent(message = "End date must be in the present or future")
    private LocalDate endDate;

    @NotBlank(message = "Reason cannot be blank")
    @Size(max = 255, message = "Reason cannot exceed 255 characters")
    private String reason;
    
    private String department;

    @NotNull(message = "Status cannot be null")
    private LeaveStatus status;
}