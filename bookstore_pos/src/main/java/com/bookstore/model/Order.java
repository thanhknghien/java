package com.bookstore.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Order {
    private int id;
    private LocalDateTime date;
    private Customer customer;
    private User employee;
    private double total;
    private ArrayList<OrderDetail> items; 
    
    public Order() {
    }

    public Order(int id, LocalDateTime date, Customer customer, User employee) {
        this.id = id;
        this.date = date;
        this.customer = customer;
        this.employee = employee;
        this.total = 0.0;
        this.items = new ArrayList<>();
    }

    public void addItem(OrderDetail item) {
        items.add(item);
        calculateTotal();
    }

    private void calculateTotal() {
        total = 0.0;
        for (OrderDetail item : items) {
            total += item.getSubtotal();
        }
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public User getEmployee() {
        return employee;
    }

    public double getTotal() {
        return total;
    }

    public ArrayList<OrderDetail> getItems() {
        return items;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setItems(ArrayList<OrderDetail> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Order #" + id + " | Date: " + date + " | Total: " + String.format("%.0f₫", total);
    }
}