package com.example.attendance.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "employeeservice")
public interface EmployeeClient {

    @GetMapping("/api/admin/employees/employee/{id}/name")
    String getEmployeeName(@PathVariable String id);
}
