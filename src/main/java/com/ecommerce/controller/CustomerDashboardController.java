package com.ecommerce.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.ecommerce.dao.*;
import com.ecommerce.model.*;
import com.ecommerce.ECommerceApp;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class CustomerDashboardController {

    @FXML private TextField searchField;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private GridPane productsGrid;
    @FXML private VBox cartItemsContainer;
    @FXML private Label totalItemsLabel;
    @FXML private Label totalAmountLabel;
    @FXML private Button cartButton;
    @FXML private MenuButton userMenuButton;
    @FXML private TabPane mainTabPane;
    @FXML private TableView<Order> ordersTable;
    @FXML private TableColumn<Order, Integer> orderIdColumn;
    @FXML private TableColumn<Order, String> orderDateColumn;
    @FXML private TableColumn<Order, BigDecimal> orderTotalColumn;
    @FXML private TableColumn<Order, String> orderStatusColumn;
    @FXML private TableColumn<Order, Void> orderActionsColumn;
    @FXML private Label statusLabel;
    
    // Profile fields
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextArea addressField;
    @FXML private TextField cityField;
    @FXML private TextField stateField;
    @FXML private TextField zipCodeField;

    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    private OrderDAO orderDAO;
    private UserDAO userDAO;
    private User currentUser;
    private Map<Integer, Integer> cart; // productId -> quantity
    private ObservableList<Order> orders;

    @FXML
    public void initialize() {
        try {
            System.out.println("Starting dashboard initialization...");
            
            currentUser = ECommerceApp.getCurrentUser();
            if (currentUser == null) {
                showError("No user session found. Please login again.");
                return;
            }
            System.out.println("Current user: " + currentUser.getEmail());
            
            cart = new HashMap<>();
            
            // Initialize DAOs
            System.out.println("Initializing DAOs...");
            productDAO = new ProductDAO();
            categoryDAO = new CategoryDAO();
            orderDAO = new OrderDAO();
            userDAO = new UserDAO();
            System.out.println("DAOs initialized successfully");
            
            // Setup UI
            System.out.println("Setting up user menu...");
            setupUserMenu();
            System.out.println("Setting up category combo box...");
            setupCategoryComboBox();
            System.out.println("Setting up orders table...");
            setupOrdersTable();
            System.out.println("Loading products...");
            loadProducts();
            System.out.println("Loading user profile...");
            loadUserProfile();
            System.out.println("Loading user orders...");
            loadUserOrders();
            
            // Update cart button
            System.out.println("Updating cart button...");
            updateCartButton();
            
            System.out.println("Dashboard initialization completed successfully");
            
        } catch (Exception e) {
            System.err.println("Dashboard initialization failed: " + e.getMessage());
            e.printStackTrace();
            showError("Failed to initialize dashboard: " + e.getMessage());
        }
    }

    private void setupUserMenu() {
        if (currentUser != null) {
            userMenuButton.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        } else {
            userMenuButton.setText("User");
        }
        
        // Add menu items programmatically to ensure they work
        if (userMenuButton.getItems().isEmpty()) {
            MenuItem profile = new MenuItem("My Profile");
            profile.setOnAction(e -> showProfile());
            MenuItem orders = new MenuItem("My Orders");
            orders.setOnAction(e -> showOrders());
            MenuItem logout = new MenuItem("Logout");
            logout.setOnAction(e -> handleLogout());
            userMenuButton.getItems().addAll(profile, orders, new SeparatorMenuItem(), logout);
        }
        
        // Ensure MenuButton is properly configured
        userMenuButton.setDisable(false);
        userMenuButton.setVisible(true);
        
        // Add debug information
        System.out.println("Customer MenuButton setup completed:");
        System.out.println("  - Text: " + userMenuButton.getText());
        System.out.println("  - Style classes: " + userMenuButton.getStyleClass());
        System.out.println("  - Items count: " + userMenuButton.getItems().size());
        System.out.println("  - Is showing: " + userMenuButton.isShowing());
        System.out.println("  - Is disabled: " + userMenuButton.isDisabled());
        System.out.println("  - Is visible: " + userMenuButton.isVisible());
        
        // Add a test event handler to verify the MenuButton is working
        userMenuButton.setOnShowing(e -> {
            System.out.println("Customer MenuButton dropdown is showing!");
        });
        
        userMenuButton.setOnHidden(e -> {
            System.out.println("Customer MenuButton dropdown is hidden!");
        });
    }

    private void setupCategoryComboBox() {
        try {
            List<Category> categories = categoryDAO.findAll();
            categoryComboBox.setItems(FXCollections.observableArrayList(categories));
        } catch (Exception e) {
            System.err.println("Error setting up category combo box: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private void setupOrdersTable() {
        orders = FXCollections.observableArrayList();
        ordersTable.setItems(orders);
        
        orderIdColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        orderDateColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getCreatedAt().toString()));
        orderTotalColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getTotalAmount()));
        orderStatusColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));
        
        // Add view details button to actions column
        orderActionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button viewButton = new Button("View Details");
            {
                viewButton.setOnAction(event -> {
                    Order order = getTableView().getItems().get(getIndex());
                    viewOrderDetails(order);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(viewButton);
                }
            }
        });
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().trim();
        if (!searchTerm.isEmpty()) {
            List<Product> products = productDAO.searchByName(searchTerm);
            displayProducts(products);
            statusLabel.setText("Search results for: " + searchTerm);
        }
    }

    @FXML
    private void handleCategoryFilter() {
        Category selectedCategory = categoryComboBox.getValue();
        if (selectedCategory != null) {
            List<Product> products = productDAO.findByCategory(selectedCategory.getId());
            displayProducts(products);
            statusLabel.setText("Filtered by category: " + selectedCategory.getName());
        }
    }

    @FXML
    private void clearFilters() {
        categoryComboBox.setValue(null);
        searchField.clear();
        loadProducts();
        statusLabel.setText("Filters cleared");
    }

    private void loadProducts() {
        try {
            List<Product> products = productDAO.findAll();
            displayProducts(products);
            statusLabel.setText("Loaded " + products.size() + " products");
        } catch (Exception e) {
            System.err.println("Error loading products: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private void displayProducts(List<Product> products) {
        productsGrid.getChildren().clear();
        
        int col = 0;
        int row = 0;
        int maxCols = 4;
        
        for (Product product : products) {
            VBox productCard = createProductCard(product);
            productsGrid.add(productCard, col, row);
            
            col++;
            if (col >= maxCols) {
                col = 0;
                row++;
            }
        }
    }

    private VBox createProductCard(Product product) {
        VBox card = new VBox(10);
        card.getStyleClass().addAll("product-card");
        card.setPrefWidth(250);
        card.setPrefHeight(350);
        
        // Product image
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        
        // Try to load product image using imageUrl, fallback to placeholder
        try {
            String imageUrl = product.getImageUrl();
            if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                Image image = new Image(imageUrl, 200, 150, true, true);
                imageView.setImage(image);
            } else {
                Image placeholder = new Image(getClass().getResourceAsStream("/images/placeholder.jpg"));
                imageView.setImage(placeholder);
            }
        } catch (Exception e) {
            Image placeholder = new Image(getClass().getResourceAsStream("/images/placeholder.jpg"));
            imageView.setImage(placeholder);
        }
        
        // Product info
        Label nameLabel = new Label(product.getName());
        nameLabel.getStyleClass().add("label-subtitle");
        nameLabel.setWrapText(true);
        
        Label priceLabel = new Label("$" + product.getPrice());
        priceLabel.getStyleClass().add("label-title");
        
        Label stockLabel = new Label(product.getStockStatus());
        stockLabel.getStyleClass().add(product.isInStock() ? "status-success" : "status-danger");
        
        // Add to cart button
        Button addToCartButton = new Button("Add to Cart");
        addToCartButton.getStyleClass().add("button");
        addToCartButton.setDisable(!product.isInStock());
        addToCartButton.setOnAction(e -> addToCart(product));
        
        card.getChildren().addAll(imageView, nameLabel, priceLabel, stockLabel, addToCartButton);
        card.setAlignment(javafx.geometry.Pos.CENTER);
        
        return card;
    }

    private void addToCart(Product product) {
        int currentQuantity = cart.getOrDefault(product.getId(), 0);
        cart.put(product.getId(), currentQuantity + 1);
        
        updateCartButton();
        updateCartDisplay();
        
        statusLabel.setText("Added " + product.getName() + " to cart");
        
        // Show confirmation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Added to Cart");
        alert.setHeaderText(null);
        alert.setContentText(product.getName() + " has been added to your cart!");
        alert.showAndWait();
    }

    private void updateCartButton() {
        int totalItems = cart.values().stream().mapToInt(Integer::intValue).sum();
        cartButton.setText("Cart (" + totalItems + ")");
    }

    private void updateCartDisplay() {
        cartItemsContainer.getChildren().clear();
        
        BigDecimal totalAmount = BigDecimal.ZERO;
        int totalItems = 0;
        
        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            Product product = productDAO.findById(entry.getKey());
            if (product != null) {
                int quantity = entry.getValue();
                BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
                totalAmount = totalAmount.add(itemTotal);
                totalItems += quantity;
                
                HBox cartItem = createCartItem(product, quantity);
                cartItemsContainer.getChildren().add(cartItem);
            }
        }
        
        totalItemsLabel.setText(String.valueOf(totalItems));
        totalAmountLabel.setText("$" + totalAmount);
    }

    private HBox createCartItem(Product product, int quantity) {
        HBox item = new HBox(15);
        item.getStyleClass().add("card");
        item.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        // Product image
        ImageView imageView = new ImageView();
        imageView.setFitWidth(60);
        imageView.setFitHeight(60);
        imageView.setPreserveRatio(true);
        
        // Try to load product image using imageUrl, fallback to placeholder
        try {
            String imageUrl = product.getImageUrl();
            if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                Image image = new Image(imageUrl, 60, 60, true, true);
                imageView.setImage(image);
            } else {
                Image placeholder = new Image(getClass().getResourceAsStream("/images/placeholder.jpg"));
                imageView.setImage(placeholder);
            }
        } catch (Exception e) {
            Image placeholder = new Image(getClass().getResourceAsStream("/images/placeholder.jpg"));
            imageView.setImage(placeholder);
        }
        
        // Product info
        VBox info = new VBox(5);
        Label nameLabel = new Label(product.getName());
        nameLabel.getStyleClass().add("label-subtitle");
        Label priceLabel = new Label("$" + product.getPrice());
        
        info.getChildren().addAll(nameLabel, priceLabel);
        
        // Quantity controls
        HBox quantityBox = new HBox(10);
        Button decreaseBtn = new Button("-");
        Button increaseBtn = new Button("+");
        Label quantityLabel = new Label(String.valueOf(quantity));
        
        decreaseBtn.setOnAction(e -> updateQuantity(product.getId(), quantity - 1));
        increaseBtn.setOnAction(e -> updateQuantity(product.getId(), quantity + 1));
        
        quantityBox.getChildren().addAll(decreaseBtn, quantityLabel, increaseBtn);
        quantityBox.setAlignment(javafx.geometry.Pos.CENTER);
        
        // Remove button
        Button removeBtn = new Button("Remove");
        removeBtn.getStyleClass().add("button-danger");
        removeBtn.setOnAction(e -> removeFromCart(product.getId()));
        
        item.getChildren().addAll(imageView, info, quantityBox, removeBtn);
        
        return item;
    }

    private void updateQuantity(int productId, int newQuantity) {
        if (newQuantity <= 0) {
            cart.remove(productId);
        } else {
            cart.put(productId, newQuantity);
        }
        
        updateCartButton();
        updateCartDisplay();
    }

    private void removeFromCart(int productId) {
        cart.remove(productId);
        updateCartButton();
        updateCartDisplay();
    }

    @FXML
    private void clearCart() {
        cart.clear();
        updateCartButton();
        updateCartDisplay();
        statusLabel.setText("Cart cleared");
    }

    @FXML
    private void proceedToCheckout() {
        if (cart.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Cart");
            alert.setHeaderText(null);
            alert.setContentText("Your cart is empty. Please add some products first.");
            alert.showAndWait();
            return;
        }
        
        // TODO: Implement checkout process
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Checkout");
        alert.setHeaderText(null);
        alert.setContentText("Checkout functionality will be implemented in the next version.");
        alert.showAndWait();
    }

    private void loadUserProfile() {
        firstNameField.setText(currentUser.getFirstName());
        lastNameField.setText(currentUser.getLastName());
        emailField.setText(currentUser.getEmail());
        phoneField.setText(currentUser.getPhone());
        addressField.setText(currentUser.getAddress());
        cityField.setText(currentUser.getCity());
        stateField.setText(currentUser.getState());
        zipCodeField.setText(currentUser.getZipCode());
    }

    @FXML
    private void updateProfile() {
        // TODO: Implement profile update
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Profile Updated");
        alert.setHeaderText(null);
        alert.setContentText("Profile update functionality will be implemented in the next version.");
        alert.showAndWait();
    }

    @FXML
    private void changePassword() {
        // TODO: Implement password change
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Change Password");
        alert.setHeaderText(null);
        alert.setContentText("Password change functionality will be implemented in the next version.");
        alert.showAndWait();
    }

    private void loadUserOrders() {
        try {
            List<Order> userOrders = orderDAO.findByUserId(currentUser.getId());
            orders.clear();
            orders.addAll(userOrders);
        } catch (Exception e) {
            System.err.println("Error loading user orders: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private void viewOrderDetails(Order order) {
        // TODO: Implement order details view
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Details");
        alert.setHeaderText("Order #" + order.getId());
        alert.setContentText("Order details functionality will be implemented in the next version.");
        alert.showAndWait();
    }

    @FXML
    private void showProducts() {
        mainTabPane.getSelectionModel().select(0);
    }

    @FXML
    private void showCategories() {
        // TODO: Implement categories view
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Categories");
        alert.setHeaderText(null);
        alert.setContentText("Categories view will be implemented in the next version.");
        alert.showAndWait();
    }

    @FXML
    private void showOrders() {
        mainTabPane.getSelectionModel().select(2);
    }

    @FXML
    private void showProfile() {
        mainTabPane.getSelectionModel().select(3);
    }

    @FXML
    private void showCart() {
        mainTabPane.getSelectionModel().select(1);
    }

    @FXML
    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        
        if (alert.showAndWait().orElse(null) == ButtonType.OK) {
            try {
                // Clear the current user session
                ECommerceApp.setCurrentUser(null);
                
                // Clear cart
                if (cart != null) {
                    cart.clear();
                }
                
                // Load login screen
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
                Parent root = loader.load();
                
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
                
                Stage stage = (Stage) mainTabPane.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("E-Store - Login");
                
                System.out.println("User logged out successfully");
                
            } catch (IOException e) {
                System.err.println("Error during logout: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 