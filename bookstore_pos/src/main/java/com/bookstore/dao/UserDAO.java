package com.bookstore.dao;

import com.bookstore.config.DataBaseConfig;
import com.bookstore.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection conn;

    public UserDAO() {
        try {
            conn = DataBaseConfig.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xem tất cả người dùng
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRoleId(rs.getObject("role_id") != null ? rs.getInt("role_id") : null);
                users.add(user);
            }
        }
        return users;
    }

    // Thêm người dùng
    public void addUser(User user) throws SQLException {
        String query = "INSERT INTO users (username, password, role_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            if (user.getRoleId() != null) {
                stmt.setInt(3, user.getRoleId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.executeUpdate();
        }
    }

    // Sửa người dùng
    public void updateUser(User user) throws SQLException {
        String query = "UPDATE users SET username = ?, password = ?, role_id = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            if (user.getRoleId() != null) {
                stmt.setInt(3, user.getRoleId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setInt(4, user.getId());
            stmt.executeUpdate();
        }
    }

    // Xóa người dùng
    public void deleteUser(int id) throws SQLException {
        String query = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Tìm kiếm người dùng theo username
    public List<User> searchUsers(String keyword) throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE username LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRoleId(rs.getObject("role_id") != null ? rs.getInt("role_id") : null);
                users.add(user);
            }
        }
        return users;
    }

    // Đăng nhập
    public User login(String username, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRoleId(rs.getObject("role_id") != null ? rs.getInt("role_id") : null);
                return user;
            }
        }
        return null;
    }
}