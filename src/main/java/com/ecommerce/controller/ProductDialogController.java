package com.ecommerce.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.ecommerce.dao.CategoryDAO;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

public class ProductDialogController {

    @FXML private Label dialogTitle;
    @FXML private TextField nameField;
    @FXML private TextArea descriptionField;
    @FXML private TextField priceField;
    @FXML private TextField stockField;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private TextField imageUrlField;
    @FXML private ImageView imagePreview;
    @FXML private Label imageStatusLabel;
    @FXML private Label statusLabel;
    @FXML public ButtonType saveButton;

    private CategoryDAO categoryDAO;
    private Product product;
    private boolean isEditMode = false;

    @FXML
    public void initialize() {
        categoryDAO = new CategoryDAO();
        loadCategories();
        setupValidation();
        setupImagePreview();
    }

    private void loadCategories() {
        List<Category> categories = categoryDAO.findAll();
        ObservableList<Category> categoryList = FXCollections.observableArrayList(categories);
        categoryComboBox.setItems(categoryList);
    }

    private void setupValidation() {
        // Price validation - only allow numbers and decimal point
        priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                priceField.setText(oldValue);
            }
        });

        // Stock validation - only allow numbers
        stockField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                stockField.setText(oldValue);
            }
        });
    }

    private void setupImagePreview() {
        imageUrlField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.trim().isEmpty()) {
                loadImagePreview(newValue);
            } else {
                clearImagePreview();
            }
        });
    }

    @FXML
    private void browseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Product Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        Stage stage = (Stage) nameField.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            String imageUrl = selectedFile.toURI().toString();
            imageUrlField.setText(imageUrl);
            loadImagePreview(imageUrl);
        }
    }

    private void loadImagePreview(String imageUrl) {
        try {
            Image image = new Image(imageUrl);
            imagePreview.setImage(image);
            imageStatusLabel.setText("Image loaded successfully");
            imageStatusLabel.setStyle("-fx-text-fill: green;");
        } catch (Exception e) {
            imagePreview.setImage(null);
            imageStatusLabel.setText("Failed to load image: " + e.getMessage());
            imageStatusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private void clearImagePreview() {
        imagePreview.setImage(null);
        imageStatusLabel.setText("No image selected");
        imageStatusLabel.setStyle("-fx-text-fill: gray;");
    }

    public void setProduct(Product product) {
        this.product = product;
        this.isEditMode = (product != null);
        
        if (isEditMode) {
            dialogTitle.setText("Edit Product");
            populateFields();
        } else {
            dialogTitle.setText("Add New Product");
            clearFields();
        }
    }

    private void populateFields() {
        nameField.setText(product.getName());
        descriptionField.setText(product.getDescription());
        priceField.setText(product.getPrice().toString());
        stockField.setText(String.valueOf(product.getStockQuantity()));
        imageUrlField.setText(product.getImageUrl());
        
        // Set category
        if (product.getCategoryId() > 0) {
            Category category = categoryDAO.findById(product.getCategoryId());
            if (category != null) {
                categoryComboBox.setValue(category);
            }
        }
        
        // Load image preview
        if (product.getImageUrl() != null && !product.getImageUrl().trim().isEmpty()) {
            loadImagePreview(product.getImageUrl());
        }
    }

    private void clearFields() {
        nameField.clear();
        descriptionField.clear();
        priceField.clear();
        stockField.clear();
        imageUrlField.clear();
        categoryComboBox.setValue(null);
        clearImagePreview();
    }

    public Product getProduct() {
        if (!validateInput()) {
            return null;
        }

        if (product == null) {
            product = new Product();
        }

        product.setName(nameField.getText().trim());
        product.setDescription(descriptionField.getText().trim());
        product.setPrice(new BigDecimal(priceField.getText().trim()));
        product.setStockQuantity(Integer.parseInt(stockField.getText().trim()));
        product.setImageUrl(imageUrlField.getText().trim());

        Category selectedCategory = categoryComboBox.getValue();
        if (selectedCategory != null) {
            product.setCategoryId(selectedCategory.getId());
            product.setCategoryName(selectedCategory.getName());
        }

        return product;
    }

    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();

        if (nameField.getText().trim().isEmpty()) {
            errors.append("Product name is required.\n");
        }

        if (priceField.getText().trim().isEmpty()) {
            errors.append("Price is required.\n");
        } else {
            try {
                BigDecimal price = new BigDecimal(priceField.getText().trim());
                if (price.compareTo(BigDecimal.ZERO) < 0) {
                    errors.append("Price must be positive.\n");
                }
            } catch (NumberFormatException e) {
                errors.append("Invalid price format.\n");
            }
        }

        if (stockField.getText().trim().isEmpty()) {
            errors.append("Stock quantity is required.\n");
        } else {
            try {
                int stock = Integer.parseInt(stockField.getText().trim());
                if (stock < 0) {
                    errors.append("Stock quantity must be non-negative.\n");
                }
            } catch (NumberFormatException e) {
                errors.append("Invalid stock quantity format.\n");
            }
        }

        if (categoryComboBox.getValue() == null) {
            errors.append("Category is required.\n");
        }

        if (errors.length() > 0) {
            statusLabel.setText(errors.toString());
            statusLabel.setStyle("-fx-text-fill: red;");
            return false;
        }

        statusLabel.setText("");
        return true;
    }
} 