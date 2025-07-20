package com.ecommerce;

import com.ecommerce.dao.UserDAO;
import com.ecommerce.dao.ProductDAO;
import com.ecommerce.dao.CategoryDAO;
import com.ecommerce.model.User;
import com.ecommerce.util.DatabaseUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class CompleteTest {
    
    public static void main(String[] args) {
        System.out.println("=== Complete E-Commerce System Test ===");
        
        try {
            // Test 1: Database Connection
            System.out.println("\n1. Testing database connection...");
            Connection conn = DatabaseUtil.getConnection();
            System.out.println("✓ Database connection successful!");
            
            // Test 2: Check if tables exist
            System.out.println("\n2. Checking database tables...");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SHOW TABLES");
            System.out.println("Tables in database:");
            while (rs.next()) {
                System.out.println("  - " + rs.getString(1));
            }
            
            // Test 3: Check users table
            System.out.println("\n3. Checking users table...");
            rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
            if (rs.next()) {
                int userCount = rs.getInt(1);
                System.out.println("✓ Users table has " + userCount + " records");
                
                if (userCount == 0) {
                    System.out.println("⚠ WARNING: No users found! Sample data may not be loaded.");
                }
            }
            
            // Test 4: Check specific demo users
            System.out.println("\n4. Checking demo users...");
            rs = stmt.executeQuery("SELECT email, role FROM users WHERE email IN ('admin@ecommerce.com', 'customer@test.com')");
            while (rs.next()) {
                System.out.println("  ✓ Found: " + rs.getString("email") + " (Role: " + rs.getString("role") + ")");
            }
            
            // Test 5: Test user authentication
            System.out.println("\n5. Testing user authentication...");
            UserDAO userDAO = new UserDAO();
            
            String[] testEmails = {"admin@ecommerce.com", "customer@test.com"};
            String testPassword = "password123";
            
            for (String email : testEmails) {
                User user = userDAO.findByEmail(email);
                if (user != null) {
                    System.out.println("✓ Found user: " + user.getEmail());
                    System.out.println("  - Role: " + user.getRole());
                    System.out.println("  - Name: " + user.getFirstName() + " " + user.getLastName());
                    
                    // Test password verification
                    if (BCrypt.checkpw(testPassword, user.getPassword())) {
                        System.out.println("  ✓ Password verification successful!");
                    } else {
                        System.out.println("  ✗ Password verification failed!");
                        System.out.println("  - Stored hash: " + user.getPassword());
                        
                        // Generate correct hash for comparison
                        String correctHash = BCrypt.hashpw(testPassword, BCrypt.gensalt());
                        System.out.println("  - Expected hash format: " + correctHash);
                    }
                } else {
                    System.out.println("✗ User not found: " + email);
                }
            }
            
            // Test 6: Check products and categories
            System.out.println("\n6. Checking products and categories...");
            ProductDAO productDAO = new ProductDAO();
            CategoryDAO categoryDAO = new CategoryDAO();
            
            List<User> allUsers = userDAO.findAll();
            System.out.println("✓ Total users in database: " + allUsers.size());
            
            List<com.ecommerce.model.Product> products = productDAO.findAll();
            System.out.println("✓ Total products in database: " + products.size());
            
            List<com.ecommerce.model.Category> categories = categoryDAO.findAll();
            System.out.println("✓ Total categories in database: " + categories.size());
            
            conn.close();
            
            System.out.println("\n=== Test completed successfully! ===");
            
            // Summary
            System.out.println("\nSUMMARY:");
            System.out.println("- Database connection: ✓");
            System.out.println("- Users table: " + allUsers.size() + " users");
            System.out.println("- Products table: " + products.size() + " products");
            System.out.println("- Categories table: " + categories.size() + " categories");
            
            if (allUsers.size() == 0) {
                System.out.println("\n⚠ ACTION REQUIRED:");
                System.out.println("No users found in database. Please run the sample data script:");
                System.out.println("mysql -u root -p java_ecommerce < database/sample_data.sql");
            }
            
        } catch (Exception e) {
            System.err.println("✗ Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 