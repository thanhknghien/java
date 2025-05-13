package com.bookstore.BUS;

import java.util.List;

import com.bookstore.dao.CustomerDAO;
import com.bookstore.model.Customer;

public class CustomerBUS {
    private CustomerDAO customerDAO;

    public CustomerBUS() {
        this.customerDAO = new CustomerDAO(); // Khởi tạo lớp DAO để thao tác với dữ liệu
    }

    // Thêm khách hàng mới
    public boolean addCustomer(Customer customer) {
        if (customer.getFullName() == null || customer.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Họ và tên không được để trống!");
        }
        if (customer.getPhoneNumber() == null || customer.getPhoneNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống!");
        }
        if (customer.getPoints() < 0) {
            throw new IllegalArgumentException("Điểm phải là số nguyên dương!");
        }
        return customerDAO.insertCustomer(customer); // Gọi phương thức DAO để thêm khách hàng
    }


    // Xóa khách hàng theo ID
    public boolean deleteCustomer(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID khách hàng phải lớn hơn 0!");
        }
        return customerDAO.deleteCustomerDAO(id);
    }

    // Sửa thông tin khách hàng
    public boolean updateCustomer(Customer customer) {
        if (customer.getFullName() == null || customer.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Họ và tên không được để trống!");
        }
        if (customer.getPhoneNumber() == null || customer.getPhoneNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống!");
        }
        if (customer.getPoints() < 0) {
            throw new IllegalArgumentException("Điểm phải là số nguyên dương!");
        }
        return customerDAO.updateCustomer(customer);
    }

    // Lấy thông tin khách hàng theo ID
    public Customer getCustomerById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID khách hàng phải lớn hơn 0!");
        }
        return customerDAO.getCustomerById(id);
    }

    // Tìm kiếm khách hàng theo tên
    public List<Customer> searchCustomersByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên khách hàng không được để trống!");
        }
        return customerDAO.getCustomersByName(name.trim());
    }
    
    // Tìm kiếm khách hàng theo số điện thoại
    public List<Customer> searchCustomersByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống!");
        }
        return customerDAO.getCustomersByPhone(phone.trim());
    }

    // Tìm kiếm khách hàng theo khoảng điểm tích lũy
    public List<Customer> searchCustomersByPointRange(int startPoints, int endPoints) {
        if (startPoints < 0 || endPoints < 0 || startPoints > endPoints) {
            throw new IllegalArgumentException("Điểm phải là số nguyên dương và khoảng tìm kiếm hợp lệ!");
        }
        return customerDAO.getCustomersByPointRange(startPoints, endPoints);
    }


    // Lấy danh sách tất cả khách hàng
    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }
}
