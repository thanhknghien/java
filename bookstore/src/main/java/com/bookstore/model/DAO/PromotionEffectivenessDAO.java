package com.bookstore.model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.bookstore.model.DBConnect;
import com.bookstore.model.DTO.PromotionEffectivenessDTO;

public class PromotionEffectivenessDAO {
    public ArrayList<PromotionEffectivenessDTO> getPromotionEffectiveness() {
        ArrayList<PromotionEffectivenessDTO> reportList = new ArrayList<>();
        String query = "SELECT p.Name AS PromotionName, p.StartDate, p.EndDate," + 
                       "COUNT(DISTINCT o.OrderID) AS TotalOrdersUsed,"+ 
                       "SUM(d.Quantity * d.UnitPrice * (p.DiscountPercent / 100)) AS TotalDiscountAmount" +
                        "FROM Promotion p" +
                "JOIN PromotionBook pb ON p.PromotionID = pb.PromotionID" +
                "JOIN OrderDetail d ON pb.BookID = d.BookID" +
                "JOIN `Order` o ON d.OrderID = o.OrderID" +
                "GROUP BY p.PromotionID" +
                "ORDER BY p.StartDate DESC;";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                reportList.add(new PromotionEffectivenessDTO(
                        rs.getString("PromotionName"),
                        rs.getDate("StartDate"),
                        rs.getDate("EndDate"),
                        rs.getInt("TotalOrdersUsed"),
                        rs.getDouble("TotalDiscountAmount")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportList;
    }
}
