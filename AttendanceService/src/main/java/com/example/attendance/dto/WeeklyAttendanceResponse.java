package com.example.attendance.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyAttendanceResponse {
    private LocalDate date;
    private String status;
    // PRESENT / ABSENT / NONE
    private LocalDateTime checkintime;
    private LocalDateTime checkouttime;
    
    
}
