package com.bookstore.model.DAO;

import com.bookstore.model.DBConnect;
import com.bookstore.model.DTO.SupplierDTO;

import java.sql.*;
import java.util.ArrayList;

public class SupplierDAO {

    // Thêm một nhà cung cấp mới vào cơ sở dữ liệu
    public boolean insertSupplier(SupplierDTO supplier) throws SQLException {
        String sql = "INSERT INTO supplier (SupplierID, SupplierName, SupplierNumber, SupplierAddress, Status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, supplier.getSupplierID());
            stmt.setString(2, supplier.getSupplierName());
            stmt.setString(3, supplier.getSupplierNumber());
            stmt.setString(4, supplier.getSupplierAddress());
            stmt.setString(5, supplier.getStatus());
            return stmt.executeUpdate() > 0;
        }
    }

    // Cập nhật thông tin nhà cung cấp theo ID
    public boolean updateSupplier(SupplierDTO supplier) throws SQLException {
        String sql = "UPDATE supplier SET SupplierName = ?, SupplierNumber = ?, SupplierAddress = ?, Status = ? WHERE SupplierID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, supplier.getSupplierName());
            stmt.setString(2, supplier.getSupplierNumber());
            stmt.setString(3, supplier.getSupplierAddress());
            stmt.setString(4, supplier.getStatus());
            stmt.setInt(5, supplier.getSupplierID());
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
    public ArrayList<SupplierDTO> getAllSuppliers() throws SQLException {
        ArrayList<SupplierDTO> suppliers = new ArrayList<>();
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