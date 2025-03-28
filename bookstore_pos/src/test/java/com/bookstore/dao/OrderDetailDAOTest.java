package com.bookstore.dao;

import com.bookstore.model.Order;
import com.bookstore.model.OrderDetail;
import com.bookstore.model.Product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDetailDAOTest {
    private OrderDetailDAO orderDetailDAO;
    private OrderDAO orderDAO;
    private ProductDAO productDAO;

    @BeforeEach
    void setUp() {
        orderDetailDAO = new OrderDetailDAO();
        orderDAO = new OrderDAO();
        productDAO = new ProductDAO();
        try {
            // Thêm dữ liệu mẫu: sản phẩm
            productDAO.deleteProduct(999);
            Product product = new Product();
            product.setId(999);
            product.setName("Test Book");
            product.setPrice(29.99);
            product.setStock(100);
            product.setCategory("Fiction");
            product.setAuthor("Test Author");
            productDAO.addProduct(product);

            // Thêm dữ liệu mẫu: đơn hàng
            orderDAO.deleteOrder(999);
            Order order = new Order();
            order.setId(999);
            order.setDate(LocalDateTime.now());
            order.setEmployeeId(1);
            order.setTotal(29.99);
            orderDAO.addOrder(order);

            // Thêm dữ liệu mẫu: chi tiết đơn hàng
            OrderDetail detail = new OrderDetail();
            detail.setId(999);
            detail.setOrderId(999);
            detail.setProductId(999);
            detail.setQuantity(1);
            detail.setPrice(29.99);
            orderDetailDAO.addOrderDetail(detail);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetOrderDetailsByOrderId() throws SQLException {
        List<OrderDetail> details = orderDetailDAO.getOrderDetailsByOrderId(999);
        assertFalse(details.isEmpty(), "Danh sách chi tiết đơn hàng không được rỗng");
        assertEquals(999, details.get(0).getOrderId());
    }

    @Test
    void testAddOrderDetail() throws SQLException {
        OrderDetail detail = new OrderDetail();
        detail.setOrderId(999);
        detail.setProductId(999);
        detail.setQuantity(2);
        detail.setPrice(29.99);
        orderDetailDAO.addOrderDetail(detail);

        List<OrderDetail> details = orderDetailDAO.getOrderDetailsByOrderId(999);
        assertTrue(details.stream().anyMatch(d -> d.getQuantity() == 2), "Chi tiết đơn hàng vừa thêm phải tồn tại");

        // Kiểm tra tồn kho
        List<Product> products = productDAO.searchProducts("Test Book");
        assertEquals(97, products.get(0).getStock(), "Tồn kho phải giảm đúng số lượng");
    }

    @Test
    void testUpdateOrderDetail() throws SQLException {
        List<OrderDetail> details = orderDetailDAO.getOrderDetailsByOrderId(999);
        OrderDetail detail = details.get(0);
        detail.setQuantity(3);
        orderDetailDAO.updateOrderDetail(detail);

        details = orderDetailDAO.getOrderDetailsByOrderId(999);
        assertEquals(3, details.get(0).getQuantity(), "Số lượng chi tiết đơn hàng phải được cập nhật");

        // Kiểm tra tồn kho
        List<Product> products = productDAO.searchProducts("Test Book");
        assertEquals(96, products.get(0).getStock(), "Tồn kho phải giảm thêm 2");
    }

    @Test
    void testDeleteOrderDetail() throws SQLException {
        orderDetailDAO.deleteOrderDetail(999);
        List<OrderDetail> details = orderDetailDAO.getOrderDetailsByOrderId(999);
        assertTrue(details.isEmpty(), "Chi tiết đơn hàng phải được xóa");

        // Kiểm tra tồn kho
        List<Product> products = productDAO.searchProducts("Test Book");
        assertEquals(100, products.get(0).getStock(), "Tồn kho phải được hoàn lại");
    }
}