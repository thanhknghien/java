package com.bookstore.dao;

import com.bookstore.config.DataBaseConfig;
import com.bookstore.model.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {
    private Connection conn;

    public RoleDAO() {
        try {
            conn = DataBaseConfig.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xem tất cả vai trò
    public List<Role> getAllRoles() throws SQLException {
        List<Role> roles = new ArrayList<>();
        String query = "SELECT * FROM roles";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setName(rs.getString("name"));
                roles.add(role);
            }
        }
        return roles;
    }

    // Thêm vai trò
    public void addRole(Role role) throws SQLException {
        String query = "INSERT INTO roles (name) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, role.getName());
            stmt.executeUpdate();
        }
    }

    // Sửa vai trò
    public void updateRole(Role role) throws SQLException {
        String query = "UPDATE roles SET name = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, role.getName());
            stmt.setInt(2, role.getId());
            stmt.executeUpdate();
        }
    }

    // Xóa vai trò
    public void deleteRole(int id) throws SQLException {
        String query = "DELETE FROM roles WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Tìm kiếm vai trò theo tên
    public List<Role> searchRoles(String keyword) throws SQLException {
        List<Role> roles = new ArrayList<>();
        String query = "SELECT * FROM roles WHERE name LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setName(rs.getString("name"));
                roles.add(role);
            }
        }
        return roles;
    }

    // Lấy thông tin vai trò theo ID
    public Role getRoleById(int id) throws SQLException {
        String query = "SELECT * FROM roles WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setName(rs.getString("name"));
                return role;
            }
        }
        return null;
    }
}