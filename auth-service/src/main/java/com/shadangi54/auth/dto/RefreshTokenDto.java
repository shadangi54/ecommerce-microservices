package com.shadangi54.auth.dto;

import lombok.Data;

@Data
public class RefreshTokenDto {
    private String refreshToken;
    
    // Explicit getter and setter
    public String getRefreshToken() {
        return refreshToken;
    }
    
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
