package com.bookstore.dao;

import com.bookstore.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDAOTest {
    private OrderDAO orderDAO;

    @BeforeEach
    void setUp() {
        orderDAO = new OrderDAO();
        try {
            orderDAO.deleteOrder(999); // Xóa dữ liệu cũ nếu có
            Order order = new Order();
            order.setId(999);
            order.setDate(LocalDateTime.now());
            order.setEmployeeId(1); // Giả định employee_id = 1 tồn tại
            order.setTotal(99.99);
            orderDAO.addOrder(order);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetAllOrders() throws SQLException {
        List<Order> orders = orderDAO.getAllOrders();
        assertFalse(orders.isEmpty(), "Danh sách đơn hàng không được rỗng");
    }

    @Test
    void testAddOrder() throws SQLException {
        Order order = new Order();
        order.setDate(LocalDateTime.now());
        order.setEmployeeId(1);
        order.setTotal(199.99);
        orderDAO.addOrder(order);

        List<Order> orders = orderDAO.getAllOrders();
        assertTrue(orders.stream().anyMatch(o -> o.getTotal() == 199.99), "Đơn hàng vừa thêm phải tồn tại");
    }

    @Test
    void testUpdateOrder() throws SQLException {
        List<Order> orders = orderDAO.getAllOrders();
        Order order = orders.stream().filter(o -> o.getId() == 999).findFirst().orElse(null);
        assertNotNull(order);
        order.setTotal(149.99);
        orderDAO.updateOrder(order);

        orders = orderDAO.getAllOrders();
        order = orders.stream().filter(o -> o.getId() == 999).findFirst().orElse(null);
        assertNotNull(order);
        assertEquals(149.99, order.getTotal(), "Tổng tiền đơn hàng phải được cập nhật");
    }

    @Test
    void testDeleteOrder() throws SQLException {
        orderDAO.deleteOrder(999);
        List<Order> orders = orderDAO.getAllOrders();
        assertFalse(orders.stream().anyMatch(o -> o.getId() == 999), "Đơn hàng phải được xóa");
    }

    @Test
    void testSearchOrdersByDate() throws SQLException {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        List<Order> orders = orderDAO.searchOrdersByDate(startDate, endDate);
        assertFalse(orders.isEmpty(), "Phải tìm thấy đơn hàng trong khoảng thời gian");
    }
}