package com.example.shiftservice.service;
 
import com.example.shiftservice.client.EmployeClient;
import com.example.shiftservice.client.NotificationClient;
import com.example.shiftservice.dto.EmployeeDTO;
import com.example.shiftservice.dto.NotificationRequest;
import com.example.shiftservice.dto.NotificationRequest.NotificationType;
import com.example.shiftservice.dto.ShiftRequestDTO;
import com.example.shiftservice.dto.ShiftResponseDTO;
import com.example.shiftservice.entity.ResultResponse;
import com.example.shiftservice.entity.Shift;
import com.example.shiftservice.enums.Role;
import com.example.shiftservice.enums.ShiftType;
import com.example.shiftservice.exception.EmployeeNotFoundException;
import com.example.shiftservice.exception.ShiftNotFoundException;
import com.example.shiftservice.repository.ShiftRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
 
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;
 
@Service
@Slf4j
public class ShiftService {
 
    @Autowired
    private ShiftRepository shiftRepository;
 
    @Autowired
    private EmployeClient employeeClient;
 
    @Autowired
    private NotificationClient notificationClient;
 
    @Transactional
    public ShiftResponseDTO createShift(ShiftRequestDTO shiftRequestDTO, String userId) {
        ResponseEntity<ResultResponse<EmployeeDTO>> employeeResponse;
        EmployeeDTO employeeDTO;
 
        try {
            employeeResponse = employeeClient.getEmployeeById(shiftRequestDTO.getEmployeeId());
            if (employeeResponse.getBody() == null) {
                throw new EmployeeNotFoundException("Employee not found with id: " + shiftRequestDTO.getEmployeeId());
            }
            employeeDTO = employeeResponse.getBody().getData();
        } catch (Exception e) {
            throw new EmployeeNotFoundException("Error while fetching employee: " + e.getMessage());
        }
 
        // Fetch the manager of the same department
        ResponseEntity<ResultResponse<List<EmployeeDTO>>> managerResponse;
        EmployeeDTO assigningManager = null;
        try {
            managerResponse = employeeClient.getAllManagersEmployee();
            if (managerResponse.getBody() != null && managerResponse.getBody().getData() != null) {
                assigningManager = managerResponse.getBody().getData().stream()
                        .filter(manager -> manager.getDepartment().equals(employeeDTO.getDepartment()))
                        .findFirst()
                        .orElse(null);
            }
        } catch (Exception e) {
            log.warn("Error while fetching managers: {}", e.getMessage());
            // Handle the case where manager retrieval fails, perhaps default to the user creating the shift
        }
 
        String assignedById = userId; // Default to the user creating the shift
        String assignedByName = null;
        if (assigningManager != null) {
            assignedById = assigningManager.getId();
            assignedByName = assigningManager.getEmployeeName();
        } else {
             ResponseEntity<ResultResponse<EmployeeDTO>> userResponse = employeeClient.getEmployeeById(userId);
             if(userResponse.getBody() != null && userResponse.getBody().getData() != null){
                 assignedByName = userResponse.getBody().getData().getEmployeeName();
             }
            log.warn("No manager found for department: {}. Shift will be assigned by user: {}", employeeDTO.getDepartment(), userId);
        }
 
        Shift shift = Shift.builder()
                .startTime(shiftRequestDTO.getStartTime())
                .endTime(shiftRequestDTO.getEndTime())
                .shiftType(shiftRequestDTO.getShiftType())
                .employeeId(employeeDTO.getId())
                .shiftDate(shiftRequestDTO.getShiftDate())
                .assignedBy(assignedById) // Set the manager's ID
                .assignedBy(assignedByName)
                .employeeName(employeeDTO.getEmployeeName())
                .department(employeeDTO.getDepartment())
                .build();
 
        Shift savedShift = shiftRepository.save(shift);
        return mapToResponseDTO(savedShift);
    }
 
    public ShiftResponseDTO updateShift(String shiftId, ShiftRequestDTO shiftRequestDTO, @Valid String employeeId) {
        Shift existingShift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ShiftNotFoundException("Shift not found with id: " + shiftId));
 
        ResponseEntity<ResultResponse<EmployeeDTO>> employeeResponse;
        EmployeeDTO assigningEmployee = null;
 
