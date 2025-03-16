package com.bookstore.model.DAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PromotionDAO {
    private Connection conn;
    
    public PromotionDAO(Connection conn) {
        this.conn = conn;
    }
    public List<String> getAllPromotions() {
        List<String> promotions = new ArrayList<>();
        String sql = "SELECT * FROM Promotion";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                promotions.add("ID: " + rs.getInt("PromotionID") + ", Name: " + rs.getString("Name") + ", Discount: " + rs.getDouble("DiscountPercent") + "%");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return promotions;
    }
    public boolean addPromotion(String name, double discountPercent, Date startDate, Date endDate, Integer categoryID) {
        String sql = "INSERT INTO Promotion (Name, DiscountPercent, StartDate, EndDate, CategoryID) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setDouble(2, discountPercent);
            stmt.setDate(3, startDate);
            stmt.setDate(4, endDate);
            if (categoryID != null) {
                stmt.setInt(5, categoryID);
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updatePromotion(int promotionID, String name, double discountPercent, Date startDate, Date endDate, Integer categoryID) {
        String sql = "UPDATE Promotion SET Name = ?, DiscountPercent = ?, StartDate = ?, EndDate = ?, CategoryID = ? WHERE PromotionID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setDouble(2, discountPercent);
            stmt.setDate(3, startDate);
            stmt.setDate(4, endDate);
            if (categoryID != null) {
                stmt.setInt(5, categoryID);
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setInt(6, promotionID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deletePromotion(int promotionID) {
        String sql = "DELETE FROM Promotion WHERE PromotionID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, promotionID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<String> searchPromotionByName(String keyword) {
        List<String> results = new ArrayList<>();
        String sql = "SELECT * FROM Promotion WHERE Name LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(rs.getString("Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}
