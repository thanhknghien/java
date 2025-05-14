/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.BUS;

import com.bookstore.model.Category;
import com.bookstore.dao.CategoryDAO;
import com.bookstore.model.User;
import com.bookstore.dao.UserDAO;
import com.bookstore.util.SessionManager;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;

public class CategoryBUS {
    private CategoryDAO categoryDAO;
    private UserDAO userDAO;
    
    public CategoryBUS(){
        this.categoryDAO = new CategoryDAO();
        userDAO = new UserDAO();
    }
    
    public boolean addCategory(Category category){
        if(category.getName() == null || category.getName().trim().isEmpty()){
            throw new IllegalArgumentException("Tên danh mục không được để trống!");
        }
        return categoryDAO.insertCategory(category);
    }
    
    public boolean deleteCategory(int categoryID){
        return categoryDAO.deleteCategory(categoryID);
    }
    
    public boolean updateCategory(Category category){
        return categoryDAO.updateCategory(category);
    }
    
    public Category getCategoryById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID danh mục phải lớn hơn 0!");
        }
        return categoryDAO.getCategoryById(id);
    }
    
    public List<Category> searchCategoriesByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên danh mục không được để trống!");
        }
        return categoryDAO.getCategoriesByName(name.trim());
    }
    
    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }
    public ArrayList<String> getAllPermissions(int userId) throws SQLException {
        User user = userDAO.getUserById(userId);
        if (user == null) {
            return new ArrayList<>();
        }

        SessionManager sessionManager = SessionManager.getInstance();
        User currentUser = sessionManager.getCurrentUser();
        sessionManager.setCurrentUser(user);
        
        try {
            ArrayList<ArrayList<String>> allPermissions = sessionManager.getAllPermissions();
            return allPermissions.get(4); 
        } finally {
            sessionManager.setCurrentUser(currentUser);
        }
    }
}
