package com.shadangi54.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shadangi54.auth.dto.LoginDto;
import com.shadangi54.auth.dto.RefreshTokenDto;
import com.shadangi54.auth.manager.TokenService;
import com.shadangi54.auth.manager.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto) {
        try {
            System.out.println("Attempting to authenticate user: " + loginDto.getUsername());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            String jwt = tokenService.generateToken(authentication);
            System.out.println("Authentication successful for user: " + loginDto.getUsername());
            
            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (Exception e) {
            System.out.println("Authentication failed for user: " + loginDto.getUsername());
            e.printStackTrace(); // Print the stack trace for detailed error info
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Authentication failed", e.getMessage()));
        }
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenDto refreshTokenDto) {
        try {
            String newToken = tokenService.refreshToken(refreshTokenDto.getRefreshToken());
            return ResponseEntity.ok(new JwtResponse(newToken));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Token refresh failed", e.getMessage()));
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new MessageResponse("User logged out successfully"));
    }
    
    // Response classes
    static class JwtResponse {
        private String token;
        private String type = "Bearer";
        
        public JwtResponse(String token) {
            this.token = token;
        }
        
        public String getToken() {
            return token;
        }
        
        public void setToken(String token) {
            this.token = token;
        }
        
        public String getType() {
            return type;
        }
        
        public void setType(String type) {
            this.type = type;
        }
    }
    
    static class ErrorResponse {
        private String error;
        private String message;
        
        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }
        
        public String getError() {
            return error;
        }
        
        public void setError(String error) {
            this.error = error;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }
    
    static class MessageResponse {
        private String message;
        
        public MessageResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }
}
