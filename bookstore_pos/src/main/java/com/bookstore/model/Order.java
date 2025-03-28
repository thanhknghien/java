package com.bookstore.model;

import java.time.LocalDateTime;

public class Order {
    private int id;
    private LocalDateTime date;
    private Integer employeeId; // Có thể null nếu nhân viên bị xóa
    private double total;
    public Order() {
    }
    public Order(int id, LocalDateTime date, Integer employeeId, double total) {
        this.id = id;
        this.date = date;
        this.employeeId = employeeId;
        this.total = total;
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