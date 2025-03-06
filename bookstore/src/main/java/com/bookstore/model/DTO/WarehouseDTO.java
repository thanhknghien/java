package com.bookstore.model.DTO;

import java.sql.Date;

public class WarehouseDTO {
    private int warehouseID;
    private int bookID;
    private int quantity;
    private Date lastUpdated;

    public WarehouseDTO(int warehouseID, int bookID, int quantity, Date lastUpdated){
        this.warehouseID = warehouseID;
        this.bookID = bookID;
        this.quantity = quantity;
        this.lastUpdated = lastUpdated;
    }

    public WarehouseDTO() {
    }

    public int getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(int warehouseID) {
        this.warehouseID = warehouseID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "WarehouseDTO [warehouseID=" + warehouseID + ", bookID=" + bookID + ", quantity=" + quantity
                + ", lastUpdated=" + lastUpdated + "]";
    }

    
    
}
