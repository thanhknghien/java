package com.bookstore.dao;

import com.bookstore.config.DataBaseConfig;
import com.bookstore.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private Connection conn;

    public ProductDAO() {
        try {
            conn = DataBaseConfig.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xem tất cả sách
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setStock(rs.getInt("stock"));
                product.setCategory(rs.getString("category"));
                product.setAuthor(rs.getString("author"));  
                product.setImage(rs.getString("image"));
                products.add(product);
            }
        }
        return products;
    }

    // Thêm sách
    public void addProduct(Product product) throws SQLException {
        String query = "INSERT INTO products (name, price, stock, category, author, image) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setInt(3, product.getStock());
            stmt.setString(4, product.getCategory());
            stmt.setString(5, product.getAuthor());
            stmt.setString(6, product.getImage());
            stmt.executeUpdate();
        }
    }

    // Sửa sách
    public void updateProduct(Product product) throws SQLException {
        String query = "UPDATE products SET name = ?, price = ?, stock = ?, category = ?, author = ?, image = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setInt(3, product.getStock());
            stmt.setString(4, product.getCategory());
            stmt.setString(5, product.getAuthor());
            stmt.setString(6, product.getImage());
            stmt.setInt(7, product.getId());
            stmt.executeUpdate();
        }
    }

    // Xóa sách
    public void deleteProduct(int id) throws SQLException {
        String query = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Tìm kiếm sách theo tên hoặc tác giả
    public List<Product> searchProducts(String keyword) throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE name LIKE ? OR author LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setStock(rs.getInt("stock"));
                product.setCategory(rs.getString("category"));
                product.setAuthor(rs.getString("author"));
                product.setImage(rs.getString("image"));
                products.add(product);
            }
        }
        return products;
    }
}