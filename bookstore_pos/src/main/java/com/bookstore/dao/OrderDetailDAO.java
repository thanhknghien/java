package com.bookstore.dao;

import com.bookstore.config.DataBaseConfig;
import com.bookstore.model.OrderDetail;
import com.bookstore.model.Product;

import java.sql.*;
import java.util.ArrayList;

public class OrderDetailDAO {
    private ProductDAO productDAO = new ProductDAO();

    public void addOrderDetail(OrderDetail detail) throws SQLException {
        String sql = "INSERT INTO order_details (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, detail.getOrderId());
            stmt.setInt(2, detail.getProduct().getId());
            stmt.setInt(3, detail.getQuantity());
            stmt.setDouble(4, detail.getPrice());
            stmt.executeUpdate();

            // Lấy ID của chi tiết đơn hàng vừa tạo
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    detail.setId(rs.getInt(1));
                }
            }
        }
    }

    public ArrayList<OrderDetail> getOrderDetailsByOrderId(int orderId) throws SQLException {
        ArrayList<OrderDetail> details = new ArrayList<>();
        String sql = "SELECT * FROM order_details WHERE order_id = ?";
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderDetail detail = new OrderDetail();
                    detail.setId(rs.getInt("id"));
                    detail.setOrderId(orderId);
                    detail.setQuantity(rs.getInt("quantity"));
                    detail.setPrice(rs.getDouble("price"));

                    int productId = rs.getInt("product_id");
                    Product product = productDAO.getAllProducts().stream()
                            .filter(p -> p.getId() == productId)
                            .findFirst()
                            .orElse(null);
                    detail.setProduct(product);

                    details.add(detail);
                }
            }
        }
        return details;
    }
}