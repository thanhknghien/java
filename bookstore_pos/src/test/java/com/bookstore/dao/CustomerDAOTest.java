package com.bookstore.dao;

import com.bookstore.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerDAOTest {
    private CustomerDAO customerDAO;

    @BeforeEach
    void setUp() {
        customerDAO = new CustomerDAO();
        try {
            customerDAO.deleteCustomer(999); // Xóa dữ liệu cũ nếu có
            Customer customer = new Customer();
            customer.setId(999);
            customer.setName("Test Customer");
            customer.setPhone("1234567890");
            customer.setPoints(100);
            customerDAO.addCustomer(customer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetAllCustomers() throws SQLException {
        List<Customer> customers = customerDAO.getAllCustomers();
        assertFalse(customers.isEmpty(), "Danh sách khách hàng không được rỗng");
    }

    @Test
    void testAddCustomer() throws SQLException {
        Customer customer = new Customer();
        customer.setName("New Customer");
        customer.setPhone("0987654321");
        customer.setPoints(50);
        customerDAO.addCustomer(customer);

        List<Customer> customers = customerDAO.searchCustomers("New Customer");
        assertFalse(customers.isEmpty(), "Khách hàng vừa thêm phải tồn tại");
        assertEquals("New Customer", customers.get(0).getName());
    }

    @Test
    void testUpdateCustomer() throws SQLException {
        List<Customer> customers = customerDAO.searchCustomers("Test Customer");
        Customer customer = customers.get(0);
        customer.setPoints(200);
        customerDAO.updateCustomer(customer);

        customers = customerDAO.searchCustomers("Test Customer");
        assertEquals(200, customers.get(0).getPoints(), "Điểm khách hàng phải được cập nhật");
    }

    @Test
    void testDeleteCustomer() throws SQLException {
        customerDAO.deleteCustomer(999);
        List<Customer> customers = customerDAO.searchCustomers("Test Customer");
        assertTrue(customers.isEmpty(), "Khách hàng phải được xóa");
    }

    @Test
    void testSearchCustomers() throws SQLException {
        List<Customer> customers = customerDAO.searchCustomers("Test");
        assertFalse(customers.isEmpty(), "Phải tìm thấy khách hàng với từ khóa 'Test'");
        assertEquals("Test Customer", customers.get(0).getName());
    }
}