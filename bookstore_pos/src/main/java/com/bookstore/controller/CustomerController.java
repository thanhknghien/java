package com.bookstore.controller;

import java.util.List;

import com.bookstore.BUS.CustomerBUS;
import com.bookstore.model.Customer;

public class CustomerController {
    private CustomerBUS customerBUS; // Lớp xử lý logic liên quan đến dữ liệu khách hàng

    public CustomerController() {
        this.customerBUS = new CustomerBUS(); // Khởi tạo đối tượng CustomerBUS
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
}

