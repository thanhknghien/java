package com.bookstore.dao;

import com.bookstore.config.DataBaseConfig;
import com.bookstore.model.UserManagement;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserManagementDAO {
    private Connection connection;

    public UserManagementDAO() throws SQLException {
        this.connection = DataBaseConfig.getConnection();
    }

    public List<UserManagement> getAll() {
        List<UserManagement> userManagements = new ArrayList<>();
        String sql = "SELECT * FROM user_management";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                UserManagement userManagement = new UserManagement(
                    rs.getInt("id"),
                    rs.getBoolean("can_add"),
                    rs.getBoolean("can_edit"),
                    rs.getBoolean("can_delete"),
                    rs.getBoolean("can_view")
                );
                userManagements.add(userManagement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userManagements;
    }

    public UserManagement getById(int id) {
        String sql = "SELECT * FROM user_management WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new UserManagement(
                    rs.getInt("id"),
                    rs.getBoolean("can_add"),
                    rs.getBoolean("can_edit"),
                    rs.getBoolean("can_delete"),
                    rs.getBoolean("can_view")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean add(UserManagement userManagement) {
        String sql = "INSERT INTO user_management (id, can_add, can_edit, can_delete, can_view) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userManagement.getId());
            pstmt.setBoolean(2, userManagement.isCanAdd());
            pstmt.setBoolean(3, userManagement.isCanEdit());
            pstmt.setBoolean(4, userManagement.isCanDelete());
            pstmt.setBoolean(5, userManagement.isCanView());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(UserManagement userManagement) {
        String sql = "UPDATE user_management SET  can_add = ?, can_edit = ?, can_delete = ?, can_view = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, userManagement.isCanAdd());
            pstmt.setBoolean(2, userManagement.isCanEdit());
            pstmt.setBoolean(3, userManagement.isCanDelete());
            pstmt.setBoolean(4, userManagement.isCanView());
            pstmt.setInt(5, userManagement.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM user_management WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
} 