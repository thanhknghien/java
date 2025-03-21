package com.bookstore.model.DTO;

import java.sql.Date;

public class ImportSlipDTO {
    private int slipID;
    private int supplierID;
    private int employeeID;
    private Date importDate;
    private double totalAmount;

    

    public ImportSlipDTO(int slipID, int supplierID, int employeeID, Date importDate, double totalAmount) {
        this.slipID = slipID;
        this.supplierID = supplierID;
        this.employeeID = employeeID;
        this.importDate = importDate;
        this.totalAmount = totalAmount;
    }

    public ImportSlipDTO() {
    }

    public int getSlipID() {
        return slipID;
    }
    
    public void setSlipID(int slipID) {
        this.slipID = slipID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    @Override
    public String toString() {
        return "ImportSlipDTO [slipID=" + slipID + ", supplierID=" + supplierID + ", employeeID=" + employeeID
                + ", importDate=" + importDate + ", totalAmount=" + totalAmount + "]";
    }

}
