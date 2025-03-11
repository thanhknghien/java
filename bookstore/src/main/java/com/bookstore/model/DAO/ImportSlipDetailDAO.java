package com.bookstore.model.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import com.bookstore.model.DBConnect;
import com.bookstore.model.DTO.ImportSlipDetailDTO;

public class ImportSlipDetailDAO {

    // Thêm một chi tiết phiếu nhập mới
    public boolean insertImportSlipDetail(ImportSlipDetailDTO importSlipDetail) {
        String sql = "INSERT INTO importslipdetail (slipID, bookID, quantity, unitPrice) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, importSlipDetail.getSlipID());
            pstmt.setInt(2, importSlipDetail.getBookID());
            pstmt.setInt(3, importSlipDetail.getQuantity());
            pstmt.setDouble(4, importSlipDetail.getUnitPrice());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật thông tin chi tiết phiếu nhập theo ID
    public boolean updateImportSlipDetail(ImportSlipDetailDTO importSlipDetail) {
        String sql = "UPDATE importslipdetail SET quantity = ?, unitPrice = ? WHERE slipID = ? AND bookID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, importSlipDetail.getQuantity());
            pstmt.setDouble(2, importSlipDetail.getUnitPrice());
            pstmt.setInt(3, importSlipDetail.getSlipID());
            pstmt.setInt(4, importSlipDetail.getBookID());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa chi tiết phiếu nhập theo ID
    public boolean deleteImportSlipDetail(int slipID, int bookID) {
        String sql = "DELETE FROM importslipdetail WHERE slipID = ? AND bookID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, slipID);
            pstmt.setInt(2, bookID);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy thông tin chi tiết phiếu nhập theo ID
    public ImportSlipDetailDTO getImportSlipDetailById(int slipID, int bookID) {
        String sql = "SELECT * FROM importslipdetail WHERE slipID = ? AND bookID = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, slipID);
            pstmt.setInt(2, bookID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                ImportSlipDetailDTO importSlipDetail = new ImportSlipDetailDTO();
                importSlipDetail.setSlipID(rs.getInt("slipID"));
                importSlipDetail.setBookID(rs.getInt("bookID"));
                importSlipDetail.setQuantity(rs.getInt("quantity"));
                importSlipDetail.setUnitPrice(rs.getDouble("unitPrice"));
                return importSlipDetail;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy danh sách tất cả chi tiết phiếu nhập
    public List<ImportSlipDetailDTO> getAllImportSlipDetails() {
        List<ImportSlipDetailDTO> importSlipDetails = new ArrayList<>();
        String sql = "SELECT * FROM importslipdetail";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                ImportSlipDetailDTO importSlipDetail = new ImportSlipDetailDTO();
                importSlipDetail.setSlipID(rs.getInt("slipID"));
                importSlipDetail.setBookID(rs.getInt("bookID"));
                importSlipDetail.setQuantity(rs.getInt("quantity"));
                importSlipDetail.setUnitPrice(rs.getDouble("unitPrice"));
                importSlipDetails.add(importSlipDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return importSlipDetails;
    }
}
