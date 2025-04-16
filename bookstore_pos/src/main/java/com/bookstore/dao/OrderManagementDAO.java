package com.bookstore.dao;

import com.bookstore.model.OrderManagement;
import com.bookstore.config.DataBaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderManagementDAO {
    private Connection connection;

    public OrderManagementDAO() throws SQLException {
        this.connection = DataBaseConfig.getConnection();
    }

    public List<OrderManagement> getAll() {
        List<OrderManagement> orderManagements = new ArrayList<>();
        String sql = "SELECT * FROM order_management";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                OrderManagement orderManagement = new OrderManagement(
                    rs.getInt("id"),
                    rs.getBoolean("can_add"),
                    rs.getBoolean("can_edit"),
                    rs.getBoolean("can_delete"),
                    rs.getBoolean("can_view")
                );
                orderManagements.add(orderManagement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderManagements;
    }

    public OrderManagement getById(int id) {
        String sql = "SELECT * FROM order_management WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new OrderManagement(
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

    public boolean add(OrderManagement orderManagement) {
        String sql = "INSERT INTO order_management (id, can_add, can_edit, can_delete, can_view) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, orderManagement.getId());
            pstmt.setBoolean(2, orderManagement.isCanAdd());
            pstmt.setBoolean(3, orderManagement.isCanEdit());
            pstmt.setBoolean(4, orderManagement.isCanDelete());
            pstmt.setBoolean(5, orderManagement.isCanView());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(OrderManagement orderManagement) {
        String sql = "UPDATE order_management SET  can_add = ?, can_edit = ?, can_delete = ?, can_view = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, orderManagement.isCanAdd());
            pstmt.setBoolean(2, orderManagement.isCanEdit());
            pstmt.setBoolean(3, orderManagement.isCanDelete());
            pstmt.setBoolean(4, orderManagement.isCanView());
            pstmt.setInt(5, orderManagement.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM order_management WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
} 