package com.bookstore.model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.bookstore.model.DBConnect;
import com.bookstore.model.DTO.RoleDTO;

public class RoleDAO {

    public boolean isRoleNameExists(String roleName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Role WHERE RoleName = ?";
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = DBConnect.getConnection();
            rs = DBConnect.executeQuery(sql, roleName);
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } finally {
            DBConnect.closeResultSet(rs);
            DBConnect.closeConnection();
        }
    }

    // Lấy danh sách tất cả vai trò
    public ArrayList<RoleDTO> getAllRoles() throws SQLException {
        ArrayList<RoleDTO> roles = new ArrayList<>();
        String sql = "SELECT * FROM Role";
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = DBConnect.getConnection();
            rs = DBConnect.executeQuery(sql);
            while (rs.next()) {
                roles.add(new RoleDTO(
                    rs.getInt("RoleID"),
                    rs.getString("RoleName")
                ));
            }
        } finally {
            DBConnect.closeResultSet(rs);
            DBConnect.closeConnection();
        }
        return roles;
    }

    // Lấy vai trò theo ID
    public RoleDTO getRoleByID(int roleID) throws SQLException {
        String sql = "SELECT * FROM Role WHERE RoleID = ?";
        ResultSet rs = null;
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = DBConnect.getConnection();
            rs = DBConnect.executeQuery(sql, roleID);
            if (rs.next()) {
                return new RoleDTO(
                    rs.getInt("RoleID"),
                    rs.getString("RoleName")
                );
            }
            return null;
        } finally {
            DBConnect.closeResultSet(rs);
            DBConnect.closeStatement(stmt);
            DBConnect.closeConnection();
        }
    }

    // Thêm vai trò mới
    public void addRole(RoleDTO role) throws SQLException {
        // Kiểm tra RoleName trùng
        if (isRoleNameExists(role.getRoleName())) {
            throw new SQLException("RoleName already exists.");
        }

        String sql = "INSERT INTO Role (RoleName) VALUES (?)";
        DBConnect.executeUpdate(sql, role.getRoleName());
    }

    // Cập nhật vai trò
    public void updateRole(RoleDTO role) throws SQLException {
        // Kiểm tra RoleName trùng (trừ chính nó)
        String sqlCheck = "SELECT COUNT(*) FROM Role WHERE RoleName = ? AND RoleID != ?";
        ResultSet rs = null;
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = DBConnect.getConnection();
            rs = DBConnect.executeQuery(sqlCheck, role.getRoleName(), role.getRoleID());
            if (rs.next() && rs.getInt(1) > 0) {
                throw new SQLException("RoleName already exists.");
            }
        } finally {
            DBConnect.closeResultSet(rs);
            DBConnect.closeStatement(stmt);
            DBConnect.closeConnection();
        }

        String sql = "UPDATE Role SET RoleName = ? WHERE RoleID = ?";
        DBConnect.executeUpdate(sql, role.getRoleName(), role.getRoleID());
    }

    public boolean deleteRole(int roleID) throws SQLException {
        String checkSQL = "SELECT COUNT(*) FROM RolePermission WHERE RoleID = ?";
        String deleteSQL = "DELETE FROM Role WHERE RoleID = ?";
    
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSQL)) {
            checkStmt.setInt(1, roleID);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new SQLException("Cannot delete Role because it is assigned to roles.");
                }
            }
        }
    
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL)) {
            deleteStmt.setInt(1, roleID);
            return deleteStmt.executeUpdate() > 0;
        }
    }
    
}
