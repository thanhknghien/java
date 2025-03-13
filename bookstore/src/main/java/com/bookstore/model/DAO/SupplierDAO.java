package com.bookstore.model.DAO;

import com.bookstore.model.DBConnect;
import com.bookstore.model.DTO.SupplierDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {

    // Thêm một nhà cung cấp mới vào cơ sở dữ liệu
    public boolean insertSupplier(String name, String number, String address) throws SQLException {
        String sql = "INSERT INTO supplier (SupplierName, SupplierNumber, SupplierAddress) VALUES (?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, number);
            stmt.setString(3, address);
            return stmt.executeUpdate() > 0;
        }
    }

    // Cập nhật thông tin nhà cung cấp theo ID
    public boolean updateSupplier(int id, String name, String number, String address) throws SQLException {
        String sql = "UPDATE supplier SET SupplierName = ?, SupplierNumber = ?, SupplierAddress = ? WHERE SupplierID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, number);
            stmt.setString(3, address);
            stmt.setInt(4, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // Xóa nhà cung cấp theo ID
    public boolean deleteSupplier(int id) throws SQLException {
        String sql = "DELETE FROM supplier WHERE SupplierID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // Lấy danh sách tất cả nhà cung cấp
    public List<SupplierDTO> getAllSuppliers() throws SQLException {
        List<SupplierDTO> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM supplier";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                suppliers.add(new SupplierDTO(
                        rs.getInt("SupplierID"),
                        rs.getString("SupplierName"),
                        rs.getString("SupplierNumber"),
                        rs.getString("SupplierAddress"),
                        rs.getString("Status")
                ));
            }
        }
        return suppliers;
    }

    // Truy vấn thông tin nhà cung cấp theo ID
    public SupplierDTO getSupplierById(int id) throws SQLException {
        String sql = "SELECT * FROM supplier WHERE SupplierID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new SupplierDTO(
                            rs.getInt("SupplierID"),
                            rs.getString("SupplierName"),
                            rs.getString("SupplierNumber"),
                            rs.getString("SupplierAddress"),
                            rs.getString("Status")
                    );
                }
            }
        }
        return null;
    }
}