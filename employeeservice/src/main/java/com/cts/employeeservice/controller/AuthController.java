package com.cts.employeeservice.controller;


import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.aot.hint.annotation.RegisterReflection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.employeeservice.dto.AuthRequest;
import com.cts.employeeservice.dto.AuthResponse;
import com.cts.employeeservice.dto.RegisterRequest;
import com.cts.employeeservice.entity.Employee;
import com.cts.employeeservice.entity.ResultResponse;
import com.cts.employeeservice.enums.Role;
import com.cts.employeeservice.exception.ResourceNotFoundException;
import com.cts.employeeservice.service.AuthService;
 

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
//@RequiredArgsConstructor

public class AuthController {
 
	@Autowired
    private AuthService authService;
 
	
//	@PostMapping("/login")
//	public ResponseEntity<ResultResponse<String>> loginUser( @RequestBody AuthRequest authRequest) {
//	    try {
//	        String token = authService.authenticate(authRequest);
//	        ResultResponse<String> response = ResultResponse.<String>builder()
//	                .success(true)
//	                .message("Authentication successful")
//	                .data(token)
//	                .timestamp(LocalDateTime.now())
//	                .build();
//	        return new ResponseEntity<>(response, HttpStatus.OK);
//	    } catch (Exception e) {
//	        ResultResponse<String> errorResponse = ResultResponse.<String>builder()
//	                .success(false)
//	                .message(e.getMessage())
//	                .data(null)
//	                .timestamp(LocalDateTime.now())
//	                .build();
//	        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
//	    }
//	}
	
	

	 @PostMapping("/login")
	    public ResponseEntity<ResultResponse<AuthResponse>> loginUser(@RequestBody AuthRequest authRequest) {
	        try {
	            AuthResponse authResponse = authService.authenticate(authRequest); // Pass the entire AuthRequest object
	            ResultResponse<AuthResponse> response = ResultResponse.<AuthResponse>builder()
	                    .success(true)
	                    .message("Authentication successful")
	                    .data(authResponse)
	                    .timestamp(LocalDateTime.now())
	                    .build();
	            return new ResponseEntity<>(response, HttpStatus.OK);
	        } catch (Exception e) {
	            ResultResponse<AuthResponse> errorResponse = ResultResponse.<AuthResponse>builder()
	                    .success(false)
	                    .message(e.getMessage())
	                    .data(null)
	                    .timestamp(LocalDateTime.now())
	                    .build();
	            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
	        }
	    }
	/**
	 * Authenticates a user and returns a token.
	 *
	 * @param authRequest The AuthRequest containing email, password, and role.
	 * @return ResponseEntity containing a ResultResponse object with the token, or an error response.
	 */
//	
//	@PostMapping("/login")
//	
//	public ResponseEntity<ResultResponse<String>> loginUser(@RequestBody AuthRequest authRequest, @RequestParam("role") Role role) {
//        try {
//            String token = authService.authenticate(authRequest.getEmail(), authRequest.getPassword(), role);
//            ResultResponse<String> response = ResultResponse.<String>builder()
//                    .success(true)
//                    .message("Authentication successful")
//                    .data(token)
//                    .timestamp(LocalDateTime.now())
//                   
//                    .build();
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        } catch (ResourceNotFoundException e) {
//            ResultResponse<String> errorResponse = ResultResponse.<String>builder()
//                    .success(false)
//                    .message(e.getMessage())
//                    .data(null)
//                    .timestamp(LocalDateTime.now())
//                    .build();
//            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
//        } catch (Exception e) {
//            ResultResponse<String> errorResponse = ResultResponse.<String>builder()
//                    .success(false)
//                    .message("Authentication failed: " + e.getMessage())
//                    .data(null)
//                    .timestamp(LocalDateTime.now())
//                    .build();
//            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
//        }
//    }

    
	/**
	 * Registers a new user and returns the registered employee details.
	 *
	 * @param request The RegisterRequest containing user registration details.
	 * @return ResponseEntity containing a ResultResponse object with the registered employee details, or an error response.
	 */
	@PostMapping("/register")
	public ResponseEntity<ResultResponse<Employee>> register(@ModelAttribute RegisterRequest request) {
	    try {
	        Employee emp = authService.registerUser(request);
	        ResultResponse<Employee> response = ResultResponse.<Employee>builder()
	                .success(true)
	                .message("Registration successful")
	                .data(emp)
	                .timestamp(LocalDateTime.now())
	                .build();
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    } catch (IllegalStateException e) {
	        // Handle the specific case where an admin already exists
	        ResultResponse<Employee> errorResponse = ResultResponse.<Employee>builder()
	                .success(false)
	                .message(e.getMessage())
	                .data(null)
	                .timestamp(LocalDateTime.now())
	                .build();
	        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	    } catch (Exception e) {
	        // Handle other exceptions
	        ResultResponse<Employee> errorResponse = ResultResponse.<Employee>builder()
	                .success(false)
	                .message(e.getMessage())
	                .data(null)
	                .timestamp(LocalDateTime.now())
	                .build();
	        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	    }
	}


    
    
    
}