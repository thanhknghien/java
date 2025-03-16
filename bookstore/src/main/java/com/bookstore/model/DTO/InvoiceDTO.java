package com.bookstore.model.DTO;

public class InvoiceDTO {
    private int invoiceID;
    private int customerID;
    private String date;
    private double totalAmount;
    private int status;
    
    public InvoiceDTO (int invoiceID, int customerID, String date, double totalAmount, int status){
        this.invoiceID = invoiceID;
        this.customerID = customerID;
        this.date = date;
        this.totalAmount = totalAmount;
        this.status = status;
    }
    
    public void setInvoiceID (int invoiceID){
        this.invoiceID = invoiceID;
    }
    public int getInvoiceID (){
        return invoiceID;
    }
    
    public void setCustomerID (int customerID){
        this.customerID = customerID;
    }
    public int getCustomerID (){
        return customerID;
    }
    
    public void setDate (String date){
        this.date = date;
    }
    public String getDate (){
        return date;
    }
    
    public void setTotalAmonunt (double totalAmount){
        this.totalAmount = totalAmount; 
    }
    public double getTotalAmount (){
        return totalAmount;
    }
    
    public void setStatus (int status){
        this.status = status;
    }
    public int getStatus (){
        return status;
    }
}
