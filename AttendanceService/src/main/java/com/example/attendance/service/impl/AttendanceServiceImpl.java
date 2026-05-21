package com.example.attendance.service.impl;

import com.example.attendance.dto.AttendanceResponse;
import com.example.attendance.dto.WeeklyAttendanceResponse;
import com.example.attendance.entity.Attendance;
import com.example.attendance.enums.AttendanceStatus;
import com.example.attendance.exception.AttendanceNotFoundException;
import com.example.attendance.feign.EmployeeClient;
import com.example.attendance.repository.AttendanceRepository;
import com.example.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository repository;
    private final EmployeeClient employeeClient;

    @Override
    public AttendanceResponse checkIn(String employeeId) {
        LocalDate today = LocalDate.now();

        Attendance attendance = repository.findByEmployeeIdAndDate(employeeId, today)
                .orElseGet(() -> Attendance.builder()
                        .employeeId(employeeId)
                        .employeeName(employeeClient.getEmployeeName(employeeId)) // Optionally fetch via Feign later
                        .date(today)
                        .checkInTime(LocalDateTime.now())
                        .status(AttendanceStatus.PRESENT.name()) // Initial status as PRESENT, might change on checkout
                        .build());

        if (attendance.getCheckInTime() == null) {
            attendance.setCheckInTime(LocalDateTime.now());
            attendance.setStatus(AttendanceStatus.PRESENT.name()); // Set status on first check-in
        }

        repository.save(attendance);
        return toResponse(attendance);
    }

    @Override
    public AttendanceResponse checkOut(String employeeId) {
        Attendance attendance = repository.findByEmployeeIdAndDate(employeeId, LocalDate.now())
                .orElseThrow(() -> new AttendanceNotFoundException("No attendance record found for today"));

        LocalDateTime checkInTime = attendance.getCheckInTime();
        LocalDateTime checkOutTime = LocalDateTime.now();
        attendance.setCheckOutTime(checkOutTime);

        if (checkInTime != null) {
            long hoursWorked = ChronoUnit.HOURS.between(checkInTime, checkOutTime);
            if (hoursWorked < 7) {
                attendance.setStatus(AttendanceStatus.EARLY_LEAVE.name());
            } else {
                attendance.setStatus(AttendanceStatus.PRESENT.name());
            }
        } else {
            // This scenario shouldn't ideally happen if check-in is enforced
            attendance.setStatus(AttendanceStatus.EARLY_LEAVE.name()); // Consider as early leave if no check-in
        }
        repository.save(attendance);
   return toResponse(attendance);
    }
//    public AttendanceResponse checkIn(String employeeId) {
//        LocalDate today = LocalDate.now();
//
//        Attendance attendance = repository.findByEmployeeIdAndDate(employeeId, today)
//                .orElseGet(() -> Attendance.builder()
//                        .employeeId(employeeId)
//                        .employeeName(employeeClient.getEmployeeName(employeeId)) // Optionally fetch via Feign later
//                        .date(today)
//                        .checkInTime(LocalDateTime.now())
//                        .status(AttendanceStatus.PRESENT.name())
//                        .build());
//
//        if (attendance.getCheckInTime() == null) {
//            attendance.setCheckInTime(LocalDateTime.now());
//        }
//
//        repository.save(attendance);
//        return toResponse(attendance);
//    }
//
//    @Override
//    public AttendanceResponse checkOut(String employeeId) {
//        Attendance attendance = repository.findByEmployeeIdAndDate(employeeId, LocalDate.now())
//                .orElseThrow(() -> new AttendanceNotFoundException("No attendance record found for today"));
//
//        attendance.setCheckOutTime(LocalDateTime.now());
//        repository.save(attendance);
//        return toResponse(attendance);
//    }

    @Override
    public List<AttendanceResponse> getAttendanceByEmployee(String employeeId) {
        return repository.findByEmployeeId(employeeId)
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }
    @Override
    public List<WeeklyAttendanceResponse> getWeeklyAttendance(String employeeId) {
        LocalDate today = LocalDate.now();

        return java.util.stream.IntStream.rangeClosed(0, 6)
                .mapToObj(i -> {
                    LocalDate date = today.minusDays(i);
                    return repository.findByEmployeeIdAndDate(employeeId, date)
                            .map(att -> WeeklyAttendanceResponse.builder()
                                    .date(date)
                                    .checkintime(att.getCheckInTime())
                                    .checkouttime(att.getCheckOutTime())
                                    .status(att.getStatus())
                                    .build())
                            .orElse(WeeklyAttendanceResponse.builder()
                                    .date(date)
                                    .checkintime(null)
                                    .checkouttime(null)
                                    .status("ABSENT")
                                    .build());
                }).toList();
    }
    public Attendance getTodayAttendance(String employeeId) {
        return repository.findTodayByEmployeeId(employeeId)
        		.orElseThrow(() -> new AttendanceNotFoundException("No attendance record found for today"));
     
     
     
    }
