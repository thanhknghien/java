package com.bookstore.model.DTO;

import java.util.Date;

public class ImportReportDTO {
    private int slipID;
    private Date importDate;
    private String employeeName;
    private String bookTitle;
    private int quantity;
    private double unitPrice;
    private double totalCost;

    public ImportReportDTO(int slipID, Date importDate, String employeeName, String bookTitle, int quantity, double unitPrice, double totalCost) {
        this.slipID = slipID;
        this.importDate = importDate;
        this.employeeName = employeeName;
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalCost = totalCost;
    }

    public int getSlipID() { return slipID; }
    public Date getImportDate() { return importDate; }
    public String getEmployeeName() { return employeeName; }
    public String getBookTitle() { return bookTitle; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public double getTotalCost() { return totalCost; }
}
