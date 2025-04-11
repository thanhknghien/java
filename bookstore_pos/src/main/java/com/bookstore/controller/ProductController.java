/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.controller;

import com.bookstore.BUS.ProductBUS;
import com.bookstore.model.Product;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;
import javax.swing.JOptionPane;
public class ProductController {
    private ProductBUS productBUS;
    
    public ProductController(){
        this.productBUS = new ProductBUS();
    }
    
    public List<Product> getAllProduct() {
        try {
            return productBUS.getAllProducts();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL khi lấy danh sách sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>(); 
        }
    }

}
