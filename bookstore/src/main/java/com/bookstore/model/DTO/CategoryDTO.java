/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.model.DTO;

/**
 *
 * @author 84862
 */
public class CategoryDTO {
    private int categoryID;
    private String name;
    public CategoryDTO(int CategoryID, String Name){
        this.categoryID = CategoryID;
        this.name = Name;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

  
    
}
