package com.bookstore.model.DTO;

import java.util.Date;

public class SalesReportDTO {
    private int orderID;
    private Date orderDate;
    private String bookTitle;
    private int quantitySold;
    private double unitPrice;
    private double revenue;

    public SalesReportDTO(int orderID, Date orderDate, String bookTitle, int quantitySold, double unitPrice, double revenue) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.bookTitle = bookTitle;
        this.quantitySold = quantitySold;
        this.unitPrice = unitPrice;
        this.revenue = revenue;
    }

    public int getOrderID() { 
        return orderID; 
    }
    public Date getOrderDate() { 
        return orderDate; 
    }
    public String getBookTitle() { 
        return bookTitle; 
    }
    public int getQuantitySold() { 
        return quantitySold; 
    }
    public double getUnitPrice() { 
        return unitPrice; 
    }
    public double getRevenue() { 
        return revenue; 
    }
}
