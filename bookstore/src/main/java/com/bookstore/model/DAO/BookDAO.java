package com.bookstore.model.DAO;

import com.bookstore.model.DTO.BookDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private Connection conn;

    public BookDAO(Connection conn) {
        this.conn = conn;
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

    // 1️⃣ Thêm sách mới
    public boolean insertBookWithDetails(String title, String author, String genre, double price, int stockQuantity, int categoryID, String imagePath) {
        String query = "INSERT INTO Book (Title, Author, Genre, Price, StockQuantity, CategoryID, ImagePath) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, genre);
            pstmt.setDouble(4, price);
            pstmt.setInt(5, stockQuantity);
            pstmt.setInt(6, categoryID);
            pstmt.setString(7, imagePath);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 2️⃣ Sửa thông tin sách
    public boolean updateBookWithID(int bookID, String title, String author, String genre, double price, int stockQuantity, int categoryID, String imagePath) {
        String query = "UPDATE Book SET Title = ?, Author = ?, Genre = ?, Price = ?, StockQuantity = ?, CategoryID = ?, ImagePath = ? WHERE BookID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, genre);
            pstmt.setDouble(4, price);
            pstmt.setInt(5, stockQuantity);
            pstmt.setInt(6, categoryID);
            pstmt.setString(7, imagePath);
            pstmt.setInt(8, bookID);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 3️⃣ Xóa sách theo ID
     public boolean deleteBookByID(int bookID) {
        String query = "DELETE FROM Book WHERE BookID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookID);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4️⃣ Tìm kiếm sách theo ID
    public BookDTO findBookByID(int bookID) {
        String query = "SELECT * FROM Book WHERE BookID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new BookDTO(
                    rs.getInt("BookID"),
                    rs.getString("Title"),
                    rs.getString("Author"),
                    rs.getString("Genre"),
                    rs.getDouble("Price"),
                    rs.getInt("StockQuantity"),
                    rs.getInt("CategoryID"),
                    rs.getString("ImagePath")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
