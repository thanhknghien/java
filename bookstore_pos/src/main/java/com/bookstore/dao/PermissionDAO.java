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

    // Hàm main để test
    public static void main(String[] args) {
        try {
            PermissionDAO permissionDAO = new PermissionDAO();
            
            // Test thêm quyền
            System.out.println("=== Test thêm quyền ===");
            Permission newPermission = new Permission();
            newPermission.setGroupId(1); // Giả sử có nhóm quyền với ID = 1
            newPermission.setName("Thêm sản phẩm");
            permissionDAO.addPermission(newPermission);
            System.out.println("Đã thêm quyền: " + newPermission.getName());
            
            // Test lấy tất cả quyền
            System.out.println("\n=== Test lấy tất cả quyền ===");
            List<Permission> allPermissions = permissionDAO.getAllPermissions();
            System.out.println("Danh sách tất cả quyền:");
            for (Permission p : allPermissions) {
                System.out.println("ID: " + p.getId() + ", Nhóm: " + p.getGroupId() + ", Tên: " + p.getName());
            }
            
            // Test lấy quyền theo nhóm
            System.out.println("\n=== Test lấy quyền theo nhóm ===");
            List<Permission> groupPermissions = permissionDAO.getPermissionsByGroupId(1);
            System.out.println("Danh sách quyền của nhóm 1:");
            for (Permission p : groupPermissions) {
                System.out.println("ID: " + p.getId() + ", Tên: " + p.getName());
            }
            
            // Test tìm kiếm quyền
            System.out.println("\n=== Test tìm kiếm quyền ===");
            List<Permission> searchResults = permissionDAO.searchPermissions("sản phẩm");
            System.out.println("Kết quả tìm kiếm quyền chứa 'sản phẩm':");
            for (Permission p : searchResults) {
                System.out.println("ID: " + p.getId() + ", Tên: " + p.getName());
            }
            
            // Test sửa quyền
            System.out.println("\n=== Test sửa quyền ===");
            if (!searchResults.isEmpty()) {
                Permission permissionToUpdate = searchResults.get(0);
                permissionToUpdate.setName("Thêm/Sửa sản phẩm");
                permissionDAO.updatePermission(permissionToUpdate);
                System.out.println("Đã sửa quyền ID " + permissionToUpdate.getId() + " thành: " + permissionToUpdate.getName());
            }
            
            // Test xóa quyền
            System.out.println("\n=== Test xóa quyền ===");
            if (!searchResults.isEmpty()) {
                int permissionId = searchResults.get(0).getId();
                permissionDAO.deletePermission(permissionId);
                System.out.println("Đã xóa quyền ID: " + permissionId);
            }
            
        } catch (SQLException e) {
            System.err.println("Lỗi khi test PermissionDAO: " + e.getMessage());
            e.printStackTrace();
        }
    }
}