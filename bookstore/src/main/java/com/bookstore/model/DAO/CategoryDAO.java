/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
    public List<CategoryDTO> getAllCategories() {
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
    public CategoryDTO getCategoryById(int id) {
        String query = "SELECT * FROM Category WHERE CategoryID = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
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
    public boolean insertCategory(CategoryDTO category) {
        String query = "INSERT INTO Category (Name) VALUES (?)";

        try (PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, category.getName());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    category.setCategoryID(rs.getInt(1)); // Gán ID mới cho đối tượng DTO
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean updateCategory(CategoryDTO category) {
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
}
