package com.shadangi54.auth.dto;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {

	private String token;
	private String type = "Bearer";
	private Long id;
	private String userName;
	private String email;
	private List<String> roles;
	
	public JwtResponse(String token, Long id, String username, String email, List<String> roles) {
        this.token = token;
        this.id = id;
        this.userName = username;
        this.email = email;
        this.roles = roles;
    }
	
	// Adding this getter specifically for Postman to capture the token
	public String getAccessToken() {
		return token;
	}
}
