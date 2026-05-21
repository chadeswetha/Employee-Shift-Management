package com.example.notification.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@FeignClient(name = "SHIFT-SERVICE")
public interface ShiftClient {

    @GetMapping("/api/shifts/today")
    List<Map<String, String>> getTodayShifts(); // id, employeeId, startTime
}
