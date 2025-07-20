package com.ecommerce;

import com.ecommerce.dao.CategoryDAO;
import com.ecommerce.dao.ProductDAO;
import com.ecommerce.dao.UserDAO;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;

import java.util.List;

public class DashboardTest {
    
    public static void main(String[] args) {
        System.out.println("=== Dashboard Components Test ===");
        
        try {
            // Test 1: Categories
            System.out.println("1. Testing categories...");
            CategoryDAO categoryDAO = new CategoryDAO();
            List<Category> categories = categoryDAO.findAll();
            System.out.println("✓ Found " + categories.size() + " categories");
            for (Category cat : categories) {
                System.out.println("  - " + cat.getName() + ": " + cat.getDescription());
            }
            
            // Test 2: Products
            System.out.println("\n2. Testing products...");
            ProductDAO productDAO = new ProductDAO();
            List<Product> products = productDAO.findAll();
            System.out.println("✓ Found " + products.size() + " products");
            for (Product prod : products) {
                System.out.println("  - " + prod.getName() + " ($" + prod.getPrice() + ") - Stock: " + prod.getStockQuantity());
            }
            
            // Test 3: Users
            System.out.println("\n3. Testing users...");
            UserDAO userDAO = new UserDAO();
            List<User> users = userDAO.findAll();
            System.out.println("✓ Found " + users.size() + " users");
            for (User user : users) {
                System.out.println("  - " + user.getEmail() + " (Role: " + user.getRole() + ")");
            }
            
            System.out.println("\n=== Dashboard components test completed! ===");
            System.out.println("If all tests passed, the dashboard should load successfully.");
            
        } catch (Exception e) {
            System.err.println("✗ Dashboard test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 