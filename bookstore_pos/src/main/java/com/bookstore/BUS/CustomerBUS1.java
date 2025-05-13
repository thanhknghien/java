package com.bookstore.BUS;

import java.sql.SQLException;
import java.util.ArrayList;

import com.bookstore.dao.CustomerDAO;
import com.bookstore.model.Customer;

public class CustomerBUS1 {
    private CustomerDAO customerDAO;

    public CustomerBUS1(){
        customerDAO = new CustomerDAO();
    }

    // Get all Customer
    public ArrayList<Customer> getALlCustomers() throws SQLException{
        return customerDAO.getAllCustomers();
    }

    // Search Customer no query
    public ArrayList<Customer> searchCustomer(ArrayList<Customer> list, String value ){
        ArrayList<Customer> result = new ArrayList<>();
        value.toLowerCase();
        for(Customer cus : list){
            if(value.contains(cus.getFullName()) || value.contains(cus.getPhoneNumber())){
                result.add(cus);
            }
        }
        return result;
    }

    public boolean addCustomer(Customer customer){
        try {
            customerDAO.insertCustomer(customer);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
