/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.BUS;

import com.bookstore.model.Category;
import com.bookstore.dao.CategoryDAO;

import java.util.List;

public class CategoryBUS {
    private CategoryDAO categoryDAO;
    
    public CategoryBUS(){
        this.categoryDAO = new CategoryDAO();
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
    
    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }
}
