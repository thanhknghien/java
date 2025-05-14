package com.bookstore.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.bookstore.BUS.CustomerBUS;
import com.bookstore.gui.panel.CustomerPanel;
import com.bookstore.model.Customer;
import com.bookstore.model.User;
import com.bookstore.util.SessionManager;

public class CustomerController {
    private CustomerBUS customerBUS; // Lớp xử lý logic liên quan đến dữ liệu khách hàng
    private CustomerPanel panel;

    public CustomerController(CustomerPanel panel) {
        this.panel = panel;
        this.customerBUS = new CustomerBUS(); // Khởi tạo đối tượng CustomerBUS
        updateUIBasedOnPermissions();
    }

    // Thêm khách hàng mới
    public boolean addCustomer(Customer customer) {
        try {
            return customerBUS.addCustomer(customer); // Gọi phương thức từ BUS
        } catch (IllegalArgumentException e) {
            throw e; // Chuyển ngoại lệ lên giao diện để hiển thị lỗi
        }
    }

    // Xóa khách hàng theo ID
    public boolean deleteCustomer(int id) {
        return customerBUS.deleteCustomer(id);
    }

    // Sửa thông tin khách hàng
    public boolean updateCustomer(Customer customer) {
        return customerBUS.updateCustomer(customer);
    }

    // Lấy thông tin khách hàng theo ID
    public Customer getCustomerById(int customerID) {
        return customerBUS.getCustomerById(customerID);
    }

    // Tìm kiếm khách hàng theo tên
    public List<Customer> searchCustomersByName(String name) {
        try {
            return customerBUS.searchCustomersByName(name);
        } catch (IllegalArgumentException e) {
            throw e; // Chuyển ngoại lệ lên giao diện để xử lý
        }
    }

    // Tìm kiếm khách hàng theo số điện thoại
    public List<Customer> searchCustomersByPhone(String phone) {
        return customerBUS.searchCustomersByPhone(phone);
    }

    // Tìm kiếm khách hàng theo khoảng điểm tích lũy
    public List<Customer> searchCustomersByPointRange(int startPoints, int endPoints) {
        return customerBUS.searchCustomersByPointRange(startPoints, endPoints);
    }


    // Lấy danh sách tất cả khách hàng
    public List<Customer> getAllCustomers() {
        return customerBUS.getAllCustomers();
    }

    public void updateUIBasedOnPermissions() {
        try {
            User currentUser = SessionManager.getInstance().getCurrentUser();
            if (currentUser == null) {
                panel.updateButtonsVisibility(false, false, false);
                return;
            }
            ArrayList<String> permissions = customerBUS.getAllPermissions(currentUser.getId());
            boolean canAdd = permissions.contains("add");
            boolean canEdit = permissions.contains("edit");
            boolean canDelete = permissions.contains("delete");
            panel.updateButtonsVisibility(canAdd, canEdit, canDelete);
        } catch (SQLException e) {
            handleError("Lỗi khi kiểm tra quyền", e);
        }
    }

    private void handleError(String title, Exception e) {
        String message = e.getMessage();
        if (message == null || message.trim().isEmpty()) {
            message = "Đã xảy ra lỗi không xác định";
        }
        JOptionPane.showMessageDialog(panel, 
            title + ": " + message, 
            "Lỗi", 
            JOptionPane.ERROR_MESSAGE);
    }
}

