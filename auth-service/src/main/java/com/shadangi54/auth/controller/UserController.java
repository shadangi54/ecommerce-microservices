package com.shadangi54.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shadangi54.auth.dto.UserDto;
import com.shadangi54.auth.entity.User;
import com.shadangi54.auth.manager.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        try {
            User user = userService.registerUser(userDto);
            return ResponseEntity.ok(
                    new RegisterResponse("User registered successfully", user.getUsername(), user.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Registration failed", e.getMessage()));
        }
    }

    // Response classes
    static class RegisterResponse {
        private String message;
        private String username;
        private String email;

        public RegisterResponse(String message, String username, String email) {
            this.message = message;
            this.username = username;
            this.email = email;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
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
}
