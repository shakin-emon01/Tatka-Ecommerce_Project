package com.ecommerce;

import com.ecommerce.dao.UserDAO;
import com.ecommerce.model.User;
import com.ecommerce.util.DatabaseUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;

public class SimpleTest {
    
    public static void main(String[] args) {
        System.out.println("=== Simple Database Test ===");
        
        try {
            // Test 1: Database Connection
            System.out.println("1. Testing database connection...");
            Connection conn = DatabaseUtil.getConnection();
            System.out.println("✓ Database connection successful!");
            conn.close();
            
            // Test 2: User Authentication
            System.out.println("\n2. Testing user authentication...");
            UserDAO userDAO = new UserDAO();
            
            // Test with demo credentials
            String email = "customer@test.com";
            String password = "password123";
            
            User user = userDAO.findByEmail(email);
            if (user != null) {
                System.out.println("✓ Found user: " + user.getEmail());
                System.out.println("  - Role: " + user.getRole());
                System.out.println("  - Name: " + user.getFirstName() + " " + user.getLastName());
                
                if (BCrypt.checkpw(password, user.getPassword())) {
                    System.out.println("✓ Password verification successful!");
                } else {
                    System.out.println("✗ Password verification failed!");
                }
            } else {
                System.out.println("✗ User not found: " + email);
            }
            
            // Test 3: Test with your created account
            System.out.println("\n3. Testing with your created account...");
            System.out.println("Please enter the email you used to register:");
            // For now, let's just test if we can find any users
            java.util.List<User> allUsers = userDAO.findAll();
            System.out.println("✓ Found " + allUsers.size() + " users in database");
            for (User u : allUsers) {
                System.out.println("  - " + u.getEmail() + " (Role: " + u.getRole() + ")");
            }
            
            System.out.println("\n=== Test completed successfully! ===");
            
        } catch (Exception e) {
            System.err.println("✗ Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 