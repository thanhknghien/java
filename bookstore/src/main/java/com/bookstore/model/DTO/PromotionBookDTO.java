/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.model.DTO;

/**
 *
 * @author 84862
 */
public class PromotionBookDTO {
    private int PromotionID;
    private int bookID;
    public PromotionBookDTO(int PromotionID, int bookID){
        this.PromotionID = PromotionID;
        this.bookID = bookID;
    }

    public int getPromotionID() {
        return PromotionID;
    }

    public void setPromotionID(int PromotionID) {
        this.PromotionID = PromotionID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }
    
}
