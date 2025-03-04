package com.bookstore.model;

import java.sql.*;

public class DBConnect {
    private static final String URL = "jdbc:mysql://localhost:3306/bookstore";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection = null;

    // Lấy kết nối
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Kết nối thành công");
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL Driver not found: " + e.getMessage());
            }
        }
        return connection;
    }

    // Đóng kết nối
    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Đóng kết nối thành công");
        }
    }

    // Đóng Statement
    public static void closeStatement(Statement stmt) throws SQLException {
        if (stmt != null) {
            stmt.close();
        }
    }

    // Đóng ResultSet
    public static void closeResultSet(ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
        }
    }

    // Thực thi lệnh INSERT, UPDATE, DELETE
    public static boolean executeUpdate(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            // Gán tham số vào câu truy vấn
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu có ít nhất 1 hàng bị ảnh hưởng
        } catch (SQLException e) {
            // Bắt MESSAGE_TEXT từ trigger MySQL
            if ("45000".equals(e.getSQLState())) {
                String messageText = e.getMessage();
                // Định dạng thông điệp lỗi có thể thay đổi, thường MESSAGE_TEXT nằm sau dấu hai chấm
                if (messageText.contains(":")) {
                    messageText = messageText.substring(messageText.lastIndexOf(":") + 2);
                }
                throw new SQLException(messageText, e);
            }
            throw e; // Ném lại các lỗi khác
        } finally {
            closeStatement(stmt);
            closeConnection();
        }
    }

    // Thực thi lệnh SELECT
    public static ResultSet executeQuery(String sql, Object... params) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        // Gán tham số vào câu truy vấn
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
        return stmt.executeQuery(); // Người gọi phải đóng ResultSet, Statement, Connection thủ công
    }
}