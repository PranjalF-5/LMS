package controller;

import com.pranjal.learning_management_system.dto.LoginRequestDTO;
import com.pranjal.learning_management_system.dto.OtpVerificationDTO;
import com.pranjal.learning_management_system.dto.UserRegistrationDTO;
import com.pranjal.learning_management_system.utils.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import service.OtpService;
import service.UserService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final OtpService otpService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    // Login endpoint - public access
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        log.info("=== Starting login process for username: {} ===", loginRequest.getUsername());
        
        try {
            // Create authentication token
            log.debug("Creating authentication token with username: {}", loginRequest.getUsername());
            UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            
            // Attempt authentication
            log.debug("Attempting authentication with AuthenticationManager");
            Authentication authentication = authenticationManager.authenticate(authToken);
            log.info("Authentication successful for user: {}", loginRequest.getUsername());

            // Get the authenticated user
            log.debug("Retrieving user details from UserService");
            User authenticatedUser = userService.findByUsername(loginRequest.getUsername());
            log.info("Retrieved user details - Username: {}, Role: {}, UserId: {}", 
                    authenticatedUser.getUsername(), 
                    authenticatedUser.getRole(),
                    authenticatedUser.getUserId());

            // Generate JWT token
            log.debug("Generating JWT token");
            String jwt = jwtUtil.generateToken(loginRequest.getUsername());
            log.info("JWT token generated successfully. Token length: {}", jwt.length());
            log.debug("Generated JWT token: {}", jwt);
            
            // Create response
            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", jwt);
            response.put("tokenType", "Bearer");
            response.put("username", authenticatedUser.getUsername());
            response.put("role", authenticatedUser.getRole().toString());
            response.put("email", authenticatedUser.getEmail());
            response.put("userId", authenticatedUser.getUserId());
            
            log.info("=== Login process completed successfully for user: {} with role: {} ===", 
                    loginRequest.getUsername(), 
                    authenticatedUser.getRole());
            log.debug("Response payload: {}", response);
            
            return ResponseEntity.ok(response);
            
        } catch (BadCredentialsException e) {
            log.error("Invalid credentials provided for username: {}", loginRequest.getUsername());
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid username or password");
                
        } catch (AuthenticationException e) {
            log.error("Authentication failed for username: {} - Error: {}", 
                    loginRequest.getUsername(), 
                    e.getMessage(), 
                    e);
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Authentication failed: " + e.getMessage());
                
        } catch (Exception e) {
            log.error("Unexpected error during login for username: {} - Error: {}", 
                    loginRequest.getUsername(), 
                    e.getMessage(), 
                    e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    // Register a new user - public access
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        log.debug("Attempting to register new user with username: {}", registrationDTO.getUsername());
        User user = userService.registerUser(registrationDTO);
        log.debug("Successfully registered user: {}", user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    // Initiate password reset - public access
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        log.info("Password reset requested for email: {}", email);
        
        try {
            // Verify if user exists
            User user = userService.findByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No user found with this email address");
            }

            // Generate and store OTP
            String otp = otpService.generateOtp(email);
            
            // In a real application, you would send this OTP via email
            // For now, we're just logging it to console (already done in OtpService)
            
            return ResponseEntity.ok("OTP has been sent to your email (check console for OTP)");
            
        } catch (Exception e) {
            log.error("Error in forgot password process: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error processing password reset request");
        }
    }

    // Reset password with OTP verification
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody OtpVerificationDTO verificationDTO) {
        log.info("Processing password reset with OTP for email: {}", verificationDTO.getEmail());
        
        try {
            // Validate OTP
            if (!otpService.validateOtp(verificationDTO.getEmail(), verificationDTO.getOtp())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid or expired OTP");
            }

            // Reset password
            userService.updatePassword(verificationDTO.getEmail(), verificationDTO.getNewPassword());
            
            log.info("Password successfully reset for user: {}", verificationDTO.getEmail());
            return ResponseEntity.ok("Password has been reset successfully");
            
        } catch (Exception e) {
            log.error("Error in password reset process: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error processing password reset");
        }
    }
} 