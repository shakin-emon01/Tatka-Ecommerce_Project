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
import com.ecommerce.dao.*;
import com.ecommerce.model.*;
import com.ecommerce.ECommerceApp;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import javafx.stage.Modality;

public class AdminDashboardController {

    @FXML private TabPane mainTabPane;
    @FXML private MenuButton userMenuButton;
    @FXML private Label statusLabel;
    
    // Dashboard statistics
    @FXML private Label totalProductsLabel;
    @FXML private Label totalOrdersLabel;
    @FXML private Label totalUsersLabel;
    @FXML private Label totalRevenueLabel;
    
    // Recent orders table
    @FXML private TableView<Order> recentOrdersTable;
    @FXML private TableColumn<Order, Integer> recentOrderIdColumn;
    @FXML private TableColumn<Order, String> recentOrderCustomerColumn;
    @FXML private TableColumn<Order, String> recentOrderDateColumn;
    @FXML private TableColumn<Order, BigDecimal> recentOrderTotalColumn;
    @FXML private TableColumn<Order, String> recentOrderStatusColumn;
    
    // Products management
    @FXML private TextField productSearchField;
    @FXML private ComboBox<Category> productCategoryFilter;
    @FXML private TableView<Product> productsTable;
    @FXML private TableColumn<Product, Integer> productIdColumn;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, String> productCategoryColumn;
    @FXML private TableColumn<Product, BigDecimal> productPriceColumn;
    @FXML private TableColumn<Product, Integer> productStockColumn;
    @FXML private TableColumn<Product, String> productStatusColumn;
    @FXML private TableColumn<Product, Void> productActionsColumn;
    @FXML private TableColumn<Product, String> productImageColumn;
    
    // Orders management
    @FXML private ComboBox<String> orderStatusFilter;
    @FXML private TableView<Order> ordersTable;
    @FXML private TableColumn<Order, Integer> orderIdColumn;
    @FXML private TableColumn<Order, String> orderCustomerColumn;
    @FXML private TableColumn<Order, String> orderDateColumn;
    @FXML private TableColumn<Order, BigDecimal> orderTotalColumn;
    @FXML private TableColumn<Order, String> orderStatusColumn;
    @FXML private TableColumn<Order, Void> orderActionsColumn;
    
    // Users management
    @FXML private TextField userSearchField;
    @FXML private ComboBox<String> userRoleFilter;
    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, Integer> userIdColumn;
    @FXML private TableColumn<User, String> userNameColumn;
    @FXML private TableColumn<User, String> userEmailColumn;
    @FXML private TableColumn<User, String> userRoleColumn;
    @FXML private TableColumn<User, String> userStatusColumn;
    @FXML private TableColumn<User, Void> userActionsColumn;
    
    // Categories management
    @FXML private TableView<Category> categoriesTable;
    @FXML private TableColumn<Category, Integer> categoryIdColumn;
    @FXML private TableColumn<Category, String> categoryNameColumn;
    @FXML private TableColumn<Category, String> categoryDescriptionColumn;
    @FXML private TableColumn<Category, Integer> categoryProductsColumn;
    @FXML private TableColumn<Category, String> categoryImageColumn;
    @FXML private TableColumn<Category, Void> categoryActionsColumn;
    
    // Admin profile fields
    @FXML private TextField adminFirstNameField;
    @FXML private TextField adminLastNameField;
    @FXML private TextField adminEmailField;
    @FXML private TextField adminPhoneField;
    @FXML private TextArea adminAddressField;
    @FXML private TextField adminCityField;
    @FXML private TextField adminStateField;
    @FXML private TextField adminZipCodeField;

    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    private OrderDAO orderDAO;
    private UserDAO userDAO;
    private User currentUser;
    private ObservableList<Product> products;
    private ObservableList<Order> orders;
    private ObservableList<User> users;
    private ObservableList<Category> categories;
    private ObservableList<Order> recentOrders;

