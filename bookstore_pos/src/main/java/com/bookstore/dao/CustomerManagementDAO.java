package com.bookstore.dao;

import com.bookstore.config.DataBaseConfig;
import com.bookstore.model.CustomerManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerManagementDAO {
    private Connection connection;

    public CustomerManagementDAO() throws SQLException {
        connection = DataBaseConfig.getConnection();
    }

    public void add(CustomerManagement customerManagement) throws SQLException {
        String query = "INSERT INTO customer_management (id, can_add, can_edit, can_delete, can_view) VALUES (?, ?, ?, ?, ?) " +
                      "ON DUPLICATE KEY UPDATE can_add = ?, can_edit = ?, can_delete = ?, can_view = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customerManagement.getId());
            stmt.setBoolean(2, customerManagement.isCanAdd());
            stmt.setBoolean(3, customerManagement.isCanEdit());
            stmt.setBoolean(4, customerManagement.isCanDelete());
            stmt.setBoolean(5, customerManagement.isCanView());
            stmt.setBoolean(6, customerManagement.isCanAdd());
            stmt.setBoolean(7, customerManagement.isCanEdit());
            stmt.setBoolean(8, customerManagement.isCanDelete());
            stmt.setBoolean(9, customerManagement.isCanView());
            stmt.executeUpdate();
        }
    }
    public CustomerManagement getById(int id) {
        String sql = "SELECT * FROM customer_management WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new CustomerManagement(
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
        String query = "DELETE FROM customer_management WHERE id = ?";
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