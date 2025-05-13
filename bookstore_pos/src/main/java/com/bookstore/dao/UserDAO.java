package com.bookstore.dao;

import com.bookstore.config.DataBaseConfig;
import com.bookstore.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection conn;

    /**
     * Khởi tạo đối tượng UserDAO với kết nối đến cơ sở dữ liệu
     */
    public UserDAO() {
        try {
            conn = DataBaseConfig.getConnection();
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }

    /**
     * Đóng kết nối cơ sở dữ liệu
     */
    public void closeConnection() {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    DataBaseConfig.closeConnection(conn);
                }
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
            }
        }
    }

    /**
     * Lấy danh sách tất cả người dùng
     * @return Danh sách người dùng
     * @throws SQLException Nếu có lỗi truy vấn SQL
     */
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        }
        return users;
    }

    /**
     * Thêm người dùng mới
     * @param user Đối tượng User cần thêm
     * @throws SQLException Nếu có lỗi truy vấn SQL
     */
    public void addUser(User user) throws SQLException {
        String query = "INSERT INTO users (username, password, role_id, status) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            setNullableInteger(stmt, 3, user.getRoleId());
            stmt.setBoolean(4, user.isStatus());
            stmt.executeUpdate();
        }
    }

    /**
     * Cập nhật thông tin người dùng
     * @param user Đối tượng User cần cập nhật
     * @throws SQLException Nếu có lỗi truy vấn SQL
     */
    public void updateUser(User user) throws SQLException {
        String query = "UPDATE users SET username = ?, password = ?, role_id = ?, status = ? WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            setNullableInteger(stmt, 3, user.getRoleId());
            stmt.setBoolean(4, user.isStatus());
            stmt.setInt(5, user.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * Xóa người dùng theo ID (thực tế là cập nhật trạng thái thành không hoạt động)
     * @param id ID của người dùng cần xóa
     * @throws SQLException Nếu có lỗi truy vấn SQL
     */
    public void deleteUser(int id) throws SQLException {
        String query = "UPDATE users SET status = false WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Tìm kiếm người dùng theo tên đăng nhập
     * @param keyword Từ khóa tìm kiếm
     * @return Danh sách người dùng phù hợp
     * @throws SQLException Nếu có lỗi truy vấn SQL
     */
    public List<User> searchUsers(String keyword) throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE username LIKE ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + keyword + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(extractUserFromResultSet(rs));
                }
            }
        }
        return users;
    }

    /**
     * Đăng nhập hệ thống
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     * @return Đối tượng User nếu đăng nhập thành công, null nếu thất bại
     * @throws SQLException Nếu có lỗi truy vấn SQL
     */
    public User login(String username, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ? AND password = ? AND status = true";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        }
        return null;
    }

    /**
     * Lấy thông tin người dùng theo ID
     * @param id ID của người dùng
     * @return Đối tượng User nếu tìm thấy, null nếu không tìm thấy
     * @throws SQLException Nếu có lỗi truy vấn SQL
     */
    public User getUserById(int id) throws SQLException {
        String query = "SELECT * FROM users WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        }
        return null;
    }

    /**
     * Tìm kiếm người dùng theo tên đăng nhập
     * @param username Tên đăng nhập
     * @return Đối tượng User nếu tìm thấy, null nếu không tìm thấy
     * @throws SQLException Nếu có lỗi truy vấn SQL
     */
    public User getUserByUsername(String username) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        }
        return null;
    }

    /**
     * Phương thức trợ giúp để trích xuất thông tin User từ ResultSet
     * @param rs ResultSet chứa dữ liệu người dùng
     * @return Đối tượng User đã được điền thông tin
     * @throws SQLException Nếu có lỗi truy vấn SQL
     */
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setStatus(rs.getBoolean("status"));
        
        Object roleIdObj = rs.getObject("role_id");
        if (roleIdObj != null) {
            user.setRoleId(rs.getInt("role_id"));
        } else {
            user.setRoleId(null);
        }
        
        return user;
    }

    /**
     * Phương thức trợ giúp để thiết lập giá trị Integer có thể null cho PreparedStatement
     * @param stmt PreparedStatement cần thiết lập tham số
     * @param paramIndex Vị trí tham số
     * @param value Giá trị Integer (có thể null)
     * @throws SQLException Nếu có lỗi truy vấn SQL
     */
    private void setNullableInteger(PreparedStatement stmt, int paramIndex, Integer value) throws SQLException {
        if (value != null) {
            stmt.setInt(paramIndex, value);
        } else {
            stmt.setNull(paramIndex, Types.INTEGER);
        }
    }
}