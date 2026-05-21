package com.cts.employeeservice.entity;

import java.time.LocalDateTime;

import com.cts.employeeservice.enums.Role;

import lombok.Builder;
import lombok.Data;
 
 
 
 
@Data
@Builder
public class ResultResponse<T> {
 
	private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private Role role;
    private byte[] image;
	
}