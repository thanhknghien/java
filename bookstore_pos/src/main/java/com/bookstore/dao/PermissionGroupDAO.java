package com.bookstore.dao;

import com.bookstore.config.DataBaseConfig;
import com.bookstore.model.PermissionGroup;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PermissionGroupDAO {
    private Connection conn;

    public PermissionGroupDAO() {
        try {
            conn = DataBaseConfig.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xem tất cả nhóm quyền
    public List<PermissionGroup> getAllPermissionGroups() throws SQLException {
        List<PermissionGroup> groups = new ArrayList<>();
        String query = "SELECT * FROM permission_groups";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                PermissionGroup group = new PermissionGroup();
                group.setId(rs.getInt("id"));
                group.setName(rs.getString("name"));
                groups.add(group);
            }
        }
        return groups;
    }

    // Thêm nhóm quyền
    public void addPermissionGroup(PermissionGroup group) throws SQLException {
        String query = "INSERT INTO permission_groups (name) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, group.getName());
            stmt.executeUpdate();
        }
    }

    // Sửa nhóm quyền
    public void updatePermissionGroup(PermissionGroup group) throws SQLException {
        String query = "UPDATE permission_groups SET name = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, group.getName());
            stmt.setInt(2, group.getId());
            stmt.executeUpdate();
        }
    }

    // Xóa nhóm quyền
    public void deletePermissionGroup(int id) throws SQLException {
        String query = "DELETE FROM permission_groups WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Tìm kiếm nhóm quyền theo tên
    public List<PermissionGroup> searchPermissionGroups(String keyword) throws SQLException {
        List<PermissionGroup> groups = new ArrayList<>();
        String query = "SELECT * FROM permission_groups WHERE name LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PermissionGroup group = new PermissionGroup();
                group.setId(rs.getInt("id"));
                group.setName(rs.getString("name"));
                groups.add(group);
            }
        }
        return groups;
    }
}