package com.bookstore.dao;

import com.bookstore.config.DataBaseConfig;
import com.bookstore.model.OrderDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAO {
    private Connection conn;

    public OrderDetailDAO() {
        try {
            conn = DataBaseConfig.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xem tất cả chi tiết đơn hàng của một đơn hàng
    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) throws SQLException {
        List<OrderDetail> details = new ArrayList<>();
        String query = "SELECT * FROM order_details WHERE order_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OrderDetail detail = new OrderDetail();
                detail.setId(rs.getInt("id"));
                detail.setOrderId(rs.getInt("order_id"));
                detail.setProductId(rs.getInt("product_id"));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setPrice(rs.getDouble("price"));
                details.add(detail);
            }
        }
        return details;
    }

    // Thêm chi tiết đơn hàng
    public void addOrderDetail(OrderDetail detail) throws SQLException {
        String query = "INSERT INTO order_details (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, detail.getOrderId());
            stmt.setInt(2, detail.getProductId());
            stmt.setInt(3, detail.getQuantity());
            stmt.setDouble(4, detail.getPrice());
            stmt.executeUpdate();

            // Cập nhật tồn kho
            String updateStockQuery = "UPDATE products SET stock = stock - ? WHERE id = ?";
            try (PreparedStatement stockStmt = conn.prepareStatement(updateStockQuery)) {
                stockStmt.setInt(1, detail.getQuantity());
                stockStmt.setInt(2, detail.getProductId());
                stockStmt.executeUpdate();
            }
        }
    }

    // Sửa chi tiết đơn hàng
    public void updateOrderDetail(OrderDetail detail) throws SQLException {
        // Lấy số lượng cũ để điều chỉnh tồn kho
        int oldQuantity = 0;
        String selectQuery = "SELECT quantity FROM order_details WHERE id = ?";
        try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
            selectStmt.setInt(1, detail.getId());
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                oldQuantity = rs.getInt("quantity");
            }
        }

        // Cập nhật chi tiết đơn hàng
        String query = "UPDATE order_details SET order_id = ?, product_id = ?, quantity = ?, price = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, detail.getOrderId());
            stmt.setInt(2, detail.getProductId());
            stmt.setInt(3, detail.getQuantity());
            stmt.setDouble(4, detail.getPrice());
            stmt.setInt(5, detail.getId());
            stmt.executeUpdate();
        }

        // Điều chỉnh tồn kho
        int quantityDiff = detail.getQuantity() - oldQuantity;
        String updateStockQuery = "UPDATE products SET stock = stock - ? WHERE id = ?";
        try (PreparedStatement stockStmt = conn.prepareStatement(updateStockQuery)) {
            stockStmt.setInt(1, quantityDiff);
            stockStmt.setInt(2, detail.getProductId());
            stockStmt.executeUpdate();
        }
    }

    // Xóa chi tiết đơn hàng
    public void deleteOrderDetail(int id) throws SQLException {
        // Lấy số lượng để hoàn lại tồn kho
        int quantity = 0;
        int productId = 0;
        String selectQuery = "SELECT product_id, quantity FROM order_details WHERE id = ?";
        try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
            selectStmt.setInt(1, id);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                quantity = rs.getInt("quantity");
                productId = rs.getInt("product_id");
            }
        }

        // Xóa chi tiết đơn hàng
        String query = "DELETE FROM order_details WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }

        // Hoàn lại tồn kho
        String updateStockQuery = "UPDATE products SET stock = stock + ? WHERE id = ?";
        try (PreparedStatement stockStmt = conn.prepareStatement(updateStockQuery)) {
            stockStmt.setInt(1, quantity);
            stockStmt.setInt(2, productId);
            stockStmt.executeUpdate();
        }
    }
}