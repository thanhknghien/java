package com.bookstore.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JOptionPane;

import com.bookstore.BUS.POSBUS;
import com.bookstore.gui.main.POSGUI;
import com.bookstore.model.Customer;
import com.bookstore.model.Product;

public class POSController {
    private POSGUI gui;
    private POSBUS bus;

    public POSController(POSGUI gui){
        this.gui = gui;
        this.bus = new POSBUS();
    }

    public Map<String, ArrayList<Product>> loadingDataProducts() throws Exception{
        try {
            return bus.loadingCategory();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
           throw new Exception("Lỗi khi tải dữ liệu");
        }
    }

    public ArrayList<Customer> loadingDataCustomers() throws SQLException{
        return bus.getAllCustomers();
    }

    
}