        try {
            employeeResponse = employeeClient.getEmployeeById(shiftRequestDTO.getEmployeeId());
            if ( employeeResponse.getBody() == null) {
                throw new EmployeeNotFoundException("Employee not found with id: " + shiftRequestDTO.getEmployeeId());
            }
            assigningEmployee = employeeResponse.getBody().getData();
        } catch (Exception e) {
            throw new EmployeeNotFoundException("Employee not found with id: " + shiftRequestDTO.getEmployeeId());
        }
 
        EmployeeDTO employee = employeeResponse.getBody().getData();
 
        existingShift.setShiftType(shiftRequestDTO.getShiftType());
        existingShift.setEmployeeId(employee.getId());
        existingShift.setAssignedBy(assigningEmployee.getId());
        existingShift.setAssignedBy(assigningEmployee.getEmployeeName());
        existingShift.setEmployeeName(employee.getEmployeeName());
        existingShift.setStartTime(shiftRequestDTO.getStartTime()); // Update start time
        existingShift.setEndTime(shiftRequestDTO.getEndTime());
        Shift updatedShift = shiftRepository.save(existingShift);
        return mapToResponseDTO(updatedShift);
    }
    public void deleteShift(String shiftId) {
        if (!shiftRepository.existsById(shiftId)) {
            throw new ShiftNotFoundException("Shift not found with id: " + shiftId);
        }
        shiftRepository.deleteById(shiftId);
    }
    
 
 
    public List<ShiftResponseDTO> getAllShifts() {
        return shiftRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
     public List<ShiftResponseDTO> getAllShiftsByAssignedBy(String assignedBy) {
        return shiftRepository.findByAssignedBy(assignedBy).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
 
    
    
 
 
    public List<ShiftResponseDTO> getAllShiftsByEmployeeId(String employeeId) {
        try {
            ResponseEntity<ResultResponse<EmployeeDTO>> employeeResponse = employeeClient.getEmployeeById(employeeId);
            if (employeeResponse.getBody() == null) {
                throw new EmployeeNotFoundException("Employee not found with id: " + employeeId);
            }
        } catch (Exception e) {
            throw new EmployeeNotFoundException("Error while fetching employee: " + e.getMessage());
        }
 
        return shiftRepository.findByEmployeeId(employeeId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    public ShiftResponseDTO getShiftById(String shiftId) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ShiftNotFoundException("Shift not found with id: " + shiftId));
        return mapToResponseDTO(shift);
    }
    
    
 
 
 
    private ShiftResponseDTO mapToResponseDTO(Shift shift) {
        System.out.println("hiiiiiiii");
        return ShiftResponseDTO.builder()
                .shiftId(shift.getShiftId())
                .startTime(shift.getStartTime())
                .endTime(shift.getEndTime())
                .shiftType(shift.getShiftType())
                .employeeId(shift.getEmployeeId().toString())
                .employeeName(shift.getEmployeeName())
                .shiftDate(shift.getShiftDate())
                .assignedByName(shift.getAssignedBy())
                .createdAt(shift.getCreatedAt())
                .department(shift.getDepartment())
                .build();
    }
 
    @Transactional
    @Scheduled(cron = "0 * * * * ?", zone = "Asia/Kolkata")
    public void scheduleWeeklyShifts() {
        log.info("Starting scheduled weekly shift assignment.");
 
        ResponseEntity<ResultResponse<List<EmployeeDTO>>> allEmployeesResponse = employeeClient.getAllEmployees();
 
        if (allEmployeesResponse.getBody() == null || allEmployeesResponse.getBody().getData() == null) {
            log.error("Failed to retrieve employees from employee service.");
            return;
        }
 
        List<EmployeeDTO> allEmployees = allEmployeesResponse.getBody().getData();
 
        ResponseEntity<ResultResponse<List<EmployeeDTO>>> managersResponse = employeeClient.getAllManagersEmployee();
 
        if (managersResponse.getBody() == null || managersResponse.getBody().getData() == null) {
            log.error("Failed to retrieve managers from employee service.");
            return;
        }
 
        List<EmployeeDTO> managers = managersResponse.getBody().getData();
 
        java.util.Map<String, List<EmployeeDTO>> employeesByDepartment = allEmployees.stream()
                .filter(employee -> !Role.ADMIN.equals(employee.getRole()) && !Role.MANAGER.equals(employee.getRole()))
                .collect(Collectors.groupingBy(EmployeeDTO::getDepartment));
 
        java.util.Map<String, List<EmployeeDTO>> managersByDepartment = managers.stream()
                .filter(employee -> Role.MANAGER.equals(employee.getRole()))
                .collect(Collectors.groupingBy(EmployeeDTO::getDepartment));
 
        for (java.util.Map.Entry<String, List<EmployeeDTO>> departmentEntry : employeesByDepartment.entrySet()) {
            String department = departmentEntry.getKey();
            List<EmployeeDTO> employeesInDepartment = departmentEntry.getValue();
            List<EmployeeDTO> managersInDepartment = managersByDepartment.getOrDefault(department, java.util.Collections.emptyList());
 
            if (managersInDepartment.isEmpty()) {
                log.warn("No manager found for department: {}. Skipping shift scheduling for this department.", department);
                continue;
            }
 
            EmployeeDTO assigningManager = managersInDepartment.get(new Random().nextInt(managersInDepartment.size()));
            log.info("Manager {} will assign shifts for department: {}", assigningManager.getEmployeeName(), department);
 
            LocalDateTime weekStartDate = LocalDateTime.now().with(DayOfWeek.MONDAY);
            for (int week = 0; week < 3; week++) {
                weekStartDate = LocalDateTime.now().with(DayOfWeek.MONDAY).plusWeeks(week);
                log.info("Week start date: {}", weekStartDate);
 
                int weekOfYear = weekStartDate.get(WeekFields.of(Locale.getDefault()).weekOfYear());
                ShiftType weeklyShiftType = getShiftTypeForWeek(weekOfYear);
                int shiftStartHour = getShiftStartHour(weeklyShiftType);
 
                for (LocalDate currentDate = weekStartDate.toLocalDate();
                     currentDate.isBefore(weekStartDate.plusWeeks(1).toLocalDate());
                     currentDate = currentDate.plusDays(1)) {
                    DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
                    if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
                        for (EmployeeDTO employee : employeesInDepartment) {
                            LocalDateTime shiftStart = currentDate.atTime(shiftStartHour, 0, 0);
                            LocalDateTime shiftEnd = shiftStart.plusHours(8);
                            LocalDate shiftDate = currentDate;
                            String employeeId = employee.getId();
 
                            if (checkIfShiftExists(employeeId, shiftDate)) {
                                log.info("Shift already exists for employee {} on {}. Skipping.", employeeId, shiftDate);
                                continue;
                            }
 
                            ShiftRequestDTO shiftRequestDTO = ShiftRequestDTO.builder()
                                    .employeeId(employee.getId())
                                    .startTime(shiftStart)
                                    .endTime(shiftEnd)
                                    .shiftType(weeklyShiftType)
                                    .shiftDate(shiftDate)
                                    .assignedByName(assigningManager.getId())
                                    .assignedByName(assigningManager.getEmployeeName())
                                    .department(employee.getDepartment())
                                    .build();
 
                            createShift(shiftRequestDTO, assigningManager.getId());
                        }
                    }
                }
            }
        }
        log.info("Weekly shift scheduling completed.");
    }
 
    public boolean checkIfShiftExists(String employeeId, LocalDate shiftDate) {
        return shiftRepository.existsByEmployeeIdAndShiftDate(employeeId, shiftDate);
    }
 
    private ShiftType getShiftTypeForWeek(int weekOfYear) {
        return switch (weekOfYear % 3) {
            case 0 -> ShiftType.MORNING;
            case 1 -> ShiftType.EVENING;
            case 2 -> ShiftType.NIGHT;
            default -> ShiftType.MORNING;
        };
    }
 
    private int getShiftStartHour(ShiftType shiftType) {
        return switch (shiftType) {
            case MORNING -> 9;
            case EVENING -> 14;
            case NIGHT -> 22;
            default -> 9;
        };
    }
 
    @Scheduled(cron = "0 * * * * *", zone = "Asia/Kolkata")
    public void notifyUpcomingShifts() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourLater = now.plusHours(1);
 
        List<Shift> upcomingShifts = shiftRepository.findBystartTimeBetween(now, oneHourLater);
 
        for (Shift shift : upcomingShifts) {
            NotificationRequest notification = NotificationRequest.builder()
                    .recipientId(shift.getEmployeeId())
                    .title("Upcoming Shift Reminder")
                    .message("Hey " + shift.getEmployeeName() + ", your shift starts at " +
                            shift.getStartTime().toLocalTime() + " on " + shift.getShiftDate() + ".")
                    .type(NotificationType.SHIFT_REMINDER)
                    .build();
 
            try {
                notificationClient.sendNotification(notification);
            } catch (Exception e) {
                log.error("Failed to send notification for shift ID: " + shift.getShiftId(), e);
            }
        }
    }
}