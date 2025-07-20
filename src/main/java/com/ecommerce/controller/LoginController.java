package com.ecommerce.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.ecommerce.dao.UserDAO;
import com.ecommerce.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private CheckBox rememberMeCheckBox;
    
    @FXML
    private Label errorLabel;
    
    private UserDAO userDAO;
    
    @FXML
    public void initialize() {
        userDAO = new UserDAO();
        
        // Add enter key support for login
        passwordField.setOnAction(event -> handleLogin());
        
        // Clear error when user starts typing
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) {
                errorLabel.setVisible(false);
            }
        });
        
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) {
                errorLabel.setVisible(false);
            }
        });
    }
    
    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        
        System.out.println("=== Login Debug ===");
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        
        // Validate input
        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Error: Empty email or password");
            showError("Please enter both email and password.");
            return;
        }
        
        if (!isValidEmail(email)) {
            System.out.println("Error: Invalid email format");
            showError("Please enter a valid email address.");
            return;
        }
        
        try {
            System.out.println("1. Looking for user in database...");
            // Find user by email
            User user = userDAO.findByEmail(email);
            
            if (user == null) {
                System.out.println("Error: User not found in database");
                showError("Invalid email or password.");
                return;
            }
            
            System.out.println("2. User found:");
            System.out.println("   - Email: " + user.getEmail());
            System.out.println("   - Role: " + user.getRole());
            System.out.println("   - Name: " + user.getFirstName() + " " + user.getLastName());
            System.out.println("   - Stored password hash: " + user.getPassword());
            
            System.out.println("3. Testing BCrypt availability...");
            try {
                System.out.println("   BCrypt class: " + (BCrypt.class != null ? "Available" : "NOT AVAILABLE"));
            } catch (Exception e) {
                System.out.println("   BCrypt class: NOT AVAILABLE - " + e.getMessage());
            }
            
            System.out.println("4. Verifying password (plain text)...");
            boolean passwordMatches = password.equals(user.getPassword());
            System.out.println("   Password verification result: " + passwordMatches);
            System.out.println("   Input password: '" + password + "'");
            System.out.println("   Stored password: '" + user.getPassword() + "'");
            
            if (!passwordMatches) {
                System.out.println("Error: Password verification failed");
                showError("Invalid email or password.");
                return;
            }
            
            System.out.println("5. Login successful! Proceeding to dashboard...");
            // Login successful
            loginSuccessful(user);
            
        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage());
            e.printStackTrace();
            showError("An error occurred during login. Please try again.");
        }
    }
    
    private void loginSuccessful(User user) {
        try {
            // Store current user in application context
            com.ecommerce.ECommerceApp.setCurrentUser(user);
            
            // Load appropriate dashboard based on user role
            String fxmlFile;
            if (user.isAdmin()) {
                fxmlFile = "/fxml/AdminDashboard.fxml";
            } else {
                fxmlFile = "/fxml/CustomerDashboard.fxml";
            }
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("E-Store - " + (user.isAdmin() ? "Admin Dashboard" : "Customer Dashboard"));
            
            // Show welcome message
            showSuccessAlert("Welcome back, " + user.getFirstName() + "!");
            
        } catch (IOException e) {
            showError("Failed to load dashboard. Please try again.");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Register.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("E-Store - Register");
            
        } catch (IOException e) {
            showError("Failed to load registration page.");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleForgotPassword() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Forgot Password");
        alert.setHeaderText("Password Reset");
        alert.setContentText("Please contact the administrator to reset your password.\n\nEmail: admin@ecommerce.com");
        alert.showAndWait();
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
} 