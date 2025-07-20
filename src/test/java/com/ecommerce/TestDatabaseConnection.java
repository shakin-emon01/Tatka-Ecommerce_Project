package com.ecommerce;

import com.ecommerce.dao.UserDAO;
import com.ecommerce.model.User;
import com.ecommerce.util.DatabaseUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;

public class TestDatabaseConnection {
    
    public static void main(String[] args) {
        System.out.println("Testing Database Connection...");
        
        try {
            // Test database connection
            Connection conn = DatabaseUtil.getConnection();
            System.out.println("✓ Database connection successful!");
            conn.close();
            
            // Test user authentication
            UserDAO userDAO = new UserDAO();
            
            // Test demo credentials
            String[] testEmails = {"admin@ecommerce.com", "customer@test.com"};
            String testPassword = "password123";
            
            for (String email : testEmails) {
                User user = userDAO.findByEmail(email);
                if (user != null) {
                    System.out.println("✓ Found user: " + user.getEmail() + " (Role: " + user.getRole() + ")");
                    
                    // Test password verification
                    if (BCrypt.checkpw(testPassword, user.getPassword())) {
                        System.out.println("✓ Password verification successful for: " + email);
                    } else {
                        System.out.println("✗ Password verification failed for: " + email);
                    }
                } else {
                    System.out.println("✗ User not found: " + email);
                }
            }
            
            // Test new user creation
            System.out.println("\nTesting new user creation...");
            User newUser = new User();
            newUser.setEmail("test@example.com");
            newUser.setPassword(BCrypt.hashpw("testpass123", BCrypt.gensalt()));
            newUser.setFirstName("Test");
            newUser.setLastName("User");
            newUser.setPhone("555-1234");
            newUser.setAddress("123 Test St");
            newUser.setCity("Test City");
            newUser.setState("TS");
            newUser.setZipCode("12345");
            newUser.setRole("CUSTOMER");
            
            boolean created = userDAO.create(newUser);
            if (created) {
                System.out.println("✓ New user created successfully!");
                
                // Test login with new user
                User foundUser = userDAO.findByEmail("test@example.com");
                if (foundUser != null && BCrypt.checkpw("testpass123", foundUser.getPassword())) {
                    System.out.println("✓ New user login test successful!");
                } else {
                    System.out.println("✗ New user login test failed!");
                }
            } else {
                System.out.println("✗ Failed to create new user!");
            }
            
        } catch (Exception e) {
            System.err.println("✗ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 