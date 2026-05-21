package com.cts.leavemanagementmodule.interfaces;

import java.util.List;



//import java.util.UUID;



import org.springframework.cloud.openfeign.FeignClient;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.leavemanagementmodule.dto.EmployeeDTO;





@FeignClient(name = "employee-service", url = "http://localhost:8762/api/admin/employees")
public interface EmployeeServiceClient {

   
    
    @GetMapping("/employee/{employeeId}")
    EmployeeDTO getEmployeeById(@PathVariable("employeeId") String employeeId);

    @GetMapping("/role/{role}/department/{department}/manager-id")
    String getManagerIdByRoleAndDepartment(
            @PathVariable("role") String role,
            @PathVariable("department") String department);
    
    @GetMapping("/employee/{employeeId}/name")
    String getEmployeeNameById(@PathVariable("employeeId") String employeeId);
    
    
}



