package com.cts.employeeservice.service;

import lombok.RequiredArgsConstructor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cts.employeeservice.dto.EmployeeRequestDto;
import com.cts.employeeservice.dto.EmployeeResponseDto;
import com.cts.employeeservice.entity.Employee;
import com.cts.employeeservice.enums.Role;
import com.cts.employeeservice.exception.ResourceNotFoundException;
import com.cts.employeeservice.repository.EmployeeRepository;

import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * EmployeeService class is responsible for handling the business logic
 * related to employee management, such as adding, updating, deleting,
 * and retrieving employees from the system.
 */
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Adds a new employee to the system.
     *
     * @param requestDto Contains employee details to be added.
     * @return EmployeeResponseDto containing the added employee details.
     * @throws IOException 
     */
    public EmployeeResponseDto addEmployee(@Valid EmployeeRequestDto requestDto) throws IOException {
        // Ensure only one manager per department
        if (requestDto.getRole() == Role.MANAGER) {
            boolean managerExists = employeeRepository.existsByDepartmentAndRole(requestDto.getDepartment(), Role.MANAGER);
            if (managerExists) {
                throw new IllegalStateException("A manager already exists in the department '" + requestDto.getDepartment() + "'.");
            }
        }

        // Validate employee data
        validateEmployeeData(requestDto, false);

        // Create the Employee object
        Employee employee = Employee.builder()
                .employeeName(requestDto.getEmployeeName())
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword())) // Encrypt password
                .department(requestDto.getDepartment())
                .role(requestDto.getRole())
                .contactNumber(requestDto.getContactNumber())
                .image(requestDto.getImage().getBytes())
                .build();

        // Save the Employee object in the database
        Employee savedEmployee = employeeRepository.save(employee);

        // Map the saved entity to a response DTO
        return mapToDto(savedEmployee);
    }


    
    
    /**
     * Updates an existing employee in the system.
     *
     * @param id         Employee ID.
     * @param requestDto Contains updated employee details.
     * @return EmployeeResponseDto containing updated employee details.
     */
    public EmployeeResponseDto updateEmployee(String id, @Valid EmployeeRequestDto requestDto) {
        // Fetch the existing employee by ID
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        

        // Ensure email uniqueness
        if (employeeRepository.existsByEmailAndIdNot(requestDto.getEmail(), id)) {
            throw new IllegalStateException("The email '" + requestDto.getEmail() + "' is already in use by another employee.");
        }

        // Ensure contact number uniqueness
        if (employeeRepository.existsByContactNumberAndIdNot(requestDto.getContactNumber(), id)) {
            throw new IllegalStateException("The contact number '" + requestDto.getContactNumber() + "' is already in use by another employee.");
        }

        // Update the employee fields with the new data
        employee.setEmployeeName(requestDto.getEmployeeName());
        employee.setEmail(requestDto.getEmail());
        employee.setDepartment(requestDto.getDepartment());
        employee.setRole(requestDto.getRole());
        employee.setContactNumber(requestDto.getContactNumber());

        // Save the updated employee in the database
        Employee updatedEmployee = employeeRepository.save(employee);

        // Map the updated entity to a response DTO
        return mapToDto(updatedEmployee);
    }






    /**
     * Deletes an employee by ID from the system.
     *
     * @param id Employee ID.
     */
    public void deleteEmployee(String id) {
        System.out.println("Deleting employee with ID: " + id); // Log the ID
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with ID: " + id);
        }
        employeeRepository.deleteById(id);
    }


    /**
     * Retrieves all employees in the system.
     *
     * @return List of EmployeeResponseDto containing all employees.
     */
    public List<EmployeeResponseDto> getAllEmployees() {

        return employeeRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    

    /**
     * Retrieves a list of employees by their role.
     *
     * @param role Employee role.
     * @return List of Employee entities matching the role.
     */
    public List<Employee> getEmpList(Role role) {
        return employeeRepository.findAllByRole(role);
    }
    
    

    /**
     * Finds a manager among the list of employees.
     *
     * @return EmployeeResponseDto containing the manager details, or null if not found.
     */
    public EmployeeResponseDto findManager() {
        List<Employee> allEmployees = employeeRepository.findAll();

        for (Employee employee : allEmployees) {
            if ("MANAGER".equalsIgnoreCase(employee.getRole().name())) {
                return mapToDto(employee);
            }
        }
        return null;
    }
    
    

    /**
     * Retrieves employees by department name.
     *
     * @param departmentName Name of the department.
     * @return List of Employee entities belonging to the department.
     */
    public List<Employee> getEmployeesByDepartmentName(String departmentName) {
        List<Employee> employees = employeeRepository.findByDepartment(departmentName);
        if (employees.isEmpty()) {
            throw new ResourceNotFoundException("No employees found in department: " + departmentName);
        }
        return employees;
    }
    
    

    /**
     * Retrieves an employee by their ID.
     *
     * @param employeeId Employee ID.
     * @return EmployeeResponseDto containing the employee details.
     */
    public EmployeeResponseDto getEmployeeById(String employeeId) {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            throw new ResourceNotFoundException("Invalid ID: ID cannot be null or empty.");
        }

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        return mapToDto(employee);
    }
    
    

    /**
     * Retrieves the manager's ID by role and department.
     *
     * @param department Department name.
     * @return Manager's ID.
     */
    public String getManagerIdByRoleAndDepartment(String department) {
        Employee manager = employeeRepository.findByRoleAndDepartment(Role.MANAGER, department)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found in department: " + department));
        return manager.getId().toString();
    }

    
    
    /**
     * Retrieves employees by their role and maps them to EmployeeResponseDto.
     *
     * @param role Employee role.
     * @return List of EmployeeResponseDto for employees matching the role.
     */
    public List<EmployeeResponseDto> getEmployeesByRole(Role role) {
        return employeeRepository.findByRole(role).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    

    /**
     * Retrieves the employee's name by their ID.
     *
     * @param employeeId Employee ID.
     * @return Name of the employee.
     */
    public String getEmployeeNameById(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));
        return employee.getEmployeeName();
    }
    
    
    
    /**
     * Validates the common employee fields like email, contact number, etc.
     * @param requestDto The employee data to validate.
     * @param isUpdate Indicates whether this validation is for an update operation.
     */
    private void validateEmployeeData(EmployeeRequestDto requestDto, boolean isUpdate) {
        // Ensure employee name is valid
        if (requestDto.getEmployeeName() == null || requestDto.getEmployeeName().isBlank()) {
            throw new IllegalArgumentException("Employee name cannot be blank.");
        }

        // Ensure email format is valid
        if (requestDto.getEmail() == null || !requestDto.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email format. Please provide a valid email address.");
        }
        
     // Ensure department is valid
        if (requestDto.getDepartment() == null || requestDto.getDepartment().isBlank()) {
            throw new IllegalArgumentException("Department cannot be blank.");
        }

        // Ensure email uniqueness
        if (!isUpdate || (isUpdate && employeeRepository.existsByEmail(requestDto.getEmail()))) {
            if (employeeRepository.existsByEmail(requestDto.getEmail())) {
                throw new IllegalStateException("The email '" + requestDto.getEmail() + "' is already in use.");
            }
        }

        // Ensure contact number format is valid
        if (requestDto.getContactNumber() == null || !requestDto.getContactNumber().matches("^[0-9]{10}$")) {
            throw new IllegalArgumentException("Contact number must consist of exactly 10 numerical digits.");
        }

        // Ensure contact number uniqueness
        if (!isUpdate || (isUpdate && employeeRepository.existsByContactNumber(requestDto.getContactNumber()))) {
            if (employeeRepository.existsByContactNumber(requestDto.getContactNumber())) {
                throw new IllegalStateException("The contact number '" + requestDto.getContactNumber() + "' is already in use.");
            }
        }
    }
    
    
    public EmployeeResponseDto getEmployeeDetailsByEmail(String email) {
        Optional<Employee> employeeOptional = employeeRepository.findByEmail(email);
        if (employeeOptional.isEmpty()) {
            throw new ResourceNotFoundException("Employee"+"email"+email);
        }
        Employee employee = employeeOptional.get();
        return mapToDto(employee);
    }
    
    /**
     * Retrieves all manager employees.
     *
     * @return List of EmployeeResponseDto for all employees with the MANAGER role.
     */
    public List<EmployeeResponseDto> getAllManagers() {
        return employeeRepository.findByRole(Role.MANAGER).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    public List<Employee> getEmployeesUnderManager(String managerId) {
        Employee manager = employeeRepository.findById(managerId).orElseThrow();
        return employeeRepository.findByDepartmentAndRole(manager.getDepartment(), Role.EMPLOYEE);
    }
    
    
    
    public String getDepartmentNameByEmployeeId(String employeeId) {
        //log.info("Fetching department name for employee ID: {}", employeeId);
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        if (employeeOptional.isEmpty()) {
            //log.warn("Employee not found with ID: {}", employeeId);
            throw new ResourceNotFoundException("Employee not found with ID: " + employeeId);
        }
        Employee employee = employeeOptional.get();
        //log.info("Department name found: {}", employee.getDepartment());
        return employee.getDepartment();
    }
    
    /**
     * Maps an Employee entity to an EmployeeResponseDto.
     *
     * @param employee Employee entity.
     * @return Mapped EmployeeResponseDto.
     */
    private EmployeeResponseDto mapToDto(Employee employee) {
        return EmployeeResponseDto.builder()
                .id(employee.getId())
                .employeeName(employee.getEmployeeName())
                .email(employee.getEmail())
                .department(employee.getDepartment())
                .role(employee.getRole())
                .contactNumber(employee.getContactNumber())
                .image(employee.getImage())
                .build();
    }


}
