package com.bookstore.dao;

import com.bookstore.config.DataBaseConfig;

import java.sql.*;

public class PermissionManagementDAO {
    private Connection connection;

    public PermissionManagementDAO() throws SQLException {
        connection = DataBaseConfig.getConnection();
    }

    public void closeConnection() {
        DataBaseConfig.closeConnection(connection);
    }

    public boolean getCanManagePermissions(int userId) throws SQLException {
        String sql = "SELECT can_manage_permissions FROM permission_management WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("can_manage_permissions");
                }
            }
        }
        return false;
    }
    public void delete(int userId) throws SQLException {
        String query = "DELETE FROM permission_management WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }
    public boolean upsert(int userId, boolean canManagePermissions) throws SQLException {
        String sql = "INSERT INTO permission_management (user_id, can_manage_permissions) " +
                     "VALUES (?, ?) ON DUPLICATE KEY UPDATE can_manage_permissions = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setBoolean(2, canManagePermissions);
            stmt.setBoolean(3, canManagePermissions);
            return stmt.executeUpdate() > 0;
        }
    }
}