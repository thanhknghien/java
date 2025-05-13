package com.bookstore.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.bookstore.config.DataBaseConfig;
import com.bookstore.model.Customer;

public class CustomerDAO {
    private Connection conn;

    public CustomerDAO() {
        try {
            conn = DataBaseConfig.getConnection(); // Kết nối cơ sở dữ liệu
        } catch (SQLException e) {
        }
    }

    // Xem tất cả khách hàng
    public ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customers";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setName(rs.getString("name"));
                customer.setPhone(rs.getString("phone"));
                customer.setPoints(rs.getInt("points"));
                customers.add(customer);
            }
        } catch (SQLException e) {
        }
        return customers;
    }

    // Thêm khách hàng
    public boolean insertCustomer(Customer customer) {
        String query = "INSERT INTO customers (name, phone, points) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, customer.getFullName());
            stmt.setString(2, customer.getPhoneNumber());
            stmt.setInt(3, customer.getPoints());
            stmt.executeUpdate();
            return true; // Thêm thành công
        } catch (SQLException e) {
            return false; // Thêm thất bại
        }
    }

    // Sửa khách hàng
    public boolean updateCustomer(Customer customer) {
        String query = "UPDATE customers SET name = ?, phone = ?, points = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, customer.getFullName());
            stmt.setString(2, customer.getPhoneNumber());
            stmt.setInt(3, customer.getPoints());
            stmt.setInt(4, customer.getId());
            stmt.executeUpdate();
            return true; // Sửa thành công
        } catch (SQLException e) {
            return false; // Sửa thất bại
        }
    }

    // Xóa khách hàng theo id
    public boolean deleteCustomerDAO(int id) {
        String query = "DELETE FROM customers WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true; // Xóa thành công
        } catch (SQLException e) {
            return false; // Xóa thất bại
        }
    }

    // Tìm kiếm khách hàng theo tên hoặc số điện thoại
    public ArrayList<Customer> searchCustomers(String keyword) {
        ArrayList<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customers WHERE name LIKE ? OR phone LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setName(rs.getString("name"));
                customer.setPhone(rs.getString("phone"));
                customer.setPoints(rs.getInt("points"));
                customers.add(customer);
            }
        } catch (SQLException e) {
        }
        return customers;
    }    
    
    // Phương thức tìm khách hàng theo ID
    public Customer getCustomerById(int id) {
        String query = "SELECT * FROM customers WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setName(rs.getString("name"));
                customer.setPhone(rs.getString("phone"));
                customer.setPoints(rs.getInt("points"));
                return customer; // Trả về đối tượng Customer nếu tìm thấy
            }
        } catch (SQLException e) {
        }
        return null; // Trả về null nếu không tìm thấy
    }

    // Phương thức tìm khách hàng theo tên
    public ArrayList<Customer> getCustomersByName(String name) {
        ArrayList<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customers WHERE name LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + name.trim() + "%"); // Sử dụng LIKE để tìm kiếm gần đúng
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setName(rs.getString("name"));
                customer.setPhone(rs.getString("phone"));
                customer.setPoints(rs.getInt("points"));
                customers.add(customer); // Thêm từng khách hàng vào danh sách
            }
        } catch (SQLException e) {
        }
        return customers; // Trả về danh sách khách hàng
    }   

    public ArrayList<Customer> getCustomersByPhone(String phone) {
        ArrayList<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customers WHERE phone LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + phone.trim() + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setName(rs.getString("name"));
                customer.setPhone(rs.getString("phone"));
                customer.setPoints(rs.getInt("points"));
                customers.add(customer);
            }
        } catch (SQLException e) {
        }
        return customers;
    }
    
    public ArrayList<Customer> getCustomersByPointRange(int startPoints, int endPoints) {
        ArrayList<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customers WHERE points BETWEEN ? AND ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, startPoints);
            stmt.setInt(2, endPoints);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setName(rs.getString("name"));
                customer.setPhone(rs.getString("phone"));
                customer.setPoints(rs.getInt("points"));
                customers.add(customer);
            }
        } catch (SQLException e) {
        }
        return customers;
    }
    public int getNextCustomerId() {
        String sql = "SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'pos_system' AND TABLE_NAME = 'customers'";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
