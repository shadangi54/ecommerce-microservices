package com.shadangi54.auth.manager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {

    private final JwtEncoder encoder;
    
    @Value("${app.jwt.issuer-uri}")
    private String issuerUri;
    
    public TokenService(JwtEncoder encoder) {
        this.encoder = encoder;
    }
    
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        
        // Extract authority strings to avoid class casting issues
        String[] roles = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .toArray(String[]::new);
        
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuerUri)  // Use the configurable issuer URI
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("roles", roles)  // Use string array format
                .build();
        
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
    
    public String refreshToken(String refreshToken) {
        // In a real implementation, you would validate the refresh token
        // and generate a new access token if valid
        // For simplicity, we're just returning a new token
        Instant now = Instant.now();
        
        String[] roles = {"ROLE_USER"};
        
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuerUri)  // Use the configurable issuer URI
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject("refreshed-user")
                .claim("roles", roles)  // Use string array format
                .build();
        
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
