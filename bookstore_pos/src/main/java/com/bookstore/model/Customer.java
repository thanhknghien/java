package com.bookstore.model;

public class Customer {
    private int id;
    private String name;
    private String phone;
    private int points;

    public Customer() {
    }
    public Customer(int id, String name, String phone, int points) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.points = points;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getFullName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhoneNumber() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }
    @Override
    public String toString() {
        return "Tên: " + name + ", SĐT: " + phone + ", points=" + points;
    }
}