package com.example.notification.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import javax.crypto.SecretKey;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
 

 
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
 
@Component
public class JwtUtil {
 
    private final String secretKey = "MOrUDS2Ef+DrJbg9J/Zho2o9Mcx6vPewlocR//VNhPbXBi83B2fuI5MH5MR1oRw8";
    private static final long EXPIRATION_TIME = 3600000; // 1 hour in milliseconds
 
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
 
//    // Generate a JWT token
//    public String generateToken(String subject, Role role, String userId) {
//        Map<String, String> claims = new HashMap<>();
//        claims.put("userId", userId);
//        claims.put("email", subject);
//        claims.put("role", role.toString());
//        System.out.print(subject);
//        return Jwts.builder()
//                .setSubject(subject)
//                .setIssuedAt(new Date())
//                .setClaims(claims)
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(getSigningKey(), SignatureAlgorithm.HS384) // Ensure HS384 is used
//                .compact();
//    }
    
 
    // Generate a JWT token with email as subject, role, and userId as claims
    public String generateToken(String email, String role, String userId) {
        Map<String, String> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", email);
        claims.put("role", role);
 
        System.out.print(email);
 
        return Jwts.builder()
                .setSubject(email) // Use email as the subject
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS384)
                .compact();
    }
 
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
 
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }
 
    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }
 
    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }
 
    public String extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", String.class));
    }
 
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }
 
    public boolean isTokenValid(String token, String subject) {
        final String extractedSubject = extractEmail(token);
        return (extractedSubject.equals(subject) && !isTokenExpired(token));
    }
 
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
 
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
 
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    // Validate the token using email as the identifier
    public Boolean validateToken(String token, String email, String role) {
        final String extractedEmail = extractEmail(token);
        final String extractedRole = extractRole(token);
        return (extractedEmail.equals(email) && extractedRole.equals(role) && !isTokenExpired(token));
    }
}

