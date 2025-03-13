package com.bookstore.model.DTO;

public class SupplierDTO {
    private int supplierID;
    private String supplierName;
    private String supplierNumber;
    private String supplierAddress;
    private String status;

    public SupplierDTO(int supplierID, String supplierName, String supplierNumber, String supplierAddress,
            String status) {
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.supplierNumber = supplierNumber;
        this.supplierAddress = supplierAddress;
        this.status = status;
    }

    public SupplierDTO() {
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierNumber() {
        return supplierNumber;
    }

    public void setSupplierNumber(String supplierNumber) {
        this.supplierNumber = supplierNumber;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SupplierDTO [supplierID=" + supplierID + ", supplierName=" + supplierName + ", supplierNumber="
                + supplierNumber + ", supplierAddress=" + supplierAddress + ", status=" + status + "]";
    }

}