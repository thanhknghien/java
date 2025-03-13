package com.bookstore.model.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.bookstore.model.DBConnect;
import com.bookstore.model.DTO.RoleDTO;

public class RoleDAO {
    // Insert
    public boolean insertRole(RoleDTO role) throws SQLException {
        String sql = "INSERT INTO Role (RoleName) VALUES (?)";
        return DBConnect.executeUpdate(sql, role.getRoleName());
    }

    // Update
    public boolean updateRole(RoleDTO role) throws SQLException {
        String sql = "UPDATE Role SET RoleName = ? WHERE RoleID = ?";
        return DBConnect.executeUpdate(sql, role.getRoleName(), role.getRoleID());
    }

    // Delete
    public boolean deleteRole(int roleID) throws SQLException {
        String sql = "DELETE FROM Role WHERE RoleID = ?";
        return DBConnect.executeUpdate(sql, roleID);
    }

    // Get All
    public ArrayList<RoleDTO> getAllRoles() throws SQLException {
        ArrayList<RoleDTO> roles = new ArrayList<>();
        String sql = "SELECT * FROM Role";
        ResultSet rs = DBConnect.executeQuery(sql);

        while (rs.next()) {
            roles.add(new RoleDTO(rs.getInt("RoleID"), rs.getString("RoleName")));
        }

        DBConnect.closeResultSet(rs);
        return roles;
    }

    // Find By ID
    public RoleDTO getRoleById(int roleID) throws SQLException {
        String sql = "SELECT * FROM Role WHERE RoleID = ?";
        ResultSet rs = DBConnect.executeQuery(sql, roleID);

        if (rs.next()) {
            RoleDTO role = new RoleDTO(rs.getInt("RoleID"), rs.getString("RoleName"));
            DBConnect.closeResultSet(rs);
            return role;
        }

        DBConnect.closeResultSet(rs);
        return null;
    }
}
