/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.controller;

import com.bookstore.BUS.ProductBUS;
import com.bookstore.gui.panel.ProductPanel;
import com.bookstore.model.Product;
import com.bookstore.model.User;
import com.bookstore.util.SessionManager;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;
import javax.swing.JOptionPane;
public class ProductController {
    private ProductBUS productBUS;
    private ProductPanel panel;
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
    public void updateUIBasedOnPermissions() {
        try {
            User currentUser = SessionManager.getInstance().getCurrentUser();
            if (currentUser == null) {
                panel.updateButtonsVisibility(false, false, false);
                return;
            }
            ArrayList<String> permissions = productBUS.getAllPermissions(currentUser.getId());
            boolean canAdd = permissions.contains("add");
            boolean canEdit = permissions.contains("edit");
            boolean canDelete = permissions.contains("delete");
            panel.updateButtonsVisibility(canAdd, canEdit, canDelete);
        } catch (SQLException e) {
            handleError("Lỗi khi kiểm tra quyền", e);
        }
    }

    private void handleError(String lỗi_khi_kiểm_tra_quyền, SQLException e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