    @FXML
    public void initialize() {
        currentUser = ECommerceApp.getCurrentUser();
        
        // Initialize DAOs
        productDAO = new ProductDAO();
        categoryDAO = new CategoryDAO();
        orderDAO = new OrderDAO();
        userDAO = new UserDAO();
        
        // Initialize observable lists
        products = FXCollections.observableArrayList();
        orders = FXCollections.observableArrayList();
        users = FXCollections.observableArrayList();
        categories = FXCollections.observableArrayList();
        recentOrders = FXCollections.observableArrayList();
        
        // Setup UI
        setupUserMenu();
        setupDashboard();
        setupProductsTable();
        setupOrdersTable();
        setupUsersTable();
        setupCategoriesTable();

        // Bind productCategoryFilter to categories list
        productCategoryFilter.setItems(categories);

        // Load initial data
        loadDashboardData();
        loadProducts();
        loadOrders();
        loadUsers();
        loadCategories();
        loadAdminProfile();
    }

    private void setupUserMenu() {
        if (currentUser != null) {
            userMenuButton.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        } else {
            userMenuButton.setText("Admin");
        }
        
        // Add menu items in code if missing
        if (userMenuButton.getItems().isEmpty()) {
            MenuItem profile = new MenuItem("My Profile");
            profile.setOnAction(e -> showProfile());
            MenuItem settings = new MenuItem("Settings");
            settings.setOnAction(e -> showSettings());
            MenuItem logout = new MenuItem("Logout");
            logout.setOnAction(e -> handleLogout());
            userMenuButton.getItems().addAll(profile, settings, new SeparatorMenuItem(), logout);
        }
        
        // Debug information
        System.out.println("MenuButton setup completed:");
        System.out.println("  - Text: " + userMenuButton.getText());
        System.out.println("  - Style classes: " + userMenuButton.getStyleClass());
        System.out.println("  - Items count: " + userMenuButton.getItems().size());
        System.out.println("  - Is showing: " + userMenuButton.isShowing());
        System.out.println("  - Is disabled: " + userMenuButton.isDisabled());
        System.out.println("  - Is visible: " + userMenuButton.isVisible());
        
        // Ensure MenuButton is properly configured
        userMenuButton.setDisable(false);
        userMenuButton.setVisible(true);
        
        // Add a test event handler to verify the MenuButton is working
        userMenuButton.setOnShowing(e -> {
            System.out.println("MenuButton dropdown is showing!");
        });
        
        userMenuButton.setOnHidden(e -> {
            System.out.println("MenuButton dropdown is hidden!");
        });
    }

