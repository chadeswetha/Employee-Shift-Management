
package com.example.shiftservice.client;



import com.example.shiftservice.entity.ResultResponse;

import com.example.shiftservice.dto.EmployeeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

/**
 * EmployeClient: Feign client interface for interacting with the Employee Service.
 * 
 * - **@FeignClient**: Connects this client to the Employee Service, making HTTP requests
 *   to interact with its API endpoints.
 * - Provides methods for fetching employee data, including employee details,
 *   all employees, and manager information.
 */

@FeignClient(name = "employeeservice")
public interface EmployeClient {
	/**
     * Fetch details of an employee by their ID.
     *
     * @param employeeId The unique ID of the employee.
     * @return Response containing the employee details wrapped in a ResultResponse object.
     */


	@GetMapping("/api/admin/employees/employee/{employeeId}")
    ResponseEntity<ResultResponse<EmployeeDTO>> getEmployeeById(@PathVariable String employeeId);
	/**
     * Fetch a list of all employees.
     *
     * @return Response containing a list of employee details wrapped in a ResultResponse object.
     */


	@GetMapping("/api/admin/employees/all")
    ResponseEntity<ResultResponse<List<EmployeeDTO>>> getAllEmployees();

    
//    @GetMapping("/api/employee/name/{name}")
//    ResponseEntity<ResultResponse<List<EmployeeDTO>>> getEmployeesByName(@PathVariable String name);
	/**
     * Fetch details of the manager employee.
     *
     * @return Response containing the manager's details wrapped in a ResultResponse object.
     */
    
    @GetMapping("/api/admin/employees/manager")
    ResponseEntity<ResultResponse<EmployeeDTO>> getManagerEmployee();
    
    @GetMapping("/api/admin/employees/all/managers")
    ResponseEntity<ResultResponse<List<EmployeeDTO>>> getAllManagersEmployee();

    

//	ResponseEntity<ResultResponse<EmployeeDTO>> getEmployeeById(String employeeId);
}