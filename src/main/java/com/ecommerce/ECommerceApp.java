package com.ecommerce;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.ecommerce.util.DatabaseUtil;
import com.ecommerce.model.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

public class ECommerceApp extends Application {

    private static Stage primaryStage;
    private static User currentUser;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        
        // Initialize SQLite database
        System.out.println("Initializing SQLite database...");
        DatabaseUtil.initializeDatabase();
        
        // Initialize database tables and sample data
        initializeDatabaseTables();
        
        // Test database connection
        if (!testDatabaseConnection()) {
            showDatabaseError();
            return;
        }
        
        // Load the login screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        
        stage.setTitle("E-Commerce Store");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(700);
        
        // Set application icon
        try {
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));
        } catch (Exception e) {
            // Icon not found, continue without it
        }
        
        stage.show();
    }

    private void initializeDatabaseTables() {
        try (Connection conn = DatabaseUtil.getConnection()) {
            System.out.println("Creating database tables...");
            
            // Create users table
            String createUsers = 
                "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "first_name TEXT NOT NULL," +
                "last_name TEXT NOT NULL," +
                "phone TEXT," +
                "address TEXT," +
                "city TEXT," +
                "state TEXT," +
                "zip_code TEXT," +
                "country TEXT DEFAULT 'USA'," +
                "role TEXT DEFAULT 'CUSTOMER' CHECK (role IN ('ADMIN', 'CUSTOMER'))," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ")";
            
            // Create categories table
            String createCategories = 
                "CREATE TABLE IF NOT EXISTS categories (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "description TEXT," +
                "image_url TEXT," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ")";
            
            // Create products table
            String createProducts = 
                "CREATE TABLE IF NOT EXISTS products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "description TEXT," +
                "price REAL NOT NULL CHECK (price >= 0)," +
                "stock_quantity INTEGER DEFAULT 0 CHECK (stock_quantity >= 0)," +
                "category_id INTEGER," +
                "image_url TEXT," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL" +
                ")";
            
            // Create orders table
            String createOrders = 
                "CREATE TABLE IF NOT EXISTS orders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER NOT NULL," +
                "total_amount REAL NOT NULL CHECK (total_amount >= 0)," +
                "status TEXT DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED'))," +
                "shipping_address TEXT," +
                "shipping_city TEXT," +
                "shipping_state TEXT," +
                "shipping_zip_code TEXT," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
                ")";
            
            // Create order items table
            String createOrderItems = 
                "CREATE TABLE IF NOT EXISTS order_items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "order_id INTEGER NOT NULL," +
                "product_id INTEGER NOT NULL," +
                "quantity INTEGER NOT NULL CHECK (quantity > 0)," +
                "unit_price REAL NOT NULL CHECK (unit_price >= 0)," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE," +
                "FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE" +
                ")";
            
            Statement stmt = conn.createStatement();
            stmt.execute(createUsers);
            stmt.execute(createCategories);
            stmt.execute(createProducts);
            stmt.execute(createOrders);
            stmt.execute(createOrderItems);
            
            // Check if sample users exist, if not insert them
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
            if (rs.next() && rs.getInt(1) == 0) {
                System.out.println("Inserting sample users...");
                String insertUsers = 
                    "INSERT INTO users (email, password, first_name, last_name, phone, country, role) VALUES " +
                    "('admin@ecommerce.com', 'password123', 'Admin', 'User', '555-0100', 'USA', 'ADMIN'), " +
                    "('customer@test.com', 'password123', 'John', 'Doe', '555-0101', 'USA', 'CUSTOMER'), " +
                    "('jane.smith@email.com', 'password123', 'Jane', 'Smith', '555-0102', 'USA', 'CUSTOMER'), " +
                    "('mike.johnson@email.com', 'password123', 'Mike', 'Johnson', '555-0103', 'USA', 'CUSTOMER')";
                
                stmt.execute(insertUsers);
                System.out.println("✓ Sample users inserted");
            }
            
            stmt.close();
            System.out.println("✓ Database tables initialized successfully");
            
        } catch (Exception e) {
            System.err.println("Error initializing database tables: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean testDatabaseConnection() {
        try (Connection conn = DatabaseUtil.getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
            return false;
        }
    }

    private void showDatabaseError() {
        // Create a simple error dialog
        Stage errorStage = new Stage();
        errorStage.setTitle("Database Connection Error");
        errorStage.setScene(new Scene(new javafx.scene.control.Label(
            "Failed to connect to SQLite database.\nPlease check if the application has write permissions."), 400, 200));
        errorStage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public static User getCurrentUser() {
        return currentUser;
    }
    
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static void main(String[] args) {
        launch(args);
    }
} 