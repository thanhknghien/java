/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.controller;

import com.bookstore.BUS.CategoryBUS;
import com.bookstore.model.Category;

import java.util.List;

public class CategoryController {
    private CategoryBUS categoryBUS;
    
    public CategoryController(){
        this.categoryBUS = new CategoryBUS();
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
}
