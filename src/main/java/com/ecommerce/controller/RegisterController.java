package com.ecommerce.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.ecommerce.dao.UserDAO;
import com.ecommerce.model.User;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField firstNameField;
    
    @FXML
    private TextField lastNameField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private TextField phoneField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private PasswordField confirmPasswordField;
    
    @FXML
    private CheckBox termsCheckBox;
    
    @FXML
    private CheckBox newsletterCheckBox;
    
    @FXML
    private Label errorLabel;
    
    private UserDAO userDAO;
    
    @FXML
    public void initialize() {
        userDAO = new UserDAO();
        
        // Clear error when user starts typing
        firstNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) {
                errorLabel.setVisible(false);
            }
        });
        
        lastNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) {
                errorLabel.setVisible(false);
            }
        });
        
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
    private void handleRegister() {
        // Validate input
        if (!validateInput()) {
            return;
        }
        
        try {
            // Check if email already exists
            User existingUser = userDAO.findByEmail(emailField.getText().trim());
            if (existingUser != null) {
                showError("An account with this email already exists.");
                return;
            }
            
            // Create new user using the simplified constructor
            User newUser = new User(
                emailField.getText().trim(),
                passwordField.getText(),
                firstNameField.getText().trim(),
                lastNameField.getText().trim(),
                phoneField.getText().trim()
            );
            
            // Save user to database
            boolean success = userDAO.create(newUser);
            
            if (success) {
                showSuccessAlert("Account created successfully! You can now login.");
                handleBackToLogin();
            } else {
                showError("Failed to create account. Please try again.");
            }
            
        } catch (Exception e) {
            showError("An error occurred during registration. Please try again.");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleBackToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            
            Stage stage = (Stage) firstNameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("E-Store - Login");
            
        } catch (IOException e) {
            showError("Failed to load login page.");
            e.printStackTrace();
        }
    }
    
    private boolean validateInput() {
        // Check required fields
        if (firstNameField.getText().trim().isEmpty()) {
            showError("First name is required.");
            return false;
        }
        
        if (lastNameField.getText().trim().isEmpty()) {
            showError("Last name is required.");
            return false;
        }
        
        if (emailField.getText().trim().isEmpty()) {
            showError("Email is required.");
            return false;
        }
        
        if (!isValidEmail(emailField.getText().trim())) {
            showError("Please enter a valid email address.");
            return false;
        }
        
        if (phoneField.getText().trim().isEmpty()) {
            showError("Phone number is required.");
            return false;
        }
        
        if (passwordField.getText().isEmpty()) {
            showError("Password is required.");
            return false;
        }
        
        if (passwordField.getText().length() < 6) {
            showError("Password must be at least 6 characters long.");
            return false;
        }
        
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            showError("Passwords do not match.");
            return false;
        }
        
        if (!termsCheckBox.isSelected()) {
            showError("You must agree to the Terms and Conditions.");
            return false;
        }
        
        return true;
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