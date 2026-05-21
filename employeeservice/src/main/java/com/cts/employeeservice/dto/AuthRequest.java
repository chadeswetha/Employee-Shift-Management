package com.cts.employeeservice.dto;


import com.cts.employeeservice.enums.Role;

import lombok.Data;

@Data
public class AuthRequest {
   private String email;
   private String password;
   private Role role;
}