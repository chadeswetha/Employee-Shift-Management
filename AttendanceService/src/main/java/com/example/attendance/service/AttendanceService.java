package com.example.attendance.service;

import com.example.attendance.dto.AttendanceRequest;
import com.example.attendance.dto.AttendanceResponse;
import com.example.attendance.dto.WeeklyAttendanceResponse;
import com.example.attendance.entity.Attendance;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    AttendanceResponse checkIn(String employeeId);
    AttendanceResponse checkOut(String employeeId);
    List<AttendanceResponse> getAttendanceByEmployee(String employeeId);
    List<WeeklyAttendanceResponse> getWeeklyAttendance(String employeeId);
    Attendance getTodayAttendance(String employeeId);
    public List<AttendanceResponse> getAttendanceBetweenDates(String employeeId, LocalDate start, LocalDate end);

}
