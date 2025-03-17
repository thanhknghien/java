package com.bookstore;

import com.bookstore.model.DAO.CategoryDAO;
import com.bookstore.model.DBConnect;
import com.bookstore.model.DTO.CategoryDTO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TestChi {
    public static void main(String[] args) throws SQLException {
        Connection conn = DBConnect.getConnection();
        if (conn == null) {
            System.out.println("Kết nối database thất bại!");
            return;
        }

    CategoryDAO categoryDAO = new CategoryDAO(conn);

    CategoryDTO updatedCategory = new CategoryDTO(1, "Sách Tâm Lý");
    
    boolean result = categoryDAO.updateCategory(updatedCategory);
    
        if (result) {
            System.out.println("✅ Cập nhật danh mục thành công!");
        } else {
            System.out.println("❌ Cập nhật danh mục thất bại!");
        }
    }
}    