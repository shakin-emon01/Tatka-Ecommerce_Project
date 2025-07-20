package com.ecommerce;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordTest {
    
    public static void main(String[] args) {
        System.out.println("=== Password Hash Test ===");
        
        String password = "password123";
        String storedHash = "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa";
        
        System.out.println("Testing password: " + password);
        System.out.println("Stored hash: " + storedHash);
        
        // Test if the stored hash matches the password
        boolean matches = BCrypt.checkpw(password, storedHash);
        System.out.println("Password matches stored hash: " + matches);
        
        // Generate a new hash for comparison
        String newHash = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println("New hash for same password: " + newHash);
        
        // Test if new hash matches the password
        boolean newMatches = BCrypt.checkpw(password, newHash);
        System.out.println("Password matches new hash: " + newMatches);
        
        // Test with wrong password
        boolean wrongMatches = BCrypt.checkpw("wrongpassword", storedHash);
        System.out.println("Wrong password matches stored hash: " + wrongMatches);
        
        System.out.println("\n=== Test completed ===");
    }
} 