package com.bookstore.dao;

import com.bookstore.config.DataBaseConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RolePermissionConfigDAO {
    private Connection connection;

    public RolePermissionConfigDAO() throws SQLException {
        // Giả sử có một lớp DatabaseConnection để lấy kết nối
        this.connection = DataBaseConfig.getConnection();    
    }

    public Map<String, Map<String, Boolean>> getPermissionsForRole(int roleId) throws SQLException {
        Map<String, Map<String, Boolean>> permissions = new HashMap<>();
        String query = "SELECT module, action, allowed FROM role_permission_configs WHERE role_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, roleId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String module = rs.getString("module");
                String action = rs.getString("action");
                boolean allowed = rs.getBoolean("allowed");
                
                permissions.computeIfAbsent(module, k -> new HashMap<>()).put(action, allowed);
            }
        }
        
        return permissions;
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}