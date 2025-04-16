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
    public int addProduct(Product product) 
        throws SQLException, IllegalArgumentException {
        return productBUS.addProduct(product);
    }
    
    public boolean deleteProduct(int id) throws SQLException{
        return productBUS.deleteProduct(id);
    }
    
    public boolean updateProduct(Product product) throws SQLException{
        return productBUS.updateProduct(product);
    }
    
    public Product searchById(int id) throws SQLException{
        return productBUS.searchById(id);
    }
    
    public List<Product> searchByName(String name) throws SQLException{
        return productBUS.searchByName(name);
    }
    
    public List<Product> searchByAuthor(String author) throws SQLException{
        return productBUS.searchByAuthor(author);
    }
    
    public List<Product> searchByCategoryId(int categoryId) throws SQLException{
        return productBUS.searchByCategoryId(categoryId);
    }
}
