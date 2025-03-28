package com.bookstore.dao;

import com.bookstore.config.DataBaseConfig;
import com.bookstore.model.Permission;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PermissionDAO {
    private Connection conn;

    public PermissionDAO() {
        try {
            conn = DataBaseConfig.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xem tất cả quyền
    public List<Permission> getAllPermissions() throws SQLException {
        List<Permission> permissions = new ArrayList<>();
        String query = "SELECT * FROM permissions";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Permission permission = new Permission();
                permission.setId(rs.getInt("id"));
                permission.setGroupId(rs.getInt("group_id"));
                permission.setName(rs.getString("name"));
                permissions.add(permission);
            }
        }
        return permissions;
    }

    // Xem quyền theo nhóm quyền
    public List<Permission> getPermissionsByGroupId(int groupId) throws SQLException {
        List<Permission> permissions = new ArrayList<>();
        String query = "SELECT * FROM permissions WHERE group_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, groupId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Permission permission = new Permission();
                permission.setId(rs.getInt("id"));
                permission.setGroupId(rs.getInt("group_id"));
                permission.setName(rs.getString("name"));
                permissions.add(permission);
            }
        }
        return permissions;
    }

    // Thêm quyền
    public void addPermission(Permission permission) throws SQLException {
        String query = "INSERT INTO permissions (group_id, name) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, permission.getGroupId());
            stmt.setString(2, permission.getName());
            stmt.executeUpdate();
        }
    }

    // Sửa quyền
    public void updatePermission(Permission permission) throws SQLException {
        String query = "UPDATE permissions SET group_id = ?, name = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, permission.getGroupId());
            stmt.setString(2, permission.getName());
            stmt.setInt(3, permission.getId());
            stmt.executeUpdate();
        }
    }

    // Xóa quyền
    public void deletePermission(int id) throws SQLException {
        String query = "DELETE FROM permissions WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Tìm kiếm quyền theo tên
    public List<Permission> searchPermissions(String keyword) throws SQLException {
        List<Permission> permissions = new ArrayList<>();
        String query = "SELECT * FROM permissions WHERE name LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Permission permission = new Permission();
                permission.setId(rs.getInt("id"));
                permission.setGroupId(rs.getInt("group_id"));
                permission.setName(rs.getString("name"));
                permissions.add(permission);
            }
        }
        return permissions;
    }
}