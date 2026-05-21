package com.example.shiftservice.controller;
 
import com.example.shiftservice.dto.ShiftRequestDTO;
import com.example.shiftservice.dto.ShiftResponseDTO;
import com.example.shiftservice.entity.ResultResponse;
import com.example.shiftservice.service.ShiftService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.time.LocalDateTime;
import java.util.List;
 
@Slf4j
@RestController
@RequestMapping("/api/shifts")
@CrossOrigin(origins = "http://localhost:3000")
public class ShiftController {
 
    @Autowired
    private ShiftService shiftService;
 
    @PostMapping
    public ResponseEntity<ResultResponse<ShiftResponseDTO>> createShift(
            @Valid @RequestBody ShiftRequestDTO shiftRequestDTO,
            @Valid @RequestParam String userId) {
        log.info("Received request to create shift for employee ID: {}", shiftRequestDTO.getEmployeeId());
        ShiftResponseDTO createdShift = shiftService.createShift(shiftRequestDTO, userId);
        log.info("Successfully created shift with ID: {}", createdShift.getShiftId());
        return new ResponseEntity<>(
                ResultResponse.<ShiftResponseDTO>builder()
                        .success(true)
                        .message("Shift created successfully")
                        .data(createdShift)
                        .timestamp(LocalDateTime.now())
                        .build(),
                HttpStatus.CREATED);
    }
 
    @PutMapping("/{id}")
    public ResponseEntity<ResultResponse<ShiftResponseDTO>> updateShift(
            @PathVariable String id,
            @Valid @RequestBody ShiftRequestDTO shiftRequestDTO) {
        log.info("Received request to update shift with ID: {}", id);
        ShiftResponseDTO updatedShift = shiftService.updateShift(id, shiftRequestDTO, shiftRequestDTO.getEmployeeId());
        log.info("Successfully updated shift with ID: {}", updatedShift.getShiftId());
        return ResponseEntity.ok(
                ResultResponse.<ShiftResponseDTO>builder()
                        .success(true)
                        .message("Shift updated successfully")
                        .data(updatedShift)
                        .timestamp(LocalDateTime.now())
                        .build());
    }
 
    @DeleteMapping("/{id}")
    public ResponseEntity<ResultResponse<Void>> deleteShift(@Valid @PathVariable String id) {
        log.info("Received request to delete shift with ID: {}", id);
        shiftService.deleteShift(id);
        log.info("Successfully deleted shift with ID: {}", id);
        return ResponseEntity.ok(
                ResultResponse.<Void>builder()
                        .success(true)
                        .message("Shift deleted successfully")
                        .timestamp(LocalDateTime.now())
                        .build());
    }
 
    @GetMapping
    public ResponseEntity<ResultResponse<List<ShiftResponseDTO>>> getAllShifts() {
        log.info("Received request to get all shifts.");
        List<ShiftResponseDTO> allShifts = shiftService.getAllShifts();
        log.info("Retrieved {} shifts.", allShifts.size());
        return ResponseEntity.ok(
                ResultResponse.<List<ShiftResponseDTO>>builder()
                        .success(true)
                        .message("All shifts retrieved successfully")
                        .data(allShifts)
                        .timestamp(LocalDateTime.now())
                        .build());
    }
 
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ResultResponse<List<ShiftResponseDTO>>> getAllShiftsByEmployeeId(
            @Valid @PathVariable String employeeId) {
        log.info("Received request to get shifts for employee ID: {}", employeeId);
        List<ShiftResponseDTO> employeeShifts = shiftService.getAllShiftsByEmployeeId(employeeId);
        log.info("Retrieved {} shifts for employee ID: {}", employeeShifts.size(), employeeId);
        return ResponseEntity.ok(
                ResultResponse.<List<ShiftResponseDTO>>builder()
                        .success(true)
                        .message("Shifts for employee retrieved successfully")
                        .data(employeeShifts)
                        .timestamp(LocalDateTime.now())
                        .build());
    }
 
    @GetMapping("/{shiftId}")
    public ResponseEntity<ResultResponse<ShiftResponseDTO>> getShiftByShiftId(
            @Valid @PathVariable String shiftId) {
        log.info("Received request to get shift by ID: {}", shiftId);
        ShiftResponseDTO shift = shiftService.getShiftById(shiftId);
        log.info("Retrieved shift with ID: {}", shift.getShiftId());
        return ResponseEntity.ok(
                ResultResponse.<ShiftResponseDTO>builder()
                        .success(true)
                        .message("Shift retrieved successfully")
                        .data(shift)
                        .timestamp(LocalDateTime.now())
                        .build());
    }
 
     @GetMapping("/assigned-by/{assignedBy}")
    public ResponseEntity<ResultResponse<List<ShiftResponseDTO>>> getAllShiftsByAssignedBy(
            @Valid @PathVariable String assignedBy) {
        log.info("Received request to get shifts assigned by user ID: {}", assignedBy);
        List<ShiftResponseDTO> assignedShifts = shiftService.getAllShiftsByAssignedBy(assignedBy);
        log.info("Retrieved {} shifts assigned by user ID: {}", assignedShifts.size(), assignedBy);
        return ResponseEntity.ok(
                ResultResponse.<List<ShiftResponseDTO>>builder()
                        .success(true)
                        .message("Shifts assigned by user retrieved successfully")
                        .data(assignedShifts)
                        .timestamp(LocalDateTime.now())
                        .build());
    }
 
    @PostMapping("/trigger-schedule")
    public ResponseEntity<ResultResponse<String>> triggerWeeklySchedule() {
        log.info("Received request to manually trigger weekly shift scheduling.");
        shiftService.scheduleWeeklyShifts();
        return ResponseEntity.ok(
                ResultResponse.<String>builder()
                        .success(true)
                        .message("Weekly shift scheduling triggered successfully.")
                        .timestamp(LocalDateTime.now())
                        .data("Scheduling initiated")
                        .build());
    }
 
    @PostMapping("/trigger-notifications")
    public ResponseEntity<ResultResponse<String>> triggerShiftNotifications() {
        log.info("Received request to manually trigger upcoming shift notifications.");
        shiftService.notifyUpcomingShifts();
        return ResponseEntity.ok(
                ResultResponse.<String>builder()
                        .success(true)
                        .message("Upcoming shift notifications triggered.")
                        .timestamp(LocalDateTime.now())
                        .data("Notifications sent")
                        .build());
    }
}
 
 