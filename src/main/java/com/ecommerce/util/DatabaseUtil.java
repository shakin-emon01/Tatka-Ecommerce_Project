package com.ecommerce.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;

public class DatabaseUtil {
    private static final String CONFIG_FILE = "database.properties";
    private static final String DEFAULT_DB_FILE = "database/ecommerce.db";
    private static Properties properties;
    
    static {
        loadProperties();
    }
    
    private static void loadProperties() {
        properties = new Properties();
        try (InputStream input = DatabaseUtil.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                // Use default SQLite values if properties file is not found
                properties.setProperty("db.url", "jdbc:sqlite:" + DEFAULT_DB_FILE);
                properties.setProperty("db.username", "");
                properties.setProperty("db.password", "");
                properties.setProperty("db.driver", "org.sqlite.JDBC");
            } else {
                properties.load(input);
            }
        } catch (IOException e) {
            System.err.println("Error loading database properties: " + e.getMessage());
            // Use default SQLite values
            properties.setProperty("db.url", "jdbc:sqlite:" + DEFAULT_DB_FILE);
            properties.setProperty("db.username", "");
            properties.setProperty("db.password", "");
            properties.setProperty("db.driver", "org.sqlite.JDBC");
        }
    }
    
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(properties.getProperty("db.driver"));
            return DriverManager.getConnection(properties.getProperty("db.url"));
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite driver not found: " + e.getMessage());
        }
    }
    
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
    
    public static void closeAutoCloseable(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                System.err.println("Error closing resource: " + e.getMessage());
            }
        }
    }
    
    public static String getDatabaseUrl() {
        return properties.getProperty("db.url");
    }
    
    public static String getDatabaseUsername() {
        return properties.getProperty("db.username");
    }
    
    public static String getDatabasePassword() {
        return properties.getProperty("db.password");
    }
    
    public static String getDatabaseFile() {
        String url = properties.getProperty("db.url");
        if (url.startsWith("jdbc:sqlite:")) {
            return url.substring("jdbc:sqlite:".length());
        }
        return DEFAULT_DB_FILE;
    }
    
    public static boolean databaseExists() {
        File dbFile = new File(getDatabaseFile());
        return dbFile.exists();
    }
    
    public static void initializeDatabase() {
        try {
            // Create database file if it doesn't exist
            File dbFile = new File(getDatabaseFile());
            if (!dbFile.exists()) {
                // Create the database by establishing a connection
                try (Connection conn = getConnection()) {
                    System.out.println("SQLite database created: " + dbFile.getAbsolutePath());
                }
            }
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
} 