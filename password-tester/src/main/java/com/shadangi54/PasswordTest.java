package com.shadangi54;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Test passwords from data.sql
        String adminStoredHash = "$2a$10$ixlPY3AAd4ty1l6E2IsQ9OFZi2ba9ZQE0bP7RFcGIWNhyFrrT3YUi";
        String adminPassword = "admin123";
        
        String userStoredHash = "$2a$10$4FVMxV8ynZ41/dTRlCyZ2.5Jt1eSPMagm9Z5PQJzVB/B904tLEfxa";
        String userPassword = "user123";
        
        String customerStoredHash = "$2a$10$7euJCPcUQBIHC8Pgm9yXkOzQZtSIw5.89yux/8uGG52tCDFtLmZ/C";
        String customerPassword = "customer123";
        
        // Test if passwords match the stored hashes
        System.out.println("Testing existing password hashes from data.sql:");
        System.out.println("admin/admin123 matches: " + encoder.matches(adminPassword, adminStoredHash));
        System.out.println("testuser/user123 matches: " + encoder.matches(userPassword, userStoredHash));
        System.out.println("customer/customer123 matches: " + encoder.matches(customerPassword, customerStoredHash));
        
        // Generate new hashes for comparison
        System.out.println("\nGenerating new password hashes:");
        System.out.println("New hash for admin123: " + encoder.encode(adminPassword));
        System.out.println("New hash for user123: " + encoder.encode(userPassword));
        System.out.println("New hash for customer123: " + encoder.encode(customerPassword));
    }
}
