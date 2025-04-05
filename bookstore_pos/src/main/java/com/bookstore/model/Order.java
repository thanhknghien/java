package com.bookstore.model;

import java.time.LocalDateTime;

public class Order {
    private int id;
    private LocalDateTime date;
    private Integer customerId;
    private Integer employeeId; // Có thể null nếu nhân viên bị xóa
    private double total;

    public Order(int id, LocalDateTime date, Integer customerId, Integer employeeId, double total) {
        this.id = id;
        this.date = date;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.total = total;
    }
    public Order() {
    }
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
    public Integer getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
    public Integer getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }
    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
    }
}