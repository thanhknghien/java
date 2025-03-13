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
                System.out.println("✅ Kết nối MySQL thành công!");
            } catch (ClassNotFoundException e) {
                throw new SQLException("❌ Lỗi: MySQL Driver không tìm thấy: " + e.getMessage());
            }
        }
        return connection;
    }

    // Đóng kết nối
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔻 Đã đóng kết nối MySQL.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi đóng kết nối: " + e.getMessage());
        }
    }

    // Đóng Statement
    public static void closeStatement(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi đóng Statement: " + e.getMessage());
        }
    }

    // Đóng ResultSet
    public static void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi đóng ResultSet: " + e.getMessage());
        }
    }

    // Thực thi INSERT, UPDATE, DELETE
    public static boolean executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; 
        } catch (SQLException e) {
            handleSQLException(e);
            return false;
        }
    }

    // Thực thi SELECT
    public static ResultSet executeQuery(String sql, Object... params) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        // Gán tham số vào câu truy vấn
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
        
        return stmt.executeQuery(); 
    }

    // Xử lý lỗi SQL (bắt lỗi từ trigger)
    private static void handleSQLException(SQLException e) throws SQLException {
        if ("45000".equals(e.getSQLState())) {
            String messageText = e.getMessage();
            if (messageText.contains(":")) {
                messageText = messageText.substring(messageText.lastIndexOf(":") + 2);
            }
            throw new SQLException(messageText, e);
        }
        throw e;
    }
}
