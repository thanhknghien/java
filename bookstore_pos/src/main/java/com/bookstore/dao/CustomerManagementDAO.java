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
    
    public boolean add(CustomerManagement customerManagement) throws SQLException {
        String query = "INSERT INTO customer_management (id, can_add, can_edit, can_delete, can_view) VALUES (?, ?, ?, ?, ?) " ;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customerManagement.getId());
            stmt.setBoolean(2, customerManagement.isCanAdd());
            stmt.setBoolean(3, customerManagement.isCanEdit());
            stmt.setBoolean(4, customerManagement.isCanDelete());
            stmt.setBoolean(5, customerManagement.isCanView());
            return stmt.executeUpdate() > 0;
        }
    }
     public boolean update(CustomerManagement customerManagement) {
        String sql = "UPDATE user_management SET  can_add = ?, can_edit = ?, can_delete = ?, can_view = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, customerManagement.isCanAdd());
            pstmt.setBoolean(2, customerManagement.isCanEdit());
            pstmt.setBoolean(3, customerManagement.isCanDelete());
            pstmt.setBoolean(4, customerManagement.isCanView());
            pstmt.setInt(5, customerManagement.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public CustomerManagement getById(int id) throws SQLException {
        String sql = "SELECT * FROM customer_management WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new CustomerManagement(
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