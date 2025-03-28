package com.bookstore.dao;

import com.bookstore.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDAOTest {
    private ProductDAO productDAO;

    @BeforeEach
    void setUp() {
        productDAO = new ProductDAO();
        // Chèn dữ liệu mẫu (có thể dùng SQL script hoặc phương thức DAO)
        try {
            productDAO.deleteProduct(999); // Xóa dữ liệu cũ nếu có
            Product product = new Product();
            product.setId(999);
            product.setName("Test Book");
            product.setPrice(29.99);
            product.setStock(100);
            product.setCategory("Fiction");
            product.setAuthor("Test Author");
            productDAO.addProduct(product);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetAllProducts() throws SQLException {
        List<Product> products = productDAO.getAllProducts();
        assertFalse(products.isEmpty(), "Danh sách sản phẩm không được rỗng");
    }

    @Test
    void testAddProduct() throws SQLException {
        Product product = new Product();
        product.setName("New Book");
        product.setPrice(19.99);
        product.setStock(50);
        product.setCategory("Non-Fiction");
        product.setAuthor("New Author");
        productDAO.addProduct(product);

        List<Product> products = productDAO.searchProducts("New Book");
        assertFalse(products.isEmpty(), "Sản phẩm vừa thêm phải tồn tại");
        assertEquals("New Book", products.get(0).getName());
    }

    @Test
    void testUpdateProduct() throws SQLException {
        List<Product> products = productDAO.searchProducts("Test Book");
        Product product = products.get(0);
        product.setPrice(39.99);
        productDAO.updateProduct(product);

        products = productDAO.searchProducts("Test Book");
        assertEquals(39.99, products.get(0).getPrice(), "Giá sản phẩm phải được cập nhật");
    }

    @Test
    void testDeleteProduct() throws SQLException {
        productDAO.deleteProduct(1);
        List<Product> products = productDAO.searchProducts("Test Book");
        assertTrue(products.isEmpty(), "Sản phẩm phải được xóa");
    }

    @Test
    void testSearchProducts() throws SQLException {
        List<Product> products = productDAO.searchProducts("Test");
        assertFalse(products.isEmpty(), "Phải tìm thấy sản phẩm với từ khóa 'Test'");
        assertEquals("Test Book", products.get(0).getName());
    }
}