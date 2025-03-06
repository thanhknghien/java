package com.bookstore.model.DTO;

public class ImportSlipDetailDTO {
    private int SlipID;
    private int BookID;
    private int Quantity;
    private double UnitPrice;

    public ImportSlipDetailDTO(int SlipID, int BookID, int Quantity, double UnitPrice){
        this.SlipID = SlipID;
        this.BookID = BookID;
        this.Quantity = Quantity;
        this.UnitPrice = UnitPrice;
    }

    public ImportSlipDetailDTO() {
    }

    public int getSlipID() {
        return SlipID;
    }

    public void setSlipID(int slipID) {
        SlipID = slipID;
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

    public double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        UnitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "ImportSlipDetailDTO [SlipID=" + SlipID + ", BookID=" + BookID + ", Quantity=" + Quantity
                + ", UnitPrice=" + UnitPrice + "]";
    }

}
