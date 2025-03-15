package com.bookstore;

import com.bookstore.model.DAO.BookDAO;
import com.bookstore.model.DAO.CategoryDAO;
import com.bookstore.model.DBConnect;
import com.bookstore.model.DTO.BookDTO;
import com.bookstore.model.DTO.CategoryDTO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class TestChi {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookstore", "root", "");
            BookDAO bookDAO = new BookDAO(conn);

            // Thêm sách
            BookDTO newBook = new BookDTO(0, "Java Programming", "John Doe", "Education", 29.99, 10, 1, "image.jpg");
            if (bookDAO.insertBook(newBook)) {
                System.out.println("Thêm sách thành công: " + newBook.getBookID());
            }

//            // Cập nhật sách
//            newBook.setPrice(35.99);
//            if (bookDAO.updateBook(newBook)) {
//                System.out.println("Cập nhật sách thành công");
//            }
//
//            // Tìm sách
//            BookDTO foundBook = bookDAO.searchBookById(newBook.getBookID());
//            if (foundBook != null) {
//                System.out.println("Tìm thấy sách: " + foundBook.getTitle());
//            }
//
//            // Xóa sách
//            if (bookDAO.deleteBook(newBook.getBookID())) {
//                System.out.println("Xóa sách thành công");
//            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}    