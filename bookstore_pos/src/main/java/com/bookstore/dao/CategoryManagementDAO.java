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

    public void add(CategoryManagement categoryManagement) throws SQLException {
        String query = "INSERT INTO category_management (id, can_add, can_edit, can_delete, can_view) VALUES (?, ?, ?, ?, ?) " +
                      "ON DUPLICATE KEY UPDATE can_add = ?, can_edit = ?, can_delete = ?, can_view = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, categoryManagement.getId());
            stmt.setBoolean(2, categoryManagement.isCanAdd());
            stmt.setBoolean(3, categoryManagement.isCanEdit());
            stmt.setBoolean(4, categoryManagement.isCanDelete());
            stmt.setBoolean(5, categoryManagement.isCanView());
            stmt.setBoolean(6, categoryManagement.isCanAdd());
            stmt.setBoolean(7, categoryManagement.isCanEdit());
            stmt.setBoolean(8, categoryManagement.isCanDelete());
            stmt.setBoolean(9, categoryManagement.isCanView());
            stmt.executeUpdate();
        }
    }
    public CategoryManagement getById(int id) {
        String sql = "SELECT * FROM customer_management WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new CategoryManagement(
                    rs.getInt("id"),
                    rs.getBoolean("can_add"),
                    rs.getBoolean("can_edit"),
                    rs.getBoolean("can_delete"),
                    rs.getBoolean("can_view")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
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