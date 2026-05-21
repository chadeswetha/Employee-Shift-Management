package com.example.attendance.controller;

import com.example.attendance.dto.AttendanceResponse;
import com.example.attendance.dto.WeeklyAttendanceResponse;
import com.example.attendance.entity.Attendance;
import com.example.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/checkin/{employeeId}")
    public ResponseEntity<AttendanceResponse> checkIn(@PathVariable String employeeId) {
        return ResponseEntity.ok(attendanceService.checkIn(employeeId));
    }

    @PostMapping("/checkout/{employeeId}")
    public ResponseEntity<AttendanceResponse> checkOut(@PathVariable String employeeId) {
        return ResponseEntity.ok(attendanceService.checkOut(employeeId));
    }
//
//    @GetMapping("/{employeeId}")
//    public ResponseEntity<List<AttendanceResponse>> getByEmployee(@PathVariable String employeeId) {
//        return ResponseEntity.ok(attendanceService.getAttendanceByEmployee(employeeId));
//    }
    @GetMapping("/employee/{employeeId}/weekly")
    public ResponseEntity<List<WeeklyAttendanceResponse>> getEmployeeWeeklyAttendance(@PathVariable String employeeId) {
        return ResponseEntity.ok(attendanceService.getWeeklyAttendance(employeeId));
    }

    @GetMapping("/manager/{managerId}/employee/{employeeId}/weekly")
    public ResponseEntity<List<WeeklyAttendanceResponse>> getEmployeeAttendanceForManager(
            @PathVariable String managerId,
            @PathVariable String employeeId) {
        // (Optional) Validate manager’s access via Feign or JWT context
        return ResponseEntity.ok(attendanceService.getWeeklyAttendance(employeeId));
    }
    @GetMapping("/today/{employeeId}")
    public ResponseEntity<Attendance> getTodayAttendance(@PathVariable String employeeId) {
        Attendance attendance = attendanceService.getTodayAttendance(employeeId);
        return ResponseEntity.ok(attendance);
    }
    
    @GetMapping("/{employeeId}/filter")
    public ResponseEntity<List<AttendanceResponse>> getAttendanceByDateRange(
            @PathVariable String employeeId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(attendanceService.getAttendanceBetweenDates(employeeId, startDate, endDate));
    }
    @GetMapping("/{employeeId}")
    public ResponseEntity<List<AttendanceResponse>> getAttendanceBetweenDates(
            @PathVariable String employeeId,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<AttendanceResponse> result = attendanceService.getAttendanceBetweenDates(employeeId, start, end);
        return ResponseEntity.ok(result);
    }
     
}