//    public List<AttendanceResponse> getAttendanceBetweenDates(String employeeId, LocalDate start, LocalDate end) {
//        List<Attendance> records = repository.findByEmployeeIdAndDateBetween(employeeId, start, end);
//        return records.stream().map(this::toResponse)
//                .collect(Collectors.toList());
//    }
//    public List<AttendanceResponse> getAttendanceBetweenDates(String employeeId, LocalDate start, LocalDate end) {
//        List<Attendance> records = repository
//                .findByEmployeeIdAndDateBetween(employeeId, start, end);
//     
//        Map<LocalDate, Attendance> recordMap = records.stream()
//                .collect(Collectors.toMap(Attendance::getDate, a -> a));
//     
//        List<AttendanceResponse> result = new ArrayList<>();
//     
//        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
//            Attendance attendance = recordMap.get(date);
//            if (attendance != null) {
//                result.add(toResponse(attendance));
//            } else {
//                // Absent day - no record in DB
//                AttendanceResponse absentResponse = new AttendanceResponse();
//                absentResponse.setDate(date);
//                absentResponse.setStatus("ABSENT");
//                absentResponse.setCheckInTime(null);
//                absentResponse.setCheckOutTime(null);
//                result.add(absentResponse);
//            }
//        }
//     
//        return result;
 //   }
    
    public List<AttendanceResponse> getAttendanceBetweenDates(String employeeId, LocalDate start, LocalDate end) {
        List<Attendance> records = repository.findByEmployeeIdAndDateBetween(employeeId, start, end);

        Map<LocalDate, Attendance> recordMap = records.stream()
                .collect(Collectors.toMap(Attendance::getDate, a -> a));

        List<AttendanceResponse> result = new ArrayList<>();

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            Attendance attendance = recordMap.get(date);
            if (attendance != null) {
                // Check for early leave if check-in and check-out times are present
                if (attendance.getCheckInTime() != null && attendance.getCheckOutTime() != null) {
                    long hoursWorked = ChronoUnit.HOURS.between(attendance.getCheckInTime(), attendance.getCheckOutTime());
                    if (hoursWorked < 7) {
                        attendance.setStatus(AttendanceStatus.EARLY_LEAVE.name());
                    }
                } else if (attendance.getCheckInTime() != null && attendance.getCheckOutTime() == null) {
                    // If checked in but not checked out, and the date is in the past, consider early leave
                    if (date.isBefore(LocalDate.now())) {
                        attendance.setStatus(AttendanceStatus.EARLY_LEAVE.name());
                    }
                    // If it's today and checked in, the status remains PRESENT (or whatever it was set on check-in)
                } else {
                    // If no check-in or check-out, and a record exists (shouldn't ideally happen), mark as early leave
                    attendance.setStatus(AttendanceStatus.EARLY_LEAVE.name());
                }
                result.add(toResponse(attendance));
            } else {
                // Absent day - no record in DB
                AttendanceResponse absentResponse = new AttendanceResponse();
                absentResponse.setDate(date);
                absentResponse.setStatus("ABSENT");
                absentResponse.setCheckInTime(null);
                absentResponse.setCheckOutTime(null);
                result.add(absentResponse);
            }
        }

        return result;
    }


    private AttendanceResponse toResponse(Attendance att) {
        return AttendanceResponse.builder()
                .id(att.getId())
                .employeeId(att.getEmployeeId())
                .employeeName(att.getEmployeeName())
                .date(att.getDate())
                .checkInTime(att.getCheckInTime())
                .checkOutTime(att.getCheckOutTime())
                .status(att.getStatus())
                .build();
    }
    
}
