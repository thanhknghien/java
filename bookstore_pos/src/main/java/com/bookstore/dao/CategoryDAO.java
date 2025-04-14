/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.dao;

import com.bookstore.config.DataBaseConfig;
import com.bookstore.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class CategoryDAO {
    private Connection conn;
    
    public CategoryDAO(){
        try{
            conn = DataBaseConfig.getConnection();
        }catch(SQLException e){
             e.printStackTrace();
        }
    }
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM Category";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                categories.add(new Category(
                       rs.getInt("CategoryID"),
                       rs.getString("Name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
    public List<Category> getCategoriesByName(String name) {
    List<Category> categories = new ArrayList<>();
    String query = "SELECT * FROM Category WHERE Name LIKE ?";
    
    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, "%" + name + "%"); // Tìm kiếm gần đúng
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            categories.add(new Category(
                rs.getInt("CategoryID"),
                rs.getString("Name")
            ));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return categories;
}
    public Category getCategoryById(int id) {
        String query = "SELECT * FROM Category WHERE CategoryID = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Category(
                        rs.getInt("CategoryID"),
                        rs.getString("Name")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean insertCategory(Category category) {
        String sql = "INSERT INTO Category (name) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, category.getName());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int newId = rs.getInt(1); // Lấy ID mới từ DB
                    category.setCategoryID(newId); // Cập nhật ID vào category
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateCategory(Category category) {
        String query = "UPDATE Category SET name = ? WHERE CategoryID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, category.getName()); // Gán giá trị Name
            pstmt.setInt(2, category.getCategoryID()); // Gán giá trị CategoryID (dùng trong WHERE)

            return pstmt.executeUpdate() > 0; // Trả về true nếu cập nhật thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean deleteCategory(int id) {
        String query = "DELETE FROM Category WHERE CategoryID = ?";

        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
             e.printStackTrace();
        }
        return false;
    }
    public boolean exists(int id) throws SQLException {
        String sql = "SELECT 1 FROM category WHERE CategoryID = ?";
        try (Connection conn = DataBaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // true nếu có dòng nào khớp
            }
        }
    }


}