    private void setupDashboard() {
        // Setup recent orders table
        recentOrdersTable.setItems(recentOrders);
        
        recentOrderIdColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        recentOrderCustomerColumn.setCellValueFactory(data -> {
            User user = userDAO.findById(data.getValue().getUserId());
            return new javafx.beans.property.SimpleStringProperty(user != null ? user.getFullName() : "Unknown");
        });
        recentOrderDateColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getCreatedAt().toString()));
        recentOrderTotalColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getTotalAmount()));
        recentOrderStatusColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));
    }

    private void setupProductsTable() {
        productsTable.setItems(products);
        
        productIdColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        productNameColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        // Use getCategoryName() directly, as ProductDAO sets it
        productCategoryColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getCategoryName()));
        productPriceColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getPrice()));
        // Use getStockQuantity() for stock column
        productStockColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleIntegerProperty(data.getValue().getStockQuantity()).asObject());
        productStatusColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getStockStatus()));
        
        // Add image preview column
        productImageColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getImageUrl()));
        productImageColumn.setCellFactory(param -> new TableCell<>() {
            private final javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView();
            {
                imageView.setFitWidth(40);
                imageView.setFitHeight(40);
                imageView.setPreserveRatio(true);
            }
            @Override
            protected void updateItem(String imageUrl, boolean empty) {
                super.updateItem(imageUrl, empty);
                if (empty || imageUrl == null || imageUrl.isEmpty()) {
                    setGraphic(null);
                } else {
                    try {
                        imageView.setImage(new javafx.scene.image.Image(imageUrl, 40, 40, true, true));
                        setGraphic(imageView);
                    } catch (Exception e) {
                        setGraphic(null);
                    }
                }
            }
        });
        
        // Add action buttons
        productActionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button();
            private final Button deleteButton = new Button();
            private final HBox buttonBox = new HBox(5, editButton, deleteButton);
            {
                // Try to set icons for edit and delete, fallback to text if not found
                try {
                    var editStream = getClass().getResourceAsStream("/icons/edit.png");
                    if (editStream != null) {
                        editButton.setGraphic(new javafx.scene.image.ImageView(new javafx.scene.image.Image(editStream, 16, 16, true, true)));
                    } else {
                        editButton.setText("Edit");
                    }
                } catch (Exception e) {
                    editButton.setText("Edit");
                }
                editButton.setTooltip(new Tooltip("Edit"));
                try {
                    var deleteStream = getClass().getResourceAsStream("/icons/delete.png");
                    if (deleteStream != null) {
                        deleteButton.setGraphic(new javafx.scene.image.ImageView(new javafx.scene.image.Image(deleteStream, 16, 16, true, true)));
                    } else {
                        deleteButton.setText("Delete");
                    }
                } catch (Exception e) {
                    deleteButton.setText("Delete");
                }
                deleteButton.setTooltip(new Tooltip("Delete"));
                editButton.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    editProduct(product);
                });
                deleteButton.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    deleteProduct(product);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonBox);
                }
            }
        });
    }

    private void setupOrdersTable() {
        ordersTable.setItems(orders);
        
        orderIdColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        orderCustomerColumn.setCellValueFactory(data -> {
            User user = userDAO.findById(data.getValue().getUserId());
            return new javafx.beans.property.SimpleStringProperty(user != null ? user.getFullName() : "Unknown");
        });
        orderDateColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getCreatedAt().toString()));
        orderTotalColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getTotalAmount()));
        orderStatusColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));
        
        // Add action buttons
        orderActionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button viewButton = new Button("View");
            private final Button updateButton = new Button("Update Status");
            
            {
                viewButton.setOnAction(event -> {
                    Order order = getTableView().getItems().get(getIndex());
                    viewOrderDetails(order);
                });
                
                updateButton.setOnAction(event -> {
                    Order order = getTableView().getItems().get(getIndex());
                    updateOrderStatus(order);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(5, viewButton, updateButton));
                }
            }
        });
    }

    private void setupUsersTable() {
        usersTable.setItems(users);
        
        // Populate userRoleFilter with available roles
        userRoleFilter.setItems(FXCollections.observableArrayList("Admin", "Customer"));
        
        userIdColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        userNameColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getFullName()));
        userEmailColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        userRoleColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().isAdmin() ? "Admin" : "Customer"));
        userStatusColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty("Active"));
        
        // Add action buttons
        userActionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            
            {
                editButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    editUser(user);
                });
                
                deleteButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    deleteUser(user);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(5, editButton, deleteButton));
                }
            }
        });
    }

    private void setupCategoriesTable() {
        categoriesTable.setItems(categories);
        
        categoryIdColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        categoryNameColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        categoryDescriptionColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));
        categoryProductsColumn.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleIntegerProperty(productDAO.countByCategory(data.getValue().getId())).asObject());
        // Add image preview column
        categoryImageColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getImageUrl()));
        categoryImageColumn.setCellFactory(param -> new TableCell<>() {
            private final javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView();
            {
                imageView.setFitWidth(40);
                imageView.setFitHeight(40);
                imageView.setPreserveRatio(true);
            }
            @Override
            protected void updateItem(String imageUrl, boolean empty) {
                super.updateItem(imageUrl, empty);
                if (empty || imageUrl == null || imageUrl.isEmpty()) {
                    setGraphic(null);
                } else {
                    try {
                        imageView.setImage(new javafx.scene.image.Image(imageUrl, 40, 40, true, true));
                        setGraphic(imageView);
                    } catch (Exception e) {
                        setGraphic(null);
                    }
                }
            }
        });
        
        // Add action buttons
        categoryActionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            
            {
                editButton.setOnAction(event -> {
                    Category category = getTableView().getItems().get(getIndex());
                    editCategory(category);
                });
                
                deleteButton.setOnAction(event -> {
                    Category category = getTableView().getItems().get(getIndex());
                    deleteCategory(category);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(5, editButton, deleteButton));
                }
            }
        });
    }

    private void loadDashboardData() {
        // Load statistics
        int totalProducts = productDAO.countAll();
        int totalOrders = orderDAO.countAll();
        int totalUsers = userDAO.countAll();
        BigDecimal totalRevenue = orderDAO.getTotalRevenue();
        
        totalProductsLabel.setText(String.valueOf(totalProducts));
        totalOrdersLabel.setText(String.valueOf(totalOrders));
        totalUsersLabel.setText(String.valueOf(totalUsers));
        totalRevenueLabel.setText("$" + totalRevenue);
        
        // Load recent orders
        List<Order> recent = orderDAO.findRecent(5);
        recentOrders.clear();
        recentOrders.addAll(recent);
        
        statusLabel.setText("Dashboard loaded successfully");
    }

    private void loadProducts() {
        List<Product> productList = productDAO.findAll();
        products.clear();
        products.addAll(productList);
        // Debug logging
        System.out.println("[DEBUG] Loaded products: " + products.size());
        for (Product p : products) {
            System.out.println("[DEBUG] Product: " + p);
        }
    }

    private void loadOrders() {
        List<Order> orderList = orderDAO.findAll();
        orders.clear();
        orders.addAll(orderList);
    }

    private void loadUsers() {
        List<User> userList = userDAO.findAll();
        users.clear();
        users.addAll(userList);
    }

    private void loadCategories() {
        List<Category> categoryList = categoryDAO.findAll();
        categories.clear();
        categories.addAll(categoryList);
    }

    private void loadAdminProfile() {
        adminFirstNameField.setText(currentUser.getFirstName());
        adminLastNameField.setText(currentUser.getLastName());
        adminEmailField.setText(currentUser.getEmail());
        adminPhoneField.setText(currentUser.getPhone());
        adminAddressField.setText(currentUser.getAddress());
        adminCityField.setText(currentUser.getCity());
        adminStateField.setText(currentUser.getState());
        adminZipCodeField.setText(currentUser.getZipCode());
    }

    // Navigation methods
    @FXML
    private void showDashboard() {
        mainTabPane.getSelectionModel().select(0);
    }

    @FXML
    private void showProducts() {
        mainTabPane.getSelectionModel().select(1);
    }

    @FXML
    private void showOrders() {
        mainTabPane.getSelectionModel().select(2);
    }

    @FXML
    private void showUsers() {
        mainTabPane.getSelectionModel().select(3);
    }

    @FXML
    private void showCategories() {
        mainTabPane.getSelectionModel().select(4);
    }

    @FXML
    private void showProfile() {
        mainTabPane.getSelectionModel().select(5);
    }

    @FXML
    private void showSettings() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Settings");
        alert.setHeaderText(null);
        alert.setContentText("Settings functionality will be implemented in the next version.");
        alert.showAndWait();
    }

    // Product management methods
    @FXML
    private void addNewProduct() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProductDialog.fxml"));
            DialogPane dialogPane = loader.load();
            ProductDialogController controller = loader.getController();

            Dialog<Product> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Add New Product");
            dialog.initModality(Modality.APPLICATION_MODAL);
            Stage stage = (Stage) mainTabPane.getScene().getWindow();
            dialog.initOwner(stage);
            dialog.setResultConverter(dialogButton -> {
                ButtonType saveButtonType = controller.saveButton;
                if (dialogButton == saveButtonType) {
                    return controller.getProduct();
                }
                return null;
            });
            Optional<Product> result = dialog.showAndWait();
            if (result.isPresent() && result.get() != null) {
                Product newProduct = result.get();
                if (productDAO.save(newProduct)) {
                    loadProducts();
                    statusLabel.setText("Product added successfully: " + newProduct.getName());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Product Added");
                    alert.setHeaderText(null);
                    alert.setContentText("Product '" + newProduct.getName() + "' added successfully.");
                    alert.showAndWait();
                } else {
                    showError("Error", "Failed to add product. Please try again.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error", "Failed to load product dialog: " + e.getMessage());
        }
    }

    @FXML
    private void searchProducts() {
        String searchTerm = productSearchField.getText().trim();
        Category selectedCategory = productCategoryFilter.getValue();
        
        List<Product> results;
        if (!searchTerm.isEmpty() && selectedCategory != null) {
            results = productDAO.searchByNameAndCategory(searchTerm, selectedCategory.getId());
        } else if (!searchTerm.isEmpty()) {
            results = productDAO.searchByName(searchTerm);
        } else if (selectedCategory != null) {
            results = productDAO.findByCategory(selectedCategory.getId());
        } else {
            results = productDAO.findAll();
        }
        
        products.clear();
        products.addAll(results);
        statusLabel.setText("Found " + results.size() + " products");
    }

    @FXML
    private void clearProductFilters() {
        productSearchField.clear();
        productCategoryFilter.setValue(null);
        loadProducts();
        statusLabel.setText("Product filters cleared");
    }

    private void editProduct(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProductDialog.fxml"));
            DialogPane dialogPane = loader.load();
            ProductDialogController controller = loader.getController();
            controller.setProduct(product);

            Dialog<Product> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Edit Product");
            dialog.initModality(Modality.APPLICATION_MODAL);
            Stage stage = (Stage) mainTabPane.getScene().getWindow();
            dialog.initOwner(stage);
            dialog.setResultConverter(dialogButton -> {
                ButtonType saveButtonType = controller.saveButton;
                if (dialogButton == saveButtonType) {
                    return controller.getProduct();
                }
                return null;
            });
            Optional<Product> result = dialog.showAndWait();
            if (result.isPresent() && result.get() != null) {
                Product updatedProduct = result.get();
                if (productDAO.save(updatedProduct)) {
                    loadProducts();
                    statusLabel.setText("Product updated successfully: " + updatedProduct.getName());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Product Updated");
                    alert.setHeaderText(null);
                    alert.setContentText("Product '" + updatedProduct.getName() + "' updated successfully.");
                    alert.showAndWait();
                } else {
                    showError("Error", "Failed to update product. Please try again.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error", "Failed to load product dialog: " + e.getMessage());
        }
    }

    private void deleteProduct(Product product) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Product");
        alert.setHeaderText("Delete: " + product.getName());
        alert.setContentText("Are you sure you want to delete this product? This action cannot be undone.");
        
        if (alert.showAndWait().orElse(null) == ButtonType.OK) {
            if (productDAO.delete(product.getId())) {
                loadProducts();
                statusLabel.setText("Product deleted successfully: " + product.getName());
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Product Deleted");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Product '" + product.getName() + "' deleted successfully.");
                successAlert.showAndWait();
            } else {
                showError("Error", "Failed to delete product. Please try again.");
            }
        }
    }

    // Order management methods
    @FXML
    private void refreshOrders() {
        loadOrders();
        statusLabel.setText("Orders refreshed");
    }

    private void viewOrderDetails(Order order) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Details");
        alert.setHeaderText("Order #" + order.getId());
        alert.setContentText("Order details functionality will be implemented in the next version.");
        alert.showAndWait();
    }

    private void updateOrderStatus(Order order) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Update Order Status");
        alert.setHeaderText("Order #" + order.getId());
        alert.setContentText("Order status update functionality will be implemented in the next version.");
        alert.showAndWait();
    }

    // User management methods
    @FXML
    private void addNewUser() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UserDialog.fxml"));
            DialogPane dialogPane = loader.load();
            UserDialogController controller = loader.getController();

            Dialog<User> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Add New User");
            dialog.initModality(Modality.APPLICATION_MODAL);
            Stage stage = (Stage) mainTabPane.getScene().getWindow();
            dialog.initOwner(stage);
            dialog.setResultConverter(dialogButton -> {
                ButtonType saveButtonType = controller.saveButtonType;
                if (dialogButton == saveButtonType) {
                    return controller.getUser();
                }
                return null;
            });
            Optional<User> result = dialog.showAndWait();
            if (result.isPresent() && result.get() != null) {
                User newUser = result.get();
                if (userDAO.create(newUser)) {
                    loadUsers();
                    statusLabel.setText("User added successfully: " + newUser.getFullName());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("User Added");
                    alert.setHeaderText(null);
                    alert.setContentText("User '" + newUser.getFullName() + "' added successfully.");
                    alert.showAndWait();
                } else {
                    showError("Error", "Failed to add user. Please try again.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error", "Failed to load user dialog: " + e.getMessage());
        }
    }

    @FXML
    private void searchUsers() {
        String searchTerm = userSearchField.getText().trim();
        String selectedRole = userRoleFilter.getValue();
        
        List<User> results;
        if (selectedRole != null && !selectedRole.isEmpty()) {
            // Map display value to DB value
            String dbRole = selectedRole.equalsIgnoreCase("Admin") ? "ADMIN" : "CUSTOMER";
            if (!searchTerm.isEmpty()) {
                // Filter by both name and role
                results = userDAO.searchByName(searchTerm);
                results.removeIf(u -> !dbRole.equalsIgnoreCase(u.getRole()));
            } else {
                // Filter by role only
                results = userDAO.findByRole(dbRole);
            }
        } else if (!searchTerm.isEmpty()) {
            results = userDAO.searchByName(searchTerm);
        } else {
            results = userDAO.findAll();
        }
        
        users.clear();
        users.addAll(results);
        statusLabel.setText("Found " + results.size() + " users");
    }

    @FXML
    private void clearUserFilters() {
        userSearchField.clear();
        userRoleFilter.setValue(null);
        loadUsers();
        statusLabel.setText("User filters cleared");
    }

    private void editUser(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UserDialog.fxml"));
            DialogPane dialogPane = loader.load();
            UserDialogController controller = loader.getController();
            controller.setUser(user);

            Dialog<User> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Edit User");
            dialog.initModality(Modality.APPLICATION_MODAL);
            Stage stage = (Stage) mainTabPane.getScene().getWindow();
            dialog.initOwner(stage);
            dialog.setResultConverter(dialogButton -> {
                ButtonType saveButtonType = controller.saveButtonType;
                if (dialogButton == saveButtonType) {
                    return controller.getUser();
                }
                return null;
            });
            Optional<User> result = dialog.showAndWait();
            if (result.isPresent() && result.get() != null) {
                User updatedUser = result.get();
                if (userDAO.update(updatedUser)) {
                    loadUsers();
                    statusLabel.setText("User updated successfully: " + updatedUser.getFullName());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("User Updated");
                    alert.setHeaderText(null);
                    alert.setContentText("User '" + updatedUser.getFullName() + "' updated successfully.");
                    alert.showAndWait();
                } else {
                    showError("Error", "Failed to update user. Please try again.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error", "Failed to load user dialog: " + e.getMessage());
        }
    }

    private void deleteUser(User user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete User");
        alert.setHeaderText("Delete: " + user.getFullName());
        alert.setContentText("Are you sure you want to delete this user? This action cannot be undone.");
        
        if (alert.showAndWait().orElse(null) == ButtonType.OK) {
            if (userDAO.delete(user.getId())) {
                loadUsers();
                statusLabel.setText("User deleted successfully: " + user.getFullName());
            } else {
                showError("Error", "Failed to delete user. Please try again.");
            }
        }
    }

    // Category management methods
    @FXML
    private void addNewCategory() {
        System.out.println("Adding new category...");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CategoryDialog.fxml"));
            DialogPane dialogPane = loader.load();
            CategoryDialogController controller = loader.getController();
            Dialog<Category> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Add New Category");
            dialog.initModality(Modality.APPLICATION_MODAL);
            Stage stage = (Stage) mainTabPane.getScene().getWindow();
            dialog.initOwner(stage);
            dialog.setResultConverter(dialogButton -> {
                ButtonType saveButtonType = controller.saveButton;
                if (dialogButton == saveButtonType) {
                    return controller.getCategory();
                }
                return null;
            });
            Optional<Category> result = dialog.showAndWait();
            if (result.isPresent() && result.get() != null) {
                Category newCategory = result.get();
                if (categoryDAO.save(newCategory)) {
                    loadCategories();
                    loadProducts();
                    statusLabel.setText("Category added successfully: " + newCategory.getName());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Category Added");
                    alert.setHeaderText(null);
                    alert.setContentText("Category '" + newCategory.getName() + "' added successfully.");
                    alert.showAndWait();
                } else {
                    showError("Error", "Failed to add category. Please try again.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading category dialog: " + e.getMessage());
            e.printStackTrace();
            showError("Error", "Failed to load category dialog: " + e.getMessage());
        }
    }

    private void editCategory(Category category) {
        System.out.println("Editing category: " + category.getName());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CategoryDialog.fxml"));
            DialogPane dialogPane = loader.load();
            CategoryDialogController controller = loader.getController();

            // Set the category to edit
            controller.setCategory(category);

            Dialog<Category> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Edit Category");

            // Set the dialog to be modal
            dialog.initModality(Modality.APPLICATION_MODAL);

            // Get the stage and set the owner
            Stage stage = (Stage) mainTabPane.getScene().getWindow();
            dialog.initOwner(stage);

            // Result converter to enforce validation
            dialog.setResultConverter(dialogButton -> {
                ButtonType saveButtonType = controller.saveButton;
                if (dialogButton == saveButtonType) {
                    return controller.getCategory();
                }
                return null;
            });

            // Show dialog and wait for result
            Optional<Category> result = dialog.showAndWait();

            if (result.isPresent() && result.get() != null) {
                Category updatedCategory = result.get();
                if (categoryDAO.save(updatedCategory)) {
                    loadCategories();
                    loadProducts(); // Refresh products to update category filter
                    statusLabel.setText("Category updated successfully: " + updatedCategory.getName());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Category Updated");
                    alert.setHeaderText(null);
                    alert.setContentText("Category '" + updatedCategory.getName() + "' updated successfully.");
                    alert.showAndWait();
                } else {
                    showError("Error", "Failed to update category. Please try again.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading category dialog: " + e.getMessage());
            e.printStackTrace();
            showError("Error", "Failed to load category dialog: " + e.getMessage());
        }
    }

    private void deleteCategory(Category category) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Category");
        alert.setHeaderText("Delete: " + category.getName());
        alert.setContentText("Are you sure you want to delete this category? This will also affect all products in this category.");
        
        if (alert.showAndWait().orElse(null) == ButtonType.OK) {
            if (categoryDAO.delete(category.getId())) {
                loadCategories();
                loadProducts(); // Refresh products to update category filter
                statusLabel.setText("Category deleted successfully: " + category.getName());
            } else {
                showError("Error", "Failed to delete category. Please try again.");
            }
        }
    }

    // Profile methods
    private String safeTrim(String value) {
        return value == null ? "" : value.trim();
    }

    @FXML
    private void updateAdminProfile() {
        System.out.println("updateAdminProfile called!");
        try {
            // Get updated values from fields
            String firstName = safeTrim(adminFirstNameField.getText());
            String lastName = safeTrim(adminLastNameField.getText());
            String email = safeTrim(adminEmailField.getText());
            String phone = safeTrim(adminPhoneField.getText());
            String address = safeTrim(adminAddressField.getText());
            String city = safeTrim(adminCityField.getText());
            String state = safeTrim(adminStateField.getText());
            String zipCode = safeTrim(adminZipCodeField.getText());
            String country = currentUser.getCountry() != null ? currentUser.getCountry() : "USA";

            // Validate required fields
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
                System.out.println("Validation failed: missing required fields");
                showError("Validation Error", "First name, last name, and email are required.");
                return;
            }

            // Update currentUser object
            currentUser.setFirstName(firstName);
            currentUser.setLastName(lastName);
            currentUser.setEmail(email);
            currentUser.setPhone(phone);
            currentUser.setAddress(address);
            currentUser.setCity(city);
            currentUser.setState(state);
            currentUser.setZipCode(zipCode);
            currentUser.setCountry(country);

            System.out.println("Attempting to update user: " + currentUser.getId() + ", " + currentUser.getEmail());
            // Save to database
            boolean success = userDAO.update(currentUser);
            System.out.println("userDAO.update returned: " + success);
            if (success) {
                loadAdminProfile();
                statusLabel.setText("Profile updated successfully.");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Profile Updated");
                alert.setHeaderText(null);
                alert.setContentText("Your profile has been updated successfully.");
                alert.showAndWait();
            } else {
                showError("Update Failed", "Failed to update profile. Please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Unexpected Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    private void changeAdminPassword() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Change Password");
        alert.setHeaderText(null);
        alert.setContentText("Password change functionality will be implemented in the next version.");
        alert.showAndWait();
    }

    @FXML
    private void handleLogout() {
        System.out.println("handleLogout method called!");
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        
        if (alert.showAndWait().orElse(null) == ButtonType.OK) {
            System.out.println("User confirmed logout - proceeding...");
            try {
                // Clear current user session
                ECommerceApp.setCurrentUser(null);
                
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
                Parent root = loader.load();
                
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
                
                Stage stage = (Stage) mainTabPane.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("E-Store - Login");
                
                System.out.println("Successfully navigated to login screen");
                
            } catch (IOException e) {
                System.err.println("Error during logout: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("User cancelled logout");
        }
    }

    // Utility methods
    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 