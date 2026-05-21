package com.cts.leavemanagementmodule.service;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import com.cts.leavemanagementmodule.dto.EmployeeDTO;
import com.cts.leavemanagementmodule.dto.LeaveRequestDTO;
import com.cts.leavemanagementmodule.dto.NotificationRequest;
import com.cts.leavemanagementmodule.dto.NotificationRequest.NotificationType;
import com.cts.leavemanagementmodule.enums.LeaveStatus;
import com.cts.leavemanagementmodule.exception.EmployeeIdNotFoundException;
import com.cts.leavemanagementmodule.exception.LeaveRequestNotFoundException;
import com.cts.leavemanagementmodule.interfaces.EmployeeServiceClient;
import com.cts.leavemanagementmodule.interfaces.NotificationClient;
import com.cts.leavemanagementmodule.model.LeaveRequest;
import com.cts.leavemanagementmodule.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;


/**
 * LeaveRequestService: Service class for managing leave requests.
 *
 * Author: K.Ankitha Reddy
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class LeaveRequestService {

    private final EmployeeServiceClient employeeServiceClient; // Unified Feign client
    private final LeaveRequestRepository leaveRequestRepository;
    private final NotificationClient notificationClient;

    /**
     * Create a new leave request.
     *
     * @param leaveRequestDTO The leave request DTO containing input data.
     * @return The created leave request DTO after saving.
     */
    @Transactional
    public LeaveRequestDTO createLeaveRequest(LeaveRequestDTO leaveRequestDTO) {
        log.info("Creating leave request for employeeId: {}", leaveRequestDTO.getEmployeeId());
 
        // Check if a leave request already exists for the same employee on the same date
        boolean alreadyRequested = leaveRequestRepository.existsByEmployeeIdAndStartDate(
                leaveRequestDTO.getEmployeeId(), leaveRequestDTO.getStartDate());
 
        if (alreadyRequested) {
            throw new RuntimeException("Leave request already exists for employee " +
                    leaveRequestDTO.getEmployeeId() + " on " + leaveRequestDTO.getStartDate());
        }
 
        // Step 1: Validate and retrieve Employee details
        EmployeeDTO employee = employeeServiceClient.getEmployeeById(leaveRequestDTO.getEmployeeId());
        if (employee == null) {
            throw new RuntimeException("Employee not found with ID: " + leaveRequestDTO.getEmployeeId());
        }
 
        // Step 2: Validate department provided by the user or fallback to employee's department
        String department = leaveRequestDTO.getDepartment();
        if (department == null || department.isEmpty()) {
            department = employee.getDepartment(); // Fallback to the department from Employee Service
            if (department == null || department.isEmpty()) {
                throw new RuntimeException("Department is required to fetch manager details.");
            }
        }
        log.info("Using department: {}", department);
 
        // Step 3: Fetch Manager ID for the department
        String managerId = employeeServiceClient.getManagerIdByRoleAndDepartment("MANAGER", department);
        if (managerId == null || managerId.isEmpty()) {
            throw new RuntimeException("Manager not found for department: " + department);
        }
 
        // Step 4: Retrieve employee name and enrich the leave request DTO
        String employeeName = employeeServiceClient.getEmployeeNameById(leaveRequestDTO.getEmployeeId());
        if (employeeName == null || employeeName.isEmpty()) {
            throw new RuntimeException("Employee name could not be retrieved for ID: " + leaveRequestDTO.getEmployeeId());
        }
        leaveRequestDTO.setEmployeeName(employeeName);
        leaveRequestDTO.setManagerId(managerId);
        leaveRequestDTO.setDepartment(department);
 
        log.info("Enriched leave request: {}", leaveRequestDTO);
 
        // Step 5: Save the leave request to the database
        LeaveRequest leaveRequest = mapToEntity(leaveRequestDTO);
        LeaveRequest savedLeaveRequest = leaveRequestRepository.save(leaveRequest);
 
        log.info("Leave request successfully saved with ID: {}", savedLeaveRequest.getLeaveId());
 
        // Notification
        notificationClient.sendNotification(NotificationRequest.builder()
                .recipientId(leaveRequestDTO.getManagerId())
                .title("Leave Request")
                .type(NotificationType.LEAVE_REQUEST)
                .message("Employee " + employeeName + " has applied for leave.")
                .build());
 
        // Step 6: Return the mapped DTO
        return mapToDTO(savedLeaveRequest);
    }

    // Other service methods (getAllLeaveRequests, acceptLeaveRequest, rejectLeaveRequest, etc.) remain unchanged.

    // Utility method to map Entity to DTO
    
    @Transactional
    public String getEmployeeName(String employeeId) {
        EmployeeDTO employee = employeeServiceClient.getEmployeeById(employeeId);
        if (employee == null) {
            throw new RuntimeException("Employee not found for ID: " + employeeId);
        }
        return employee.getEmployeeName(); // Extract and return the employeeName
    }
   
    



    /**
     * Retrieve all leave requests.
     *
     * @return A list of all leave requests.
     */
    public List<LeaveRequestDTO> getAllLeaveRequests() {
        log.info("Fetching all leave requests");

        List<LeaveRequestDTO> leaveRequests = leaveRequestRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        log.info("Retrieved {} leave requests", leaveRequests.size());
        return leaveRequests;
    }

    /**
     * Accept a pending leave request.
     *
     * @param employeeId The employee ID for the leave request.
     * @param reason     The reason for accepting the leave.
     * @return The updated leave request DTO.
     */
    @Transactional
    public LeaveRequestDTO acceptLeaveRequestByEmployee(String employeeId, String reason) {
    	
            log.info("Rejecting leave request for employeeId: {}", employeeId);

            // Step 1: Fetch the pending leave request
            LeaveRequest leaveRequest = fetchPendingLeaveRequest(employeeId);
            if (leaveRequest == null) {
                throw new RuntimeException("No pending leave request found for employeeId: " + employeeId);
            }

            // Step 2: Update leave request status and reason
            leaveRequest.setStatus(LeaveStatus.APPROVED);
            if (reason != null && !reason.isEmpty()) {
                leaveRequest.setReason(reason);
            }

            // Step 3: Save the updated leave request
            try {
                LeaveRequest savedLeaveRequest = leaveRequestRepository.save(leaveRequest);
                log.info("Leave request rejected successfully: {}", savedLeaveRequest);
                notificationClient.sendNotification(NotificationRequest.builder()
                        .recipientId(employeeId)
                        .title("Leave Accepted ")
                        .type(NotificationType.LEAVE_RESPONSE)
                        .message("Your leave request has been Accepted" +  ".!!!!")
                        .build());
                return mapToDTO(savedLeaveRequest);
            } catch (Exception e) {
                log.error("Failed to save leave request: {}", e.getMessage(), e);
                throw new RuntimeException("Unable to reject leave request. Please try again.");
            }
        }
    /**
     * Reject a pending leave request.
     *
     * @param employeeId The employee ID for the leave request.
     * @param reason     The reason for rejecting the leave.
     * @return The updated leave request DTO.
     */
    @Transactional
    public LeaveRequestDTO rejectLeaveRequestByEmployee(String employeeId, String reason) {
        log.info("Rejecting leave request for employeeId: {}", employeeId);

        // Step 1: Fetch the pending leave request
        LeaveRequest leaveRequest = fetchPendingLeaveRequest(employeeId);
        if (leaveRequest == null) {
            throw new RuntimeException("No pending leave request found for employeeId: " + employeeId);
        }

        // Step 2: Update leave request status and reason
        leaveRequest.setStatus(LeaveStatus.REJECTED);
        if (reason != null && !reason.isEmpty()) {
            leaveRequest.setReason(reason);
        }

        // Step 3: Save the updated leave request
        try {
            LeaveRequest savedLeaveRequest = leaveRequestRepository.save(leaveRequest);
            log.info("Leave request rejected successfully: {}", savedLeaveRequest);
            notificationClient.sendNotification(NotificationRequest.builder()
                    .recipientId(employeeId)
                    .title("Leave Rejected ")
                    .type(NotificationType.LEAVE_RESPONSE)
                    .message("Your leave request has been rejected" +  ".!!!!")
                    .build());
            return mapToDTO(savedLeaveRequest);
        } catch (Exception e) {
            log.error("Failed to save leave request: {}", e.getMessage(), e);
            throw new RuntimeException("Unable to reject leave request. Please try again.");
        }
    }
    /**
     * Fetch leave history for a specific employee ID.
     *
     * @param employeeId The employee ID.
     * @return A list of leave requests for the specified employee.
     */
    public List<LeaveRequestDTO> getLeaveHistoryByEmployeeId(String employeeId) {
        log.info("Fetching leave history for employeeId: {}", employeeId);

        List<LeaveRequestDTO> leaveHistory = leaveRequestRepository.findLeaveRequestsByEmployeeId(employeeId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        log.info("Found {} leave requests for employeeId: {}", leaveHistory.size(), employeeId);
        return leaveHistory;
    }

    // Utility method to fetch pending leave request
    private LeaveRequest fetchPendingLeaveRequest(String employeeId) {
        return leaveRequestRepository.findLeaveRequestsByEmployeeId(employeeId)
                .stream()
                .filter(request -> request.getStatus() == LeaveStatus.PENDING)
                .findFirst()
                .orElseThrow(() -> new LeaveRequestNotFoundException("No pending leave request found for employee ID: " + employeeId));
    }
    public List<LeaveRequestDTO> getPendingLeaveRequestsByManagerId(String managerId) {
        log.info("Fetching pending leave requests for managerId: {}", managerId);
        List<LeaveRequest> pendingRequests = leaveRequestRepository.findPendingLeaveRequestsByManagerId(managerId);

        // Convert entities to DTOs before returning
        return pendingRequests.stream()
                .map(this::mapToDTO)
                .toList();
    }
    
    

    // Utility method to map Entity to DTO
    private LeaveRequestDTO mapToDTO(LeaveRequest leaveRequest) {
        return LeaveRequestDTO.builder()
                .leaveId(leaveRequest.getLeaveId())
                .employeeId(leaveRequest.getEmployeeId())
                .employeeName(leaveRequest.getEmployeeName())
                .managerId(leaveRequest.getManagerId())
                .department(leaveRequest.getDepartment())
                .startDate(leaveRequest.getStartDate())
                .endDate(leaveRequest.getEndDate())
                .reason(leaveRequest.getReason())
                .status(leaveRequest.getStatus())
                .build();
    }

    
    
    private LeaveRequest mapToEntity(LeaveRequestDTO dto) {
        return LeaveRequest.builder()
            .leaveId(dto.getLeaveId())
            .employeeId(dto.getEmployeeId())
            .employeeName(dto.getEmployeeName())  // Ensure this mapping exists
            .department(dto.getDepartment())      // Ensure this mapping exists
            .managerId(dto.getManagerId())
            .startDate(dto.getStartDate())
            .endDate(dto.getEndDate())
            .reason(dto.getReason())
            .status(dto.getStatus())
            .build();
    }
}
