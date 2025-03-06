package com.bookstore.model.DTO;

public class CustomerPurchaseDTO {
    private String customerName;
    private int totalOrders;
    private double totalSpent;

    public CustomerPurchaseDTO(String customerName, int totalOrders, double totalSpent) {
        this.customerName = customerName;
        this.totalOrders = totalOrders;
        this.totalSpent = totalSpent;
    }

    public String getCustomerName() { 
        return customerName; 
    }
    public int getTotalOrders() { 
        return totalOrders; 
    }
    public double getTotalSpent() { 
        return totalSpent; 
    }
}
