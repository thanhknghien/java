package com.bookstore.dao;

import com.bookstore.config.DataBaseConfig;
import com.bookstore.model.ProductManagement;
import com.bookstore.config.DataBaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductManagementDAO {
    private Connection connection;

    public ProductManagementDAO() throws SQLException {
        this.connection = DataBaseConfig.getConnection();
    }

    public List<ProductManagement> getAll() {
        List<ProductManagement> productManagements = new ArrayList<>();
        String sql = "SELECT * FROM product_management";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ProductManagement productManagement = new ProductManagement(
                    rs.getInt("id"),
                    rs.getBoolean("can_add"),
                    rs.getBoolean("can_edit"),
                    rs.getBoolean("can_delete"),
                    rs.getBoolean("can_view")
                );
                productManagements.add(productManagement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productManagements;
    }

    public ProductManagement getById(int id) {
        String sql = "SELECT * FROM product_management WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new ProductManagement(
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

    public boolean add(ProductManagement productManagement) {
        String sql = "INSERT INTO product_management (id, can_add, can_edit, can_delete, can_view) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, productManagement.getId());
            pstmt.setBoolean(2, productManagement.isCanAdd());
            pstmt.setBoolean(3, productManagement.isCanEdit());
            pstmt.setBoolean(4, productManagement.isCanDelete());
            pstmt.setBoolean(5, productManagement.isCanView());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(ProductManagement productManagement) {
        String sql = "UPDATE product_management SET can_add = ?, can_edit = ?, can_delete = ?, can_view = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, productManagement.isCanAdd());
            pstmt.setBoolean(2, productManagement.isCanEdit());
            pstmt.setBoolean(3, productManagement.isCanDelete());
            pstmt.setBoolean(4, productManagement.isCanView());
            pstmt.setInt(5, productManagement.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM product_management WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
} 