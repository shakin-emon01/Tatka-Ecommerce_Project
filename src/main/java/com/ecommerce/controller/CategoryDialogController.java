package com.ecommerce.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.ecommerce.model.Category;

import java.io.File;

public class CategoryDialogController {

    @FXML private Label dialogTitle;
    @FXML private TextField nameField;
    @FXML private TextArea descriptionField;
    @FXML private TextField imageUrlField;
    @FXML private ImageView imagePreview;
    @FXML private Label imageStatusLabel;
    @FXML private Label statusLabel;
    @FXML public ButtonType saveButton;

    private Category category;
    private boolean isEditMode = false;

    @FXML
    public void initialize() {
        System.out.println("CategoryDialogController initialized");
        setupImagePreview();
        setupValidation();
    }

    private void setupImagePreview() {
        System.out.println("Setting up image preview");
        imageUrlField.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Image URL changed: " + newValue);
            if (newValue != null && !newValue.trim().isEmpty()) {
                loadImagePreview(newValue);
            } else {
                clearImagePreview();
            }
        });
    }

    private void setupValidation() {
        // Add real-time validation feedback
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.trim().isEmpty()) {
                nameField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            } else {
                nameField.setStyle("");
            }
        });
    }

    @FXML
    private void browseImage() {
        System.out.println("Browse image button clicked");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Category Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        try {
            Stage stage = (Stage) nameField.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {
                System.out.println("File selected: " + selectedFile.getAbsolutePath());
                String imageUrl = selectedFile.toURI().toString();
                imageUrlField.setText(imageUrl);
                loadImagePreview(imageUrl);
            } else {
                System.out.println("No file selected");
            }
        } catch (Exception e) {
            System.err.println("Error in browseImage: " + e.getMessage());
            e.printStackTrace();
            showError("Error", "Failed to open file chooser: " + e.getMessage());
        }
    }

    private void loadImagePreview(String imageUrl) {
        System.out.println("Loading image preview: " + imageUrl);
        try {
            Image image = new Image(imageUrl);
            
            // Check if image loaded successfully
            if (image.isError()) {
                throw new Exception("Failed to load image");
            }
            
            imagePreview.setImage(image);
            imageStatusLabel.setText("Image loaded successfully");
            imageStatusLabel.setStyle("-fx-text-fill: green;");
            System.out.println("Image loaded successfully");
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
            imagePreview.setImage(null);
            imageStatusLabel.setText("Failed to load image: " + e.getMessage());
            imageStatusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private void clearImagePreview() {
        System.out.println("Clearing image preview");
        imagePreview.setImage(null);
        imageStatusLabel.setText("No image selected");
        imageStatusLabel.setStyle("-fx-text-fill: gray;");
    }

    public void setCategory(Category category) {
        System.out.println("Setting category: " + (category != null ? category.getName() : "null"));
        this.category = category;
        this.isEditMode = (category != null);
        
        if (isEditMode) {
            dialogTitle.setText("Edit Category");
            populateFields();
        } else {
            dialogTitle.setText("Add New Category");
            clearFields();
        }
    }

    private void populateFields() {
        System.out.println("Populating fields for category: " + category.getName());
        nameField.setText(category.getName());
        descriptionField.setText(category.getDescription());
        imageUrlField.setText(category.getImageUrl());
        
        // Load image preview
        if (category.getImageUrl() != null && !category.getImageUrl().trim().isEmpty()) {
            loadImagePreview(category.getImageUrl());
        }
    }

    private void clearFields() {
        System.out.println("Clearing fields");
        nameField.clear();
        descriptionField.clear();
        imageUrlField.clear();
        clearImagePreview();
    }

    public Category getCategory() {
        System.out.println("Getting category data");
        if (!validateInput()) {
            System.out.println("Validation failed");
            return null;
        }

        if (category == null) {
            category = new Category();
        }

        String name = nameField.getText().trim();
        String description = descriptionField.getText().trim();
        String imageUrl = imageUrlField.getText().trim();

        System.out.println("Category data - Name: " + name + ", Description: " + description + ", ImageUrl: " + imageUrl);

        category.setName(name);
        category.setDescription(description);
        category.setImageUrl(imageUrl);

        return category;
    }

    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();

        if (nameField.getText().trim().isEmpty()) {
            errors.append("Category name is required.\n");
        }

        if (errors.length() > 0) {
            statusLabel.setText(errors.toString());
            statusLabel.setStyle("-fx-text-fill: red;");
            return false;
        }

        statusLabel.setText("");
        return true;
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 