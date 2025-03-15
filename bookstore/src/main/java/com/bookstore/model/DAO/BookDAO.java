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

    // 1️⃣ Thêm sách mới
    public boolean insertBook(BookDTO book) {
        String query = "INSERT INTO Book (Title, Author, Genre, Price, StockQuantity, CategoryID, ImagePath) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getGenre());
            pstmt.setDouble(4, book.getPrice());
            pstmt.setInt(5, book.getStockQuantity());
            pstmt.setInt(6, book.getCategoryID());
            pstmt.setString(7, book.getImagePath());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    book.setBookID(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 2️⃣ Sửa thông tin sách
    public boolean updateBook(BookDTO book) {
        String query = "UPDATE Book SET Title = ?, Author = ?, Genre = ?, Price = ?, StockQuantity = ?, CategoryID = ?, ImagePath = ? WHERE BookID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getGenre());
            pstmt.setDouble(4, book.getPrice());
            pstmt.setInt(5, book.getStockQuantity());
            pstmt.setInt(6, book.getCategoryID());
            pstmt.setString(7, book.getImagePath());
            pstmt.setInt(8, book.getBookID());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 3️⃣ Xóa sách theo ID
    public boolean deleteBook(int bookID) {
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
    public BookDTO searchBookById(int bookID) {
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
