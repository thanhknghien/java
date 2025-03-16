/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.model.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PromotionBookDAO {
    private Connection conn;
    
    public PromotionBookDAO(Connection conn) {
        this.conn = conn;
    }
    
    public boolean addPromotionBook(int promotionID, int bookID) {
        String sql = "INSERT INTO PromotionBook (PromotionID, BookID) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, promotionID);
            stmt.setInt(2, bookID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updatePromotionBook(int promotionID, int bookID, int newPromotionID, int newBookID) {
        String sql = "UPDATE PromotionBook SET PromotionID = ?, BookID = ? WHERE PromotionID = ? AND BookID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newPromotionID);
            stmt.setInt(2, newBookID);
            stmt.setInt(3, promotionID);
            stmt.setInt(4, bookID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deletePromotionBook(int promotionID, int bookID) {
        String sql = "DELETE FROM PromotionBook WHERE PromotionID = ? AND BookID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, promotionID);
            stmt.setInt(2, bookID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<String> searchPromotionBookByPromotionID(int promotionID) {
        List<String> results = new ArrayList<>();
        String sql = "SELECT * FROM PromotionBook WHERE PromotionID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, promotionID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add("PromotionID: " + rs.getInt("PromotionID") + ", BookID: " + rs.getInt("BookID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
    
    public List<String> getAllPromotionBooks() {
        List<String> promotionBooks = new ArrayList<>();
        String sql = "SELECT * FROM PromotionBook";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                promotionBooks.add("PromotionID: " + rs.getInt("PromotionID") + ", BookID: " + rs.getInt("BookID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return promotionBooks;
    }
    
}

