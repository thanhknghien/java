package com.bookstore.dao;

import com.bookstore.config.DataBaseConfig;
import com.bookstore.model.CategoryManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryManagementDAO {
    private Connection connection;

    public CategoryManagementDAO() throws SQLException {
        this.connection = DataBaseConfig.getConnection();
    }
    
    public boolean add(CategoryManagement categoryManagement) throws SQLException {
        String query = "INSERT INTO category_management (id, can_add, can_edit, can_delete, can_view) VALUES (?, ?, ?, ?, ?) " ;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, categoryManagement.getId());
            stmt.setBoolean(2, categoryManagement.isCanAdd());
            stmt.setBoolean(3, categoryManagement.isCanEdit());
            stmt.setBoolean(4, categoryManagement.isCanDelete());
            stmt.setBoolean(5, categoryManagement.isCanView());
            return stmt.executeUpdate() > 0;
        }
    }
    
     public boolean update(CategoryManagement categoryManagement) {
        String sql = "UPDATE category_management SET  can_add = ?, can_edit = ?, can_delete = ?, can_view = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, categoryManagement.isCanAdd());
            pstmt.setBoolean(2, categoryManagement.isCanEdit());
            pstmt.setBoolean(3, categoryManagement.isCanDelete());
            pstmt.setBoolean(4, categoryManagement.isCanView());
            pstmt.setInt(5, categoryManagement.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public CategoryManagement getById(int id) throws SQLException {
        String sql = "SELECT * FROM category_management WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new CategoryManagement(
                        rs.getInt("id"),
                        rs.getBoolean("can_add"),
                        rs.getBoolean("can_edit"),
                        rs.getBoolean("can_delete"),
                        rs.getBoolean("can_view")
                    );
                }
            }
        }
        return null;
    }

    public void delete(int userId) throws SQLException {
        String query = "DELETE FROM category_management WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}