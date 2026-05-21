package com.cts.employeeservice.controller;

import lombok.RequiredArgsConstructor;


import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.cts.employeeservice.dto.EmployeeRequestDto;
import com.cts.employeeservice.dto.EmployeeResponseDto;
import com.cts.employeeservice.entity.Employee;
import com.cts.employeeservice.entity.ResultResponse;
import com.cts.employeeservice.enums.Role;
import com.cts.employeeservice.exception.ResourceNotFoundException;
import com.cts.employeeservice.service.EmployeeService;

import jakarta.validation.Valid;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * EmployeeController is a REST controller responsible for handling API 
 * requests related to employees. It provides endpoints for employee 
 * management, including adding, updating, deleting, and fetching employee 
 * information.
 */

@RestController
@RequestMapping("/api/admin/employees")
@RequiredArgsConstructor
@Slf4j

public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * Adds a new employee.
     *
     * @param requestDto The employee details to be added.
     * @return The response containing the added employee details.
     * @throws IOException 
     */
    //@PreAuthorize("hasRole('ADMIM')")
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<ResultResponse<EmployeeResponseDto>> addEmployee(@Valid @ModelAttribute EmployeeRequestDto requestDto) throws IOException {
        log.info("Adding a new employee with details: {}", requestDto);
        EmployeeResponseDto employee = employeeService.addEmployee(requestDto);
        log.info("Employee added successfully: {}", employee);
        return ResponseEntity.ok(ResultResponse.<EmployeeResponseDto>builder()
                .success(true)
                .message("Employee added successfully")
                .data(employee)
                .timestamp(LocalDateTime.now())
                .build());
    }

    
    

    /**
     * Updates an existing employee.
     *
     * @param id The ID of the employee to be updated.
     * @param requestDto The updated employee details.
     * @return The response containing the updated employee details.
     */
   // @PreAuthorize("hasRole('ADMIM')")
    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<ResultResponse<EmployeeResponseDto>> updateEmployee(@PathVariable String id, @Valid @RequestBody EmployeeRequestDto requestDto) {
        log.info("Updating employee with ID: {}", id);
        EmployeeResponseDto employee = employeeService.updateEmployee(id, requestDto);
        log.info("Employee updated successfully: {}", employee);
        return ResponseEntity.ok(ResultResponse.<EmployeeResponseDto>builder()
                .success(true)
                .message("Employee updated successfully")
                .data(employee)
                .timestamp(LocalDateTime.now())
                .build());
    }

    

    /**
     * Deletes an existing employee.
     *
     * @param id The ID of the employee to be deleted.
     * @return The response indicating the deletion status.
     */
   // @PreAuthorize("hasRole('ADMIM')")
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<ResultResponse<String>> deleteEmployee(@PathVariable String id) {
        log.info("Deleting employee with ID: {}", id);
        employeeService.deleteEmployee(id);
        log.info("Employee deleted successfully!");
        return ResponseEntity.ok(ResultResponse.<String>builder()
                .success(true)
                .message("Employee deleted successfully")
                .data("Employee deleted successfully")
                .timestamp(LocalDateTime.now())
                .build());
    }
    
    

    /**
     * Fetches all employees.
     *
     * @return The response containing the list of all employees.
     */
    //@PreAuthorize("hasRole('ADMIM')")
    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<ResultResponse<List<EmployeeResponseDto>>> getAllEmployees() {
        log.info("Fetching all employees");
        List<EmployeeResponseDto> employees = employeeService.getAllEmployees();
        log.info("Fetched all employees successfully");
        return ResponseEntity.ok(ResultResponse.<List<EmployeeResponseDto>>builder()
                .success(true)
                .message("All employees fetched successfully")
                .data(employees)
                .timestamp(LocalDateTime.now())
                .build());
    }
 
    

    /**
     * Fetches an employee by their ID.
     *
     * @param employeeId The ID of the employee to be fetched.
     * @return The response containing the employee details, or a 404 status if not found.
     */
    //@PreAuthorize("hasRole('ADMIM','EMPLOYEE')")
    @GetMapping("/employee/{employeeId}")
    @ResponseBody
    public ResponseEntity<ResultResponse<EmployeeResponseDto>> getEmployeeById(@PathVariable String employeeId) {
        log.info("Fetching employee with ID: {}", employeeId);
        EmployeeResponseDto employee = employeeService.getEmployeeById(employeeId);
        if (employee != null) {
            log.info("Employee fetched successfully: {}", employee);
            return ResponseEntity.ok(ResultResponse.<EmployeeResponseDto>builder()
                    .success(true)
                    .message("Employee fetched successfully")
                    .data(employee)
                    .timestamp(LocalDateTime.now())
                    .build());
        } else {
            log.warn("Employee with ID: {} not found", employeeId);
            return ResponseEntity.status(404).body(ResultResponse.<EmployeeResponseDto>builder()
                    .success(false)
                    .message("Employee not found with ID: " + employeeId)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }
    
    

    /**
     * Fetches the manager's employee details.
     *
     * @return The response containing the manager's details, or a 404 status if not found.
     */
    @GetMapping("/manager")
    @ResponseBody
    public ResponseEntity<ResultResponse<EmployeeResponseDto>> getManagerEmployee() {
        log.info("Fetching manager details");
        EmployeeResponseDto manager = employeeService.findManager();
        if (manager != null) {
            log.info("Manager fetched successfully: {}", manager);
            return ResponseEntity.ok(ResultResponse.<EmployeeResponseDto>builder()
                    .success(true)
                    .message("Manager fetched successfully")
                    .data(manager)
                    .timestamp(LocalDateTime.now())
                    .build());
        } else {
            log.warn("Manager not found");
            return ResponseEntity.status(404).body(ResultResponse.<EmployeeResponseDto>builder()
                    .success(false)
                    .message("Manager details not found")
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    
    
    /**
     * Fetches employees by department name.
     *
     * @param departmentName The name of the department.
     * @return The response containing the list of employees in the department.
     */
    @GetMapping("/department/{departmentName}")
    @ResponseBody
    public ResponseEntity<ResultResponse<List<Employee>>> getEmployeesByDepartmentName(@PathVariable String departmentName) {
        log.info("Fetching employees in department: {}", departmentName);
        List<Employee> employees = employeeService.getEmployeesByDepartmentName(departmentName);
        log.info("Employees fetched successfully for department: {}", departmentName);
        return ResponseEntity.ok(ResultResponse.<List<Employee>>builder()
                .success(true)
                .message("Employees fetched successfully for department: " + departmentName)
                .data(employees)
                .timestamp(LocalDateTime.now())
                .build());
    }

    
    
    /**
     * Fetches the manager's ID based on role and department.
     *
     * @param role The role of the manager.
     * @param department The department name.
     * @return The manager's ID as a plain response.
     */
    @GetMapping("/role/{role}/department/{department}/manager-id")
    @ResponseBody
    public ResponseEntity<String> getManagerIdByRoleAndDepartment(@PathVariable String role, @PathVariable String department) {
        log.info("Fetching manager ID for role: {} and department: {}", role, department);
        String managerId = employeeService.getManagerIdByRoleAndDepartment(department);
        log.info("Manager ID fetched successfully: {}", managerId);
        return ResponseEntity.ok(managerId);
    }

    

    /**
     * Fetches employees by their role.
     *
     * @param role The role of the employees to be fetched.
     * @return The response containing the list of employees with the specified role.
     */
    @GetMapping("/by-role/{role}")
    @ResponseBody
    public ResponseEntity<ResultResponse<List<EmployeeResponseDto>>> getEmployeesByRole(@PathVariable Role role) {
        log.info("Fetching employees with role: {}", role);
        List<EmployeeResponseDto> employees = employeeService.getEmployeesByRole(role);
        log.info("Employees fetched successfully for role: {}", role);
        return ResponseEntity.ok(ResultResponse.<List<EmployeeResponseDto>>builder()
                .success(true)
                .message("Employees fetched successfully for role: " + role)
                .data(employees)
                .timestamp(LocalDateTime.now())
                .build());
    }
    
    

    /**
     * Fetches the name of an employee by their ID.
     *
     * @param employeeId The ID of the employee.
     * @return The employee name as a plain response.
     */
    @GetMapping("/employee/{employeeId}/name")
    @ResponseBody
    public ResponseEntity<String> getEmployeeNameById(@PathVariable("employeeId") String employeeId) {
        log.info("Fetching name of employee with ID: {}", employeeId);
        String employeeName = employeeService.getEmployeeNameById(employeeId);
        log.info("Fetched employee name successfully: {}", employeeName);
        return ResponseEntity.ok(employeeName);
    }
    
    
    
    @GetMapping("/email/{email}")
    public ResponseEntity<ResultResponse<EmployeeResponseDto>> getEmployeeByEmail(@PathVariable String email) {
        try {
            EmployeeResponseDto employeeDto = employeeService.getEmployeeDetailsByEmail(email);
            ResultResponse<EmployeeResponseDto> response = ResultResponse.<EmployeeResponseDto>builder()
                    .success(true)
                    .message("Employee details retrieved successfully")
                    .data(employeeDto)
                    .timestamp(LocalDateTime.now())
                    .role(employeeDto.getRole()) // Assuming EmployeeResponseDto has a getRole() method
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            ResultResponse<EmployeeResponseDto> response = ResultResponse.<EmployeeResponseDto>builder()
                    .success(false)
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .role(null) // Or handle role based on the error context if needed
                    .data(null)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        
}
    /**
     * Fetches all manager employees.
     *
     * @return The response containing the list of all managers.
     */
    @GetMapping("/all/managers")
    @ResponseBody
    public ResponseEntity<ResultResponse<List<EmployeeResponseDto>>> getAllManagersEmployee() {
        log.info("Fetching all managers");
        List<EmployeeResponseDto> managers = employeeService.getAllManagers();
        log.info("Fetched all managers successfully");
        return ResponseEntity.ok(ResultResponse.<List<EmployeeResponseDto>>builder()
                .success(true)
                .message("All managers fetched successfully")
                .data(managers)
                .timestamp(LocalDateTime.now())
                .build());
    }
    @GetMapping("/department/manager/{managerId}")
    public ResponseEntity<List<Employee>> getEmployeesByManagerDepartment(@PathVariable String managerId) {
        List<Employee> employees = employeeService.getEmployeesUnderManager(managerId);
        return ResponseEntity.ok(employees);
    }
    
    
    @GetMapping("/{employeeId}/department-name")
    public ResponseEntity<String> getDepartmentNameByEmployeeId(@PathVariable String employeeId) {
        try {
            String departmentName = employeeService.getDepartmentNameByEmployeeId(employeeId);
            return ResponseEntity.ok(departmentName);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found with ID: " + employeeId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }   

}