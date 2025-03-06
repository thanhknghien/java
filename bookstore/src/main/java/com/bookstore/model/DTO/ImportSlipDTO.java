package com.bookstore.model.DTO;

import java.sql.Date;

public class ImportSlipDTO {
    private int SlipID;
    private int employeeID;
    private Date ImportDate;
    private double TotalAmount;

    public ImportSlipDTO(int SlipID, int employeeID, Date ImportDate, double TotalAmount){
        this.SlipID = SlipID;
        this.employeeID = employeeID;
        this.ImportDate = ImportDate;
        this.TotalAmount = TotalAmount;
    }

    public ImportSlipDTO() {
    }

    public int getSlipID() {
        return SlipID;
    }

    public void setSlipID(int slipID) {
        SlipID = slipID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public Date getImportDate() {
        return ImportDate;
    }

    public void setImportDate(Date importDate) {
        ImportDate = importDate;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        TotalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "ImportSlipDTO [SlipID=" + SlipID + ", employeeID=" + employeeID + ", ImportDate=" + ImportDate
                + ", TotalAmount=" + TotalAmount + "]";
    }

}
