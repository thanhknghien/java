package com.bookstore.dao;

import com.bookstore.config.DataBaseConfig;
import com.bookstore.model.OrderManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderManagementDAO {
    private Connection connection;

    public OrderManagementDAO() throws SQLException {
        connection = DataBaseConfig.getConnection();
    }
    public OrderManagement getById(int id) {
        String sql = "SELECT * FROM order_management WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new OrderManagement(
                    rs.getInt("id"),
                    rs.getBoolean("can_view")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
     public boolean add(OrderManagement orderManagement) {
        String sql = "INSERT INTO order_management (id, can_view) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, orderManagement.getId());
            pstmt.setBoolean(2, orderManagement.isCanView());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(OrderManagement orderManagement) {
        String sql = "UPDATE order_management SET  can_view = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, orderManagement.isCanView());
            pstmt.setInt(2, orderManagement.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void delete(int userId) throws SQLException {
        String query = "DELETE FROM order_management WHERE id = ?";
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