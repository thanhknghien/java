package com.bookstore.dao;

import com.bookstore.model.InvoiceManagement;
import com.bookstore.config.DataBaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceManagementDAO {
    private Connection connection;

    public InvoiceManagementDAO() throws SQLException {
        this.connection = DataBaseConfig.getConnection();
    }

    public List<InvoiceManagement> getAll() {
        List<InvoiceManagement> invoiceManagements = new ArrayList<>();
        String sql = "SELECT * FROM invoice_management";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                InvoiceManagement invoiceManagement = new InvoiceManagement(
                    rs.getInt("id"),
                    rs.getBoolean("can_add"),
                    rs.getBoolean("can_edit"),
                    rs.getBoolean("can_delete"),
                    rs.getBoolean("can_view")
                );
                invoiceManagements.add(invoiceManagement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoiceManagements;
    }

    public InvoiceManagement getById(int id) {
        String sql = "SELECT * FROM invoice_management WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new InvoiceManagement(
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

    public boolean add(InvoiceManagement invoiceManagement) {
        String sql = "INSERT INTO invoice_management (id, can_add, can_edit, can_delete, can_view) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, invoiceManagement.getId());
            pstmt.setBoolean(2, invoiceManagement.isCanAdd());
            pstmt.setBoolean(3, invoiceManagement.isCanEdit());
            pstmt.setBoolean(4, invoiceManagement.isCanDelete());
            pstmt.setBoolean(5, invoiceManagement.isCanView());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(InvoiceManagement invoiceManagement) {
        String sql = "UPDATE invoice_management SET  can_add = ?, can_edit = ?, can_delete = ?, can_view = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, invoiceManagement.isCanAdd());
            pstmt.setBoolean(2, invoiceManagement.isCanEdit());
            pstmt.setBoolean(3, invoiceManagement.isCanDelete());
            pstmt.setBoolean(4, invoiceManagement.isCanView());
            pstmt.setInt(5, invoiceManagement.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM invoice_management WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
} 