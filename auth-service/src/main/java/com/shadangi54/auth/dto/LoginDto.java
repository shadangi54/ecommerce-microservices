package com.shadangi54.auth.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String username;
    private String password;
    
    // Explicit getters and setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
