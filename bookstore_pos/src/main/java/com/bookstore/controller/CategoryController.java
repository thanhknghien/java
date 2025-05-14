/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.controller;

import com.bookstore.BUS.CategoryBUS;
import com.bookstore.model.Category;
import com.bookstore.gui.panel.CategoryPanel;
import com.bookstore.model.User;
import com.bookstore.util.SessionManager;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;
import javax.swing.JOptionPane;

public class CategoryController {
    private CategoryBUS categoryBUS;
    private CategoryPanel panel;
    public CategoryController(CategoryPanel panel){
        this.panel = panel;
        this.categoryBUS = new CategoryBUS();
        updateUIBasedOnPermissions();
    }
    
    public boolean addCategory(Category category) {
        try {
            return categoryBUS.addCategory(category);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }
    
    public boolean deleteCategory(int categoryID){
        return categoryBUS.deleteCategory(categoryID);
    }
    
    public boolean updateCategory(Category category){
        return categoryBUS.updateCategory(category);
    }
    
    public Category getCategoryById(int categoryID){
        return categoryBUS.getCategoryById(categoryID);
    }
    
    public List<Category> searchCategoriesByName(String name) {
        try {
            return categoryBUS.searchCategoriesByName(name);
        } catch (IllegalArgumentException e) {
            throw e; // Chuyển ngoại lệ lên giao diện để xử lý
        }
    }
    
    public List<Category> getAllCategories() {
        return categoryBUS.getAllCategories();
    }
    public void updateUIBasedOnPermissions() {
        try {
            User currentUser = SessionManager.getInstance().getCurrentUser();
            if (currentUser == null) {
                panel.updateButtonsVisibility(false, false, false);
                return;
            }
            ArrayList<String> permissions = categoryBUS.getAllPermissions(currentUser.getId());
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
