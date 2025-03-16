package com.bookstore.model.DAO;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import com.bookstore.model.DBConnect;
import com.bookstore.model.DTO.CategoryDTO;

public class CategoryDAO {
    private Connection conn;
    
    public CategoryDAO(Connection conn) {
        this.conn = conn;
    }

    // Lấy danh sách tất cả thể loại
    public List<CategoryDTO> getAllCategoriesDAO() {
        List<CategoryDTO> categories = new ArrayList<>();
        String query = "SELECT * FROM Category";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                categories.add(new CategoryDTO(
                       rs.getInt("CategoryID"),
                       rs.getString("Name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    // Lấy thể loại theo ID
    public CategoryDTO getCategoryByIdDAO(int categoryID) {
        String query = "SELECT * FROM Category WHERE CategoryID = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, categoryID);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new CategoryDTO(
                        rs.getInt("CategoryID"),
                        rs.getString("Name")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm thể loại mới
    public boolean insertCategoryDAO(String categoryName) {
        String query = "INSERT INTO Category (Name) VALUES (?)";

        try (PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, categoryName);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật thể loại theo ID
    public boolean updateCategoryByIdDAO(int categoryID, String newCategoryName) {
        String query = "UPDATE Category SET Name = ? WHERE CategoryID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, newCategoryName);
            pstmt.setInt(2, categoryID);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa thể loại theo ID
    public boolean deleteCategoryByIdDAO(int categoryID) {
        String query = "DELETE FROM Category WHERE CategoryID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, categoryID);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
