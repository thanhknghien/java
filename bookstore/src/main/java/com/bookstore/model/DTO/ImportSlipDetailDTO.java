package com.bookstore.model.DTO;

public class ImportSlipDetailDTO {
    private int slipID;
    private int bookID;
    private int quantity;
    private double unitPrice;

    public ImportSlipDetailDTO(int slipID, int bookID, int quantity, double unitPrice){
        this.slipID = slipID;
        this.bookID = bookID;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public ImportSlipDetailDTO() {
    }

    public int getSlipID() {
        return slipID;
    }

    public void setSlipID(int slipID) {
        this.slipID = slipID;
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

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "ImportSlipDetailDTO [SlipID=" + slipID + ", BookID=" + bookID + ", Quantity=" + quantity
                + ", UnitPrice=" + unitPrice + "]";
    }

}
