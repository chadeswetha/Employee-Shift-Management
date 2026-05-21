package com.cts.leavemanagementmodule.model;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

import com.cts.leavemanagementmodule.enums.LeaveStatus;
/**
 * Entity class representing a leave request.
 * 
 * This class is used to store information about leave requests made by employees.
 * It includes details such as employee ID, manager ID, start and end dates, reason for leave, and status.
 * 
 * Author: K.Ankitha Reddy
 */

@Entity
@Table(name = "leave_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequest {

    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "leave_id")
    private String leaveId;

    @Column(name = "employee_id")
    private String employeeId;

    @Column(name = "employee_name")
    private String employeeName;
    
    @Column(name = "manager_id")
    private String managerId;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "reason")
    private String reason;
    
    @Column(name = "department")
    private String department;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LeaveStatus status;
    /**
     * Generates UUIDs for employeeId and managerId if they are not already set.
     */
    @PrePersist
    public void generateUUIDs() {
        if (this.leaveId == null) {
            this.leaveId = UUID.randomUUID().toString();
        }
        
    }

	
}