package com.bookstore.dao;

import com.bookstore.config.DataBaseConfig;
import com.bookstore.model.RolePermission;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RolePermissionDAO {
    private Connection conn;

    public RolePermissionDAO() {
        try {
            conn = DataBaseConfig.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xem tất cả ánh xạ quyền-vai trò
    public List<RolePermission> getAllRolePermissions() throws SQLException {
        List<RolePermission> rolePermissions = new ArrayList<>();
        String query = "SELECT * FROM role_permissions";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(rs.getInt("role_id"));
                rolePermission.setPermissionId(rs.getInt("permission_id"));
                rolePermissions.add(rolePermission);
            }
        }
        return rolePermissions;
    }

    // Xem quyền của một vai trò
    public List<RolePermission> getRolePermissionsByRoleId(int roleId) throws SQLException {
        List<RolePermission> rolePermissions = new ArrayList<>();
        String query = "SELECT * FROM role_permissions WHERE role_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, roleId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(rs.getInt("role_id"));
                rolePermission.setPermissionId(rs.getInt("permission_id"));
                rolePermissions.add(rolePermission);
            }
        }
        return rolePermissions;
    }

    // Thêm ánh xạ quyền-vai trò
    public void addRolePermission(RolePermission rolePermission) throws SQLException {
        String query = "INSERT INTO role_permissions (role_id, permission_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, rolePermission.getRoleId());
            stmt.setInt(2, rolePermission.getPermissionId());
            stmt.executeUpdate();
        }
    }

    // Xóa ánh xạ quyền-vai trò
    public void deleteRolePermission(int roleId, int permissionId) throws SQLException {
        String query = "DELETE FROM role_permissions WHERE role_id = ? AND permission_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, roleId);
            stmt.setInt(2, permissionId);
            stmt.executeUpdate();
        }
    }

    // Xóa tất cả quyền của một vai trò
    public void deleteRolePermissionsByRoleId(int roleId) throws SQLException {
        String query = "DELETE FROM role_permissions WHERE role_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, roleId);
            stmt.executeUpdate();
        }
    }
}