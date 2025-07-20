package com.ecommerce.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.ecommerce.model.User;

public class UserDialogController {
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private Label statusLabel;
    @FXML public Button saveButton;
    @FXML public ButtonType saveButtonType;

    private User user;
    private boolean isEditMode = false;

    @FXML
    public void initialize() {
        roleComboBox.getItems().setAll("ADMIN", "CUSTOMER");
        statusComboBox.getItems().setAll("Active", "Inactive");
        statusLabel.setText("");
    }

    public void setUser(User user) {
        this.user = user;
        this.isEditMode = (user != null);
        if (isEditMode) {
            firstNameField.setText(user.getFirstName());
            lastNameField.setText(user.getLastName());
            emailField.setText(user.getEmail());
            phoneField.setText(user.getPhone());
            roleComboBox.setValue(user.isAdmin() ? "ADMIN" : "CUSTOMER");
            statusComboBox.setValue(user.isActive() ? "Active" : "Inactive");
            passwordField.setText(""); // Do not show password
        } else {
            firstNameField.clear();
            lastNameField.clear();
            emailField.clear();
            phoneField.clear();
            passwordField.clear();
            roleComboBox.setValue("CUSTOMER");
            statusComboBox.setValue("Active");
        }
    }

    public User getUser() {
        if (!validateInput()) return null;
        if (user == null) user = new User();
        user.setFirstName(firstNameField.getText().trim());
        user.setLastName(lastNameField.getText().trim());
        user.setEmail(emailField.getText().trim());
        user.setPhone(phoneField.getText().trim());
        if (!passwordField.getText().isEmpty()) {
            user.setPassword(passwordField.getText());
        }
        user.setRole(roleComboBox.getValue().equals("ADMIN") ? "ADMIN" : "CUSTOMER");
        user.setActive(statusComboBox.getValue().equals("Active"));
        return user;
    }

    private boolean validateInput() {
        if (firstNameField.getText().trim().isEmpty()) {
            statusLabel.setText("First name is required.");
            return false;
        }
        if (lastNameField.getText().trim().isEmpty()) {
            statusLabel.setText("Last name is required.");
            return false;
        }
        if (emailField.getText().trim().isEmpty()) {
            statusLabel.setText("Email is required.");
            return false;
        }
        if (!emailField.getText().trim().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            statusLabel.setText("Invalid email address.");
            return false;
        }
        if (!isEditMode && passwordField.getText().isEmpty()) {
            statusLabel.setText("Password is required for new users.");
            return false;
        }
        statusLabel.setText("");
        return true;
    }
} 