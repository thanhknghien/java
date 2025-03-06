package com.bookstore.model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.bookstore.model.DBConnect;
import com.bookstore.model.DTO.SalesReportDTO;

public class SalesReportDAO {
    public ArrayList<SalesReportDTO> getSalesReport() {
        ArrayList<SalesReportDTO> reportList = new ArrayList<>();
        String query = "SELECT o.OrderID, o.OrderDate, b.Title AS BookTitle,"+ 
                       "d.Quantity, d.UnitPrice, (d.Quantity * d.UnitPrice) AS Revenue" +
                "FROM `Order` o" +
                "JOIN OrderDetail d ON o.OrderID = d.OrderID" +
                "JOIN Book b ON d.BookID = b.BookID" +
                "ORDER BY o.OrderDate DESC;";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                reportList.add(new SalesReportDTO(
                        rs.getInt("OrderID"),
                        rs.getTimestamp("OrderDate"),
                        rs.getString("BookTitle"),
                        rs.getInt("Quantity"),
                        rs.getDouble("UnitPrice"),
                        rs.getDouble("Revenue")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportList;
    }
}
