package com.shadangi54.auth.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTestUtil {
    
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Test the stored password hash from data.sql
        String storedHash = "$2a$10$ixlPY3AAd4ty1l6E2IsQ9OFZi2ba9ZQE0bP7RFcGIWNhyFrrT3YUi";
        String rawPassword = "admin123";
        
        boolean matches = encoder.matches(rawPassword, storedHash);
        System.out.println("Does admin123 match the stored hash? " + matches);
        
        // Generate a new hash for comparison
        String newHash = encoder.encode(rawPassword);
        System.out.println("New hash for 'admin123': " + newHash);
        System.out.println("Does new hash match raw password? " + encoder.matches(rawPassword, newHash));
        
        // Check if the encoding is consistent
        String anotherHash = encoder.encode(rawPassword);
        System.out.println("Another hash for 'admin123': " + anotherHash);
        System.out.println("Does another hash match raw password? " + encoder.matches(rawPassword, anotherHash));
    }
}
