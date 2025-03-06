package com.bookstore.model.DAO;

import com.bookstore.model.DTO.PermissionDTO;
import com.bookstore.model.DTO.RoleDTO;
import com.bookstore.model.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class PermissionDAO {

    public boolean isPermissionNameExists(String PermissionName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Permission WHERE PermissionName = ?";
        ResultSet rs = null;
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = DBConnect.getConnection();
            rs = DBConnect.executeQuery(sql, PermissionName);
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } finally {
            DBConnect.closeResultSet(rs);
            DBConnect.closeStatement(stmt);
            DBConnect.closeConnection();
        }
    }

    public ArrayList<PermissionDTO> getAllPermission() throws SQLException{
        ArrayList<PermissionDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Permission";
        ResultSet rs = null;
        PreparedStatement stmt  = null;
        Connection conn = null;
        try{
            conn = DBConnect.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new PermissionDTO(
                    rs.getInt("PermissionID"),
                    rs.getString("PermissionName")
                ));
            }
        }finally {
            DBConnect.closeResultSet(rs);
            DBConnect.closeStatement(stmt);
            DBConnect.closeConnection();
        }
        return list;
    }

    public PermissionDTO getPermissionByID(int PermissionID) throws SQLException {
        String sql = "SELECT * FROM Permission WHERE PermissionID = ?";
        ResultSet rs = null;
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = DBConnect.getConnection();
            rs = DBConnect.executeQuery(sql, PermissionID);
            if (rs.next()) {
                return new PermissionDTO(
                    rs.getInt("PermissionID"),
                    rs.getString("PermissionName")
                );
            }
            return null;
        } finally {
            DBConnect.closeResultSet(rs);
            DBConnect.closeStatement(stmt);
            DBConnect.closeConnection();
        }
    }

    public void addPermission(PermissionDTO Permission) throws SQLException {
        // Kiểm tra PermissionName trùng
        if (isPermissionNameExists(Permission.getPermissionName())) {
            throw new SQLException("PermissionName already exists.");
        }

        String sql = "INSERT INTO Permission (PermissionName) VALUES (?)";
        DBConnect.executeUpdate(sql, Permission.getPermissionName());
    }

    public void updatePermission(PermissionDTO Permission) throws SQLException {
        // Kiểm tra PermissionName trùng (trừ chính nó)
        String sqlCheck = "SELECT COUNT(*) FROM Permission WHERE PermissionName = ? AND PermissionID != ?";
        ResultSet rs = null;
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = DBConnect.getConnection();
            rs = DBConnect.executeQuery(sqlCheck, Permission.getPermissionName(), Permission.getPermissionID());
            if (rs.next() && rs.getInt(1) > 0) {
                throw new SQLException("PermissionName already exists.");
            }
        } finally {
            DBConnect.closeResultSet(rs);
            DBConnect.closeStatement(stmt);
            DBConnect.closeConnection();
        }

        String sql = "UPDATE Permission SET PermissionName = ? WHERE PermissionID = ?";
        DBConnect.executeUpdate(sql, Permission.getPermissionName(), Permission.getPermissionID());
    }

    public boolean deletePermission(int permissionID) throws SQLException {
        String checkSQL = "SELECT COUNT(*) FROM Role_Permission WHERE PermissionID = ?";
        String deleteSQL = "DELETE FROM Permission WHERE PermissionID = ?";
    
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSQL)) {
            checkStmt.setInt(1, permissionID);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new SQLException("Cannot delete permission because it is assigned to roles.");
                }
            }
        }
    
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL)) {
            deleteStmt.setInt(1, permissionID);
            return deleteStmt.executeUpdate() > 0;
        }
    }
    
}
