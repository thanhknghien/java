package com.bookstore.model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.bookstore.model.DBConnect;
import com.bookstore.model.DTO.PermissionDTO;
import com.bookstore.model.DTO.RoleDTO;

public class RolePermissionDAO {
    public boolean addPermissionToRole(int roleID, int permissionID) throws SQLException {
        String sql = "INSERT INTO RolePermission (RoleID, PermissionID) VALUES (?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roleID);
            stmt.setInt(2, permissionID);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deletePermissionFromRole(int roleID, int permissionID) throws SQLException {
        String sql = "DELETE FROM RolePermission WHERE RoleID = ? AND PermissionID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roleID);
            stmt.setInt(2, permissionID);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteAllPermissionsFromRole(int roleID) throws SQLException {
        String sql = "DELETE FROM RolePermission WHERE RoleID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roleID);
            return stmt.executeUpdate() > 0;
        }
    }

    public ArrayList<PermissionDTO> getPermissionsByRoleID(int roleID) throws SQLException {
        ArrayList<PermissionDTO> list = new ArrayList<>();
        String sql = "SELECT P.PermissionID, P.PermissionName " +
                     "FROM Permission P " +
                     "JOIN RolePermission RP ON P.PermissionID = RP.PermissionID " +
                     "WHERE RP.RoleID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roleID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new PermissionDTO(rs.getInt("PermissionID"), rs.getString("PermissionName")));
                }
            }
        }
        return list;
    }

    public ArrayList<RoleDTO> getRolesByPermissionID(int permissionID) throws SQLException {
        ArrayList<RoleDTO> list = new ArrayList<>();
        String sql = "SELECT R.RoleID, R.RoleName " +
                     "FROM Role R " +
                     "JOIN RolePermission RP ON R.RoleID = RP.RoleID " +
                     "WHERE RP.PermissionID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, permissionID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new RoleDTO(rs.getInt("RoleID"), rs.getString("RoleName")));
                }
            }
        }
        return list;
    }

    public boolean isPermissionAssigned(int roleID, int permissionID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM RolePermission WHERE RoleID = ? AND PermissionID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roleID);
            stmt.setInt(2, permissionID);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}
