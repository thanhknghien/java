package com.bookstore.model;

import java.time.LocalDateTime;

public class Order {
    private int id;
    private LocalDateTime date;
    private Customer customer;
    private User employee;
    private double total;

    // Constructor
    public Order() {}

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", date=" + date + ", customer=" + customer + ", employee=" + employee + ", total="
                + total + "]";
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}