package com.bookstore.model.DAO;

import java.sql.*;
import java.util.ArrayList;

import com.bookstore.model.DBConnect;
import com.bookstore.model.DTO.InvoiceDTO;

public class InvoiceDAO {
    public boolean insertInvoice (InvoiceDTO invoice) throws SQLException {
        String sql = "INSERT INTO `Order` (OrderID, CustomerID, OrderDate, TotalAmount, Status) Values (?, ?, ?, ?, ?)";
        return DBConnect.executeUpdate(sql, 
                                            invoice.getInvoiceID(),
                                            invoice.getCustomerID(),
                                            invoice.getDate(),
                                            invoice.getTotalAmount(),
                                            invoice.getStatus());
    }
    public boolean updateInvoice (InvoiceDTO invoice) throws SQLException {
        String sql = "UPDATE `Order` SET CustomerID = ?, OrderDate = ?, TotalAmount = ?, Status = ? WHERE OrderID = ?";
        return DBConnect.executeUpdate(sql,
                                            invoice.getCustomerID(),
                                            invoice.getDate(),
                                            invoice.getStatus(),
                                            invoice.getInvoiceID());
    }
    public boolean deleteInvoice (int invoiceID) throws SQLException {
        String sql = "DELETE FROM `Order` WHERE OrderID = ?";
        return DBConnect.executeUpdate(sql, invoiceID);
    }
    
    public InvoiceDTO getInvoiceByID (int invoiceID) throws SQLException {
        String sql = "SELECT * FROM `Order` WHERE OrderID = ?";
        ResultSet rs = DBConnect.executeQuery(sql, invoiceID);
        if (rs.next()){
            return new InvoiceDTO (
                                    rs.getInt("OrderID"),
                                    rs.getInt("CustomerID"),
                                    rs.getString("OrderDate"),
                                    rs.getDouble("TotalAmounts"),
                                    rs.getInt("Status")
            );
        }
        return null;
    }
    public ArrayList<InvoiceDTO> getAllInvoices() throws SQLException {
        ArrayList<InvoiceDTO> invoices = new ArrayList<>();
        String sql = "SELECT * FROM `Order`";
        ResultSet rs = DBConnect.executeQuery(sql);
        
        while (rs.next()){
            invoices.add(new InvoiceDTO(
                                        rs.getInt("OrderID"),
                                        rs.getInt("CustomerID"),
                                        rs.getString("OrderDate"),
                                        rs.getDouble("TotalAmount"),
                                        rs.getInt("Status")
            ));
        }
        DBConnect.closeResultSet(rs);
        return invoices;
    } 
}
