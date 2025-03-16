package com.bookstore;

import com.bookstore.model.DAO.BookDAO;
import com.bookstore.model.DAO.CategoryDAO;
import com.bookstore.model.DAO.PromotionBookDAO;
import com.bookstore.model.DAO.PromotionDAO;
import com.bookstore.model.DBConnect;
import com.bookstore.model.DTO.BookDTO;
import com.bookstore.model.DTO.CategoryDTO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class TestChi {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookstore", "root", "");
            CategoryDAO categoryDAO = new CategoryDAO(conn);
            BookDAO bookDAO = new BookDAO(conn);
            PromotionDAO dao = new PromotionDAO(conn);
            PromotionBookDAO pbookDao = new PromotionBookDAO(conn);
            // 1️⃣ Test thêm thể loại
//            System.out.println("🟢 Thêm thể loại: " + categoryDAO.insertCategoryDAO("Tiểu thuyết"));

            // 2️⃣ Test lấy danh sách thể loại
//            List<CategoryDTO> categories = categoryDAO.getAllCategoriesDAO();
//            System.out.println("📚 Danh sách thể loại:");
//            for (CategoryDTO category : categories) {
//                System.out.println("- ID: " + category.getCategoryID() + ", Tên: " + category.getName());
//            }
//
//            // 3️⃣ Test lấy thể loại theo ID
//            if (!categories.isEmpty()) {
//                int testID = categories.get(0).getCategoryID();
//                System.out.println("🔍 Tìm thể loại với ID " + testID + ": " + categoryDAO.getCategoryByIdDAO(testID));
//            }
//
//            // 4️⃣ Test cập nhật thể loại
//            if (!categories.isEmpty()) {
//                int testID = categories.get(0).getCategoryID();
//                System.out.println("📝 Cập nhật thể loại: " + categoryDAO.updateCategoryByIdDAO(testID, "Văn học hiện đại"));
//            }
//
//            // 5️⃣ Test xóa thể loại
//            if (!categories.isEmpty()) {
//                int testID = categories.get(categories.size() - 1).getCategoryID(); // Xóa phần tử cuối
//                System.out.println("🗑️ Xóa thể loại ID " + testID + ": " + categoryDAO.deleteCategoryByIdDAO(testID));
//            }

//            System.out.println("✅ Thêm sách mới...");
//            boolean inserted = bookDAO.insertBookWithDetails(
//                "Lập trình Java", "Nguyễn Văn A", "Công nghệ", 199000, 20, 1, "java.jpg"
//            );
//            System.out.println("Thêm sách thành công? " + inserted);
//
//            // 2️⃣ Test sửa thông tin sách (giả sử sửa sách có ID 1)
//            System.out.println("✅ Sửa thông tin sách...");
//            boolean updated = bookDAO.updateBookWithID(
//                1, "Lập trình Java (Phiên bản 3)", "Nguyễn Văn An", "Công nghệ", 209000, 15, 1, "java_v2.jpg"
//            );
//            System.out.println("Sửa sách thành công? " + updated);
//
//            // 3️⃣ Test tìm sách theo ID
//            System.out.println("✅ Tìm sách theo ID...");
//            BookDTO book = bookDAO.findBookByID(1);
//            if (book != null) {
//                System.out.println("Tìm thấy sách: " + book.getTitle() + " - " + book.getAuthor());
//            } else {
//                System.out.println("Không tìm thấy sách.");
//            }
//
//            }

            // 5️⃣ Test xóa sách
//            System.out.println("✅ Xóa sách...");
//            boolean deleted = bookDAO.deleteBookByID(5);
//            System.out.println("Xóa sách thành công? " + deleted);

            
//              Thêm khuyến mãi mới
//            dao.addPromotion("Giảm giá hè", 10.5, Date.valueOf("2025-06-01"), Date.valueOf("2025-06-30"), null);
            
            // Cập nhật khuyến mãi
//            dao.updatePromotion(2, "Giảm giá mùa đông", 15.0, Date.valueOf("2025-12-01"), Date.valueOf("2025-12-31"), null);
//            
//            // Tìm kiếm khuyến mãi theo tên
//            List<String> promotions = dao.searchPromotionByName("Giảm giá");
//            for (String promo : promotions) {
//                System.out.println("Tìm thấy: " + promo);
//            }
//            
//            // Xóa khuyến mãi
//            dao.deletePromotion(1);
//            // tat ca khuyen 
//            List<String> allPromotions = dao.getAllPromotions();
//            for (String promo : allPromotions) {
//                System.out.println("Khuyến mãi: " + promo);
//            }
             // Thêm sách vào khuyến mãi
//              pbookDao.addPromotionBook(3, 2);
            
//            // Cập nhật sách trong khuyến mãi
//            pbookDao.updatePromotionBook(2, 4, 3, 2);
//            
            // Tìm kiếm sách theo mã khuyến mãi
//            List<String> promotions = pbookDao.searchPromotionBookByPromotionID(2);
//            for (String promo : promotions) {
//                System.out.println("Tìm thấy: " + promo);
//            }
//            
            // Xuất tất cả sách trong các khuyến mãi
//            List<String> allPromotionBooks = pbookDao.getAllPromotionBooks();
//            for (String promo : allPromotionBooks) {
//                System.out.println("Khuyến mãi sách: " + promo);
//            }
//            
//            // Xóa sách khỏi khuyến mãi
//            pbookDao.deletePromotionBook(2, 4);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}    