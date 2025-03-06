package com.bookstore.model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.bookstore.model.DBConnect;
import com.bookstore.model.DTO.CustomerPurchaseDTO;

public class CustomerPurchaseDAO {
    public ArrayList<CustomerPurchaseDTO> getTopCustomers() {
        ArrayList<CustomerPurchaseDTO> reportList = new ArrayList<>();
        String query = "SELECT c.Name AS CustomerName, COUNT(o.OrderID) AS TotalOrders," + 
                       "SUM(o.TotalAmount) AS TotalSpent" +
                "FROM Customer c" +
                "JOIN `Order` o ON c.CustomerID = o.CustomerID" +
                "GROUP BY c.CustomerID" +
                "ORDER BY TotalSpent DESC" +
                "LIMIT 10;";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                reportList.add(new CustomerPurchaseDTO(
                        rs.getString("CustomerName"),
                        rs.getInt("TotalOrders"),
                        rs.getDouble("TotalSpent")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportList;
    }
}
