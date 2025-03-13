package com.bookstore.model.DAO;

import java.sql.*;
import java.util.*;

import com.bookstore.model.DBConnect;
import com.bookstore.model.DTO.ImportSlipDTO;

public class ImportSlipDAO {

    // Thêm một phiếu nhập mới
    public boolean insertImportSlip(ImportSlipDTO importSlip) {
        String sql = "INSERT INTO importslip (SlipID, EmployeeID, SupplierID, ImportDate, TotalAmount) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, importSlip.getSlipID());
            pstmt.setInt(2, importSlip.getEmployeeID());
            pstmt.setInt(3, importSlip.getSupplierID());
            pstmt.setDate(4, importSlip.getImportDate());
            pstmt.setDouble(5, importSlip.getTotalAmount());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật thông tin phiếu nhập theo ID
    public boolean updateImportSlip(ImportSlipDTO importSlip) {
        String sql = "UPDATE importslip SET employeeID = ?, supplierID = ?, importDate = ?, totalAmount = ? WHERE slipID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, importSlip.getEmployeeID());
            pstmt.setInt(2, importSlip.getSupplierID());
            pstmt.setDate(3, importSlip.getImportDate());
            pstmt.setDouble(4, importSlip.getTotalAmount());
            pstmt.setInt(5, importSlip.getSlipID());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa phiếu nhập theo ID
    public boolean deleteImportSlip(int slipID) {
        String sql = "DELETE FROM importslip WHERE slipID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, slipID);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy danh sách tất cả phiếu nhập
    public List<ImportSlipDTO> getAllImportSlips() {
        List<ImportSlipDTO> importSlips = new ArrayList<>();
        String sql = "SELECT * FROM importSlip";    
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                ImportSlipDTO importSlip = new ImportSlipDTO();
                importSlip.setSlipID(rs.getInt("slipID"));
                importSlip.setEmployeeID(rs.getInt("employeeID"));
                importSlip.setSupplierID(rs.getInt("supplierID"));
                importSlip.setImportDate(rs.getDate("importDate"));
                importSlip.setTotalAmount(rs.getDouble("totalAmount"));
                importSlips.add(importSlip);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return importSlips;
    }

    // Truy vấn thông tin phiếu nhập theo ID
    public ImportSlipDTO getImportSlipById(int slipID) {
        String sql = "SELECT * FROM importslip WHERE slipID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, slipID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                ImportSlipDTO importSlip = new ImportSlipDTO();
                importSlip.setSlipID(rs.getInt("slipID"));
                importSlip.setEmployeeID(rs.getInt("employeeID"));
                importSlip.setSupplierID(rs.getInt("supplierID"));
                importSlip.setImportDate(rs.getDate("importDate"));
                importSlip.setTotalAmount(rs.getDouble("totalAmount"));
                return importSlip;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}