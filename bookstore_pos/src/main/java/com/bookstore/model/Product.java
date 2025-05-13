package com.bookstore.model;

public class Product {
    private int id;
    private String name;
    private String author;
    private double price;
    private int categoryId;
    private String imagePath;

    // Constructor mặc định
    public Product() {}

    // Constructor đầy đủ
    public Product(int id, String name, String author, double price, int categoryId, String imagePath) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.price = price;
        this.categoryId = categoryId;
        this.imagePath = imagePath;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", categoryId=" + categoryId +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}