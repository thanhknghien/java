package com.bookstore.model.DTO;

import java.sql.Date;

public class WarehouseDTO {
    private int WarehouseID;
    private int BookID;
    private int Quantity;
    private Date LastUpdated;

    public WarehouseDTO(int WarehouseID, int BookID, int Quantity, Date LastUpdated){
        this.WarehouseID = WarehouseID;
        this.BookID = BookID;
        this.Quantity = Quantity;
        this.LastUpdated = LastUpdated;
    }

    public WarehouseDTO() {
    }

    public int getWarehouseID() {
        return WarehouseID;
    }

    public void setWarehouseID(int warehouseID) {
        WarehouseID = warehouseID;
    }

    public int getBookID() {
        return BookID;
    }

    public void setBookID(int bookID) {
        BookID = bookID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public Date getLastUpdated() {
        return LastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        LastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "WarehouseDTO [BookID= " + BookID + ", LastUpdated= " + LastUpdated + ", Quantity= " + Quantity
                + ", WarehouseID= " + WarehouseID + "]";
    }
    
}
