package com.bookstore.dao;

import com.bookstore.config.DataBaseConfig;
import com.bookstore.model.Order;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    private Connection conn;

    public OrderDAO() {
        try {
            conn = DataBaseConfig.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xem tất cả đơn hàng
    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setDate(rs.getTimestamp("date").toLocalDateTime());
                order.setEmployeeId(rs.getObject("employee_id") != null ? rs.getInt("employee_id") : null);
                order.setTotal(rs.getDouble("total"));
                orders.add(order);
            }
        }
        return orders;
    }

    // Thêm đơn hàng
    public void addOrder(Order order) throws SQLException {
        String query = "INSERT INTO orders (date, employee_id, total) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setTimestamp(1, Timestamp.valueOf(order.getDate()));
            if (order.getEmployeeId() != null) {
                stmt.setInt(2, order.getEmployeeId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setDouble(3, order.getTotal());
            stmt.executeUpdate();

            // Lấy ID của đơn hàng vừa thêm
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                order.setId(rs.getInt(1));
            }
        }
    }

    // Sửa đơn hàng
    public void updateOrder(Order order) throws SQLException {
        String query = "UPDATE orders SET date = ?, employee_id = ?, total = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.valueOf(order.getDate()));
            if (order.getEmployeeId() != null) {
                stmt.setInt(2, order.getEmployeeId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setDouble(3, order.getTotal());
            stmt.setInt(4, order.getId());
            stmt.executeUpdate();
        }
    }

    // Xóa đơn hàng
    public void deleteOrder(int id) throws SQLException {
        String query = "DELETE FROM orders WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Tìm kiếm đơn hàng theo ngày
    public List<Order> searchOrdersByDate(LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE date BETWEEN ? AND ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.valueOf(startDate));
            stmt.setTimestamp(2, Timestamp.valueOf(endDate));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setDate(rs.getTimestamp("date").toLocalDateTime());
                order.setEmployeeId(rs.getObject("employee_id") != null ? rs.getInt("employee_id") : null);
                order.setTotal(rs.getDouble("total"));
                orders.add(order);
            }
        }
        return orders;
    }
}