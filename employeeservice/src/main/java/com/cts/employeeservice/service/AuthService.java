//package com.cts.employeeservice.service;
//
//
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import lombok.extern.slf4j.*;
//import com.cts.employeeservice.dto.AuthRequest;
//import com.cts.employeeservice.dto.AuthResponse;
//import com.cts.employeeservice.dto.RegisterRequest;
//import com.cts.employeeservice.entity.Employee;
//import com.cts.employeeservice.entity.UserPrincipal;
//import com.cts.employeeservice.enums.Role;
//import com.cts.employeeservice.exception.ResourceNotFoundException;
//import com.cts.employeeservice.repository.EmployeeRepository;
//import com.cts.employeeservice.security.JwtUtil;
// 
//@Slf4j
//@Service
////@RequiredArgsConstructor
//public class AuthService implements UserDetailsService{
// 
//	@Autowired
//    private EmployeeRepository employeeRepository;
//
//	@Autowired
//    private PasswordEncoder passwordEncoder;
//
//	@Autowired
//    private JwtUtil jwtUtil;
//
//	@Lazy
//	@Autowired
//    private AuthenticationManager authenticationManager;
// 
////    public String authenticate(AuthRequest authRequest) {
////        Authentication authentication = authenticationManager.authenticate(
////                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
////        
////        if(!authentication.isAuthenticated()) {
////        	throw new RuntimeException("Invalid Login Credentials");
////        }
//// 
////        Employee employee = employeeRepository.findByEmail(authRequest.getEmail())
////                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//// 
////        return jwtUtil.generateToken(employee.getEmail(), employee.getRole(), employee.getId());
////    }
////
////	@Override
////	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
////		
////		Employee user = employeeRepository.findByEmail(username).orElseThrow(() ->
////					new UsernameNotFoundException("No user found with email: " + username )
////				);
////		
////		return new UserPrincipal(user);
////	}
//	
//	
//	public AuthResponse authenticate(AuthRequest authRequest) {
//        String email = authRequest.getEmail();
//        String password = authRequest.getPassword();
//        Role requestedRole = authRequest.getRole();
//
//        log.info("Attempting login for email: {}", email);
//
//        Employee user = employeeRepository.findByEmail(email).orElseThrow(() -> {
//            log.warn("User not found with email: {}", email);
//            return new ResourceNotFoundException("Invalid Login Credentials!");
//        });
//
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(email, password)
//            );
//
//            if (!authentication.isAuthenticated()) {
//                log.warn("Authentication failed for email: {}", email);
//                throw new ResourceNotFoundException("Invalid Login Credentials!");
//            }
//        } catch (BadCredentialsException e) {
//            log.warn("Bad credentials for email: {}", email);
//            throw new ResourceNotFoundException("Invalid Login Credentials!");
//        }
//
//        if (user.getRole() != requestedRole) {
//            log.warn("Role mismatch for user with email: {} - Expected: {}, Found: {}",
//                     email, requestedRole, user.getRole());
//            throw new ResourceNotFoundException("Unauthorized access for the requested role.");
//        }
//
//        // Update the line where you call generateToken
//        String token = jwtUtil.generateToken(email, requestedRole.toString(), user.getId().toString());
//        log.info("Login successful for email: {} with role: {}", email, requestedRole);
//        return new AuthResponse(token, requestedRole.toString(), email);
//    }
//	
//	
//	
//	
//	
//	public Employee registerUser(RegisterRequest request) {
//	    // Check if an admin already exists
//	    if ("ADMIN".equals(request.getRole()) && employeeRepository.existsByRole("ADMIN")) {
//	        throw new IllegalStateException("An admin already exists. Only one admin can be registered.");
//	    }
//
//	    Employee emp = new Employee();
//	    emp.setContactNumber(request.getContact());
//	    emp.setDepartment(request.getDepartment());
//	    emp.setRole(request.getRole());
//	    emp.setEmail(request.getEmail());
//	    emp.setPassword(passwordEncoder.encode(request.getPassword()));
//	    emp.setEmployeeName(request.getName());
//	    return employeeRepository.save(emp);
//	}
//}


package com.cts.employeeservice.service;

import com.cts.employeeservice.dto.AuthRequest;
import com.cts.employeeservice.dto.AuthResponse;
import com.cts.employeeservice.dto.RegisterRequest;
import com.cts.employeeservice.entity.Employee;
import com.cts.employeeservice.entity.UserPrincipal;
import com.cts.employeeservice.enums.Role;
import com.cts.employeeservice.exception.ResourceNotFoundException;
import com.cts.employeeservice.repository.EmployeeRepository;
import com.cts.employeeservice.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
//@RequiredArgsConstructor // You are using @Autowired, so this might not be strictly needed
public class AuthService implements UserDetailsService { // Implement the interface

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee user = employeeRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + username));

        // Create a UserDetails object from your Employee entity.
        // You might have a separate UserPrincipal class as in your commented-out code,
        // or you can directly use Spring Security's User class.
        return new UserPrincipal(user); // Assuming you have a UserPrincipal
        // OR
        // return new User(user.getEmail(), user.getPassword(), Collections.singletonList(() -> "ROLE_" + user.getRole().name()));
    }

    public AuthResponse authenticate(AuthRequest authRequest) {
        String email = authRequest.getEmail();
        String password = authRequest.getPassword();
        Role requestedRole = authRequest.getRole();

        log.info("Attempting login for email: {}", email);

        Employee user = employeeRepository.findByEmail(email).orElseThrow(() -> {
            log.warn("User not found with email: {}", email);
            return new ResourceNotFoundException("Invalid Login Credentials!");
        });

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            if (!authentication.isAuthenticated()) {
                log.warn("Authentication failed for email: {}", email);
                throw new ResourceNotFoundException("Invalid Login Credentials!");
            }
        } catch (BadCredentialsException e) {
            log.warn("Bad credentials for email: {}", email);
            throw new ResourceNotFoundException("Invalid Login Credentials!");
        }

        if (user.getRole() != requestedRole) {
            log.warn("Role mismatch for user with email: {} - Expected: {}, Found: {}",
                     email, requestedRole, user.getRole());
            throw new ResourceNotFoundException("Unauthorized access for the requested role.");
        }

        String token = jwtUtil.generateToken(email, requestedRole.toString(), user.getId().toString());
        log.info("Login successful for email: {} with role: {}", email, requestedRole);
        return new AuthResponse(token, requestedRole.toString(), email);
    }

    public Employee registerUser(RegisterRequest request) throws IOException {
    	// Check if an admin already exists
        if (employeeRepository.existsByRole(Role.ADMIN)) {
            throw new IllegalStateException("An admin already exists. Only one admin can be registered.");
        }

        // Ensure the requested role is ADMIN
        if (request.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("Only the ADMIN role can be registered through this endpoint.");
        }

        Employee emp = new Employee();
        emp.setContactNumber(request.getContact());
        emp.setDepartment(request.getDepartment());
        emp.setRole(request.getRole());
        emp.setEmail(request.getEmail());
        emp.setPassword(passwordEncoder.encode(request.getPassword()));
        emp.setEmployeeName(request.getName());
       emp.setImage(request.getImage().getBytes());
        return employeeRepository.save(emp);
    }
}