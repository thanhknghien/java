package com.bookstore.model.DTO;

public class InvoiceDTO {
    private int invoiceID;
    private String customerName;
    private String date;
    private double totalAmount;
    
    public InvoiceDTO (int invoiceID, String customerName, String date, double totalAmount){
        this.invoiceID = invoiceID;
        this.customerName = customerName;
        this.date = date;
        this.totalAmount = totalAmount;
    }
    
    public void setInvoiceID (int invoiceID){
        this.invoiceID = invoiceID;
    }
    public int getInvoiceID (){
        return invoiceID;
    }
    
    public void setCustomerName (String customerName){
        this.customerName = customerName;
    }
    public String setCustomerName (){
        return customerName;
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
}
