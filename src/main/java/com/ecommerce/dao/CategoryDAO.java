package com.ecommerce.dao;

import com.ecommerce.model.Category;
import com.ecommerce.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY name";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                categories.add(mapResultSetToCategory(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding all categories: " + e.getMessage());
        }
        
        return categories;
    }
    
    public Category findById(int id) {
        String sql = "SELECT * FROM categories WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToCategory(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding category by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    public boolean save(Category category) {
        System.out.println("CategoryDAO.save() called for category: " + category.getName());
        if (category.getId() == 0) {
            System.out.println("Inserting new category...");
            return insert(category);
        } else {
            System.out.println("Updating existing category with ID: " + category.getId());
            return update(category);
        }
    }
    
    private boolean insert(Category category) {
        String sql = "INSERT INTO categories (name, description, image_url) VALUES (?, ?, ?)";
        System.out.println("Insert SQL: " + sql);
        System.out.println("Values: name='" + category.getName() + "', description='" + category.getDescription() + "', image_url='" + category.getImageUrl() + "'");
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setString(3, category.getImageUrl());
            
            int affectedRows = stmt.executeUpdate();
            System.out.println("Affected rows: " + affectedRows);
            
            if (affectedRows > 0) {
                // Get the last inserted row id (SQLite specific)
                try (Statement idStmt = conn.createStatement();
                     ResultSet rs = idStmt.executeQuery("SELECT last_insert_rowid()")) {
                    if (rs.next()) {
                        category.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error inserting category: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    private boolean update(Category category) {
        String sql = "UPDATE categories SET name = ?, description = ?, image_url = ? WHERE id = ?";
        System.out.println("Update SQL: " + sql);
        System.out.println("Values: name='" + category.getName() + "', description='" + category.getDescription() + "', image_url='" + category.getImageUrl() + "', id=" + category.getId());
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setString(3, category.getImageUrl());
            stmt.setInt(4, category.getId());
            
            int affectedRows = stmt.executeUpdate();
            System.out.println("Affected rows: " + affectedRows);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating category: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean create(Category category) {
        return insert(category);
    }
    
    public boolean delete(int id) {
        String sql = "DELETE FROM categories WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting category: " + e.getMessage());
        }
        
        return false;
    }
    
    private Category mapResultSetToCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getInt("id"));
        category.setName(rs.getString("name"));
        category.setDescription(rs.getString("description"));
        category.setImageUrl(rs.getString("image_url"));
        category.setCreatedAt(rs.getTimestamp("created_at"));
        return category;
    }
} 