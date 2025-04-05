package com.bookstore.dao;

import com.bookstore.config.DataBaseConfig;
import com.bookstore.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // Lấy tất cả sản phẩm
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection conn = DataBaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setAuthor(rs.getString("author"));
                product.setPrice(rs.getDouble("price"));
                product.setCategoryId(rs.getInt("categoryid"));
                product.setImagePath(rs.getString("imagePath"));
                products.add(product);
            }
        }
        return products;
    }

    // Lấy sản phẩm theo ID
    public Product getProductById(int id) throws SQLException {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setAuthor(rs.getString("author"));
                    product.setPrice(rs.getDouble("price"));
                    product.setCategoryId(rs.getInt("categoryid"));
                    product.setImagePath(rs.getString("imagePath"));
                    return product;
                }
            }
        }
        return null;
    }

    // Thêm sản phẩm mới
    public int addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO products (name, author, price, categoryid, imagePath) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getAuthor());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getCategoryId());
            stmt.setString(5, product.getImagePath());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // Trả về ID của sản phẩm vừa thêm
                }
            }
        }
        throw new SQLException("Không thể lấy ID của sản phẩm vừa thêm!");
    }

    // Cập nhật sản phẩm
    public void updateProduct(Product product) throws SQLException {
        String sql = "UPDATE products SET name = ?, author = ?, price = ?, categoryid = ?, imagePath = ? WHERE id = ?";
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getAuthor());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getCategoryId());
            stmt.setString(5, product.getImagePath());
            stmt.setInt(6, product.getId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không tìm thấy sản phẩm với ID: " + product.getId());
            }
        }
    }

    // Xóa sản phẩm
    public void deleteProduct(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không tìm thấy sản phẩm với ID: " + id);
            }
        }
    }
}