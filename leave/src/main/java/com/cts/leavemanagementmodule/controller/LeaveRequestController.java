package com.cts.leavemanagementmodule.controller;

import com.cts.leavemanagementmodule.dto.LeaveRequestDTO;

import com.cts.leavemanagementmodule.exception.EmployeeIdNotFoundException;
import com.cts.leavemanagementmodule.exception.LeaveRequestNotFoundException;
import com.cts.leavemanagementmodule.exception.UserNotFoundException;
import com.cts.leavemanagementmodule.model.ResultResponse;
import com.cts.leavemanagementmodule.service.LeaveRequestService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
/**
 * LeaveRequestController: REST controller for managing leave requests.
 *
 * Author: K.Ankitha Reddy
 */

@RestController
@RequestMapping("/api/leaves")
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class LeaveRequestController {
    
	@Autowired
    private final LeaveRequestService leaveRequestService;

       public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }
       /**
        * Create a new leave request.
        *
        * @param leaveRequestDTO The DTO containing leave request details.
        * @return ResponseEntity with the result of the creation operation.
        */
      

@PostMapping
public ResponseEntity <?> createLeaveRequest(@RequestBody LeaveRequestDTO leaveRequestDTO) {
try {
// Date validation
LocalDate currentDate = LocalDate.now();
if (leaveRequestDTO.getStartDate().isBefore(currentDate) || leaveRequestDTO.getEndDate().isBefore(currentDate)) {
return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start and End dates must be in the present or future.");
}

LeaveRequestDTO createdLeaveRequest = leaveRequestService.createLeaveRequest(leaveRequestDTO);
return ResponseEntity.status(HttpStatus.CREATED).body(createdLeaveRequest);
} catch (RuntimeException e) {
log.error("Validation error: {}", e.getMessage(), e);
return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
} catch (Exception e) {
log.error("Internal server error: {}", e.getMessage(), e);
return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
}
}
       
    /**
     * Get all leave requests.
     *
     * @return ResponseEntity with the list of all leave requests.
     */

    @GetMapping
    public ResponseEntity<ResultResponse> getAllLeaveRequests() {
        log.info("Fetching all leave requests");
        List<LeaveRequestDTO> leaveRequests = leaveRequestService.getAllLeaveRequests();
        return ResponseEntity.ok(new ResultResponse(
                true,
                "Leave requests retrieved successfully",
                leaveRequests,
                LocalDateTime.now()
        ));
    }
    /**
     * Accept a pending leave request by employee ID.
     *
     * @param employeeId The ID of the employee.
     * @param leaveRequestDTO The DTO containing leave request details (optional).
     * @return ResponseEntity with the result of the acceptance operation.
     */

    @PutMapping("/{employeeId}/accept")
    public ResponseEntity<ResultResponse> acceptLeaveRequest(
            @PathVariable String employeeId,
            @RequestBody(required = false) LeaveRequestDTO leaveRequestDTO
    ) {
        log.info("Accepting leave request for employee ID: {} with data: {}", employeeId, leaveRequestDTO);
        try {
            String reason = leaveRequestDTO != null ? leaveRequestDTO.getReason() : null;
            log.info("Reason received: {}", reason);
            LeaveRequestDTO updatedRequest = leaveRequestService.acceptLeaveRequestByEmployee(employeeId, reason);
            return ResponseEntity.ok(new ResultResponse(
                    true,
                    "Leave request accepted successfully",
                    updatedRequest,
                    LocalDateTime.now()
            ));
        } catch (LeaveRequestNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResultResponse(
                    false,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            ));
        }
    }
    /**
     * Reject a pending leave request by employee ID.
     *
     * @param employeeId The ID of the employee.
     * @param leaveRequestDTO The DTO containing leave request details (optional).
     * @return ResponseEntity with the result of the rejection operation.
     */

    @PutMapping("/{employeeId}/reject")
    public ResponseEntity<LeaveRequestDTO> rejectLeaveRequest(
            @PathVariable("employeeId") String employeeId,
            @RequestParam(value = "reason", required = false) String reason) {
        try {
            // Call the service method to reject the leave request
            LeaveRequestDTO rejectedLeaveRequest = leaveRequestService.rejectLeaveRequestByEmployee(employeeId, reason);
            return ResponseEntity.status(HttpStatus.OK).body(rejectedLeaveRequest);
        } catch (RuntimeException e) {
            // Handle validation or business logic errors
            log.error("Error rejecting leave request for employeeId {}: {}", employeeId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            // Handle unexpected errors
            log.error("Unexpected error while rejecting leave request for employeeId {}: {}", employeeId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    /**
     * Get leave history by employee ID.
     *
     * @param employeeId The ID of the employee.
     * @return ResponseEntity with the leave history.
     */
    @GetMapping("/history/{employeeId}")
    public ResponseEntity<ResultResponse> getLeaveHistoryByEmployeeId(@PathVariable String employeeId) {
        log.info("Fetching leave history for employee ID: {}", employeeId);

        try {
            // Step 1: Fetch leave history from the service
            List<LeaveRequestDTO> leaveHistory = leaveRequestService.getLeaveHistoryByEmployeeId(employeeId);

            // Step 2: Return success response
            return ResponseEntity.ok(new ResultResponse(
                    true,
                    "Leave history retrieved successfully",
                    leaveHistory,
                    LocalDateTime.now()
            ));
        } catch (LeaveRequestNotFoundException e) {
            // Handle case when no leave history is found for the employee
            log.error("No leave history found for employee ID {}: {}", employeeId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResultResponse(
                    false,
                    "No leave history found for employee ID: " + employeeId,
                    null,
                    LocalDateTime.now()
            ));
        } catch (Exception e) {
            // Handle unexpected server errors
            log.error("Error fetching leave history for employee ID {}: {}", employeeId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResultResponse(
                    false,
                    "An unexpected error occurred while retrieving leave history.",
                    null,
                    LocalDateTime.now()
            ));
        }
    }

    ///api/leaves/pending/manager/{managerId}
    @GetMapping("/pending/manager/{managerId}")
    public ResponseEntity<List<LeaveRequestDTO>> getPendingLeaveRequestsByManagerId(@PathVariable("managerId") String managerId) {
        try {
            List<LeaveRequestDTO> pendingRequests = leaveRequestService.getPendingLeaveRequestsByManagerId(managerId);
            return ResponseEntity.ok(pendingRequests);
        } catch (Exception e) {
            log.error("Error fetching pending leave requests for managerId {}: {}", managerId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }}   
      
