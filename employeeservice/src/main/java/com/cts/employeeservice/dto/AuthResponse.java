package com.cts.employeeservice.dto;


import com.cts.employeeservice.enums.Role;


import lombok.Data;

@Data
public class AuthResponse {
	 private String token;     // The generated JWT token
	    private String role;      // The role of the authenticated user (as a String)
	    private String email;     // The email of the authenticated user
	    // You might include other relevant information like:
	    // private Long userId;
	    // private String refreshToken; // If you implement refresh tokens
	    // private String displayName;
	    // private List<String> permissions;
	    
	    

	    public AuthResponse(String token, String role, String email) {
	        this.token = token;
	        this.role = role;
	        this.email = email;
	    }
}