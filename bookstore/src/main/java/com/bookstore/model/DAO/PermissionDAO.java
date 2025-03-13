package com.bookstore.model.DAO;

import com.bookstore.model.DTO.PermissionDTO;
import com.bookstore.model.DBConnect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class PermissionDAO {
    // Insert
    public boolean insertPermission(PermissionDTO permission) throws SQLException {
        String sql = "INSERT INTO Permission (PermissionName) VALUES (?)";
        return DBConnect.executeUpdate(sql, permission.getPermissionName());
    }

    // Update
    public boolean updatePermission(PermissionDTO permission) throws SQLException {
        String sql = "UPDATE Permission SET PermissionName = ? WHERE PermissionID = ?";
        return DBConnect.executeUpdate(sql, permission.getPermissionName(), permission.getPermissionID());
    }

    // Delete
    public boolean deletePermission(int permissionID) throws SQLException {
        String sql = "DELETE FROM Permission WHERE PermissionID = ?";
        return DBConnect.executeUpdate(sql, permissionID);
    }

    // Get All
    public ArrayList<PermissionDTO> getAllPermissions() throws SQLException {
        ArrayList<PermissionDTO> permissions = new ArrayList<>();
        String sql = "SELECT * FROM Permission";
        ResultSet rs = DBConnect.executeQuery(sql);

        while (rs.next()) {
            permissions.add(new PermissionDTO(rs.getInt("PermissionID"), rs.getString("PermissionName")));
        }

        DBConnect.closeResultSet(rs);
        return permissions;
    }

    // Find by ID
    public PermissionDTO getPermissionById(int permissionID) throws SQLException {
        String sql = "SELECT * FROM Permission WHERE PermissionID = ?";
        ResultSet rs = DBConnect.executeQuery(sql, permissionID);

        if (rs.next()) {
            PermissionDTO permission = new PermissionDTO(rs.getInt("PermissionID"), rs.getString("PermissionName"));
            DBConnect.closeResultSet(rs);
            return permission;
        }

        DBConnect.closeResultSet(rs);
        return null;
    }
}