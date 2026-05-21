package com.cts.employeeservice.security;


import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cts.employeeservice.repository.EmployeeRepository;
import com.cts.employeeservice.service.AuthService;


import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
 
@Component
//@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
 
	@Lazy
	@Autowired
    private JwtUtil jwtUtil;
	
	@Lazy
	@Autowired
    private AuthService authService;
	
	
   
	
	
	 /**
     * Performs JWT authentication for each incoming request.
     *
     * @param request     The HttpServletRequest.
     * @param response    The HttpServletResponse.
     * @param filterChain The FilterChain for passing the request along the filter chain.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException      If an I/O error occurs.
     */
// 
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//            throws ServletException, IOException {
//    	
// 
//        String token = request.getHeader("Authorization");
////    	System.out.print(token);
//        if (token != null && token.startsWith("Bearer ")) {
//            token = token.substring(7);
//            try {
//                String email = jwtUtil.extractEmail(token);
//                String role = jwtUtil.extractRole(token);
//                String userId = jwtUtil.extractUserId(token);
//                
//                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                    if (jwtUtil.isTokenValid(token, email)) {
//                        UserDetails userDetails = authService.loadUserByUsername(email); 
//                        
//                        UsernamePasswordAuthenticationToken authToken =
//                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                        
//                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                        SecurityContextHolder.getContext().setAuthentication(authToken);
//                        
//
//                        request.setAttribute("userId", userId);
//                        request.setAttribute("email", email);
//                        request.setAttribute("role", role);
//                    }
//                }
//
//            } catch (ExpiredJwtException e) {
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
//                return;
//            }
//        }
//        chain.doFilter(request, response);
//    }
//    
    
    
    
	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        if (StringUtils.isEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authorizationHeader.substring(7);

        if (StringUtils.isEmpty(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtUtil.extractEmail(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        	 UserDetails userDetails = authService.loadUserByUsername(email);

            // Extract the role from UserDetails
            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(authority -> authority.getAuthority().replace("ROLE_", "")) // Remove "ROLE_" prefix if present
                    .orElse(null);

            if (role != null && jwtUtil.validateToken(token, email, role)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}