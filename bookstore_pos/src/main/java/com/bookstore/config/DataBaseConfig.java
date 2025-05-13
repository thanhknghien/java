package com.bookstore.config;

import java.sql.*;

public class DataBaseConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/pos_system";
    private static final String USER = "root";  
    private static final String PASSWORD = ""; 

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Không tìm thấy Driver MySQL!", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Đóng Connection
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println(" Đã đóng kết nối Database.");
            } catch (SQLException e) {
                System.err.println(" Lỗi khi đóng Connection: " + e.getMessage());
            }
        }
    }

// Đóng Statement hoặc PreparedStatement
    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
                System.out.println(" Đã đóng Statement.");
            } catch (SQLException e) {
                System.err.println(" Lỗi khi đóng Statement: " + e.getMessage());
            }
        }
    }

// Đóng ResultSet
public static void closeResultSet(ResultSet rs) {
    if (rs != null) {
        try {
            rs.close();
            System.out.println(" Đã đóng ResultSet.");
        } catch (SQLException e) {
            System.err.println(" Lỗi khi đóng ResultSet: " + e.getMessage());
        }
    }
}

}