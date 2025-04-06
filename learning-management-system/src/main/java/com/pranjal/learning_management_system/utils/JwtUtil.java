package com.pranjal.learning_management_system.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import service.UserService;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class JwtUtil {

    private final UserService userService;

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;

    public JwtUtil(UserService userService) {
        this.userService = userService;
    }

    private SecretKey getSigningKey() {
        log.debug("Generating signing key from secret");
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(String username) {
        log.info("=== Starting token generation for username: {} ===", username);
        
        try {
            Map<String, Object> claims = new HashMap<>();
            log.debug("Finding user details for token claims");
            var user = userService.findByUsername(username);
            
            claims.put("role", user.getRole().toString());
            claims.put("email", user.getEmail());
            claims.put("userId", user.getUserId());
            
            log.debug("Claims prepared - Role: {}, Email: {}, UserId: {}", 
                    user.getRole(), 
                    user.getEmail(), 
                    user.getUserId());
            
            String token = createToken(claims, username);
            log.info("Token generated successfully. Token length: {}", token.length());
            log.debug("Generated token: {}", token);
            return token;
        } catch (Exception e) {
            log.error("Error generating token for user: {} - Error: {}", username, e.getMessage(), e);
            throw e;
        }
    }

    public boolean validateToken(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return tokenUsername.equals(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        log.debug("Creating JWT token for subject: {} with claims: {}", subject, claims);
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
        
        log.debug("Token details - IssuedAt: {}, ExpiresAt: {}", now, expiryDate);
        
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        
        log.debug("Token created successfully. Token: {}", token);
        return token;
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
