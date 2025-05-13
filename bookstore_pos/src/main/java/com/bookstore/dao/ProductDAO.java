package com.bookstore.dao;

import com.bookstore.config.DataBaseConfig;
import com.bookstore.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDAO {

    // Lấy tất cả sản phẩm
    public ArrayList<Product> getAllProducts() throws SQLException {
        ArrayList<Product> products = new ArrayList<>();
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

    public Map<String, ArrayList<Product>> getAllProductsByCategory() throws SQLException {
        Map<String, ArrayList<Product>> categoryProductMap = new HashMap<>();
        String sql =  "SELECT p.id, p.name, p.author, p.price, p.categoryid, p.imagePath, c.name AS categoryName " +
                "FROM products p " +
                "JOIN category c ON p.categoryid = c.categoryid";
        try (Connection conn = DataBaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String categoryName = rs.getString("categoryName");
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setAuthor(rs.getString("author"));
                    product.setPrice(rs.getDouble("price"));
                    product.setCategoryId(rs.getInt("categoryid"));
                    product.setImagePath(rs.getString("imagePath"));
                    categoryProductMap.computeIfAbsent(categoryName, k -> new ArrayList<>()).add(product);
            }
        }
        return categoryProductMap;
    }

    // Search Product By Criteria
    public ArrayList<Product> searchProducts(Map<String, String> criteria) throws SQLException {
        ArrayList<Product> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT p.*, c.name AS category_name FROM products p LEFT JOIN category c ON p.categoryid = c.categoryid WHERE 1=1");
        ArrayList<String> params = new ArrayList<>();
    
        if (criteria.containsKey("name")) {
            sql.append(" AND p.name LIKE ?");
            params.add("%" + criteria.get("name") + "%");
        }
        if (criteria.containsKey("author")) {
            sql.append(" AND p.author LIKE ?");
            params.add("%" + criteria.get("author") + "%");
        }
        if (criteria.containsKey("category_id")) {
            sql.append(" AND p.categoryid = ?");
            params.add(criteria.get("category_id"));
        }
        if (criteria.containsKey("price_min")) {
            sql.append(" AND p.price >= ?");
            params.add(criteria.get("price_min"));
        }
        if (criteria.containsKey("price_max")) {
            sql.append(" AND p.price <= ?");
            params.add(criteria.get("price_max"));
        }
    
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setString(i + 1, params.get(i));
            }
    
            try (ResultSet rs = stmt.executeQuery()) {
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
        }
        return products;
    }
    
    public Product searchById(int id) throws SQLException {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
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
        return null;
    }
    
    public List<Product> searchByName(String name) throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE name LIKE ?";
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setAuthor(rs.getString("author"));
                product.setPrice(rs.getDouble("price"));
                product.setCategoryId(rs.getInt("categoryid"));
                product.setImagePath(rs.getString("imagePath"));
                list.add(product);
            }
        }
        return list;
    }

    public List<Product> searchByAuthor(String author) throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE author LIKE ?";
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + author + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setAuthor(rs.getString("author"));
                product.setPrice(rs.getDouble("price"));
                product.setCategoryId(rs.getInt("categoryid"));
                product.setImagePath(rs.getString("imagePath"));
                list.add(product);
            }
        }
        return list;
    }
    
    public List<Product> searchByCategoryId(int categoryId) throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE categoryid = ?";
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setAuthor(rs.getString("author"));
                product.setPrice(rs.getDouble("price"));
                product.setCategoryId(rs.getInt("categoryid"));
                product.setImagePath(rs.getString("imagePath"));
                list.add(product);
            }
        }
        return list;
    }
}